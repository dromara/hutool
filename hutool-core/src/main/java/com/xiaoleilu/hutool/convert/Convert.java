package com.xiaoleilu.hutool.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.xiaoleilu.hutool.convert.impl.CollectionConverter;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 类型转换器
 * 
 * @author xiaoleilu
 * 
 */
public final class Convert {

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
		return convert(String.class, value, defaultValue);
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
		return convert(Character.class, value, defaultValue);
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
		return convert(Byte.class, value, defaultValue);
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
		return convert(Short.class, value, defaultValue);
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
		return convert(Number.class, value, defaultValue);
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
		return convert(Integer.class, value, defaultValue);
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
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Integer[] toIntArray(Object value) {
		return convert(Integer[].class, value);
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
		return convert(Long.class, value, defaultValue);
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
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Long[] toLongArray(Object value) {
		return convert(Long[].class, value);
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
		return convert(Double.class, value, defaultValue);
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
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Double[] toDoubleArray(Object value) {
		return convert(Double[].class, value);
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
		return convert(Float.class, value, defaultValue);
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
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Float[] toFloatArray(Object value) {
		return convert(Float[].class, value);
	}

	/**
	 * 转换为boolean<br>
	 * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Boolean toBool(Object value, Boolean defaultValue) {
		return convert(Boolean.class, value, defaultValue);
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
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Boolean[] toBooleanArray(Object value) {
		return convert(Boolean[].class, value);
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
		return convert(BigInteger.class, value, defaultValue);
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
		return convert(BigDecimal.class, value, defaultValue);
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

	/**
	 * 转换为Enum对象<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 
	 * @param <E> 枚举类型
	 * @param clazz Enum的Class
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return Enum
	 */
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (clazz.isAssignableFrom(value.getClass())) {
			@SuppressWarnings("unchecked")
			E myE = (E) value;
			return myE;
		}
		final String valueStr = toStr(value, null);
		if (StrUtil.isBlank(valueStr)) {
			return defaultValue;
		}
		try {
			return Enum.valueOf(clazz, valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 转换为Enum对象<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
	 * 
	 * @param <E> 枚举类型
	 * @param clazz Enum的Class
	 * @param value 值
	 * @return Enum
	 */
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
		return toEnum(clazz, value, null);
	}
	
	/**
	 * 转换为集合类
	 * @param collectionType 集合类型
	 * @param elementType 集合中元素类型
	 * @param value 被转换的值
	 * @return {@link Collection}
	 * @since 3.0.8
	 */
	public static Collection<?> toCollection(Class<?> collectionType, Class<?> elementType, Object value){
		return new CollectionConverter(collectionType, elementType).convert(value, null);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 目标类型
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值
	 */
	public static <T> T convert(Class<T> type, Object value) {
		return convert(type, value, null);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 目标类型
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public static <T> T convert(Class<T> type, Object value, T defaultValue) throws ConvertException {
		return ConverterRegistry.getInstance().convert(type, value, defaultValue);
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
			if (null != notConvertSet && notConvertSet.contains(c[i])) {
				// 跳过不替换的字符
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
	 * 
	 * @param text 文本
	 * @param notConvertSet 不替换的字符集合
	 * @return 替换后的字符
	 */
	public static String toDBC(String text, Set<Character> notConvertSet) {
		char c[] = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (null != notConvertSet && notConvertSet.contains(c[i])) {
				// 跳过不替换的字符
				continue;
			}

			if (c[i] == '\u3000' || c[i] == '\u00a0' || c[i] == '\u2007' || c[i] == '\u202F') {
				//\u3000是中文全角空格，\u00a0、\u2007、\u202F是不间断空格
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);

		return returnString;
	}

	// --------------------------------------------------------------------- hex
	/**
	 * 字符串转换成十六进制字符串，结果为小写
	 * 
	 * @param str 待转换的ASCII字符串
	 * @param charset 编码
	 * @return 16进制字符串
	 * @see HexUtil#encodeHexStr(String, Charset)
	 */
	public static String toHex(String str, Charset charset) {
		return HexUtil.encodeHexStr(str, charset);
	}

	/**
	 * byte数组转16进制串
	 * 
	 * @param bytes 被转换的byte数组
	 * @return 转换后的值
	 * @see HexUtil#encodeHexStr(byte[])
	 */
	public static String toHex(byte[] bytes) {
		return HexUtil.encodeHexStr(bytes);
	}

	/**
	 * Hex字符串转换为Byte值
	 * 
	 * @param src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 * @see HexUtil#decodeHex(char[])
	 */
	public static byte[] hexToBytes(String src) {
		return HexUtil.decodeHex(src.toCharArray());
	}

	/**
	 * 十六进制转换字符串
	 * 
	 * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @param charset 编码 {@link Charset}
	 * @return 对应的字符串
	 * @see HexUtil#decodeHexStr(String, Charset)
	 */
	public static String hexStrToStr(String hexStr, Charset charset) {
		return HexUtil.decodeHexStr(hexStr, charset);
	}

	/**
	 * String的字符串转换成unicode的String
	 * 
	 * @param strText 全角字符串
	 * @return String 每个unicode之间无分隔符
	 */
	public static String strToUnicode(String strText) {
		int strLength = strText.length();
		final StringBuilder str = new StringBuilder(strLength * 6);
		String strHex;
		int strHexLen;
		for (int i = 0; i < strLength; i++) {
			strHex = Integer.toHexString(strText.charAt(i));
			strHexLen = strHex.length();
			str.append("\\u");
			//对不够4位的在前补零
			if(strHexLen > 0 && strHexLen < 4){
				str.append(StrUtil.repeat('0', 4 - strHexLen));
			}
			str.append(strHex);
		}
		return str.toString();
	}

	/**
	 * unicode的String转换成String的字符串
	 * 
	 * @param unicode Unicode符
	 * @return String 字符串
	 */
	public static String unicodeToStr(String unicode) {
		StringBuffer string = new StringBuffer();
		String[] hex = StrUtil.split(unicode, "\\u");
		for (int i = 1; i < hex.length; i++) {
			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);
			// 追加成string
			string.append((char) data);
		}
		return string.toString();
	}

	/**
	 * 给定字符串转换字符编码<br>
	 * 如果参数为空，则返回原字符串，不报错。
	 * 
	 * @param str 被转码的字符串
	 * @param sourceCharset 原字符集
	 * @param destCharset 目标字符集
	 * @return 转换后的字符串
	 * @see CharsetUtil#convert(String, String, String)
	 */
	public static String convertCharset(String str, String sourceCharset, String destCharset) {
		if (StrUtil.hasBlank(str, sourceCharset, destCharset)) {
			return str;
		}

		return CharsetUtil.convert(str, sourceCharset, destCharset);
	}

	/**
	 * 转换时间单位
	 * 
	 * @param sourceDuration 时长
	 * @param sourceUnit 源单位
	 * @param destUnit 目标单位
	 * @return 目标单位的时长
	 */
	public static long convertTime(long sourceDuration, TimeUnit sourceUnit, TimeUnit destUnit) {
		Assert.notNull(sourceUnit, "sourceUnit is null !");
		Assert.notNull(destUnit, "destUnit is null !");
		return destUnit.convert(sourceDuration, sourceUnit);
	}

	/**
	 * 数字金额大写转换 先写个完整的然后将如零拾替换成零
	 * 
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

	// --------------------------------------------------------------- 原始包装类型转换
	/**
	 * 原始类转为包装类，非原始类返回原类
	 * 
	 * @see BasicType#wrap(Class)
	 * @param clazz 原始类
	 * @return 包装类
	 */
	public static Class<?> wrap(Class<?> clazz) {
		return BasicType.wrap(clazz);
	}

	/**
	 * 包装类转为原始类，非包装类返回原类
	 * 
	 * @see BasicType#unWrap(Class)
	 * @param clazz 包装类
	 * @return 原始类
	 */
	public static Class<?> unWrap(Class<?> clazz) {
		return BasicType.unWrap(clazz);
	}
	
	//-------------------------------------------------------------------------- 数字和英文转换
	/**
	 * 将阿拉伯数字转为英文表达式
	 * @param number {@link Number}对象
	 * @return 英文表达式
	 * @since 3.0.9
	 */
	public static String numberToWord(Number number) {
		return NumberWordFormater.format(number);
	}
}
