package com.xiaoleilu.hutool.json;

import com.xiaoleilu.hutool.convert.ConvertException;
import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;

/**
 * 用于JSON的Getter类，提供各种类型的Getter方法
 * @author Looly
 *
 * @param <K> Key类型
 */
public abstract class JSONGetter<K> extends OptNullBasicTypeFromObjectGetter<K>{
	
	/**
	 * 获得JSONArray对象
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
	 */
	public JSONArray getJSONArray(K key) {
		final Object o = this.getObj(key);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * 获得JSONObject对象
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
	 */
	public JSONObject getJSONObject(K key) {
		final Object object = this.getObj(key);
		return object instanceof JSONObject ? (JSONObject) object : null;
	}
	
	/**
	 * 获取指定类型的对象<br>
	 * 转换失败或抛出异常
	 * 
	 * @param <T> 获取的对象类型
	 * @param key 键
	 * @param type 获取对象类型
	 * @return 对象
	 * @throws ConvertException 转换异常
	 * @since 3.0.8
	 */
	public <T> T get(K key, Class<T> type) throws ConvertException{
		return get(key, type, false);
	}
	
	/**
	 * 获取指定类型的对象
	 * 
	 * @param <T> 获取的对象类型
	 * @param key 键
	 * @param type 获取对象类型
	 * @param ignoreError 是否跳过转换失败的对象或值
	 * @return 对象
	 * @throws ConvertException 转换异常
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(K key, Class<T> type, boolean ignoreError) throws ConvertException{
		final Object value = this.getObj(key);
		if(null == value){
			return null;
		}
		return (T) InternalJSONUtil.jsonConvert(type, value, ignoreError);
	}
}
