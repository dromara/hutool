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

package org.dromara.hutool.codec;

import java.util.Objects;

/**
 * 128位数字表示，分高位和低位
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class Number128 extends Number {
	private static final long serialVersionUID = 1L;

	private long lowValue;
	private long highValue;

	/**
	 * 构造
	 *
	 * @param lowValue  低位
	 * @param highValue 高位
	 */
	public Number128(final long lowValue, final long highValue) {
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	/**
	 * 获取低位值
	 *
	 * @return 地位值
	 */
	public long getLowValue() {
		return lowValue;
	}

	/**
	 * 设置低位值
	 *
	 * @param lowValue 低位值
	 */
	public void setLowValue(final long lowValue) {
		this.lowValue = lowValue;
	}

	/**
	 * 获取高位值
	 *
	 * @return 高位值
	 */
	public long getHighValue() {
		return highValue;
	}

	/**
	 * 设置高位值
	 *
	 * @param hiValue 高位值
	 */
	public void setHighValue(final long hiValue) {
		this.highValue = hiValue;
	}

	/**
	 * 获取高低位数组，long[0]：低位，long[1]：高位
	 *
	 * @return 高低位数组，long[0]：低位，long[1]：高位
	 */
	public long[] getLongArray() {
		return new long[]{lowValue, highValue};
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return this.lowValue;
	}

	@Override
	public float floatValue() {
		return longValue();
	}

	@Override
	public double doubleValue() {
		return longValue();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Number128) {
			final Number128 number128 = (Number128) o;
			return lowValue == number128.lowValue && highValue == number128.highValue;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lowValue, highValue);
	}
}
