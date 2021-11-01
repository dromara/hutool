package cn.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;

import java.util.Properties;

/**
 * 系统属性工具<br>
 * 此工具用于读取系统属性或环境变量信息，封装包括：
 * <ul>
 *     <li>{@link System#getProperty(String)}</li>
 *     <li>{@link System#getenv(String)}</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.16
 */
public class SystemPropsUtil {

	/** Hutool自定义系统属性：是否解析日期字符串采用严格模式 */
	public static String HUTOOL_DATE_LENIENT = "hutool.date.lenient";

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 defaultValue
	 *
	 * @param name         属性名
	 * @param defaultValue 默认值
	 * @return 属性值或defaultValue
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String name, String defaultValue) {
		return StrUtil.nullToDefault(get(name, false), defaultValue);
	}

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 {@code null}
	 *
	 * @param name  属性名
	 * @param quiet 安静模式，不将出错信息打在{@code System.err}中
	 * @return 属性值或{@code null}
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String name, boolean quiet) {
		String value = null;
		try {
			value = System.getProperty(name);
		} catch (SecurityException e) {
			if (false == quiet) {
				Console.error("Caught a SecurityException reading the system property '{}'; " +
						"the SystemUtil property value will default to null.", name);
			}
		}

		if (null == value) {
			try {
				value = System.getenv(name);
			} catch (SecurityException e) {
				if (false == quiet) {
					Console.error("Caught a SecurityException reading the system env '{}'; " +
							"the SystemUtil env value will default to null.", name);
				}
			}
		}

		return value;
	}

	/**
	 * 获得System属性
	 *
	 * @param key 键
	 * @return 属性值
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * 获得boolean类型值
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}

		value = value.trim().toLowerCase();
		if (value.isEmpty()) {
			return true;
		}

		return Convert.toBool(value, defaultValue);
	}

	/**
	 * 获得int类型值
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getInt(String key, int defaultValue) {
		return Convert.toInt(get(key), defaultValue);
	}

	/**
	 * 获得long类型值
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getLong(String key, long defaultValue) {
		return Convert.toLong(get(key), defaultValue);
	}

	/**
	 * @return 属性列表
	 */
	public static Properties getProps() {
		return System.getProperties();
	}

	/**
	 * 设置系统属性，value为{@code null}表示移除此属性
	 *
	 * @param key   属性名
	 * @param value 属性值，{@code null}表示移除此属性
	 */
	public static void set(String key, String value) {
		if (null == value) {
			System.clearProperty(key);
		} else {
			System.setProperty(key, value);
		}
	}
}
