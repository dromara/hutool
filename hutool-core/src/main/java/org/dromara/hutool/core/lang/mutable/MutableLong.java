/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.comparator.CompareUtil;

/**
 * 可变 {@code long} 类型
 *
 * @see Long
 * @since 3.0.1
 */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {
	private static final long serialVersionUID = 1L;

	private long value;

	/**
	 * 构造，默认值0
	 */
	public MutableLong() {
	}

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public MutableLong(final long value) {
		this.value = value;
	}

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public MutableLong(final Number value) {
		this(value.longValue());
	}

	/**
	 * 构造
	 *
	 * @param value String值
	 * @throws NumberFormatException 数字转换错误
	 */
	public MutableLong(final String value) throws NumberFormatException {
		this.value = Long.parseLong(value);
	}

	@Override
	public Long get() {
		return this.value;
	}

	/**
	 * 设置值
	 *
	 * @param value 值
	 */
	public void set(final long value) {
		this.value = value;
	}

	@Override
	public void set(final Number value) {
		this.value = value.longValue();
	}

	// -----------------------------------------------------------------------

	/**
	 * 值+1
	 *
	 * @return this
	 */
	public MutableLong increment() {
		value++;
		return this;
	}

	/**
	 * 值减一
	 *
	 * @return this
	 */
	public MutableLong decrement() {
		value--;
		return this;
	}

	// -----------------------------------------------------------------------

	/**
	 * 增加值
	 *
	 * @param operand 被增加的值
	 * @return this
	 */
	public MutableLong add(final long operand) {
		this.value += operand;
		return this;
	}

	/**
	 * 增加值
	 *
	 * @param operand 被增加的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableLong add(final Number operand) {
		this.value += operand.longValue();
		return this;
	}

	/**
	 * 减去值
	 *
	 * @param operand 被减的值
	 * @return this
	 */
	public MutableLong subtract(final long operand) {
		this.value -= operand;
		return this;
	}

	/**
	 * 减去值
	 *
	 * @param operand 被减的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableLong subtract(final Number operand) {
		this.value -= operand.longValue();
		return this;
	}

	// -----------------------------------------------------------------------
	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	// -----------------------------------------------------------------------

	/**
	 * 相等需同时满足如下条件：
	 * <ol>
	 * 	<li>非空</li>
	 * 	<li>类型为 MutableLong</li>
	 * 	<li>值相等</li>
	 * </ol>
	 *
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 {@code false}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableLong) {
			return value == ((MutableLong) obj).longValue();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) (value ^ (value >>> 32));
	}

	// -----------------------------------------------------------------------

	/**
	 * 比较
	 *
	 * @param other 其它 MutableLong 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableLong other) {
		return CompareUtil.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
