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

package org.dromara.hutool.core.codec;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;
import java.util.Objects;

/**
 * 128位数字表示，分为：
 * <ul>
 *     <li>最高有效位（Most Significant Bit），64 bit（8 bytes）</li>
 *     <li>最低有效位（Least Significant Bit），64 bit（8 bytes）</li>
 * </ul>
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class Number128 extends Number implements Comparable<Number128>{
	private static final long serialVersionUID = 1L;

	/**
	 * 最高有效位（Most Significant Bit），64 bit（8 bytes）
	 */
	private long mostSigBits;
	/**
	 * 最低有效位（Least Significant Bit），64 bit（8 bytes）
	 */
	private long leastSigBits;

	/**
	 * 构造
	 *
	 * @param mostSigBits 高位
	 * @param leastSigBits  低位
	 */
	public Number128(final long mostSigBits, final long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}

	/**
	 * 获取最高有效位（Most Significant Bit），64 bit（8 bytes）
	 *
	 * @return 最高有效位（Most Significant Bit），64 bit（8 bytes）
	 */
	public long getMostSigBits() {
		return mostSigBits;
	}

	/**
	 * 设置最高有效位（Most Significant Bit），64 bit（8 bytes）
	 *
	 * @param hiValue 最高有效位（Most Significant Bit），64 bit（8 bytes）
	 */
	public void setMostSigBits(final long hiValue) {
		this.mostSigBits = hiValue;
	}

	/**
	 * 获取最低有效位（Least Significant Bit），64 bit（8 bytes）
	 *
	 * @return 最低有效位（Least Significant Bit），64 bit（8 bytes）
	 */
	public long getLeastSigBits() {
		return leastSigBits;
	}

	/**
	 * 设置最低有效位（Least Significant Bit），64 bit（8 bytes）
	 *
	 * @param leastSigBits 最低有效位（Least Significant Bit），64 bit（8 bytes）
	 */
	public void setLeastSigBits(final long leastSigBits) {
		this.leastSigBits = leastSigBits;
	}

	/**
	 * 获取高低位数组，long[0]：低位，long[1]：高位
	 *
	 * @return 高低位数组，long[0]：低位，long[1]：高位
	 */
	public long[] getLongArray() {
		return getLongArray(ByteOrder.BIG_ENDIAN);
	}

	/**
	 * 获取高低位数组，规则为：
	 * <ul>
	 *     <li>{@link ByteOrder#LITTLE_ENDIAN}，则long[0]：低位，long[1]：高位</li>
	 *     <li>{@link ByteOrder#BIG_ENDIAN}，则long[0]：高位，long[1]：低位</li>
	 * </ul>
	 *
	 *
	 * @param byteOrder 端续
	 * @return 高低位数组，long[0]：低位，long[1]：高位
	 */
	public long[] getLongArray(final ByteOrder byteOrder) {
		if(byteOrder == ByteOrder.BIG_ENDIAN){
			return new long[]{leastSigBits, mostSigBits};
		} else{
			return new long[]{mostSigBits, leastSigBits};
		}
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return this.leastSigBits;
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
			return leastSigBits == number128.leastSigBits && mostSigBits == number128.mostSigBits;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(leastSigBits, mostSigBits);
	}

	@Override
	public int compareTo(@NotNull final Number128 o) {
		final int mostSigBits = Long.compare(this.mostSigBits, o.mostSigBits);
		return mostSigBits != 0 ? mostSigBits : Long.compare(this.leastSigBits, o.leastSigBits);
	}


}
