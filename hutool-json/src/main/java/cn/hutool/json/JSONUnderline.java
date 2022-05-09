package cn.hutool.json;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;

/**
 * @author leitao
 * @date 2022-04-19
 */
public class JSONUnderline {

    private static final String SEPARATOR = "_";


	/**
	 * json key 驼峰转下划线
	 *
	 * @param json 驼峰json数据
	 */
    public static void jsonToUnderline(Object json) {
        if (json instanceof JSONArray) {
            for (Object object : (JSONArray) json) {
                jsonToUnderline(object);
            }
        }
        if (json instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) json;
            List<String> keys = new ArrayList<>(jsonObject.keySet());
            keys.forEach(key -> {
                Object value = jsonObject.get(key);
                String underlineKey = toUnderlineCase(key);
                jsonObject.remove(key);
                jsonObject.set(underlineKey, value);
                jsonToUnderline(value);
            });
        }
    }

    /**
     * json 字符串 下划线转驼峰
     *
     * @param json 下划线json字符串
     * @return 转换完成的object对象
     */
    public static Object jsonStringToUnderline(String json) {
        if (json == null) {
            return null;
        }
        Object object = JSON.parse(json);
        jsonToUnderline(object);
        return object;
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下划线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        char[] charArray = camelCaseStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                charArray[i] += 32;
                sb.append(SEPARATOR).append(charArray[i]);
            } else {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }
}
