package cn.hutool.core.collection;

import java.util.*;

/**
 * 使用起来比较简单的 集合类
 * Some copies from ServiceFrameWork(https://github.com/allwefantasy/ServiceFramework),
 * it is so easy to create Map, List, and Set.
 * Example:
 *      Map m = map(
 *          "key1", "value1",
 *          "key2", "value2"
 *      );
 *
 *      Set set = set(
 *          "v1", "v2", "v3"
 *      );
 *
 *      List list = list(
 *          "v1", "v2", "v3"
 *      );
 *      
 *      Map smap = sortedMap(
 *          "key1", "value1",
 *          "key2", "value2"
 *      );
 *
 * Consider of that collection`s resize, here is `map32`, `map128`.
 *
 * @author zhangshuang
 * @date 2020-05-21 17:28:26
 */
public class EasyCollections {

    /**
     * 普通hashmap
     * @param arrays
     *
     * @return
     */
    public static Map map(Object... arrays) {
        Map maps = new HashMap();
        return toMap(maps, arrays);
    }

    /**
     * 初始32长度hashmap
     * @param arrays
     * @return
     */
    public static Map map32(Object... arrays) {
        Map maps = new HashMap(32);
        return toMap(maps, arrays);
    }

    /**
     * 初始128长度hashmap
     * @param arrays
     * @return
     */
    public static Map map128(Object... arrays) {
        Map maps = new HashMap(128);
        return toMap(maps, arrays);
    }

    /**
     * 按照插入顺序存储
     * @param arrays
     * @return
     */
    public static Map sortedMap(Object... arrays) {
        Map maps = new LinkedHashMap();
        return toMap(maps, arrays);
    }

    private static Map toMap(Map maps, Object[] arrays) {
        if (arrays.length % 2 != 0) {
            throw new CollectionException("长度必须为偶数");
        }
        for (int i = 0; i < arrays.length; i++) {
            maps.put(arrays[i], arrays[++i]);
        }
        return maps;
    }

    /**
     * set集合
     * @param arrays
     * @return
     */
    public static Set set(Object... arrays) {
        Set set = new HashSet();
        for (int i = 0; i < arrays.length; i++) {
            set.add(arrays[i]);
        }
        return set;
    }

    /**
     * list集合
     * @param arrays
     * @return
     */
    public static List list(Object... arrays) {
        List list = new ArrayList(arrays.length);
        for (Object obj : arrays) {
            list.add(obj);
        }
        return list;
    }
}

class CollectionException extends RuntimeException {
    public CollectionException(String message) {
        super(message);
    }
}


