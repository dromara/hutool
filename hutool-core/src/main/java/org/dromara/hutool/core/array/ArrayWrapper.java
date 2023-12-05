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

import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 数组包装，提供一系列数组方法
 *
 * @param <A> 数组类型
 * @param <E> 数组元素类型
 * @author looly
 * @since 6.0.0
 */
public class ArrayWrapper<A, E> implements Wrapper<A>, Iterable<E> {

	private final Class<E> componentType;
	private A array;
	private int length;

	/**
	 * 创建ArrayWrapper，创建一个指定长度的空数组
	 *
	 * @param componentType 元素类型
	 * @param length        长度
	 * @param <A>           数组类型
	 * @param <E>           数组元素类型
	 * @return ArrayWrapper
	 */
	@SuppressWarnings("unchecked")
	public static <A, E> ArrayWrapper<A, E> of(final Class<E> componentType, final int length) {
		return (ArrayWrapper<A, E>) of(Array.newInstance(componentType, length));
	}

	/**
	 * 包装数组为ArrayWrapper
	 *
	 * @param array 数组（非空）
	 * @param <A>   数组类型
	 * @param <E>   元素类型
	 * @return ArrayWrapper
	 */
	public static <A, E> ArrayWrapper<A, E> of(final A array) {
		return new ArrayWrapper<>(array);
	}

	/**
	 * 构造
	 *
	 * @param array 数组对象（非空）
	 */
	@SuppressWarnings("unchecked")
	public ArrayWrapper(final A array) {
		Assert.notNull(array, "Array must be not null!");
		if (!ArrayUtil.isArray(array)) {
			throw new IllegalArgumentException("Object is not a array!");
		}
		this.componentType = (Class<E>) array.getClass().getComponentType();
		setNewArray(array);
	}

	@Override
	public A getRaw() {
		return this.array;
	}

	/**
	 * 获取数组长度
	 *
	 * @return 数组长度
	 */
	public int length() {
		return length;
	}

	/**
	 * 是否原始类型数组
	 *
	 * @return 是否原始类型数组
	 */
	public boolean isPrimitive() {
		return this.componentType.isPrimitive();
	}

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
	 * @return 元素类型
	 */
	public Class<?> getComponentType() {
		return this.componentType;
	}

	/**
	 * 获得数组类型
	 *
	 * @return 数组类型
	 */
	public Class<?> getArrayType() {
		return array.getClass();
	}

	/**
	 * 数组是否为空
	 *
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return 0 == length;
	}

	/**
	 * 获取数组对象中指定index的值，支持负数，例如-1表示倒数第一个值<br>
	 * 如果数组下标越界，返回null
	 *
	 * @param index 下标，支持负数，-1表示最后一个元素
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public E get(int index) {
		final int length = this.length;
		if (index < 0) {
			index += length;
		}
		if (index < 0 || index >= length) {
			return null;
		}
		return (E) Array.get(array, index);
	}

	// region ----- index

	/**
	 * 返回数组中第一个非空元素
	 *
	 * @return 第一个非空元素，如果 不存在非空元素 或 数组为空，返回{@code null}
	 */
	public E firstNonNull() {
		return firstMatch(ObjUtil::isNotNull);
	}

