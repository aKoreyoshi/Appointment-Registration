package com.mac.ghpt.manage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月02日, 18:02:10
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("admin-api")         // 分组名称
                .pathsToMatch("/admin/**")  // 接口请求路径规则
                .build();
    }

    /**
     * 自定义接口文档
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("医疗服务预约平台后台管理")              // 标题
                        .version("1.0")                             // 版本
                        .description("医疗服务预约平台后台管理API文档")  // 描述
                        .contact(new Contact().name("马聪")));       // 作者
    }

}
