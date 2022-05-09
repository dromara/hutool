package cn.hutool.json;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSON;

/**
 * @author leitao
 * @date 2022-04-18
 */
public class JSONHump {

    private static final Pattern LINE_PATTERN = Pattern.compile("_([a-z])");

    /**
     * json key 下划线转驼峰
     *
     * @param json 下划线json数据
     */
    public static void jsonToHump(Object json) {
        if (json instanceof JSONArray) {
            for (Object object : (JSONArray) json) {
                jsonToHump(object);
            }
        }
        if (json instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) json;
            List<String> keys = new ArrayList<>(jsonObject.keySet());
            keys.forEach(key -> {
                Object value = jsonObject.get(key);
                String humpKey = stringToHump(key);
                jsonObject.remove(key);
                jsonObject.set(humpKey, value);
                jsonToHump(value);
            });
        }
    }

    /**
     * json 字符串 下划线转驼峰
     *
     * @param json 下划线json字符串
     * @return 转换完成的object对象
     */
    public static Object jsonStringToHump(String json) {
        if (json == null) {
            return null;
        }
        Object object = JSON.parse(json);
        jsonToHump(object);
        return object;
    }

    /**
     * 字符串下划线转驼峰
     *
     * @param str 下划线字符串
     * @return 转换完成的string
     */
    public static String stringToHump(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