	/**
	 * 返回数组中第一个匹配规则的值
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @return 第一个匹配元素，如果 不存在匹配元素 或 数组为空，返回 {@code null}
	 */
	public E firstMatch(final Predicate<E> matcher) {
		final int index = matchIndex(matcher);
		if (index == ArrayUtil.INDEX_NOT_FOUND) {
			return null;
		}

		return get(index);
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 *
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 */
	public int indexOf(final Object value) {
		return matchIndex((obj) -> ObjUtil.equals(value, obj));
	}

	/**
	 * 返回数组中第一个匹配规则的值的位置
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @return 第一个匹配元素的位置，{@link ArrayUtil#INDEX_NOT_FOUND}表示未匹配到
	 */
	public int matchIndex(final Predicate<E> matcher) {
		return matchIndex(0, matcher);
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 *
	 * @param value  被检查的元素
	 * @param offset 开始的位置
	 * @return 数组中指定元素所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 */
	public int indexOf(final Object value, final int offset) {
		return matchIndex(offset, (obj) -> ObjUtil.equals(value, obj));
	}

	/**
	 * 返回数组中第一个匹配规则的值的位置
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param offset  检索开始的位置，不能为负数
	 * @return 第一个匹配元素的位置，{@link ArrayUtil#INDEX_NOT_FOUND}表示未匹配到
	 */
	public int matchIndex(final int offset, final Predicate<E> matcher) {
		if (null == matcher && offset < this.length) {
			return offset;
		}
		for (int i = offset; i < length; i++) {
			if (matcher.test(get(i))) {
				return i;
			}
		}

		return ArrayUtil.INDEX_NOT_FOUND;
	}
	// endregion

	// region ----- last index

	/**
	 * 返回数组中指定最后的所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 *
	 * @param value 被检查的元素
	 * @return 数组中指定元素最后的所在位置，未找到返回{@link ArrayUtil#INDEX_NOT_FOUND}
	 */
	public int lastIndexOf(final Object value) {
		return matchLastIndex((obj) -> ObjUtil.equals(value, obj));
	}

	/**
	 * 返回数组中最后一个匹配规则的值的位置(从后向前查找)
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @return 最后一个匹配元素的位置，{@link ArrayUtil#INDEX_NOT_FOUND}表示未匹配到
	 */
	public int matchLastIndex(final Predicate<E> matcher) {
		return matchLastIndex(length - 1, matcher);
	}

	/**
	 * 返回数组中最后一个匹配规则的值的位置(从后向前查找)
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param offset  从后向前查找时的起始位置，一般为{@code array.length - 1}
	 * @return 最后一个匹配元素的位置，{@link ArrayUtil#INDEX_NOT_FOUND}表示未匹配到
	 */
	public int matchLastIndex(final int offset, final Predicate<E> matcher) {
		if (null == matcher && offset >= 0) {
			return offset;
		}
		for (int i = Math.min(offset, length - 1); i >= 0; i--) {
			if (matcher.test(get(i))) {
				return i;
			}
		}

		return ArrayUtil.INDEX_NOT_FOUND;
	}
	// endregion

	/**
	 * 将元素值设置为数组的某个位置，当index小于数组的长度时，替换指定位置的值，否则追加{@code null}或{@code 0}直到到达index后，设置值
	 *
	 * @param index 位置
	 * @param value 新元素或新数组
	 * @return this
	 * @since 6.0.0
	 */
	@SuppressWarnings("unchecked")
	public ArrayWrapper<A, E> setOrPadding(final int index, final E value) {
		return setOrPadding(index, value, (E) ClassUtil.getDefaultValue(this.componentType));
	}

	/**
	 * 将元素值设置为数组的某个位置，当index小于数组的长度时，替换指定位置的值，否则追加{@code paddingElement}直到到达index后，设置值
	 *
	 * @param index 位置
	 * @param value 新元素或新数组
	 * @param paddingElement 填充
	 * @return this
	 * @since 6.0.0
	 */
	public ArrayWrapper<A, E> setOrPadding(final int index, final E value, final E paddingElement) {
		if (index < this.length) {
			Array.set(array, index, value);
		} else {
			// issue#3286, 增加安全检查，最多增加10倍
			Validator.checkIndexLimit(index, this.length);

			for (int i = length; i < index; i++) {
				append(paddingElement);
			}
			append(value);
		}

		return this;
	}

	/**
	 * 将元素值设置为数组的某个位置，当给定的index大于等于数组长度，则追加
	 *
	 * @param index 位置，大于等于长度则追加，否则替换
	 * @param value 新元素或新数组
	 * @return this
	 */
	public ArrayWrapper<A, E> setOrAppend(final int index, final E value) {
		if (index < this.length) {
			Array.set(array, index, value);
		} else {
			append(value);
		}

		return this;
	}

	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param element 新元素或新数组
	 * @return 新数组
	 */
	public ArrayWrapper<A, E> append(final E element) {
		return insert(this.length, element);
	}

	/**
	 * 将新数组追加到已有数组中<br>
	 * 追加新数组会生成一个新的数组，不影响原数组
	 *
	 * @param array 需要追加的数组数组
	 * @return 新数组
	 */
	public ArrayWrapper<A, E> appendArray(final A array) {
		return insertArray(this.length, array);
	}

	/**
	 * 将新元素插入到已有数组中的某个位置
	 * 如果插入位置为负数，从原数组从后向前计数，若大于原数组长度，则空白处用默认值填充<br>
	 *
	 * @param index   插入位置，支持负数。此位置为对应此位置元素之前的空档
	 * @param element 元素
	 * @return 新数组
	 */
	public ArrayWrapper<A, E> insert(final int index, final E element) {
		return insertArray(index, ArrayUtil.ofArray(element, this.componentType));
	}

	/**
	 * 将新元素插入到已有数组中的某个位置
	 * 如果插入位置为负数，从原数组从后向前计数，若大于原数组长度，则空白处用默认值填充<br>
	 *
	 * @param index         插入位置，支持负数。此位置为对应此位置元素之前的空档
	 * @param arrayToInsert 新元素数组
	 * @return 新数组
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public ArrayWrapper<A, E> insertArray(int index, A arrayToInsert) {
		final int appendLength = ArrayUtil.length(arrayToInsert);
		if (0 == appendLength) {
			return this;
		}
		if (isEmpty()) {
			setNewArray((A) Convert.convert(array.getClass(), arrayToInsert));
			return this;
		}

		final int len = this.length;
		if (index < 0) {
			index = (index % len) + len;
		}

		// 已有数组的元素类型
		// 如果 已有数组的元素类型是 原始类型，则需要转换 新元素数组 为该类型，避免ArrayStoreException
		if (this.componentType.isPrimitive()) {
			arrayToInsert = (A) Convert.convert(array.getClass(), arrayToInsert);
		}

		final A result = (A) Array.newInstance(this.componentType, Math.max(len, index) + appendLength);
		// 原数组到index位置
		System.arraycopy(array, 0, result, 0, Math.min(len, index));
		// 新增的数组追加
		System.arraycopy(arrayToInsert, 0, result, index, appendLength);
		if (index < len) {
			// 原数组剩余部分
			System.arraycopy(array, index, result, index + appendLength, len - index);
		}
		setNewArray(result);

		return this;
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
	 * @param index  位置
	 * @param values 新值或新数组
	 * @return this
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public ArrayWrapper<A, E> replace(final int index, final A values) {
		final int valuesLength = ArrayUtil.length(values);
		if (0 == valuesLength) {
			return this;
		}
		if (isEmpty()) {
			setNewArray((A) Convert.convert(array.getClass(), values));
		}
		if (index < 0) {
			// 从头部追加
			return insertArray(0, values);
		}
		if (index >= length) {
			// 超出长度，尾部追加
			return appendArray(values);
		}

		// 在原数组范围内
		if (length >= valuesLength + index) {
			System.arraycopy(values, 0, this.array, index, valuesLength);
			return this;
		}

		// 超出范围，替换长度大于原数组长度，新建数组
		final A result = (A) Array.newInstance(this.componentType, index + valuesLength);
		System.arraycopy(this.array, 0, result, 0, index);
		System.arraycopy(values, 0, result, index, valuesLength);
		setNewArray(result);

		return this;
	}

	/**
	 * 对每个数组元素执行指定操作，替换元素为修改后的元素
	 *
	 * @param editor 编辑器接口，为 {@code null}则返回原数组
	 * @return this
	 */
	public ArrayWrapper<A, E> edit(final UnaryOperator<E> editor) {
		if (null == array || null == editor) {
			return this;
		}

		for (int i = 0; i < length; i++) {
			setOrAppend(i, editor.apply(get(i)));
		}
		return this;
	}

	/**
	 * 获取子数组
	 *
	 * @param beginInclude 开始位置（包括）
	 * @param endExclude   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.2.2
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public A getSub(int beginInclude, int endExclude) {
		final int length = this.length;
		if (beginInclude < 0) {
			beginInclude += length;
		}
		if (endExclude < 0) {
			endExclude += length;
		}
		if (beginInclude > endExclude) {
			final int tmp = beginInclude;
			beginInclude = endExclude;
			endExclude = tmp;
		}
		if (beginInclude >= length) {
			return (A) Array.newInstance(this.componentType, 0);
		}
		if (endExclude > length) {
			endExclude = length;
		}

		final A result = (A) Array.newInstance(this.componentType, endExclude - beginInclude);
		System.arraycopy(this.array, beginInclude, result, 0, endExclude - beginInclude);
		return result;
	}

	/**
	 * 获取子数组
	 *
	 * @param beginInclude 开始位置（包括）
	 * @param endExclude   结束位置（不包括）
	 * @param step         步进
	 * @return 新的数组
	 */
	@SuppressWarnings("unchecked")
	public A getSub(int beginInclude, int endExclude, int step) {
		final int length = this.length;
		if (beginInclude < 0) {
			beginInclude += length;
		}
		if (endExclude < 0) {
			endExclude += length;
		}
		if (beginInclude > endExclude) {
			final int tmp = beginInclude;
			beginInclude = endExclude;
			endExclude = tmp;
		}
		if (beginInclude >= length) {
			return (A) Array.newInstance(this.componentType, 0);
		}
		if (endExclude > length) {
			endExclude = length;
		}

		if (step <= 1) {
			step = 1;
		}

		final int size = (endExclude - beginInclude + step - 1) / step;
		final A result = (A) Array.newInstance(this.componentType, size);
		int j = 0;
		for (int i = beginInclude; i < endExclude; i += step) {
			Array.set(result, j, get(i));
			j++;
		}
		return result;
	}

	/**
	 * 检查数组是否有序，升序或者降序
	 * <p>若传入空数组，则返回{@code false}；元素全部相等，返回 {@code true}</p>
	 *
	 * @param comparator 比较器
	 * @return 数组是否有序
	 * @throws NullPointerException 如果数组元素含有null值
	 * @since 6.0.0
	 */
	public boolean isSorted(final Comparator<E> comparator) {
		if (isEmpty()) {
			return false;
		}
		final int lastIndex = this.length - 1;
		// 对比第一个和最后一个元素，大致预估这个数组是升序还是降序
		final int cmp = comparator.compare(get(0), get(lastIndex));
		if (cmp < 0) {
			return isSorted(comparator, false);
		} else if (cmp > 0) {
			return isSorted(comparator, true);
		}

		// 可能全等数组
		for (int i = 0; i < lastIndex; i++) {
			if (comparator.compare(get(i), get(i + 1)) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 数组是否有有序
	 * <ul>
	 *     <li>反序，前一个小于后一个则返回错</li>
	 *     <li>正序，前一个大于后一个则返回错</li>
	 * </ul>
	 *
	 * @param comparator {@link Comparator}
	 * @param isDESC     是否反序
	 * @return 是否有序
	 */
	public boolean isSorted(final Comparator<E> comparator, final boolean isDESC) {
		if (null == comparator) {
			return false;
		}

		int compare;
		for (int i = 0; i < this.length - 1; i++) {
			compare = comparator.compare(get(i), get(i + 1));
			// 反序，前一个小于后一个则返回错
			if (isDESC && compare < 0) {
				return false;
			}
			// 正序，前一个大于后一个则返回错
			if(!isDESC && compare > 0){
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayIter<>(this.array);
	}

	@Override
	public String toString() {
		return ArrayUtil.toString(this.array);
	}

	// region ----- private methods

	/**
	 * 设置新数组，并更新长度
	 *
	 * @param newArray 数组
	 */
	private void setNewArray(final A newArray) {
		this.array = newArray;
		this.length = Array.getLength(newArray);
	}
	// endregion
}
