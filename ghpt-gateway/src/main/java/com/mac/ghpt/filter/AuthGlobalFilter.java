package com.mac.ghpt.filter;

import com.alibaba.fastjson2.JSONObject;
import com.mac.ghpt.helper.JWTHelper;
import com.mac.ghpt.helper.JWTUtil;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月06日, 10:31:24
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 如果是内部请求，不允许访问
        if (antPathMatcher.match("/**/inner/**", path)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.INNER_PATH_ACCESS_ERROR);
        }
        // 如果是api接口，说明异步请求，需要用户必须登录
        // TODO 这里后期需要对path匹配再做调整 可以把现在的验证改为在redis验证
        if (antPathMatcher.match("/api/**/auth/**", path)) {
            String phone = this.getUserPhone(request);
            if (StringUtils.isEmpty(phone)) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
        }
        return chain.filter(exchange);
    }


    /**
     * 接口鉴权失败处理
     * @param response
     * @param resultCodeEnum
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bytes = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private String getUserPhone(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if (null != tokenList) {
            token = tokenList.get(0);
        }
        if (!StringUtils.isEmpty(token)) {
            return JWTUtil.getPhone(token);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}