/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.core.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.reflect.TypeReference;

import java.util.Date;
import java.util.Map;

/**
 * Map的getXXX封装，提供针对通用型的value按照所需类型获取值
 *
 * @author looly
 * @since 6.0.0
 */
public class MapGetUtil {
	/**
	 * 获取Map指定key的值，并转换为字符串
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static String getStr(final Map<?, ?> map, final Object key) {
		return get(map, key, String.class);
	}

	/**
	 * 获取Map指定key的值，并转换为字符串
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static String getStr(final Map<?, ?> map, final Object key, final String defaultValue) {
		return get(map, key, String.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Integer
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Integer getInt(final Map<?, ?> map, final Object key) {
		return get(map, key, Integer.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Integer
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Integer getInt(final Map<?, ?> map, final Object key, final Integer defaultValue) {
		return get(map, key, Integer.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Double
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Double getDouble(final Map<?, ?> map, final Object key) {
		return get(map, key, Double.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Double
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Double getDouble(final Map<?, ?> map, final Object key, final Double defaultValue) {
		return get(map, key, Double.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Float
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Float getFloat(final Map<?, ?> map, final Object key) {
		return get(map, key, Float.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Float
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Float getFloat(final Map<?, ?> map, final Object key, final Float defaultValue) {
		return get(map, key, Float.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Short
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Short getShort(final Map<?, ?> map, final Object key) {
		return get(map, key, Short.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Short
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Short getShort(final Map<?, ?> map, final Object key, final Short defaultValue) {
		return get(map, key, Short.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Bool
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Boolean getBool(final Map<?, ?> map, final Object key) {
		return get(map, key, Boolean.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Bool
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Boolean getBool(final Map<?, ?> map, final Object key, final Boolean defaultValue) {
		return get(map, key, Boolean.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Character
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Character getChar(final Map<?, ?> map, final Object key) {
		return get(map, key, Character.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Character
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Character getChar(final Map<?, ?> map, final Object key, final Character defaultValue) {
		return get(map, key, Character.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为Long
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Long getLong(final Map<?, ?> map, final Object key) {
		return get(map, key, Long.class);
	}

	/**
	 * 获取Map指定key的值，并转换为Long
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Long getLong(final Map<?, ?> map, final Object key, final Long defaultValue) {
		return get(map, key, Long.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为{@link Date}
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.1.2
	 */
	public static Date getDate(final Map<?, ?> map, final Object key) {
		return get(map, key, Date.class);
	}

	/**
	 * 获取Map指定key的值，并转换为{@link Date}
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 4.1.2
	 */
	public static Date getDate(final Map<?, ?> map, final Object key, final Date defaultValue) {
		return get(map, key, Date.class, defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型
	 *
	 * @param <T>  目标值类型
	 * @param map  Map
	 * @param key  键
	 * @param type 值类型
	 * @return 值
	 * @since 4.0.6
	 */
	public static <T> T get(final Map<?, ?> map, final Object key, final Class<T> type) {
		return get(map, key, type, null);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static <T> T get(final Map<?, ?> map, final Object key, final Class<T> type, final T defaultValue) {
		return null == map ? defaultValue : Convert.convert(type, map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型，此方法在转换失败后不抛异常，返回null。
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.5.3
	 */
	public static <T> T getQuietly(final Map<?, ?> map, final Object key, final Class<T> type, final T defaultValue) {
		return null == map ? defaultValue : Convert.convertQuietly(type, map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型
	 *
	 * @param <T>  目标值类型
	 * @param map  Map
	 * @param key  键
	 * @param type 值类型
	 * @return 值
	 * @since 4.5.12
	 */
	public static <T> T get(final Map<?, ?> map, final Object key, final TypeReference<T> type) {
		return get(map, key, type, null);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static <T> T get(final Map<?, ?> map, final Object key, final TypeReference<T> type, final T defaultValue) {
		return null == map ? defaultValue : Convert.convert(type, map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型，转换失败后返回null，不抛异常
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.5.3
	 */
	public static <T> T getQuietly(final Map<?, ?> map, final Object key, final TypeReference<T> type, final T defaultValue) {
		return null == map ? defaultValue : Convert.convertQuietly(type, map.get(key), defaultValue);
	}
}
