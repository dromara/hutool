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

package org.dromara.hutool.core.array;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.collection.set.UniqueKeySet;
import org.dromara.hutool.core.comparator.CompareUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrJoiner;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.RandomUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * 数组工具类
 *
 * @author Looly
 */
public class ArrayUtil extends PrimitiveArrayUtil {

	// region ----- ofArray

	/**
	 * 转为数组，如果values为数组，返回，否则返回一个只有values一个元素的数组
	 *
	 * @param <A>    数组类型
	 * @param values 元素值
	 * @return 数组
	 */
	public static <A> A ofArray(final Object values) {
		return ofArray(values, null);
	}

	/**
	 * 转为数组，如果values为数组，返回，否则返回一个只有values一个元素的数组
	 *
	 * @param <A>         数组类型
	 * @param values      元素值
	 * @param elementType 数组元素类型，{@code null}表示使用values的类型
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public static <A> A ofArray(final Object values, final Class<?> elementType) {
		if (isArray(values)) {
			return (A) values;
		}

		// 插入单个元素
		final Object newInstance = Array.newInstance(
			null == elementType ? values.getClass() : elementType, 1);
		Array.set(newInstance, 0, values);
		return (A) newInstance;
	}

	/**
	 * 将集合转为数组
	 *
	 * @param <T>           数组元素类型
	 * @param iterator      {@link Iterator}
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] ofArray(final Iterator<T> iterator, final Class<T> componentType) {
		if (null == iterator) {
			return newArray(componentType, 0);
		}
		return ListUtil.of(iterator).toArray(newArray(componentType, 0));
	}

	/**
	 * 将集合转为数组
	 *
	 * @param <T>           数组元素类型
	 * @param iterable      {@link Iterable}
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] ofArray(final Iterable<T> iterable, final Class<T> componentType) {
		return ofArray(IterUtil.getIter(iterable), componentType);
	}
	// endregion

	// region ----- isBlank

	/**
	 * <p>指定字符串数组中，是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code hasBlank()                  // true}</li>
	 *     <li>{@code hasBlank("", null, " ")     // true}</li>
	 *     <li>{@code hasBlank("123", " ")        // true}</li>
	 *     <li>{@code hasBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>hasBlank(CharSequence...)            等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>{@link #isAllBlank(CharSequence...)} 等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasBlank(final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (StrUtil.isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否存都不为{@code null}或空对象或空白符的对象，通过{@link #hasBlank(CharSequence...)} 判断元素
	 *
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都不为空
	 */
	public static boolean isAllNotBlank(final CharSequence... args) {
		return !hasBlank(args);
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code isAllBlank()                  // true}</li>
	 *     <li>{@code isAllBlank("", null, " ")     // true}</li>
	 *     <li>{@code isAllBlank("123", " ")        // false}</li>
	 *     <li>{@code isAllBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #hasBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #hasBlank(CharSequence...)}   等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>isAllBlank(CharSequence...)          等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllBlank(final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (StrUtil.isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region ----- isEmpty

	/**
	 * 数组是否为空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(final T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 如果给定数组为空，返回默认数组
	 *
	 * @param <T>          数组元素类型
	 * @param array        数组
	 * @param defaultArray 默认数组
	 * @return 非空（empty）的原数组或默认数组
	 * @since 4.6.9
	 */
	public static <T> T[] defaultIfEmpty(final T[] array, final T[] defaultArray) {
		return isEmpty(array) ? defaultArray : array;
	}

	/**
	 * 数组是否为空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
	 * 如果此对象为数组对象，数组长度大于0的情况下返回false，否则返回true
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final Object array) {
		if (array != null) {
			if (isArray(array)) {
				return 0 == Array.getLength(array);
			}
			return false;
		}
		return true;
	}

	/**
	 * 数组是否为非空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return !isEmpty(array);
	}

	/**
	 * 数组是否为非空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回false<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true<br>
	 * 如果此对象为数组对象，数组长度大于0的情况下返回true，否则返回false
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final Object array) {
		return !isEmpty(array);
	}

	/**
	 * 计算{@code null}或空元素对象的个数，通过{@link ObjUtil#isEmpty(Object)} 判断元素
	 *
	 * @param args 被检查的对象,一个或者多个
	 * @return {@code null}或空元素对象的个数
	 * @since 4.5.18
	 */
	public static int emptyCount(final Object... args) {
		int count = 0;
		if (isNotEmpty(args)) {
			for (final Object element : args) {
				if (ObjUtil.isEmpty(element)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 是否存在{@code null}或空对象，通过{@link ObjUtil#isEmpty(Object)} 判断元素<br>
	 * <p>如果提供的数组本身为空，则返回{@code false}</p>
	 *
	 * @param <T>  元素类型
	 * @param args 被检查对象
	 * @return 是否存在 {@code null} 或空对象
	 * @since 4.5.18
	 */
	public static <T> boolean hasEmpty(final T[] args) {
		if (isNotEmpty(args)) {
			for (final T element : args) {
				if (ObjUtil.isEmpty(element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否存在{@code null}或空对象，通过{@link ObjUtil#isEmpty(Object)} 判断元素<br>
	 * <p>如果提供的数组本身为空，则返回{@code false}</p>
	 * <p><strong>限制条件：args的每个item不能是数组、不能是集合</strong></p>
	 *
	 * @param <T>  元素类型
	 * @param args 被检查对象
	 * @return 是否存在 {@code null} 或空对象
	 * @throws IllegalArgumentException 如果提供的args的item存在数组或集合，抛出异常
	 * @author dazer
	 * @since 6.0.0
	 */
	@SafeVarargs
	public static <T> boolean hasEmptyVarargs(final T... args) {
		return hasEmpty(args);
	}

	/**
	 * 是否所有元素都为{@code null}或空对象，通过{@link ObjUtil#isEmpty(Object)} 判断元素
	 * <p>如果提供的数组本身为空，则返回{@code true}</p>
	 *
	 * @param <T>  元素类型
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都为空
	 * @since 4.5.18
	 */
	public static <T> boolean isAllEmpty(final T[] args) {
		for (final T obj : args) {
			if (!ObjUtil.isEmpty(obj)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否所有元素都为{@code null}或空对象，通过{@link ObjUtil#isEmpty(Object)} 判断元素
	 * <p>如果提供的数组本身为空，则返回{@code true}</p>
	 * <p><strong>限制条件：args的每个item不能是数组、不能是集合</strong></p>
	 *
	 * @param <T>  元素类型
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都为空
	 * @throws IllegalArgumentException 如果提供的args的item存在数组或集合，抛出异常
	 * @author dazer
	 * @since 6.0.0
	 */
	@SafeVarargs
	public static <T> boolean isAllEmptyVarargs(final T... args) {
		return isAllEmpty(args);
	}

	/**
	 * 是否所有元素都不为{@code null}或空对象，通过{@link ObjUtil#isEmpty(Object)} 判断元素
	 * <p>如果提供的数组本身为空，则返回{@code true}</p>
	 *
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都不为空
	 * @since 4.5.18
	 */
	public static boolean isAllNotEmpty(final Object... args) {
		return !hasEmpty(args);
	}

	// endregion

	// region ----- isNull or hasNull

	/**
	 * 是否包含{@code null}元素
	 * <p>如果数组为null，则返回{@code true}，如果数组为空，则返回{@code false}</p>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含 {@code null} 元素
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(final T... array) {
		if (isNotEmpty(array)) {
			for (final T element : array) {
				if (ObjUtil.isNull(element)) {
					return true;
				}
			}
		}
		return array == null;
	}

	/**
	 * 所有字段是否全为null
	 * <p>如果数组为{@code null}或者空，则返回 {@code true}</p>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 所有字段是否全为null
	 * @author dahuoyzs
	 * @since 5.4.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isAllNull(final T... array) {
		return null == firstNonNull(array);
	}

	/**
	 * 是否所有元素都不为 {@code null}
	 * <p>如果提供的数组为null，则返回{@code false}，如果提供的数组为空，则返回{@code true}</p>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 是否所有元素都不为 {@code null}
	 * @since 5.4.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isAllNotNull(final T... array) {
		return !hasNull(array);
	}

	/**
	 * 是否包含非{@code null}元素<br>
	 * <p>如果数组是{@code null}或者空，返回{@code false}，否则当数组中有非{@code null}元素时返回{@code true}</p>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含非 {@code null} 元素
	 * @since 5.4.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNonNull(final T... array) {
		return null != firstNonNull(array);
	}

	/**
	 * 返回数组中第一个非空元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 第一个非空元素，如果 不存在非空元素 或 数组为空，返回{@code null}
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> T firstNonNull(final T... array) {
		if (isEmpty(array)) {
			return null;
		}
		return firstMatch(ObjUtil::isNotNull, array);
	}
	// endregion

	// region ----- match

	/**
	 * 返回数组中第一个匹配规则的值
	 *
	 * @param <T>     数组元素类型
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param array   数组
	 * @return 第一个匹配元素，如果 不存在匹配元素 或 数组为空，返回 {@code null}
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> T firstMatch(final Predicate<T> matcher, final T... array) {
		final int index = matchIndex(matcher, array);
		if (index == INDEX_NOT_FOUND) {
			return null;
		}

		return array[index];
	}

	/**
	 * 返回数组中第一个匹配规则的值的位置
	 *
	 * @param <T>     数组元素类型
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param array   数组
	 * @return 第一个匹配元素的位置，{@link #INDEX_NOT_FOUND}表示未匹配到
	 * @since 5.6.6
	 */
	@SuppressWarnings("unchecked")
	public static <T> int matchIndex(final Predicate<T> matcher, final T... array) {
		return matchIndex(0, matcher, array);
	}

	/**
	 * 返回数组中第一个匹配规则的值的位置
	 *
	 * @param <E>               数组元素类型
	 * @param matcher           匹配接口，实现此接口自定义匹配规则
	 * @param beginIndexInclude 检索开始的位置，不能为负数
	 * @param array             数组
	 * @return 第一个匹配元素的位置，{@link #INDEX_NOT_FOUND}表示未匹配到
	 * @since 5.7.3
	 */
	@SuppressWarnings("unchecked")
	public static <E> int matchIndex(final int beginIndexInclude, final Predicate<E> matcher, final E... array) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		final ArrayWrapper<E[], E> arrayWrapper = ArrayWrapper.of(array);
		return arrayWrapper.matchIndex(beginIndexInclude, matcher);
	}
	// endregion

	// region ----- newArray

	/**
	 * 新建一个空数组
	 *
	 * @param <T>           数组元素类型
	 * @param componentType 元素类型，例如：{@code Integer.class}，但是不能使用原始类型，例如：{@code int.class}
	 * @param newSize       大小
	 * @return 空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(final Class<?> componentType, final int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	/**
	 * 新建一个{@code Object}类型空数组
	 *
	 * @param newSize 大小
	 * @return {@code Object}类型的空数组
	 * @since 3.3.0
	 */
	public static Object[] newArray(final int newSize) {
		return new Object[newSize];
	}
	// endregion

	// region ----- type

	/**
	 * 获取数组对象的元素类型，方法调用参数与返回结果举例：
	 * <ul>
	 *     <li>Object[] =》 Object.class</li>
	 *     <li>String[] =》 String.class</li>
	 *     <li>int[] =》 int.class</li>
	 *     <li>Integer[] =》 Integer.class</li>
	 *     <li>null =》 null</li>
	 *     <li>String =》 null</li>
	 * </ul>
	 *
	 * @param array 数组对象
	 * @return 元素类型
	 * @since 3.2.2
	 */
	public static Class<?> getComponentType(final Object array) {
		return null == array ? null : getComponentType(array.getClass());
	}

	/**
	 * 获取数组对象的元素类型，方法调用参数与返回结果举例：
	 * <ul>
	 *     <li>Object[].class =》 Object.class</li>
	 *     <li>String[].class =》 String.class</li>
	 *     <li>int[].class =》 int.class</li>
	 *     <li>Integer[].class =》 Integer.class</li>
	 *     <li>null =》 null</li>
	 * 	   <li>String.class =》 null</li>
	 * </ul>
	 *
	 * @param arrayClass 数组对象的class
	 * @return 元素类型
	 * @since 3.2.2
	 */
	public static Class<?> getComponentType(final Class<?> arrayClass) {
		return null == arrayClass ? null : arrayClass.getComponentType();
	}

	/**
	 * 根据数组元素类型，获取数组的类型<br>
	 * 方法是通过创建一个空数组从而获取其类型
	 * <p>本方法是 {@link #getComponentType(Class)}的逆方法</p>
	 *
	 * @param componentType 数组元素类型
	 * @return 数组类型
	 * @since 3.2.2
	 */
	public static Class<?> getArrayType(final Class<?> componentType) {
		return Array.newInstance(componentType, 0).getClass();
	}

	/**
	 * 强转数组类型<br>
	 * 强制转换的前提是数组元素类型可被强制转换<br>
	 * 强制转换后会生成一个新数组
	 *
	 * @param type     数组类型或数组元素类型
	 * @param arrayObj 原数组
	 * @return 转换后的数组类型
	 * @throws NullPointerException     提供参数为空
	 * @throws IllegalArgumentException 参数arrayObj不是数组
	 * @since 3.0.6
	 */
	public static Object[] cast(final Class<?> type, final Object arrayObj) throws NullPointerException, IllegalArgumentException {
		if (null == arrayObj) {
			throw new NullPointerException("Argument [arrayObj] is null !");
		}
		if (!arrayObj.getClass().isArray()) {
			throw new IllegalArgumentException("Argument [arrayObj] is not array !");
		}
		if (null == type) {
			return (Object[]) arrayObj;
		}

		final Class<?> componentType = type.isArray() ? type.getComponentType() : type;
		final Object[] array = (Object[]) arrayObj;
		final Object[] result = ArrayUtil.newArray(componentType, array.length);
		System.arraycopy(array, 0, result, 0, array.length);
		return result;
	}
	//endregion

	// region ----- append

	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param <T>         数组元素类型
	 * @param buffer      已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> T[] append(final T[] buffer, final T... newElements) {
		if (isEmpty(buffer)) {
			return newElements;
		}
		return insert(buffer, buffer.length, newElements);
	}

	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param <A>         数组类型
	 * @param <T>         数组元素类型
	 * @param array       已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <A, T> A append(final A array, final T... newElements) {
		if (isEmpty(array)) {
			if (null == array) {
				return (A) newElements;
			}
			// 可变长参数可能为包装类型，如果array是原始类型，则此处强转不合适，采用万能转换器完成转换
			return (A) Convert.convert(array.getClass(), newElements);
		}
		return insert(array, length(array), newElements);
	}

	/**
	 * 将元素值设置为数组的某个位置，当给定的index大于等于数组长度，则追加
	 *
	 * @param <T>   数组元素类型
	 * @param array 已有数组
	 * @param index 位置，大于等于长度则追加，否则替换
	 * @param value 新值
	 * @return 新数组或原有数组
	 * @since 4.1.2
	 */
	public static <T> T[] setOrAppend(final T[] array, final int index, final T value) {
		if (isEmpty(array)) {
			return ofArray(value, null == array ? null : array.getClass().getComponentType());
		}
		return ArrayWrapper.of(array).setOrAppend(index, value).getRaw();
	}

	/**
	 * 将元素值设置为数组的某个位置，当给定的index大于等于数组长度，则追加
	 *
	 * @param <A>   数组类型
	 * @param array 已有数组
	 * @param index 位置，大于等于长度则追加，否则替换
	 * @param value 新值
	 * @return 新数组或原有数组
	 * @since 4.1.2
	 */
	public static <A> A setOrAppend(final A array, final int index, final Object value) {
		if (isEmpty(array)) {
			return ofArray(value, null == array ? null : array.getClass().getComponentType());
		}
		return ArrayWrapper.of(array).setOrAppend(index, value).getRaw();
	}

	/**
	 * 将元素值设置为数组的某个位置，当index小于数组的长度时，替换指定位置的值，否则追加{@code null}或{@code 0}直到到达index后，设置值
	 *
	 * @param <A>   数组类型
	 * @param array 已有数组
	 * @param index 位置，大于等于长度则追加，否则替换
	 * @param value 新值
	 * @return 新数组或原有数组
	 * @since 6.0.0
	 */
	public static <A> A setOrPadding(final A array, final int index, final Object value) {
		if (index == 0 && isEmpty(array)) {
			return ofArray(value, null == array ? null : array.getClass().getComponentType());
		}
		return ArrayWrapper.of(array).setOrPadding(index, value).getRaw();
	}

	/**
	 * 将元素值设置为数组的某个位置，当index小于数组的长度时，替换指定位置的值，否则追加paddingValue直到到达index后，设置值
	 *
	 * @param <A>          数组类型
	 * @param <E>          元素类型
	 * @param array        已有数组
	 * @param index        位置，大于等于长度则追加，否则替换
	 * @param value        新值
	 * @param paddingValue 填充值
	 * @return 新数组或原有数组
	 * @since 6.0.0
	 */
	public static <A, E> A setOrPadding(final A array, final int index, final E value, final E paddingValue) {
		if (index == 0 && isEmpty(array)) {
			return ofArray(value, null == array ? null : array.getClass().getComponentType());
		}
		return ArrayWrapper.of(array).setOrPadding(index, value, paddingValue).getRaw();
	}

	/**
	 * 合并所有数组，返回合并后的新数组<br>
	 * 忽略null的数组
	 *
	 * @param <T>    数组元素类型
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 */
	@SafeVarargs
	public static <T> T[] addAll(final T[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		int length = 0;
		for (final T[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}

		final T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);
		if (length == 0) {
			return result;
		}

		length = 0;
		for (final T[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}
	// endregion

	// region ----- replace or insert

	/**
	 * 从数组中的指定位置开始，按顺序使用新元素替换旧元素<br>
	 * <ul>
	 *     <li>如果 指定位置 为负数，那么生成一个新数组，其中新元素按顺序放在数组头部</li>
	 *     <li>如果 指定位置 大于等于 旧数组长度，那么生成一个新数组，其中新元素按顺序放在数组尾部</li>
	 *     <li>如果 指定位置 加上 新元素数量 大于 旧数组长度，那么生成一个新数组，指定位置之前是旧数组元素，指定位置及之后为新元素</li>
	 *     <li>否则，从已有数组中的指定位置开始，按顺序使用新元素替换旧元素，返回旧数组</li>
	 * </ul>
	 *
	 * @param <T>    数组元素类型
	 * @param buffer 已有数组
	 * @param index  位置
	 * @param values 新值
	 * @return 新数组或原有数组
	 * @since 5.7.23
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T[] replace(final T[] buffer, final int index, final T... values) {
		if (isEmpty(buffer)) {
			return values;
		}
		return ArrayWrapper.of(buffer).replace(index, values).getRaw();
	}

	/**
	 * 从数组中的指定位置开始，按顺序使用新元素替换旧元素<br>
	 * <ul>
	 *     <li>如果 指定位置 为负数，那么生成一个新数组，其中新元素按顺序放在数组头部</li>
	 *     <li>如果 指定位置 大于等于 旧数组长度，那么生成一个新数组，其中新元素按顺序放在数组尾部</li>
	 *     <li>如果 指定位置 加上 新元素数量 大于 旧数组长度，那么生成一个新数组，指定位置之前是旧数组元素，指定位置及之后为新元素</li>
	 *     <li>否则，从已有数组中的指定位置开始，按顺序使用新元素替换旧元素，返回旧数组</li>
	 * </ul>
	 *
	 * @param <A>    数组类型
	 * @param array  已有数组
	 * @param index  位置
	 * @param values 新值
	 * @return 新数组或原有数组
	 * @since 5.7.23
	 */
	public static <A> A replace(final A array, final int index, final A values) {
		if (isEmpty(array)) {
			return ofArray(values, null == array ? null : array.getClass().getComponentType());
		}
		return ArrayWrapper.of(array).replace(index, values).getRaw();
	}

	/**
	 * 将新元素插入到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充<br>
	 *
	 * @param <T>         数组元素类型
	 * @param buffer      已有数组
	 * @param index       插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(final T[] buffer, final int index, final T... newElements) {
		return (T[]) insert((Object) buffer, index, newElements);
	}

	/**
	 * 将新元素插入到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为负数，从原数组从后向前计数，若大于原数组长度，则空白处用默认值填充<br>
	 *
	 * @param <A>         数组类型
	 * @param <T>         数组元素类型
	 * @param array       已有数组，可以为原始类型数组
	 * @param index       插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SafeVarargs
	public static <A, T> A insert(final A array, final int index, final T... newElements) {
		return ArrayWrapper.of(array).insert(index, newElements).getRaw();
	}
	// endregion

	// region ----- resize

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后，按顺序拷贝原数组到新数组中，新长度更小则截断<br>
	 *
	 * @param <T>           数组元素类型
	 * @param data          原数组
	 * @param newSize       新的数组大小
	 * @param componentType 数组元素类型
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(final T[] data, final int newSize, final Class<?> componentType) {
		if (newSize < 0) {
			return data;
		}

		final T[] newArray = newArray(componentType, newSize);
		if (newSize > 0 && isNotEmpty(data)) {
			System.arraycopy(data, 0, newArray, 0, Math.min(data.length, newSize));
		}
		return newArray;
	}

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后，按顺序拷贝原数组到新数组中，新长度更小则截断<br>
	 *
	 * @param array   原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 * @see System#arraycopy(Object, int, Object, int, int)
	 * @since 4.6.7
	 */
	public static Object resize(final Object array, final int newSize) {
		if (newSize < 0) {
			return array;
		}
		if (null == array) {
			return null;
		}
		final int length = length(array);
		final Object newArray = Array.newInstance(array.getClass().getComponentType(), newSize);
		if (newSize > 0 && isNotEmpty(array)) {
			//noinspection SuspiciousSystemArraycopy
			System.arraycopy(array, 0, newArray, 0, Math.min(length, newSize));
		}
		return newArray;
	}

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后，按顺序拷贝原数组到新数组中，新长度更小则截断原数组<br>
	 *
	 * @param <T>     数组元素类型
	 * @param buffer  原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(final T[] buffer, final int newSize) {
		return resize(buffer, newSize, buffer.getClass().getComponentType());
	}
	// endregion

	// region ----- copy and clone

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制，源数组和目标数组都是从位置0开始复制，复制长度为源数组的长度<br>
	 *
	 * @param <T>  目标数组类型
	 * @param src  源数组
	 * @param dest 目标数组
	 * @return 目标数组
	 */
	public static <T> T copy(final Object src, final T dest) {
		return copy(src, dest, length(src));
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制，源数组和目标数组都是从位置0开始复制<br>
	 *
	 * @param <T>    目标数组类型
	 * @param src    源数组
	 * @param dest   目标数组
	 * @param length 拷贝数组长度
	 * @return 目标数组
	 * @since 3.0.6
	 */
	public static <T> T copy(final Object src, final T dest, final int length) {
		return copy(src, 0, dest, 0, length);
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制
	 *
	 * @param <T>     目标数组类型
	 * @param src     源数组
	 * @param srcPos  源数组开始位置
	 * @param dest    目标数组
	 * @param destPos 目标数组开始位置
	 * @param length  拷贝数组长度
	 * @return 目标数组
	 * @since 3.0.6
	 */
	public static <T> T copy(final Object src, final int srcPos, final T dest, final int destPos, final int length) {
		//noinspection SuspiciousSystemArraycopy
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}

	/**
	 * 克隆数组
	 *
	 * @param <T>   数组元素类型
	 * @param array 被克隆的数组
	 * @return 新数组
	 */
	public static <T> T[] clone(final T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * 克隆数组，如果非数组返回{@code null}
	 *
	 * @param <T> 数组元素类型
	 * @param obj 数组对象
	 * @return 克隆后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(final T obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			final Object result;
			final Class<?> componentType = obj.getClass().getComponentType();
			// 原始类型
			if (componentType.isPrimitive()) {
				final int length = Array.getLength(obj);
				result = Array.newInstance(componentType, length);
				copy(obj, result, length);
			} else {
				result = ((Object[]) obj).clone();
			}
			return (T) result;
		}
		return null;
	}
	// endregion

	// region ----- filter

	/**
	 * 对每个数组元素执行指定操作，返回操作后的元素<br>
	 * 这个Editor实现可以实现以下功能：
	 * <ol>
	 *     <li>过滤出需要的对象，如果返回{@code null}则抛弃这个元素对象</li>
	 *     <li>修改元素对象，返回修改后的对象</li>
	 * </ol>
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return 编辑后的数组
	 * @since 5.3.3
	 */
	public static <T> T[] edit(final T[] array, final UnaryOperator<T> editor) {
		if (null == array || null == editor) {
			return array;
		}

		final List<T> list = new ArrayList<>(array.length);
		T modified;
		for (final T t : array) {
			modified = editor.apply(t);
			if (null != modified) {
				list.add(modified);
			}
		}
		final T[] result = newArray(array.getClass().getComponentType(), list.size());
		return list.toArray(result);
	}

	/**
	 * 过滤数组元素<br>
	 * 保留 {@link Predicate#test(Object)}为{@code true}的元素
	 *
	 * @param <T>       数组元素类型
	 * @param array     数组
	 * @param predicate 过滤器接口，用于定义过滤规则，为{@code null}则返回原数组
	 * @return 过滤后的数组
	 * @since 3.2.1
	 */
	public static <T> T[] filter(final T[] array, final Predicate<T> predicate) {
		if (null == array || null == predicate) {
			return array;
		}
		return edit(array, t -> predicate.test(t) ? t : null);
	}

	/**
	 * 去除 {@code null} 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T> T[] removeNull(final T[] array) {
		// 返回元素本身，如果为null便自动过滤
		return edit(array, UnaryOperator.identity());
	}

	/**
	 * 去除{@code null}或者"" 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T extends CharSequence> T[] removeEmpty(final T[] array) {
		return filter(array, StrUtil::isNotEmpty);
	}

	/**
	 * 去除{@code null}或者""或者空白字符串 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T extends CharSequence> T[] removeBlank(final T[] array) {
		return filter(array, StrUtil::isNotBlank);
	}

	/**
	 * 数组元素中的null转换为""
	 *
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.1
	 */
	public static String[] nullToEmpty(final String[] array) {
		return edit(array, t -> null == t ? StrUtil.EMPTY : t);
	}
	// endregion

	// region ----- zip

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 *
	 * @param <K>     Key类型
	 * @param <V>     Value类型
	 * @param keys    键列表
	 * @param values  值列表
	 * @param isOrder Map中的元素是否保留键值数组本身的顺序
	 * @return Map
	 * @since 3.0.4
	 */
	public static <K, V> Map<K, V> zip(final K[] keys, final V[] values, final boolean isOrder) {
		if (isEmpty(keys) || isEmpty(values)) {
			return MapUtil.newHashMap(0, isOrder);
		}

		final int size = Math.min(keys.length, values.length);
		final Map<K, V> map = MapUtil.newHashMap(size, isOrder);
		for (int i = 0; i < size; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	/**
	 * 映射键值（参考Python的zip()函数），返回Map无序<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 *
	 * @param <K>    Key类型
	 * @param <V>    Value类型
	 * @param keys   键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static <K, V> Map<K, V> zip(final K[] keys, final V[] values) {
		return zip(keys, values, false);
	}
	// endregion

	// region ----- indexOf and lastIndexOf and contains

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>               数组类型
	 * @param array             数组
	 * @param value             被检查的元素
	 * @param beginIndexInclude 检索开始的位置
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static <T> int indexOf(final T[] array, final Object value, final int beginIndexInclude) {
		return ArrayWrapper.of(array).indexOf(value, beginIndexInclude);
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>   数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static <T> int indexOf(final T[] array, final Object value) {
		return ArrayWrapper.of(array).indexOf(value);
	}

	/**
	 * 返回数组中指定元素所在位置，忽略大小写，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.1.2
	 */
	public static int indexOfIgnoreCase(final CharSequence[] array, final CharSequence value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (StrUtil.equalsIgnoreCase(array[i], value)) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>   数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static <T> int lastIndexOf(final T[] array, final Object value) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOf(array, value, array.length - 1);
	}

	/**
	 * 返回数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>        数组类型
	 * @param array      数组
	 * @param value      被检查的元素
	 * @param endInclude 从后向前查找时的起始位置，一般为{@code array.length - 1}
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 5.7.3
	 */
	public static <T> int lastIndexOf(final T[] array, final Object value, final int endInclude) {
		if (isNotEmpty(array)) {
			for (int i = endInclude; i >= 0; i--) {
				if (ObjUtil.equals(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}
	// endregion

	// region ----- contains

	/**
	 * 数组中是否包含指定元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static <T> boolean contains(final T[] array, final T value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含指定元素中的任意一个
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param values 被检查的多个元素
	 * @return 是否包含指定元素中的任意一个
	 * @since 4.1.20
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean containsAny(final T[] array, final T... values) {
		for (final T value : values) {
			if (contains(array, value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数组中是否包含所有指定元素
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param values 被检查的多个元素
	 * @return 是否包含所有指定元素
	 * @since 5.4.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean containsAll(final T[] array, final T... values) {
		for (final T value : values) {
			if (!contains(array, value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 数组中是否包含元素，忽略大小写
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.1.2
	 */
	public static boolean containsIgnoreCase(final CharSequence[] array, final CharSequence value) {
		return indexOfIgnoreCase(array, value) > INDEX_NOT_FOUND;
	}
	// endregion

	/**
	 * 包装数组对象
	 *
	 * @param obj 对象，可以是对象数组或者基本类型数组
	 * @return 包装类型数组或对象数组
	 * @throws HutoolException 对象为非数组
	 */
	public static Object[] wrap(final Object obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			try {
				final String className = obj.getClass().getComponentType().getName();
				switch (className) {
					case "long":
						return wrap((long[]) obj);
					case "int":
						return wrap((int[]) obj);
					case "short":
						return wrap((short[]) obj);
					case "char":
						return wrap((char[]) obj);
					case "byte":
						return wrap((byte[]) obj);
					case "boolean":
						return wrap((boolean[]) obj);
					case "float":
						return wrap((float[]) obj);
					case "double":
						return wrap((double[]) obj);
					default:
						return (Object[]) obj;
				}
			} catch (final Exception e) {
				throw ExceptionUtil.wrapRuntime(e);
			}
		}
		throw new HutoolException(StrUtil.format("[{}] is not Array!", obj.getClass()));
	}

	/**
	 * 对象是否为数组对象
	 *
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(final Object obj) {
		return null != obj && obj.getClass().isArray();
	}

	// region ----- get

	/**
	 * 获取数组对象中指定index的值，支持负数，例如-1表示倒数第一个值<br>
	 * 如果数组下标越界，返回null
	 *
	 * @param <E>   数组元素类型
	 * @param array 数组对象
	 * @param index 下标，支持负数
	 * @return 值
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	public static <E> E get(final Object array, final int index) {
		return (E) ArrayWrapper.of(array).get(index);
	}

	/**
	 * 获取满足条件的第一个元素
	 *
	 * @param array     数组
	 * @param predicate 条件
	 * @param <E>       元素类型
	 * @return 满足条件的第一个元素，未找到返回{@code null}
	 */
	public static <E> E get(final E[] array, final Predicate<E> predicate) {
		for (final E e : array) {
			if (predicate.test(e)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取数组中所有指定位置的元素值，组成新数组
	 *
	 * @param <T>     数组元素类型
	 * @param array   数组，如果提供为{@code null}则返回{@code null}
	 * @param indexes 下标列表
	 * @return 指定位置的元素值数组
	 */
	public static <T> T[] getAny(final Object array, final int... indexes) {
		if (null == array) {
			return null;
		}
		if (null == indexes) {
			return newArray(array.getClass().getComponentType(), 0);
		}

		final T[] result = newArray(array.getClass().getComponentType(), indexes.length);
		for (int i = 0; i < indexes.length; i++) {
			result[i] = ArrayUtil.get(array, indexes[i]);
		}
		return result;
	}
	// endregion

	/**
	 * 数组或集合转String
	 *
	 * @param obj 集合或数组对象
	 * @return 数组字符串，与集合转字符串格式相同
	 */
	public static String toString(final Object obj) {
		if (Objects.isNull(obj)) {
			return null;
		}
		if (obj instanceof long[]) {
			return Arrays.toString((long[]) obj);
		} else if (obj instanceof int[]) {
			return Arrays.toString((int[]) obj);
		} else if (obj instanceof short[]) {
			return Arrays.toString((short[]) obj);
		} else if (obj instanceof char[]) {
			return Arrays.toString((char[]) obj);
		} else if (obj instanceof byte[]) {
			return Arrays.toString((byte[]) obj);
		} else if (obj instanceof boolean[]) {
			return Arrays.toString((boolean[]) obj);
		} else if (obj instanceof float[]) {
			return Arrays.toString((float[]) obj);
		} else if (obj instanceof double[]) {
			return Arrays.toString((double[]) obj);
		} else if (ArrayUtil.isArray(obj)) {
			// 对象数组
			try {
				return Arrays.deepToString((Object[]) obj);
			} catch (final Exception ignore) {
				//ignore
			}
		}

		return obj.toString();
	}

	/**
	 * 获取数组长度<br>
	 * 如果参数为{@code null}，返回0
	 *
	 * <pre>
	 * ArrayUtil.length(null)            = 0
	 * ArrayUtil.length([])              = 0
	 * ArrayUtil.length([null])          = 1
	 * ArrayUtil.length([true, false])   = 2
	 * ArrayUtil.length([1, 2, 3])       = 3
	 * ArrayUtil.length(["a", "b", "c"]) = 3
	 * </pre>
	 *
	 * @param array 数组对象
	 * @return 数组长度
	 * @throws IllegalArgumentException 如果参数不为数组，抛出此异常
	 * @see Array#getLength(Object)
	 * @since 3.0.8
	 */
	public static int length(final Object array) throws IllegalArgumentException {
		if (null == array) {
			return 0;
		}
		return Array.getLength(array);
	}

	// region ----- join

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param <T>         数组元素类型
	 * @param array       数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(final T[] array, final CharSequence conjunction) {
		return join(array, conjunction, null, null);
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param <T>       数组元素类型
	 * @param array     数组
	 * @param delimiter 分隔符
	 * @param prefix    每个元素添加的前缀，null表示不添加
	 * @param suffix    每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 * @since 4.0.10
	 */
	public static <T> String join(final T[] array, final CharSequence delimiter, final String prefix, final String suffix) {
		if (null == array) {
			return null;
		}

		return StrJoiner.of(delimiter, prefix, suffix)
			// 每个元素都添加前后缀
			.setWrapElement(true)
			.append(array)
			.toString();
	}

	/**
	 * 先处理数组元素，再以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param <T>         数组元素类型
	 * @param array       数组
	 * @param conjunction 分隔符
	 * @param editor      每个元素的编辑器，null表示不编辑
	 * @return 连接后的字符串
	 * @since 5.3.3
	 */
	public static <T> String join(final T[] array, final CharSequence conjunction, final UnaryOperator<T> editor) {
		return StrJoiner.of(conjunction).append(edit(array, editor)).toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param array       数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(final Object array, final CharSequence conjunction) {
		if (null == array) {
			return null;
		}
		if (!isArray(array)) {
			throw new IllegalArgumentException(StrUtil.format("[{}] is not a Array!", array.getClass()));
		}

		return StrJoiner.of(conjunction).append(array).toString();
	}
	// endregion

	// region ----- remove

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(final T[] array, final int index) throws IllegalArgumentException {
		return (T[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素<br>
	 * copy from commons-lang<br>
	 *
	 * @param <T>     数组元素类型
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static <T> T[] removeEle(final T[] array, final T element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}
	// endregion

	// region ----- reverse

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param <T>                 数组元素类型
	 * @param array               数组，会变更
	 * @param startIndexInclusive 开始位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static <T> T[] reverse(final T[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		T tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static <T> T[] reverse(final T[] array) {
		return reverse(array, 0, array.length);
	}
	// endregion

	// region ----- min and max

	/**
	 * 取最小值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static <T extends Comparable<? super T>> T min(final T[] numberArray) {
		return min(numberArray, null);
	}

	/**
	 * 取最小值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @param comparator  比较器，null按照默认比较
	 * @return 最小值
	 * @since 5.3.4
	 */
	public static <T extends Comparable<? super T>> T min(final T[] numberArray, final Comparator<T> comparator) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T min = numberArray[0];
		for (final T t : numberArray) {
			if (CompareUtil.compare(min, t, comparator) > 0) {
				min = t;
			}
		}
		return min;
	}

	/**
	 * 取最大值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static <T extends Comparable<? super T>> T max(final T[] numberArray) {
		return max(numberArray, null);
	}

	/**
	 * 取最大值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @param comparator  比较器，null表示默认比较器
	 * @return 最大值
	 * @since 5.3.4
	 */
	public static <T extends Comparable<? super T>> T max(final T[] numberArray, final Comparator<T> comparator) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (CompareUtil.compare(max, numberArray[i], comparator) < 0) {
				max = numberArray[i];
			}
		}
		return max;
	}
	//endregion

	// region ----- shuffle

	/**
	 * 打乱数组顺序，会变更原数组<br>
	 * 使用Fisher–Yates洗牌算法，以线性时间复杂度打乱数组顺序
	 *
	 * @param <T>   元素类型
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static <T> T[] shuffle(final T[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组<br>
	 * 使用Fisher–Yates洗牌算法，以线性时间复杂度打乱数组顺序
	 *
	 * @param <T>    元素类型
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static <T> T[] shuffle(final T[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}
	// endregion

	// region ----- swap

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param <T>    元素类型
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static <T> T[] swap(final T[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		final T tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组对象
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static Object swap(final Object array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		final Object tmp = get(array, index1);
		Array.set(array, index1, Array.get(array, index2));
		Array.set(array, index2, tmp);
		return array;
	}
	//endregion

	// region ----- distinct

	/**
	 * 去重数组中的元素，去重后生成新的数组，原数组不变<br>
	 * 此方法通过{@link LinkedHashSet} 去重
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 去重后的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] distinct(final T[] array) {
		if (isEmpty(array)) {
			return array;
		}

		final Set<T> set = new LinkedHashSet<>(array.length, 1);
		Collections.addAll(set, array);
		return ofArray(set, (Class<T>) getComponentType(array));
	}

	/**
	 * 去重数组中的元素，去重后生成新的数组，原数组不变<br>
	 * 此方法通过{@link LinkedHashSet} 去重
	 *
	 * @param <T>             数组元素类型
	 * @param <K>             唯一键类型
	 * @param array           数组
	 * @param uniqueGenerator 唯一键生成器
	 * @param override        是否覆盖模式，如果为{@code true}，加入的新值会覆盖相同key的旧值，否则会忽略新加值
	 * @return 去重后的数组
	 * @since 5.8.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T[] distinct(final T[] array, final Function<T, K> uniqueGenerator, final boolean override) {
		if (isEmpty(array)) {
			return array;
		}

		final UniqueKeySet<K, T> set = new UniqueKeySet<>(true, uniqueGenerator);
		if (override) {
			Collections.addAll(set, array);
		} else {
			for (final T t : array) {
				set.addIfAbsent(t);
			}
		}
		return ofArray(set, (Class<T>) getComponentType(array));
	}
	// endregion

	// region ----- map

	/**
	 * 按照指定规则，将一种类型的数组转换为另一种类型
	 *
	 * @param array               被转换的数组
	 * @param targetComponentType 目标的元素类型
	 * @param func                转换规则函数
	 * @param <T>                 原数组类型
	 * @param <R>                 目标数组类型
	 * @return 转换后的数组
	 * @since 5.4.2
	 */
	public static <T, R> R[] map(final T[] array, final Class<R> targetComponentType, final Function<? super T, ? extends R> func) {
		final int length = length(array);
		final R[] result = newArray(targetComponentType, length);
		for (int i = 0; i < length; i++) {
			result[i] = func.apply(array[i]);
		}
		return result;
	}

	/**
	 * 按照指定规则，将一种类型的数组转换为另一种类型
	 *
	 * @param array               被转换的数组
	 * @param targetComponentType 目标的元素类型
	 * @param func                转换规则函数
	 * @param <T>                 原数组类型
	 * @param <R>                 目标数组类型
	 * @return 转换后的数组
	 * @since 5.5.8
	 */
	public static <T, R> R[] map(final Object array, final Class<R> targetComponentType, final Function<? super T, ? extends R> func) {
		final int length = length(array);
		final R[] result = newArray(targetComponentType, length);
		for (int i = 0; i < length; i++) {
			result[i] = func.apply(get(array, i));
		}
		return result;
	}

	/**
	 * 按照指定规则，将一种类型的数组元素转换为另一种类型，并保存为 {@link List}
	 *
	 * @param array 被转换的数组
	 * @param func  转换规则函数
	 * @param <T>   原数组类型
	 * @param <R>   目标数组类型
	 * @return 列表
	 * @since 5.5.7
	 */
	public static <T, R> List<R> map(final T[] array, final Function<? super T, ? extends R> func) {
		return Arrays.stream(array).map(func).collect(Collectors.toList());
	}

	/**
	 * 按照指定规则，将一种类型的数组元素转换为另一种类型，并保存为 {@link Set}
	 *
	 * @param array 被转换的数组
	 * @param func  转换规则函数
	 * @param <T>   原数组类型
	 * @param <R>   目标数组类型
	 * @return 集合
	 * @since 5.8.0
	 */
	public static <T, R> Set<R> mapToSet(final T[] array, final Function<? super T, ? extends R> func) {
		return Arrays.stream(array).map(func).collect(Collectors.toSet());
	}

	/**
	 * 按照指定规则，将一种类型的数组元素转换为另一种类型，并保存为 {@link Set}
	 *
	 * @param array     被转换的数组
	 * @param func      转换规则函数
	 * @param generator 数组生成器，如返回String[]，则传入String[]::new
	 * @param <T>       原数组类型
	 * @param <R>       目标数组类型
	 * @return 集合
	 */
	public static <T, R> R[] mapToArray(final T[] array, final Function<? super T, ? extends R> func,
										final IntFunction<R[]> generator) {
		return Arrays.stream(array).map(func).toArray(generator);
	}
	// endregion

	/**
	 * 判断两个数组是否相等，判断依据包括数组长度和每个元素都相等。
	 *
	 * @param array1 数组1
	 * @param array2 数组2
	 * @return 是否相等
	 * @since 5.4.2
	 */
	public static boolean equals(final Object array1, final Object array2) {
		if (array1 == array2) {
			return true;
		}
		if (hasNull(array1, array2)) {
			return false;
		}

		Assert.isTrue(isArray(array1), "First is not a Array !");
		Assert.isTrue(isArray(array2), "Second is not a Array !");

		if (array1 instanceof long[]) {
			return Arrays.equals((long[]) array1, (long[]) array2);
		} else if (array1 instanceof int[]) {
			return Arrays.equals((int[]) array1, (int[]) array2);
		} else if (array1 instanceof short[]) {
			return Arrays.equals((short[]) array1, (short[]) array2);
		} else if (array1 instanceof char[]) {
			return Arrays.equals((char[]) array1, (char[]) array2);
		} else if (array1 instanceof byte[]) {
			return Arrays.equals((byte[]) array1, (byte[]) array2);
		} else if (array1 instanceof double[]) {
			return Arrays.equals((double[]) array1, (double[]) array2);
		} else if (array1 instanceof float[]) {
			return Arrays.equals((float[]) array1, (float[]) array2);
		} else if (array1 instanceof boolean[]) {
			return Arrays.equals((boolean[]) array1, (boolean[]) array2);
		} else {
			// Not an array of primitives
			return Arrays.deepEquals((Object[]) array1, (Object[]) array2);
		}
	}

	// region ----- sub

	/**
	 * 获取子数组
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组，不允许为空
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.2.2
	 */
	public static <T> T[] sub(final T[] array, int start, int end) {
		Assert.notNull(array, "array must be not null !");
		final int length = length(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (start >= length) {
			return newArray(array.getClass().getComponentType(), 0);
		}
		if (end > length) {
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array        数组
	 * @param beginInclude 开始位置（包括）
	 * @param endExclude   结束位置（不包括）
	 * @param <A>          数组类型
	 * @return 新的数组
	 * @since 4.0.6
	 */
	public static <A> A sub(final A array,
							final int beginInclude, final int endExclude) {
		return ArrayWrapper.of(array).getSub(beginInclude, endExclude);
	}

	/**
	 * 获取子数组
	 *
	 * @param array        数组
	 * @param beginInclude 开始位置（包括）
	 * @param endExclude   结束位置（不包括）
	 * @param step         步进
	 * @param <A>          数组类型
	 * @return 新的数组
	 * @since 4.0.6
	 */
	public static <A> A sub(final A array,
							final int beginInclude, final int endExclude, final int step) {
		return ArrayWrapper.of(array).getSub(beginInclude, endExclude, step);
	}

	/**
	 * 是否是数组的子数组
	 *
	 * @param array    数组
	 * @param subArray 子数组
	 * @param <T>      数组元素类型
	 * @return 是否是数组的子数组
	 * @since 5.4.8
	 */
	public static <T> boolean isSub(final T[] array, final T[] subArray) {
		return indexOfSub(array, subArray) > INDEX_NOT_FOUND;
	}

	/**
	 * 查找子数组的位置
	 *
	 * @param array    数组
	 * @param subArray 子数组
	 * @param <T>      数组元素类型
	 * @return 子数组的开始位置，即子数字第一个元素在数组中的位置
	 * @since 5.4.8
	 */
	public static <T> int indexOfSub(final T[] array, final T[] subArray) {
		return indexOfSub(array, 0, subArray);
	}

	/**
	 * 查找子数组的位置
	 *
	 * @param array        数组
	 * @param beginInclude 查找开始的位置（包含）
	 * @param subArray     子数组
	 * @param <T>          数组元素类型
	 * @return 子数组的开始位置，即子数字第一个元素在数组中的位置
	 * @since 5.4.8
	 */
	public static <T> int indexOfSub(final T[] array, final int beginInclude, final T[] subArray) {
		if (isEmpty(array) || isEmpty(subArray) || subArray.length > array.length) {
			return INDEX_NOT_FOUND;
		}
		final int firstIndex = indexOf(array, subArray[0], beginInclude);
		if (firstIndex < 0 || firstIndex + subArray.length > array.length) {
			return INDEX_NOT_FOUND;
		}

		for (int i = 0; i < subArray.length; i++) {
			if (!ObjUtil.equals(array[i + firstIndex], subArray[i])) {
				return indexOfSub(array, firstIndex + 1, subArray);
			}
		}

		return firstIndex;
	}

	/**
	 * 查找最后一个子数组的开始位置
	 *
	 * @param array    数组
	 * @param subArray 子数组
	 * @param <T>      数组元素类型
	 * @return 最后一个子数组的开始位置，即子数字第一个元素在数组中的位置
	 * @since 5.4.8
	 */
	public static <T> int lastIndexOfSub(final T[] array, final T[] subArray) {
		if (isEmpty(array) || isEmpty(subArray)) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfSub(array, array.length - 1, subArray);
	}

	/**
	 * 查找最后一个子数组的开始位置
	 *
	 * @param array      数组
	 * @param endInclude 从后往前查找时的开始位置（包含）
	 * @param subArray   子数组
	 * @param <T>        数组元素类型
	 * @return 最后一个子数组的开始位置，即从后往前，子数字第一个元素在数组中的位置
	 * @since 5.4.8
	 */
	public static <T> int lastIndexOfSub(final T[] array, final int endInclude, final T[] subArray) {
		if (isEmpty(array) || isEmpty(subArray) || subArray.length > array.length || endInclude < 0) {
			return INDEX_NOT_FOUND;
		}

		final int firstIndex = lastIndexOf(array, subArray[0], endInclude);
		if (firstIndex < 0 || firstIndex + subArray.length > array.length) {
			return INDEX_NOT_FOUND;
		}

		for (int i = 0; i < subArray.length; i++) {
			if (!ObjUtil.equals(array[i + firstIndex], subArray[i])) {
				return lastIndexOfSub(array, firstIndex - 1, subArray);
			}
		}

		return firstIndex;
	}
	// region

	// region ----- isSorted O(n)时间复杂度检查数组是否有序

	/**
	 * 检查数组是否有序，升序或者降序，使用指定比较器比较
	 * <p>若传入空数组或空比较器，则返回{@code false}；元素全部相等，返回 {@code true}</p>
	 *
	 * @param <T>        数组元素类型
	 * @param array      数组
	 * @param comparator 比较器，需要自己处理null值比较
	 * @return 数组是否有序
	 * @since 6.0.0
	 */
	public static <T> boolean isSorted(final T[] array, final Comparator<? super T> comparator) {
		if (isEmpty(array) || null == comparator) {
			return false;
		}

		final int size = array.length - 1;
		final int cmp = comparator.compare(array[0], array[size]);
		if (cmp < 0) {
			return isSortedASC(array, comparator);
		} else if (cmp > 0) {
			return isSortedDESC(array, comparator);
		}
		for (int i = 0; i < size; i++) {
			if (comparator.compare(array[i], array[i + 1]) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查数组是否有序，升序或者降序
	 * <p>若传入空数组，则返回{@code false}；元素全部相等，返回 {@code true}</p>
	 *
	 * @param <T>   数组元素类型，该类型需要实现Comparable接口
	 * @param array 数组
	 * @return 数组是否有序
	 * @throws NullPointerException 如果数组元素含有null值
	 * @since 6.0.0
	 */
	public static <T extends Comparable<? super T>> boolean isSorted(final T[] array) {
		if (isEmpty(array)) {
			return false;
		}
		final int size = array.length - 1;
		final int cmp = array[0].compareTo(array[size]);
		if (cmp < 0) {
			return isSortedASC(array);
		} else if (cmp > 0) {
			return isSortedDESC(array);
		}
		for (int i = 0; i < size; i++) {
			if (array[i].compareTo(array[i + 1]) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查数组是否升序，即 {@code array[i].compareTo(array[i + 1]) <= 0}
	 * <p>若传入空数组，则返回{@code false}</p>
	 *
	 * @param <T>   数组元素类型，该类型需要实现Comparable接口
	 * @param array 数组
	 * @return 数组是否升序
	 * @throws NullPointerException 如果数组元素含有null值
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static <T extends Comparable<? super T>> boolean isSortedASC(final T[] array) {
		if (isEmpty(array)) {
			return false;
		}

		final int size = array.length - 1;
		for (int i = 0; i < size; i++) {
			if (array[i].compareTo(array[i + 1]) > 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即 {@code array[i].compareTo(array[i + 1]) >= 0}
	 * <p>若传入空数组，则返回{@code false}</p>
	 *
	 * @param <T>   数组元素类型，该类型需要实现Comparable接口
	 * @param array 数组
	 * @return 数组是否降序
	 * @throws NullPointerException 如果数组元素含有null值
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static <T extends Comparable<? super T>> boolean isSortedDESC(final T[] array) {
		if (isEmpty(array)) {
			return false;
		}

		final int size = array.length - 1;
		for (int i = 0; i < size; i++) {
			if (array[i].compareTo(array[i + 1]) < 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，使用指定的比较器比较，即 {@code comparator.compare(array[i], array[i + 1]) <= 0}
	 * <p>若传入空数组或空比较器，则返回{@code false}</p>
	 *
	 * @param <T>        数组元素类型
	 * @param array      数组
	 * @param comparator 比较器，需要自己处理null值比较
	 * @return 数组是否升序
	 * @since 6.0.0
	 */
	public static <T> boolean isSortedASC(final T[] array, final Comparator<? super T> comparator) {
		if (isEmpty(array) || null == comparator) {
			return false;
		}

		final int size = array.length - 1;
		for (int i = 0; i < size; i++) {
			if (comparator.compare(array[i], array[i + 1]) > 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，使用指定的比较器比较，即 {@code comparator.compare(array[i], array[i + 1]) >= 0}
	 * <p>若传入空数组或空比较器，则返回{@code false}</p>
	 *
	 * @param <T>        数组元素类型
	 * @param array      数组
	 * @param comparator 比较器，需要自己处理null值比较
	 * @return 数组是否降序
	 * @since 6.0.0
	 */
	public static <T> boolean isSortedDESC(final T[] array, final Comparator<? super T> comparator) {
		if (isEmpty(array) || null == comparator) {
			return false;
		}

		final int size = array.length - 1;
		for (int i = 0; i < size; i++) {
			if (comparator.compare(array[i], array[i + 1]) < 0) {
				return false;
			}
		}

		return true;
	}
	// endregion

	/**
	 * 判断数组中是否有相同元素
	 * <p>若传入空数组，则返回{@code false}</p>
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 数组是否有相同元素
	 * @since 6.0.0
	 */
	public static <T> Boolean hasSameElement(final T[] array) {
		if (isEmpty(array)) {
			return false;
		}

		final Set<T> elementSet = SetUtil.of(Arrays.asList(array));
		return elementSet.size() != array.length;
	}

	/**
	 * array数组是否以prefix开头，每个元素的匹配使用{@link ObjUtil#equals(Object, Object)}匹配。
	 * <ul>
	 *     <li>array和prefix为同一个数组（即array == prefix），返回{@code true}</li>
	 *     <li>array或prefix为空数组（null或length为0的数组），返回{@code true}</li>
	 *     <li>prefix长度大于array，返回{@code false}</li>
	 * </ul>
	 *
	 * @param array  数组
	 * @param prefix 前缀
	 * @param <T>    数组元素类型
	 * @return 是否开头
	 */
	public static <T> boolean startWith(final T[] array, final T[] prefix) {
		if (array == prefix) {
			return true;
		}
		if (isEmpty(array)) {
			return isEmpty(prefix);
		}
		if (prefix.length > array.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (ObjUtil.notEquals(array[i], prefix[i])) {
				return false;
			}
		}
		return true;
	}
}
