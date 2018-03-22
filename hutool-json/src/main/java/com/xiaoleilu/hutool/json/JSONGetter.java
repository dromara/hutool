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
	
	@Override
	public String getStr(K key, String defaultValue) {
		String str = super.getStr(key, defaultValue);
		if(null == str) {
			return defaultValue;
		}
		return JSONUtil.escape(str);
	}
	
	/**
	 * 获得JSONArray对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONArray}返回，否则抛出异常
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
	 */
	public JSONArray getJSONArray(K key) {
		final Object object = this.getObj(key);
		if(null == object) {
			return null;
		}
		
		if(object instanceof JSONArray) {
			return (JSONArray) object;
		}
		return new JSONArray(object);
	}

	/**
	 * 获得JSONObject对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONObject}返回，否则抛出异常
	 * 
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
	 */
	public JSONObject getJSONObject(K key) {
		final Object object = this.getObj(key);
		if(null == object) {
			return null;
		}
		
		if(object instanceof JSONObject) {
			return (JSONObject) object;
		}
		return new JSONObject(object);
	}
	
	/**
	 * 从JSON中直接获取Bean对象<br>
	 * 先获取JSONObject对象，然后转为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param key KEY
	 * @param beanType Bean类型
	 * @return Bean对象，如果值为null或者非JSONObject类型，返回null
	 * @since 3.1.1
	 */
	public <T> T getBean(K key, Class<T> beanType) {
		final JSONObject obj = getJSONObject(key);
		return (null == obj) ? null : obj.toBean(beanType);
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
