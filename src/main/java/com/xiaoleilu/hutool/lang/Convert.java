package com.xiaoleilu.hutool.lang;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Set;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 类型转换器
 * 
 * @author xiaoleilu
 * 
 */
public class Convert {
	
	private Convert() {
		// 静态类不可实例化
	}
	
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
			}else if(clazz == BigInteger.class){
				//数学计算数字
				return new BigInteger(valueStr);
			}else if(clazz == BigDecimal.class) {
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
	 * 转换基本类型<br>
	 * 将字符串转换为原始类型或包装类型
	 * @param clazz 转换到的类，可以是原始类型类，也可以是包装类型类
	 * @param valueStr 被转换的字符串
	 * @return 转换后的对象，如果非基本类型，返回null
	 */
	public static Object parseBasic(Class<?> clazz, String valueStr) {
		if(null == clazz || null == valueStr) {
			return null;
		}
		
		if(clazz.isAssignableFrom(String.class)){
			return valueStr;
		}
		
		BasicType basicType = null;
		try {
			basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
		} catch (Exception e) {
			//非基本类型数据
			return null;
		}
		
		switch (basicType) {
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
					return new BigDecimal(valueStr).longValue();
				}
				return Long.valueOf(valueStr);
			case DOUBLE:
				if(clazz == double.class) {
					return new BigDecimal(valueStr).doubleValue();
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
		}else if(CollectionUtil.isArray(value)){
			return CollectionUtil.toString(value);
		}
		return value.toString();
	}
	
	/**
	 * 转换为字符串<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static String toStr(Object value) {
		return toStr(value, null);
	}
	
	/**
	 * 转换为字符<br>
	 * 如果给定的值为null，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Character toChar(Object value, Character defaultValue) {
		if(null == value) {
			return defaultValue;
		}
		if(value instanceof Character) {
			return (Character)value;
		}
		
		final String valueStr = toStr(value, null);
		return StrUtil.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
	}
	
	/**
	 * 转换为字符<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Character toChar(Object value) {
		return toChar(value, null);
	}

	/**
	 * 转换为byte<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Byte toByte(Object value, Byte defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Byte) {
			return (Byte)value;
		}
		if(value instanceof Number){
			return ((Number) value).byteValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Byte.parseByte(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为byte<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Byte toByte(Object value) {
		return toByte(value, null);
	}
	
	/**
	 * 转换为Short<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Short toShort(Object value, Short defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Short) {
			return (Short)value;
		}
		if(value instanceof Number){
			return ((Number) value).shortValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Short.parseShort(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Short<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Short toShort(Object value) {
		return toShort(value, null);
	}
	
	/**
	 * 转换为Number<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Number toNumber(Object value, Number defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Number) {
			return (Number)value;
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return NumberFormat.getInstance().parse(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Number<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Number toNumber(Object value) {
		return toNumber(value, null);
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
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Integer) {
			return (Integer)value;
		}
		if(value instanceof Number){
			return ((Number) value).intValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Integer.parseInt(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为int<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Integer toInt(Object value) {
		return toInt(value, null);
	}
	
	/**
	 * 转换为Integer数组<br>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static Integer[] toIntArray(boolean isIgnoreConvertError, Object... values) {
		if(CollectionUtil.isEmpty(values)) {
			return new Integer[]{};
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
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Long) {
			return (Long)value;
		}
		if(value instanceof Number){
			return ((Number) value).longValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			//支持科学计数法
			return new BigDecimal(valueStr.trim()).longValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为long<br>
	 * 如果给定的值为<code>null</code>，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Long toLong(Object value) {
		return toLong(value, null);
	}
	
	/**
	 * 转换为Long数组<br>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static Long[] toLongArray(boolean isIgnoreConvertError, Object... values) {
		if(CollectionUtil.isEmpty(values)) {
			return new Long[]{};
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
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Double) {
			return (Double)value;
		}
		if(value instanceof Number){
			return ((Number) value).doubleValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			//支持科学计数法
			return new BigDecimal(valueStr.trim()).doubleValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为double<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Double toDouble(Object value) {
		return toDouble(value, null);
	}
	
	/**
	 * 转换为Double数组<br>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static Double[] toDoubleArray(boolean isIgnoreConvertError, Object... values) {
		if(CollectionUtil.isEmpty(values)) {
			return new Double[]{};
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
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Float) {
			return (Float)value;
		}
		if(value instanceof Number){
			return ((Number) value).floatValue();
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Float.parseFloat(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Float<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Float toFloat(Object value) {
		return toFloat(value, null);
	}
	
	/**
	 * 转换为Float数组<br>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static <T> Float[] toFloatArray(boolean isIgnoreConvertError, Object... values) {
		if(CollectionUtil.isEmpty(values)) {
			return new Float[]{};
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
	 * String支持的值为：true、false、yes、ok、no，1,0
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Boolean toBool(Object value, Boolean defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Boolean) {
			return (Boolean)value;
		}
		String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		valueStr = valueStr.trim().toLowerCase();
		switch (valueStr) {
			case "true":
				return true;
			case "false":
				return false;
			case "yes":
				return true;
			case "ok":
				return true;
			case "no":
				return false;
			case "1":
				return true;
			case "0":
				return false;
			default:
				return defaultValue;
		}
	}
	
	/**
	 * 转换为boolean<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Boolean toBool(Object value) {
		return toBool(value, null);
	}
	
	/**
	 * 转换为Boolean数组<br>
	 * @param isIgnoreConvertError 是否忽略转换错误，忽略则给值null
	 * @param values 被转换的值
	 * @return 结果
	 */
	public static Boolean[] toBooleanArray(boolean isIgnoreConvertError, Object... values) {
		if(CollectionUtil.isEmpty(values)) {
			return new Boolean[]{};
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
	
	/**
	 * 转换为Enum对象<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * @param clazz Enum的Class
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return Enum
	 */
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if (clazz.isAssignableFrom(value.getClass())) {
			@SuppressWarnings("unchecked")
			E myE = (E) value;
			return myE;
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Enum.valueOf(clazz,valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为Enum对象<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * @param clazz Enum的Class
	 * @param value 值
	 * @return Enum
	 */
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
		return toEnum(clazz, value, null);
	}
	
	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof BigInteger) {
			return (BigInteger)value;
		}
		if(value instanceof Long) {
			return BigInteger.valueOf((Long)value);
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return new BigInteger(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static BigInteger toBigInteger(Object value) {
		return toBigInteger(value, null);
	}
	
	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof BigDecimal) {
			return (BigDecimal)value;
		}
		if(value instanceof Long) {
			return new BigDecimal((Long)value);
		}
		if(value instanceof Double) {
			return new BigDecimal((Double)value);
		}
		if(value instanceof Integer) {
			return new BigDecimal((Integer)value);
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return new BigDecimal(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static BigDecimal toBigDecimal(Object value) {
		return toBigDecimal(value, null);
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
	
	/**
	 * 给定字符串转换字符编码<br/>
	 * 如果参数为空，则返回原字符串，不报错。
	 * 
	 * @param str 被转码的字符串
	 * @param sourceCharset 原字符集
	 * @param destCharset 目标字符集
	 * @return 转换后的字符串
	 */
	public static String convertCharset(String str, String sourceCharset, String destCharset) {
		if(StrUtil.hasBlank(str, sourceCharset, destCharset)) {
			return str;
		}
		
		try {
			return new String(str.getBytes(sourceCharset), destCharset);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	/**
	 * 数字金额大写转换
	 * 先写个完整的然后将如零拾替换成零
	 * @param n 数字
	 * @return 中文大写数字
	 */
	public static String digitUppercase(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };

		String head = n < 0 ? "负" : "";
		n = Math.abs(n);

		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);

		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
		}
		return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}
}
