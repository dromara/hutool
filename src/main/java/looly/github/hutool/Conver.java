package looly.github.hutool;

/**
 * 类型转换器
 * @author xiaoleilu
 *
 */
public class Conver {
	
	/**
	 * 转换为字符串<br>
	 * 如果给定的值为null，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static String toStr(Object value, String defaultValue) {
		return value == null ? defaultValue : value.toString();
	}
	
	/**
	 * 转换为int<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Integer toInt(Object value, Integer defaultValue) {
		if(value == null) return defaultValue;
		final String valueStr = value.toString();
		if(StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Integer.parseInt(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为long<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Long toLong(Object value, Long defaultValue) {
		if(value == null) return defaultValue;
		final String valueStr = value.toString();
		if(StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Long.parseLong(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为double<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Double toDouble(Object value, Double defaultValue) {
		if(value == null) return defaultValue;
		final String valueStr = value.toString();
		if(StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Double.parseDouble(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为boolean<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Boolean toBool(Object value, Boolean defaultValue) {
		if(value == null) return defaultValue;
		final String valueStr = value.toString();
		if(StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Boolean.parseBoolean(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
