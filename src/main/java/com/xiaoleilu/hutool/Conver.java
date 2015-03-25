package com.xiaoleilu.hutool;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 类型转换器
 * 
 * @author xiaoleilu
 * 
 */
public class Conver {
	
	/**
	 * 强制转换类型
	 * @param clazz 被转换成的类型
	 * @param value 需要转换的对象
	 * @return 转换后的对象
	 */
	public static Object parse(Class<?> clazz, Object value) {
		try {
			return clazz.cast(value);
		} catch (ClassCastException e) {
			String valueStr = String.valueOf(value);
			
			Object result = parseBasic(clazz, valueStr);
			if(result != null) {
				return result;
			}
			
			if(Date.class.isAssignableFrom(clazz)) {
				//判断标准日期
				return DateUtil.parse(valueStr);
			} else if(clazz == BigDecimal.class) {
				//数学计算数字
				return new BigDecimal(valueStr);
			}else if(clazz == byte[].class) {
				//流，由于有字符编码问题，在此使用系统默认
				return valueStr.getBytes();
			}
			
			//未找到可转换的类型，返回原值
			return value;
		}
	}
	
	/**
	 * 转换基本类型
	 * @param clazz 转换到的类
	 * @param valueStr 被转换的字符串
	 * @return 转换后的对象，如果非基本类型，返回null
	 */
	public static Object parseBasic(Class<?> clazz, String valueStr) {
		if(null == clazz || null == valueStr) {
			return null;
		}
		
		BasicType basicType = null;
		try {
			basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
		} catch (Exception e) {
			//非基本类型数据
			return null;
		}
		
		switch (basicType) {
			case STRING:
				return valueStr;
			case BYTE:
				if(clazz == byte.class) {
					return Byte.parseByte(valueStr);
				}
				return Byte.valueOf(valueStr);
			case SHORT:
				if(clazz == short.class) {
					return Short.parseShort(valueStr);
				}
				return Short.valueOf(valueStr);
			case INT:
				return Integer.parseInt(valueStr);
			case INTEGER:
				return Integer.valueOf(valueStr);
			case LONG:
				if(clazz == long.class) {
					return Long.parseLong(valueStr);
				}
				return Long.valueOf(valueStr);
			case DOUBLE:
				if(clazz == double.class) {
					return Double.parseDouble(valueStr);
				}
			case FLOAT:
				if(clazz == float.class) {
					return Float.parseFloat(valueStr);
				}
				return Float.valueOf(valueStr);
			case BOOLEAN:
				if(clazz == boolean.class) {
					return Boolean.parseBoolean(valueStr);
				}
				return Boolean.valueOf(valueStr);
			case CHAR:
				return valueStr.charAt(0);
			case CHARACTER:
				return Character.valueOf(valueStr.charAt(0));
			default:
				return null;
		}
	}

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
	@SafeVarargs
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
			return new BigDecimal(valueStr).longValue();
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
	@SafeVarargs
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
			return new BigDecimal(valueStr).doubleValue();
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
	@SafeVarargs
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
	@SafeVarargs
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
	@SafeVarargs
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
		return toSBC(input, null);
	}
	
	/**
	 * 半角转全角
	 * 
	 * @param input String
	 * @param notConvertSet 不替换的字符集合
	 * @return 全角字符串.
	 */
	public static String toSBC(String input, Set<Character> notConvertSet) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(null != notConvertSet && notConvertSet.contains(c[i])) {
				//跳过不替换的字符
				continue;
			}
			
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
		return toDBC(input, null);
	}
	
	/**
	 * 替换全角为半角
	 * @param text 文本
	 * @param notConvertSet 不替换的字符集合
	 * @return 替换后的字符
	 */
	public static String toDBC(String text, Set<Character> notConvertSet) {
		char c[] = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(null != notConvertSet && notConvertSet.contains(c[i])) {
				//跳过不替换的字符
				continue;
			}
			
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		
		return returnString;
	}
	
	/**
	 * byte数组转16进制串
	 * @param bytes 被转换的byte数组
	 * @return 转换后的值
	 */
	public static String toHex(byte[] bytes) {
		final StringBuilder des = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < bytes.length; i++) {
			tmp = (Integer.toHexString(bytes[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}
}
