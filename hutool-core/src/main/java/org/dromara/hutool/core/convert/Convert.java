/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.convert.impl.CollectionConverter;
import org.dromara.hutool.core.convert.impl.EnumConverter;
import org.dromara.hutool.core.convert.impl.MapConverter;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberChineseFormatter;
import org.dromara.hutool.core.math.NumberWordFormatter;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.UnicodeUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 类型转换器
 *
 * @author looly
 */
public class Convert {

	/**
	 * 转换为字符串<br>
	 * 如果给定的值为null，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static String toStr(final Object value, final String defaultValue) {
		return convertQuietly(String.class, value, defaultValue);
	}

	/**
	 * 转换为字符串<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static String toStr(final Object value) {
		return toStr(value, null);
	}

	/**
	 * 转换为String数组
	 *
	 * @param value 被转换的值
	 * @return String数组
	 * @since 3.2.0
	 */
	public static String[] toStrArray(final Object value) {
		return convert(String[].class, value);
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
	public static Character toChar(final Object value, final Character defaultValue) {
		return convertQuietly(Character.class, value, defaultValue);
	}

	/**
	 * 转换为字符<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Character toChar(final Object value) {
		return toChar(value, null);
	}

	/**
	 * 转换为Character数组
	 *
	 * @param value 被转换的值
	 * @return Character数组
	 * @since 3.2.0
	 */
	public static Character[] toCharArray(final Object value) {
		return convert(Character[].class, value);
	}

	/**
	 * 转换为byte<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Byte toByte(final Object value, final Byte defaultValue) {
		return convertQuietly(Byte.class, value, defaultValue);
	}

	/**
	 * 转换为byte<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Byte toByte(final Object value) {
		return toByte(value, null);
	}

	/**
	 * 转换为Byte数组
	 *
	 * @param value 被转换的值
	 * @return Byte数组
	 * @since 3.2.0
	 */
	public static Byte[] toByteArray(final Object value) {
		return convert(Byte[].class, value);
	}

	/**
	 * 转换为Byte数组
	 *
	 * @param value 被转换的值
	 * @return Byte数组
	 * @since 5.1.1
	 */
	public static byte[] toPrimitiveByteArray(final Object value) {
		return convert(byte[].class, value);
	}

	/**
	 * 转换为Short<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 */
	public static Short toShort(final Object value, final Short defaultValue) {
		return convertQuietly(Short.class, value, defaultValue);
	}

	/**
	 * 转换为Short<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Short toShort(final Object value) {
		return toShort(value, null);
	}

	/**
	 * 转换为Short数组
	 *
	 * @param value 被转换的值
	 * @return Short数组
	 * @since 3.2.0
	 */
	public static Short[] toShortArray(final Object value) {
		return convert(Short[].class, value);
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
	public static Number toNumber(final Object value, final Number defaultValue) {
		return convertQuietly(Number.class, value, defaultValue);
	}

	/**
	 * 转换为Number<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Number toNumber(final Object value) {
		return toNumber(value, null);
	}

	/**
	 * 转换为Number数组
	 *
	 * @param value 被转换的值
	 * @return Number数组
	 * @since 3.2.0
	 */
	public static Number[] toNumberArray(final Object value) {
		return convert(Number[].class, value);
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
	public static Integer toInt(final Object value, final Integer defaultValue) {
		return convertQuietly(Integer.class, value, defaultValue);
	}

	/**
	 * 转换为int<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Integer toInt(final Object value) {
		return toInt(value, null);
	}

	/**
	 * 转换为Integer数组<br>
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Integer[] toIntArray(final Object value) {
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
	public static Long toLong(final Object value, final Long defaultValue) {
		return convertQuietly(Long.class, value, defaultValue);
	}

	/**
	 * 转换为long<br>
	 * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Long toLong(final Object value) {
		return toLong(value, null);
	}

	/**
	 * 转换为Long数组<br>
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Long[] toLongArray(final Object value) {
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
	public static Double toDouble(final Object value, final Double defaultValue) {
		return convertQuietly(Double.class, value, defaultValue);
	}

	/**
	 * 转换为double<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Double toDouble(final Object value) {
		return toDouble(value, null);
	}

	/**
	 * 转换为Double数组<br>
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Double[] toDoubleArray(final Object value) {
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
	public static Float toFloat(final Object value, final Float defaultValue) {
		return convertQuietly(Float.class, value, defaultValue);
	}

	/**
	 * 转换为Float<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Float toFloat(final Object value) {
		return toFloat(value, null);
	}

	/**
	 * 转换为Float数组<br>
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Float[] toFloatArray(final Object value) {
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
	public static Boolean toBoolean(final Object value, final Boolean defaultValue) {
		return convertQuietly(Boolean.class, value, defaultValue);
	}

	/**
	 * 转换为boolean<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Boolean toBoolean(final Object value) {
		return toBoolean(value, null);
	}

	/**
	 * 转换为Boolean数组<br>
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static Boolean[] toBooleanArray(final Object value) {
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
	public static BigInteger toBigInteger(final Object value, final BigInteger defaultValue) {
		return convertQuietly(BigInteger.class, value, defaultValue);
	}

	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static BigInteger toBigInteger(final Object value) {
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
	public static BigDecimal toBigDecimal(final Object value, final BigDecimal defaultValue) {
		return convertQuietly(BigDecimal.class, value, defaultValue);
	}

	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回null<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static BigDecimal toBigDecimal(final Object value) {
		return toBigDecimal(value, null);
	}

	/**
	 * 转换为Date<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 * @since 4.1.6
	 */
	public static Date toDate(final Object value, final Date defaultValue) {
		return convertQuietly(Date.class, value, defaultValue);
	}

	/**
	 * LocalDateTime<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 * @since 5.0.7
	 */
	public static LocalDateTime toLocalDateTime(final Object value, final LocalDateTime defaultValue) {
		return convertQuietly(LocalDateTime.class, value, defaultValue);
	}

	/**
	 * 转换为LocalDateTime<br>
	 * 如果给定的值为空，或者转换失败，返回{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 */
	public static LocalDateTime toLocalDateTime(final Object value) {
		return toLocalDateTime(value, null);
	}

	/**
	 * Instant<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @param defaultValue 转换错误时的默认值
	 * @return 结果
	 * @since 5.0.7
	 */
	public static Date toInstant(final Object value, final Date defaultValue) {
		return convertQuietly(Instant.class, value, defaultValue);
	}

	/**
	 * 转换为Date<br>
	 * 如果给定的值为空，或者转换失败，返回{@code null}<br>
	 * 转换失败不会报错
	 *
	 * @param value 被转换的值
	 * @return 结果
	 * @since 4.1.6
	 */
	public static Date toDate(final Object value) {
		return toDate(value, null);
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
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E toEnum(final Class<E> clazz, final Object value, final E defaultValue) {
		try{
			return (E) EnumConverter.INSTANCE.convert(clazz, value);
		} catch (final Exception ignore){
			return defaultValue;
		}
	}

	/**
	 * 转换为Enum对象<br>
	 * 如果给定的值为空，或者转换失败，返回默认值{@code null}<br>
	 *
	 * @param <E> 枚举类型
	 * @param clazz Enum的Class
	 * @param value 值
	 * @return Enum
	 */
	public static <E extends Enum<E>> E toEnum(final Class<E> clazz, final Object value) {
		return toEnum(clazz, value, null);
	}

	/**
	 * 转换为集合类
	 *
	 * @param collectionType 集合类型
	 * @param elementType 集合中元素类型
	 * @param value 被转换的值
	 * @return {@link Collection}
	 * @since 3.0.8
	 */
	public static Collection<?> toCollection(final Class<?> collectionType, final Class<?> elementType, final Object value) {
		return new CollectionConverter().convert(collectionType, elementType, value);
	}

	/**
	 * 转换为ArrayList，元素类型默认Object
	 *
	 * @param value 被转换的值
	 * @return {@link List}
	 * @since 4.1.11
	 */
	public static List<?> toList(final Object value) {
		return convert(List.class, value);
	}

	/**
	 * 转换为ArrayList
	 *
	 * @param <T> 元素类型
	 * @param elementType 集合中元素类型
	 * @param value 被转换的值
	 * @return {@link ArrayList}
	 * @since 4.1.20
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(final Class<T> elementType, final Object value) {
		return (List<T>) toCollection(ArrayList.class, elementType, value);
	}

	/**
	 * 转换为HashSet
	 *
	 * @param <T> 元素类型
	 * @param elementType 集合中元素类型
	 * @param value 被转换的值
	 * @return {@link HashSet}
	 * @since 5.7.3
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> toSet(final Class<T> elementType, final Object value) {
		return (Set<T>) toCollection(HashSet.class, elementType, value);
	}

	/**
	 * 转换为Map，若value原本就是Map，则转为原始类型，若不是则默认转为HashMap
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param keyType   键类型
	 * @param valueType 值类型
	 * @param value     被转换的值
	 * @return {@link Map}
	 * @since 4.6.8
	 */
	public static <K, V> Map<K, V> toMap(final Class<K> keyType, final Class<V> valueType, final Object value) {
		if (value instanceof Map) {
			return toMap(value.getClass(), keyType, valueType, value);
		} else {
			return toMap(HashMap.class, keyType, valueType, value);
		}
	}

	/**
	 * 转换为Map
	 *
	 * @param mapType   转后的具体Map类型
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param keyType   键类型
	 * @param valueType 值类型
	 * @param value     被转换的值
	 * @return {@link Map}
	 */
	@SuppressWarnings({"unchecked"})
	public static <K, V> Map<K, V> toMap(final Class<?> mapType, final Class<K> keyType, final Class<V> valueType, final Object value) {
		return (Map<K, V>) MapConverter.INSTANCE.convert(mapType, keyType, valueType, value);
	}

	/**
	 * 转换值为指定类型，类型采用字符串表示
	 *
	 * @param <T> 目标类型
	 * @param className 类的字符串表示
	 * @param value 值
	 * @return 转换后的值
	 * @since 4.0.7
	 * @throws ConvertException 转换器不存在
	 */
	public static <T> T convertByClassName(final String className, final Object value) throws ConvertException{
		return convert(ClassLoaderUtil.loadClass(className), value);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T> 目标类型
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值
	 * @since 4.0.0
	 * @throws ConvertException 转换器不存在
	 */
	public static <T> T convert(final Class<T> type, final Object value) throws ConvertException{
		return convert((Type)type, value);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T> 目标类型
	 * @param reference 类型参考，用于持有转换后的泛型类型
	 * @param value 值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public static <T> T convert(final TypeReference<T> reference, final Object value) throws ConvertException{
		return convert(reference.getType(), value, null);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T> 目标类型
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public static <T> T convert(final Type type, final Object value) throws ConvertException{
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
	 * @since 4.0.0
	 */
	public static <T> T convert(final Class<T> type, final Object value, final T defaultValue) throws ConvertException {
		return convert((Type)type, value, defaultValue);
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
	public static <T> T convert(final Type type, final Object value, final T defaultValue) throws ConvertException {
		return convertWithCheck(type, value, defaultValue, false);
	}

	/**
	 * 转换值为指定类型，不抛异常转换<br>
	 * 当转换失败时返回{@code null}
	 *
	 * @param <T> 目标类型
	 * @param type 目标类型
	 * @param value 值
	 * @return 转换后的值，转换失败返回null
	 * @since 4.5.10
	 */
	public static <T> T convertQuietly(final Type type, final Object value) {
		return convertQuietly(type, value, null);
	}

	/**
	 * 转换值为指定类型，不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param <T> 目标类型
	 * @param type 目标类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @since 4.5.10
	 */
	public static <T> T convertQuietly(final Type type, final Object value, final T defaultValue) {
		return convertWithCheck(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型，可选是否不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param <T> 目标类型
	 * @param type 目标类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @param quietly 是否静默转换，true不抛异常
	 * @return 转换后的值
	 * @since 5.3.2
	 */
	public static <T> T convertWithCheck(final Type type, final Object value, final T defaultValue, final boolean quietly) {
		final CompositeConverter compositeConverter = CompositeConverter.getInstance();
		try {
			return compositeConverter.convert(type, value, defaultValue);
		} catch (final Exception e) {
			if(quietly){
				return defaultValue;
			}
			throw e;
		}
	}

	// ----------------------------------------------------------------------- 全角半角转换
	/**
	 * 半角转全角
	 *
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String toSBC(final String input) {
		return toSBC(input, null);
	}

	/**
	 * 半角转全角，{@code null}返回{@code null}
	 *
	 * @param input String
	 * @param notConvertSet 不替换的字符集合
	 * @return 全角字符串，{@code null}返回{@code null}
	 */
	public static String toSBC(final String input, final Set<Character> notConvertSet) {
		if(StrUtil.isEmpty(input)){
			return input;
		}
		final char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (null != notConvertSet && notConvertSet.contains(c[i])) {
				// 跳过不替换的字符
				continue;
			}

			if (c[i] == CharUtil.SPACE) {
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
	public static String toDBC(final String input) {
		return toDBC(input, null);
	}

	/**
	 * 替换全角为半角
	 *
	 * @param text 文本
	 * @param notConvertSet 不替换的字符集合
	 * @return 替换后的字符
	 */
	@SuppressWarnings("UnnecessaryUnicodeEscape")
	public static String toDBC(final String text, final Set<Character> notConvertSet) {
		if(StrUtil.isBlank(text)) {
			return text;
		}
		final char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (null != notConvertSet && notConvertSet.contains(c[i])) {
				// 跳过不替换的字符
				continue;
			}

			if (c[i] == '\u3000' || c[i] == '\u00a0' || c[i] == '\u2007' || c[i] == '\u202F') {
				// \u3000是中文全角空格，\u00a0、\u2007、\u202F是不间断空格
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}

		return new String(c);
	}

	// --------------------------------------------------------------------- hex
	/**
	 * 字符串转换成十六进制字符串，结果为小写
	 *
	 * @param str 待转换的ASCII字符串
	 * @param charset 编码
	 * @return 16进制字符串
	 * @see HexUtil#encodeStr(String, Charset)
	 */
	public static String toHex(final String str, final Charset charset) {
		return HexUtil.encodeStr(str, charset);
	}

	/**
	 * byte数组转16进制串
	 *
	 * @param bytes 被转换的byte数组
	 * @return 转换后的值
	 * @see HexUtil#encodeStr(byte[])
	 */
	public static String toHex(final byte[] bytes) {
		return HexUtil.encodeStr(bytes);
	}

	/**
	 * Hex字符串转换为Byte值
	 *
	 * @param src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 * @see HexUtil#decode(char[])
	 */
	public static byte[] hexToBytes(final String src) {
		return HexUtil.decode(src.toCharArray());
	}

	/**
	 * 十六进制转换字符串
	 *
	 * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @param charset 编码 {@link Charset}
	 * @return 对应的字符串
	 * @see HexUtil#decodeStr(String, Charset)
	 * @since 4.1.11
	 */
	public static String hexToStr(final String hexStr, final Charset charset) {
		return HexUtil.decodeStr(hexStr, charset);
	}

	/**
	 * String的字符串转换成unicode的String
	 *
	 * @param strText 全角字符串
	 * @return String 每个unicode之间无分隔符
	 * @see UnicodeUtil#toUnicode(String)
	 */
	public static String strToUnicode(final String strText) {
		return UnicodeUtil.toUnicode(strText);
	}

	/**
	 * unicode的String转换成String的字符串
	 *
	 * @param unicode Unicode符
	 * @return String 字符串
	 * @see UnicodeUtil#toString(String)
	 */
	public static String unicodeToStr(final String unicode) {
		return UnicodeUtil.toString(unicode);
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
	public static String convertCharset(final String str, final String sourceCharset, final String destCharset) {
		if (ArrayUtil.hasBlank(str, sourceCharset, destCharset)) {
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
	public static long convertTime(final long sourceDuration, final TimeUnit sourceUnit, final TimeUnit destUnit) {
		Assert.notNull(sourceUnit, "sourceUnit is null !");
		Assert.notNull(destUnit, "destUnit is null !");
		return destUnit.convert(sourceDuration, sourceUnit);
	}

	// --------------------------------------------------------------- 原始包装类型转换
	/**
	 * 原始类转为包装类，非原始类返回原类
	 *
	 * @see BasicType#wrap(Class)
	 * @param clazz 原始类
	 * @return 包装类
	 * @see BasicType#wrap(Class)
	 */
	public static Class<?> wrap(final Class<?> clazz) {
		return BasicType.wrap(clazz);
	}

	/**
	 * 包装类转为原始类，非包装类返回原类
	 *
	 * @see BasicType#unWrap(Class)
	 * @param clazz 包装类
	 * @return 原始类
	 * @see BasicType#unWrap(Class)
	 */
	public static Class<?> unWrap(final Class<?> clazz) {
		return BasicType.unWrap(clazz);
	}

	// -------------------------------------------------------------------------- 数字和英文转换
	/**
	 * 将阿拉伯数字转为英文表达方式
	 *
	 * @param number {@link Number}对象
	 * @return 英文表达式
	 * @since 3.0.9
	 */
	public static String numberToWord(final Number number) {
		return NumberWordFormatter.format(number);
	}

	/**
	 * 将阿拉伯数字转为精简表示形式，例如:
	 *
	 * <pre>
	 *     1200 -》 1.2k
	 * </pre>
	 *
	 * @param number {@link Number}对象
	 * @return 英文表达式
	 * @since 5.5.9
	 */
	public static String numberToSimple(final Number number) {
		return NumberWordFormatter.formatSimple(number.longValue());
	}

	/**
	 * 将阿拉伯数字转为中文表达方式
	 *
	 * @param number 数字
	 * @param isUseTraditional 是否使用繁体字（金额形式）
	 * @return 中文
	 * @since 3.2.3
	 */
	public static String numberToChinese(final double number, final boolean isUseTraditional) {
		return NumberChineseFormatter.format(number, isUseTraditional);
	}

	/**
	 * 数字中文表示形式转数字
	 * <ul>
	 *     <li>一百一十二 -》 112</li>
	 *     <li>一千零一十二 -》 1012</li>
	 * </ul>
	 *
	 * @param number 数字中文表示
	 * @return 数字
	 * @since 5.6.0
	 */
	public static int chineseToNumber(final String number){
		return NumberChineseFormatter.chineseToNumber(number);
	}

	/**
	 * 金额转为中文形式
	 *
	 * @param n 数字
	 * @return 中文大写数字
	 * @since 3.2.3
	 */
	public static String digitToChinese(final Number n) {
		if(null == n) {
			return "零";
		}
		return NumberChineseFormatter.format(n.doubleValue(), true, true);
	}

	/**
	 * 中文大写数字金额转换为数字，返回结果以元为单位的BigDecimal类型数字
	 * 如：
	 * 	“陆万柒仟伍佰伍拾陆元叁角贰分”返回“67556.32”
	 * 	“叁角贰分”返回“0.32”
	 *
	 * @param chineseMoneyAmount 中文大写数字金额
	 * @return 返回结果以元为单位的BigDecimal类型数字
	 * @since 5.8.5
	 */
	public static BigDecimal chineseMoneyToNumber(final String chineseMoneyAmount){
		return NumberChineseFormatter.chineseMoneyToNumber(chineseMoneyAmount);
	}

	// -------------------------------------------------------------------------- 数字转换
	/**
	 * int转byte
	 *
	 * @param intValue int值
	 * @return byte值
	 * @since 3.2.0
	 */
	public static byte intToByte(final int intValue) {
		return (byte) intValue;
	}

	/**
	 * byte转无符号int
	 *
	 * @param byteValue byte值
	 * @return 无符号int值
	 * @since 3.2.0
	 */
	public static int byteToUnsignedInt(final byte byteValue) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return byteValue & 0xFF;
	}

	/**
	 * byte数组转short<br>
	 * 默认以小端序转换
	 *
	 * @param bytes byte数组
	 * @return short值
	 * @since 5.6.3
	 */
	public static short bytesToShort(final byte[] bytes) {
		return ByteUtil.toShort(bytes);
	}

	/**
	 * short转byte数组<br>
	 * 默认以小端序转换
	 *
	 * @param shortValue short值
	 * @return byte数组
	 * @since 5.6.3
	 */
	public static byte[] shortToBytes(final short shortValue) {
		return ByteUtil.toBytes(shortValue);
	}

	/**
	 * byte[]转int值<br>
	 * 默认以小端序转换
	 *
	 * @param bytes byte数组
	 * @return int值
	 * @since 5.6.3
	 */
	public static int bytesToInt(final byte[] bytes) {
		return ByteUtil.toInt(bytes);
	}

	/**
	 * int转byte数组<br>
	 * 默认以小端序转换
	 *
	 * @param intValue int值
	 * @return byte数组
	 * @since 5.6.3
	 */
	public static byte[] intToBytes(final int intValue) {
		return ByteUtil.toBytes(intValue);
	}

	/**
	 * long转byte数组<br>
	 * 默认以小端序转换<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param longValue long值
	 * @return byte数组
	 * @since 5.6.3
	 */
	public static byte[] longToBytes(final long longValue) {
		return ByteUtil.toBytes(longValue);
	}

	/**
	 * byte数组转long<br>
	 * 默认以小端序转换<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param bytes byte数组
	 * @return long值
	 * @since 5.6.3
	 */
	public static long bytesToLong(final byte[] bytes) {
		return ByteUtil.toLong(bytes);
	}
}
