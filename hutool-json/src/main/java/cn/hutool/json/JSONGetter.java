package cn.hutool.json;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.getter.OptNullBasicTypeFromObjectGetter;

/**
 * 用于JSON的Getter类，提供各种类型的Getter方法
 *
 * @param <K> Key类型
 * @author Looly
 */
public interface JSONGetter<K> extends OptNullBasicTypeFromObjectGetter<K> {

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 * @since 5.3.0
	 */
	JSONConfig getConfig();

	/**
	 * key对应值是否为<code>null</code>或无此key
	 *
	 * @param key 键
	 * @return true 无此key或值为<code>null</code>或{@link JSONNull#NULL}返回<code>false</code>，其它返回<code>true</code>
	 */
	default boolean isNull(K key) {
		return JSONNull.NULL.equals(this.getObj(key));
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key 键
	 * @return 字符串类型值
	 * @since 4.2.2
	 */
	default String getStrEscaped(K key) {
		return getStrEscaped(key, null);
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 字符串类型值
	 * @since 4.2.2
	 */
	default String getStrEscaped(K key, String defaultValue) {
		return JSONUtil.escape(getStr(key, defaultValue));
	}

	/**
	 * 获得JSONArray对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONArray}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
	 */
	default JSONArray getJSONArray(K key) {
		final Object object = this.getObj(key);
		if (null == object) {
			return null;
		}

		if (object instanceof JSON) {
			return (JSONArray) object;
		}
		return new JSONArray(object, getConfig());
	}

	/**
	 * 获得JSONObject对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONObject}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
	 */
	default JSONObject getJSONObject(K key) {
		final Object object = this.getObj(key);
		if (null == object) {
			return null;
		}

		if (object instanceof JSON) {
			return (JSONObject) object;
		}
		return new JSONObject(object, getConfig());
	}

	/**
	 * 从JSON中直接获取Bean对象<br>
	 * 先获取JSONObject对象，然后转为Bean对象
	 *
	 * @param <T>      Bean类型
	 * @param key      KEY
	 * @param beanType Bean类型
	 * @return Bean对象，如果值为null或者非JSONObject类型，返回null
	 * @since 3.1.1
	 */
	default <T> T getBean(K key, Class<T> beanType) {
		final JSONObject obj = getJSONObject(key);
		return (null == obj) ? null : obj.toBean(beanType);
	}

	/**
	 * 获取指定类型的对象<br>
	 * 转换失败或抛出异常
	 *
	 * @param <T>  获取的对象类型
	 * @param key  键
	 * @param type 获取对象类型
	 * @return 对象
	 * @throws ConvertException 转换异常
	 * @since 3.0.8
	 */
	default <T> T get(K key, Class<T> type) throws ConvertException {
		return get(key, type, false);
	}

	/**
	 * 获取指定类型的对象
	 *
	 * @param <T>         获取的对象类型
	 * @param key         键
	 * @param type        获取对象类型
	 * @param ignoreError 是否跳过转换失败的对象或值
	 * @return 对象
	 * @throws ConvertException 转换异常
	 * @since 3.0.8
	 */
	default <T> T get(K key, Class<T> type, boolean ignoreError) throws ConvertException {
		final Object value = this.getObj(key);
		if (null == value) {
			return null;
		}
		return JSONConverter.jsonConvert(type, value, ignoreError);
	}
}
