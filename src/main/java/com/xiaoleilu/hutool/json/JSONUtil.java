package com.xiaoleilu.hutool.json;

public class JSONUtil {
	public static JSONObject parse(String jsonStr){
		return new JSONObject(jsonStr);
	}
	
	public static JSONArray parseArray(String jsonStr){
		return new JSONArray(jsonStr);
	}
	
	public static String toJsonStr(JSONObject json){
		return json.toString();
	}
	
	public static String toJsonStr(JSONArray jsonArray){
		return jsonArray.toString();
	}
}
