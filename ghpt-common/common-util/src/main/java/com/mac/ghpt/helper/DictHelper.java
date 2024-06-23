package com.mac.ghpt.helper;

import com.mac.ghpt.model.entity.system.Dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 14:25:16
 */
public class DictHelper {

    public static List<Dict> build(List<Dict> dicts) {
        // 将 dicts 转换为 Map，以字典项的 ID 作为键
        Map<Long, Dict> dictMap = new HashMap<>();
        for (Dict dict : dicts) {
            dictMap.put(dict.getId(), dict);
        }
        // 新建一个集合，用来存储后续遍历的数据
        List<Dict> treeList = new ArrayList<>();
        // 遍历
        for (Dict dict : dicts) {
            // 判断是否为顶级节点  此时parent_id=0
            if (dict.getParentId().longValue() == 0) {
                treeList.add(findChildren(dict, dictMap));
            }
        }
        return treeList;
    }

    // 优化后代码
    private static Dict findChildren(Dict dict, Map<Long, Dict> dictMap) {
        dict.setChildren(new ArrayList<>());
        for (Dict item : dictMap.values()) {
            if (item.getParentId().longValue() == dict.getId().longValue()) {
                dict.getChildren().add(findChildren(item, dictMap));
            }
        }
        return dict;
    }


    // private static Dict findChildren(Dict dict, List<Dict> dicts) {
    //     dict.setChildren(new ArrayList<>());
    //     dicts.stream().forEach(item -> {
    //         // 判断当前节点item是否是dict的子节点
    //         if (item.getParentId().longValue() == dict.getId().longValue()) {
    //             dict.getChildren().add(findChildren(item, dicts));
    //         }
    //     });
    //     return dict;
    // }
}
