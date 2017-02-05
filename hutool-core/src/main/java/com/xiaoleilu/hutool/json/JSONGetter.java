package com.xiaoleilu.hutool.json;

import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;

/**
 * 用于JSON的Getter类，提供各种类型的Getter方法
 * @author Looly
 *
 * @param <K>
 */
public abstract class JSONGetter<K> extends OptNullBasicTypeFromObjectGetter<K>{
	
	/**
	 * 获得JSONArray对象
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
	 */
	public JSONArray getJSONArray(K key) {
		Object o = this.getObj(key);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * 获得JSONObject对象
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
	 */
	public JSONObject getJSONObject(K key) {
		Object object = this.getObj(key);
		return object instanceof JSONObject ? (JSONObject) object : null;
	}
}
