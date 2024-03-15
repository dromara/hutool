/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.codec.Number128;
import org.dromara.hutool.core.codec.binary.CrockfordBase32Codec;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * 参考：https://github.com/zjcscut/framework-mesh/blob/master/ulid4j/src/main/java/cn/vlts/ulid/ULID.java
 * <pre>{@code
 *   01AN4Z07BY      79KA1307SR9X4MV3
 *  |----------|    |----------------|
 *   Timestamp          Randomness
 *    48bits             80bits
 * }</pre>
 *
 * @author throwable
 * @since 6.0.0
 */
public class ULID implements Comparable<ULID>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Timestamp component mask
	 */
	private static final long TIMESTAMP_MASK = 0xffff000000000000L;
	/**
	 * The length of randomness component of ULID
	 */
	private static final int RANDOMNESS_BYTE_LEN = 10;
	/**
	 * The least significant 64 bits increase overflow, 0xffffffffffffffffL + 1
	 */
	private static final long OVERFLOW = 0x0000000000000000L;

	// region ----- Factory methods
	public static ULID of() {
		return of(System.currentTimeMillis(), RandomUtil.randomBytes(RANDOMNESS_BYTE_LEN));
	}

	public static ULID of(final String ulidString) {
		Objects.requireNonNull(ulidString, "ulidString must not be null!");
		if (ulidString.length() != 26) {
			throw new IllegalArgumentException("ulidString must be exactly 26 chars long.");
		}

		final String timeString = ulidString.substring(0, 10);
		final long time = CrockfordBase32Codec.parseCrockford(timeString);
		checkTimestamp(time);

		final String part1String = ulidString.substring(10, 18);
		final String part2String = ulidString.substring(18);
		final long part1 = CrockfordBase32Codec.parseCrockford(part1String);
		final long part2 = CrockfordBase32Codec.parseCrockford(part2String);

		final long most = (time << 16) | (part1 >>> 24);
		final long least = part2 | (part1 << 40);
		return new ULID(new Number128(least, most));
	}

	public static ULID of(final byte[] data) {
		Objects.requireNonNull(data, "data must not be null!");
		if (data.length != 16) {
			throw new IllegalArgumentException("data must be 16 bytes in length!");
		}
		long mostSignificantBits = 0;
		long leastSignificantBits = 0;
		for (int i = 0; i < 8; i++) {
			mostSignificantBits = (mostSignificantBits << 8) | (data[i] & 0xff);
		}
		for (int i = 8; i < 16; i++) {
			leastSignificantBits = (leastSignificantBits << 8) | (data[i] & 0xff);
		}
		return new ULID(new Number128(leastSignificantBits, mostSignificantBits));
	}

	public static ULID of(final long timestamp, final byte[] randomness) {
		// 时间戳最多为48 bit(6 bytes)
		checkTimestamp(timestamp);
		Assert.notNull(randomness);
		// 随机数部分长度必须为80 bit(10 bytes)
		Assert.isTrue(RANDOMNESS_BYTE_LEN == randomness.length, "Invalid randomness");

		long msb = 0;
		// 时间戳左移16位，低位补零准备填入部分随机数位，即16_bit_uint_random
		msb |= timestamp << 16;
		// randomness[0]左移0位填充到16_bit_uint_random的高8位，randomness[1]填充到16_bit_uint_random的低8位
		msb |= (long) (randomness[0x0] & 0xff) << 8;
		// randomness[1]填充到16_bit_uint_random的低8位
		msb |= randomness[0x1] & 0xff;

		return new ULID(new Number128(ByteUtil.toLong(randomness, 2, ByteOrder.BIG_ENDIAN), msb));
	}

	// endregion

	private final Number128 idValue;

	/**
	 * Creates a new ULID with the high 64 bits and low 64 bits as long value.
	 *
	 * @param number128 the low 8 bytes of ULID
	 */
	public ULID(final Number128 number128) {
		this.idValue = number128;
	}

	public long getMostSignificantBits() {
		return this.idValue.getMostSigBits();
	}

	public long getLeastSignificantBits() {
		return this.idValue.getLeastSigBits();
	}

	/**
	 * Get the timestamp component of ULID
	 *
	 * @return the timestamp component
	 */
	public long getTimestamp() {
		return this.idValue.getMostSigBits() >>> 16;
	}

	/**
	 * Get the randomness component of ULID
	 *
	 * @return the randomness component
	 */
	public byte[] getRandomness() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		final byte[] randomness = new byte[RANDOMNESS_BYTE_LEN];
		// 这里不需要& 0xff，因为多余的位会被截断
		randomness[0x0] = (byte) (msb >>> 8);
		randomness[0x1] = (byte) msb;

		ByteUtil.fill(lsb, 2, ByteOrder.BIG_ENDIAN, randomness);
		return randomness;
	}

	public ULID increment() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		long newMsb = msb;
		final long newLsb = lsb + 1;
		if (newLsb == OVERFLOW) {
			newMsb += 1;
		}
		return new ULID(new Number128(lsb, msb));
	}

	public byte[] toBytes() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		final byte[] result = new byte[16];
		for (int i = 0; i < 8; i++) {
			result[i] = (byte) ((msb >> ((7 - i) * 8)) & 0xFF);
		}
		for (int i = 8; i < 16; i++) {
			result[i] = (byte) ((lsb >> ((15 - i) * 8)) & 0xFF);
		}

		return result;
	}

	public UUID toUUID() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		return new UUID(msb, lsb);
	}

	public java.util.UUID toJdkUUID() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		return new java.util.UUID(msb, lsb);
	}

	@Override
	public int compareTo(final ULID o) {
		return this.idValue.compareTo(o.idValue);
	}

	@Override
	public boolean equals(final Object obj) {
		if ((Objects.isNull(obj)) || (obj.getClass() != ULID.class)) {
			return false;
		}
		final ULID id = (ULID) obj;
		return this.idValue.equals(id.idValue);
	}

	@Override
	public int hashCode() {
		return this.idValue.hashCode();
	}

	@Override
	public String toString() {
		final long msb = this.idValue.getMostSigBits();
		final long lsb = this.idValue.getLeastSigBits();
		final char[] buffer = new char[26];

		CrockfordBase32Codec.writeCrockford(buffer, getTimestamp(), 10, 0);
		long value = ((msb & 0xFFFFL) << 24);
		final long interim = (lsb >>> 40);
		value = value | interim;
		CrockfordBase32Codec.writeCrockford(buffer, value, 8, 10);
		CrockfordBase32Codec.writeCrockford(buffer, lsb, 8, 18);

		return new String(buffer);
	}

	/**
	 * 检查日期
	 *
	 * @param timestamp 时间戳
	 */
	private static void checkTimestamp(final long timestamp) {
		Assert.isTrue((timestamp & TIMESTAMP_MASK) == 0,
			"ULID does not support timestamps after +10889-08-02T05:31:50.655Z!");
	}
}
