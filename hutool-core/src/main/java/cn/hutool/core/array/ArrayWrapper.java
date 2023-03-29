package cn.hutool.core.array;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Wrapper;
import cn.hutool.core.util.ObjUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 数组包装，提供一系列数组方法
 *
 * @param <A> 数组类型
 * @author looly
 * @since 6.0.0
 */
public class ArrayWrapper<A> implements Wrapper<A> {

	private final Class<?> componentType;
	private A array;
	private int length;

	/**
	 * 创建ArrayWrapper，创建一个指定长度的空数组
	 *
	 * @param componentType 元素类型
	 * @param length        长度
	 * @param <A>           数组类型
	 * @return ArrayWrapper
	 */
	@SuppressWarnings("unchecked")
	public static <A> ArrayWrapper<A> of(final Class<?> componentType, final int length) {
		return (ArrayWrapper<A>) of(Array.newInstance(componentType, length));
	}

	/**
	 * 包装数组为ArrayWrapper
	 *
	 * @param array 数组（非空）
	 * @param <A>   数组类型
	 * @return ArrayWrapper
	 */
	public static <A> ArrayWrapper<A> of(final A array) {
		return new ArrayWrapper<>(array);
	}

	/**
	 * 构造
	 *
	 * @param array 数组对象（非空）
	 */
	public ArrayWrapper(final A array) {
		Assert.notNull(array, "Array must be not null!");
		if (false == ArrayUtil.isArray(array)) {
			throw new IllegalArgumentException("Object is not a array!");
		}
		this.componentType = array.getClass().getComponentType();
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
	 * @param <E>   数组元素类型
	 * @param index 下标，支持负数，-1表示最后一个元素
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(int index) {
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
	 * @param <E> 数组元素类型
	 * @return 第一个非空元素，如果 不存在非空元素 或 数组为空，返回{@code null}
	 */
	public <E> E firstNonNull() {
		return firstMatch(ObjUtil::isNotNull);
	}

	/**
	 * 返回数组中第一个匹配规则的值
	 *
	 * @param <E>     元素类型
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @return 第一个匹配元素，如果 不存在匹配元素 或 数组为空，返回 {@code null}
	 */
	public <E> E firstMatch(final Predicate<?> matcher) {
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
	public int matchIndex(final Predicate<?> matcher) {
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
	public int matchIndex(final int offset, final Predicate<?> matcher) {
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
	public int matchLastIndex(final Predicate<?> matcher) {
		return matchLastIndex(length - 1, matcher);
	}

	/**
	 * 返回数组中最后一个匹配规则的值的位置(从后向前查找)
	 *
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param offset  从后向前查找时的起始位置，一般为{@code array.length - 1}
	 * @return 最后一个匹配元素的位置，{@link ArrayUtil#INDEX_NOT_FOUND}表示未匹配到
	 */
	public int matchLastIndex(final int offset, final Predicate<?> matcher) {
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
	 * 将元素值设置为数组的某个位置，当给定的index大于等于数组长度，则追加
	 *
	 * @param index 位置，大于等于长度则追加，否则替换
	 * @param value 新元素或新数组
	 * @return this
	 */
	public ArrayWrapper<A> setOrAppend(final int index, final Object value) {
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
	 * @param newElements 新元素或新数组
	 * @return 新数组
	 */
	public ArrayWrapper<A> append(final Object newElements) {
		return insert(this.length, newElements);
	}

	/**
	 * 将新元素插入到已有数组中的某个位置
	 * 如果插入位置为负数，从原数组从后向前计数，若大于原数组长度，则空白处用默认值填充<br>
	 *
	 * @param index         插入位置，支持负数。此位置为对应此位置元素之前的空档
	 * @param arrayToAppend 新元素
	 * @return 新数组
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public ArrayWrapper<A> insert(int index, Object arrayToAppend) {
		if (false == ArrayUtil.isArray(arrayToAppend)) {
			// 用户传入单个元素则创建单元素数组
			arrayToAppend = createSingleElementArray(arrayToAppend);
		}

		final int appendLength = ArrayUtil.length(arrayToAppend);
		if (0 == appendLength) {
			return this;
		}
		if (isEmpty()) {
			setNewArray((A) Convert.convert(array.getClass(), arrayToAppend));
			return this;
		}

		final int len = this.length;
		if (index < 0) {
			index = (index % len) + len;
		}

		// 已有数组的元素类型
		// 如果 已有数组的元素类型是 原始类型，则需要转换 新元素数组 为该类型，避免ArrayStoreException
		if (this.componentType.isPrimitive()) {
			arrayToAppend = Convert.convert(array.getClass(), arrayToAppend);
		}

		final A result = (A) Array.newInstance(this.componentType, Math.max(len, index) + appendLength);
		// 原数组到index位置
		System.arraycopy(array, 0, result, 0, Math.min(len, index));
		// 新增的数组追加
		System.arraycopy(arrayToAppend, 0, result, index, appendLength);
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
	 * @param values 新值
	 * @return this
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public ArrayWrapper<A> replace(final int index, Object values) {
		if (false == ArrayUtil.isArray(values)) {
			// 用户传入单个元素则创建单元素数组
			values = createSingleElementArray(values);
		}

		final int valuesLength = ArrayUtil.length(values);
		if (0 == valuesLength) {
			return this;
		}
		if (isEmpty()) {
			setNewArray((A) Convert.convert(array.getClass(), values));
		}
		if (index < 0) {
			// 从头部追加
			return insert(0, values);
		}
		if (index >= length) {
			// 超出长度，尾部追加
			return append(values);
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
	public ArrayWrapper<A> edit(final UnaryOperator<?> editor) {
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
	 * @param begin 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.2.2
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public A getSub(int begin, int end) {
		final int length = this.length;
		if (begin < 0) {
			begin += length;
		}
		if (end < 0) {
			end += length;
		}
		if (begin > end) {
			final int tmp = begin;
			begin = end;
			end = tmp;
		}
		if (begin >= length) {
			return (A) Array.newInstance(this.componentType, 0);
		}
		if (end > length) {
			end = length;
		}

		final A result = (A) Array.newInstance(this.componentType, end - begin);
		System.arraycopy(this.array, begin, result, 0, end - begin);
		return result;
	}

	@Override
	public String toString() {
		final A array = this.array;
		if (null == array) {
			return null;
		}

		if (array instanceof long[]) {
			return Arrays.toString((long[]) array);
		} else if (array instanceof int[]) {
			return Arrays.toString((int[]) array);
		} else if (array instanceof short[]) {
			return Arrays.toString((short[]) array);
		} else if (array instanceof char[]) {
			return Arrays.toString((char[]) array);
		} else if (array instanceof byte[]) {
			return Arrays.toString((byte[]) array);
		} else if (array instanceof boolean[]) {
			return Arrays.toString((boolean[]) array);
		} else if (array instanceof float[]) {
			return Arrays.toString((float[]) array);
		} else if (array instanceof double[]) {
			return Arrays.toString((double[]) array);
		} else if (ArrayUtil.isArray(array)) {
			// 对象数组
			try {
				return Arrays.deepToString((Object[]) array);
			} catch (final Exception ignore) {
				//ignore
			}
		}

		return array.toString();
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

	/**
	 * 创建单一元素数组
	 *
	 * @param value 元素值
	 * @return 数组
	 */
	private Object createSingleElementArray(final Object value) {
		// 插入单个元素
		final Object newInstance = Array.newInstance(this.componentType, 1);
		Array.set(newInstance, 0, value);
		return newInstance;
	}
	// endregion
}
