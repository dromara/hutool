package com.xiaoleilu.hutool;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 类型转换器
 * 
 * @author xiaoleilu
 * 
 */
public class Conver {

	/**
	 * 转换为字符串<br>
	 * 如果给定的值为null，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static String toStr(Object value, String defaultValue) {
		if(null == value) {
			return defaultValue;
		}
		if(value instanceof String) {
			return (String)value;
		}
		return value.toString();
	}

	/**
	 * 转换为int<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Integer toInt(Object value, Integer defaultValue) {
		if (value == null) return defaultValue;
		if(value instanceof Integer) {
			return (Integer)value;
		}
		final String valueStr = value.toString();
		if (StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Integer.parseInt(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Integer数组<br>
	 * @param <T>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Integer[] toIntArray(boolean isIgnoreConvertError, T... values) {
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		final Integer[] ints = new Integer[values.length];
		for(int i = 0; i < values.length; i++) {
			final Integer v = toInt(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new UtilException(StrUtil.format("Convert [{}] to Integer error!", values[i]));
			}
			ints[i] = v;
		}
		return ints;
	}

	/**
	 * 转换为long<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Long toLong(Object value, Long defaultValue) {
		if (value == null) return defaultValue;
		if(value instanceof Long) {
			return (Long)value;
		}
		final String valueStr = value.toString();
		if (StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Long.parseLong(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Long数组<br>
	 * @param <T>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Long[] toLongArray(boolean isIgnoreConvertError, T... values) {
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		final Long[] longs = new Long[values.length];
		for(int i = 0; i < values.length; i++) {
			final Long v = toLong(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new UtilException(StrUtil.format("Convert [{}] to Long error!", values[i]));
			}
			longs[i] = v;
		}
		return longs;
	}

	/**
	 * 转换为double<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Double toDouble(Object value, Double defaultValue) {
		if (value == null) return defaultValue;
		if(value instanceof Double) {
			return (Double)value;
		}
		final String valueStr = value.toString();
		if (StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Double.parseDouble(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Double数组<br>
	 * @param <T>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Double[] toDoubleArray(boolean isIgnoreConvertError, T... values) {
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		final Double[] doubles = new Double[values.length];
		for(int i = 0; i < values.length; i++) {
			final Double v = toDouble(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new UtilException(StrUtil.format("Convert [{}] to Double error!", values[i]));
			}
			doubles[i] = v;
		}
		return doubles;
	}
	
	/**
	 * 转换为Float<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Float toFloat(Object value, Float defaultValue) {
		if (value == null) return defaultValue;
		if(value instanceof Float) {
			return (Float)value;
		}
		final String valueStr = value.toString();
		if (StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Float.parseFloat(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Float数组<br>
	 * @param <T>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Float[] toFloatArray(boolean isIgnoreConvertError, T... values) {
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		final Float[] floats = new Float[values.length];
		for(int i = 0; i < values.length; i++) {
			final Float v = toFloat(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new UtilException(StrUtil.format("Convert [{}] to Float error!", values[i]));
			}
			floats[i] = v;
		}
		return floats;
	}

	/**
	 * 转换为boolean<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Boolean toBool(Object value, Boolean defaultValue) {
		if (value == null) return defaultValue;
		if(value instanceof Boolean) {
			return (Boolean)value;
		}
		final String valueStr = value.toString();
		if (StrUtil.isBlank(valueStr)) return defaultValue;
		try {
			return Boolean.parseBoolean(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Boolean数组<br>
	 * @param <T>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Boolean[] toBooleanArray(boolean isIgnoreConvertError, T... values) {
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		final Boolean[] bools = new Boolean[values.length];
		for(int i = 0; i < values.length; i++) {
			final Boolean v = toBool(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new UtilException(StrUtil.format("Convert [{}] to Boolean error!", values[i]));
			}
			bools[i] = v;
		}
		return bools;
	}

	// ----------------------------------------------------------------------- 全角半角转换
	/**
	 * 半角转全角
	 * 
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String toSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}
}
