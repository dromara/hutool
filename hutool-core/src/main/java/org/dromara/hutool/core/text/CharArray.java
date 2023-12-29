package org.dromara.hutool.core.text;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;

import java.util.Arrays;
import java.util.Iterator;

/**
 * char[]包装，提供zero-copy的数组操作
 *
 * @author Looly
 */
public class CharArray implements CharSequence, Iterable<Character> {

	private final char[] value;

	/**
	 * 构造
	 *
	 * @param value String值
	 */
	public CharArray(final String value) {
		this(value.toCharArray(), false);
	}

	/**
	 * 构造，注意此方法共享数组
	 *
	 * @param value char数组
	 * @param copy  可选是否拷贝数组，如果为{@code false}则复用数组
	 */
	public CharArray(final char[] value, final boolean copy) {
		this.value = copy ? value.clone() : value;
	}

	@Override
	public int length() {
		return value.length;
	}

	@Override
	public char charAt(int index) {
		if (index < 0) {
			index += value.length;
		}
		return value[index];
	}

	/**
	 * 设置字符
	 *
	 * @param index 位置，支持复数，-1表示最后一个位置
	 * @param c     字符
	 * @return this
	 */
	public CharArray set(int index, final char c) {
		if (index < 0) {
			index += value.length;
		}
		value[index] = c;
		return this;
	}

	/**
	 * 获取原始数组，不做拷贝
	 *
	 * @return array
	 */
	public char[] array() {
		return this.value;
	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		return new CharArray(ArrayUtil.sub(value, start, end), false);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CharArray charArray = (CharArray) o;
		return Arrays.equals(value, charArray.value);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	@Override
	public Iterator<Character> iterator() {
		return new ArrayIter<>(this.value);
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
