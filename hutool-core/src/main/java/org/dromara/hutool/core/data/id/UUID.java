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

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 提供通用唯一识别码（universally unique identifier）（UUID）实现，UUID表示一个128位的值。<br>
 * 此类拷贝自java.util.UUID，用于生成不带-的UUID字符串
 * <ul>
 *     <li>Generate UUID 不同版本UUID在线生成和参考：<a href="https://idtools.co/uuid/v4">Generate UUID</a></li>
 *     <li>UUID 的 5 个版本：<a href="https://juejin.cn/post/7297225106203689001">UUID 5 version 区别</a></li>
 *     <li>UUID代码实现参考：<a href="https://github.com/sake/uuid4j">sake/uuid4j实现</a></li>
 * </ul>
 * <ul>
 * 		<li>UUIDv1: Structure,形如：xxxxxxxx-xxxx-1xxx-yxxx-xxxxxxxxxxxx，UUID v1 表示为 32 个字符的十六进制字符串，分五组显示，并用连字符分隔; <strong>基于时间</strong>，同时访问主机的 MAC 地址； generate a time based UUID (V1)</li>
 * 		<li>UUIDv2: Structure,形如：xxxxxxxx-xxxx-2xxx-yxxx-xxxxxxxxxxxx，UUID v2 的结构与其他 UUID 相同;需要<strong> DCE–分布式计算机环境 </strong>生成唯一标识符;由于基于计算主机名，有隐私风险，未大规模使用</li>
 * 		<li>UUIDv3: Structure,形如：xxxxxxxx-xxxx-3xxx-yxxx-xxxxxxxxxxxx，UUID v3 的结构与其他 UUID 相同;需要<strong> 基于命名·使用MD5哈希加密 </strong>生成唯一标识符;</li>
 * 		<li>UUIDv4: Structure,形如：xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx，UUID v4 的结构与其他 UUID 相同,<strong>使用最多的版本</strong>，使用<strong>随机数生成</strong>。 hutool中默认实现都是该版本； generate a random UUID (V4)</li>
 * 		<li>UUIDv5: Structure,形如：xxxxxxxx-xxxx-5xxx-yxxx-xxxxxxxxxxxx，UUID v5 的结构与其他 UUID 相同,需要根据<strong> 基于命名·使用SHA-1哈希加密 </strong>生成唯一标识符； generate name based UUID with SHA1 hashing (v5)</li>
 * 		<li>UUIDv6: Structure,形如：xxxxxxxx-xxxx-6xxx-yxxx-xxxxxxxxxxxx，UUID v6 的结构与其他 UUID 相同,<strong>与 UUIDv1 的字段兼容版本</strong>,<strong>结合 UUIDv1 和 UUIDv4 的优点</strong>，确保基于时间的自然排序和更好的隐私;</li>
 * 		<li>UUIDv7: Structure,形如：xxxxxxxx-xxxx-7xxx-yxxx-xxxxxxxxxxxx，UUID v7 的结构与其他 UUID 相同,提供了从 <strong>Unix Epoch 时间戳</strong>派生的时间排序值，以及改进的熵特性。如果可能，<strong>建议使用版本 1 和 6</strong>； generate a ordered time based UUID (V7)</li>
 * </ul>
 * version 字段保存描述此 UUID 类型的值。有 7 种不同的基本 UUID 类型：基于时间的 UUIDv1、DCE 安全 UUIDv2、基于名称的 UUIDv3 、随机生成的 UUIDv4、基于名称的SHA-1算法的 UUIDv5、基于时间的随机生成的 UUIDv6 和 基于时间戳的 UUIDv7。<br>
 * 这些类型的 version 值分别为 1、2、3、4、5、6 和 7。最常用的V4
 *
 * <p>
 * 这些通用标识符具有不同的变体。此类的方法用于操作 Leach-Salz 变体，不过构造方法允许创建任何 UUID 变体（将在下面进行描述）。
 * <p>
 * 变体 2 (Leach-Salz) UUID 的布局如下： long 型数据的最高有效位由以下无符号字段组成：
 *
 * <pre>
 * 0xFFFFFFFF00000000 time_low
 * 0x00000000FFFF0000 time_mid
 * 0x000000000000F000 version
 * 0x0000000000000FFF time_hi
 * </pre>
 * <p>
 * long 型数据的最低有效位由以下无符号字段组成：
 *
 * <pre>
 * 0xC000000000000000 variant
 * 0x3FFF000000000000 clock_seq
 * 0x0000FFFFFFFFFFFF node
 * </pre>
 *
 * <p>
 * variant 字段包含一个表示 UUID 布局的值。以上描述的位布局仅在 UUID 的 variant 值为 2（表示 Leach-Salz 变体）时才有效。 *
 *
 * @since 4.1.11
 */
public class UUID implements java.io.Serializable, Comparable<UUID> {
	private static final long serialVersionUID = -1185015143654744140L;

	/**
	 * {@link SecureRandom} 的单例
	 *
	 * @author looly
	 */
	private static class Holder {
		static final SecureRandom NUMBER_GENERATOR = RandomUtil.getSecureRandom();
	}

	/**
	 * 此UUID的最高64有效位
	 */
	private final long mostSigBits;

	/**
	 * 此UUID的最低64有效位
	 */
	private final long leastSigBits;

	/**
	 * 私有构造
	 *
	 * @param data 数据
	 */
	private UUID(final byte[] data) {
		long msb = 0;
		long lsb = 0;
		assert data.length == 16 : "data must be 16 bytes in length";
		for (int i = 0; i < 8; i++) {
			msb = (msb << 8) | (data[i] & 0xff);
		}
		for (int i = 8; i < 16; i++) {
			lsb = (lsb << 8) | (data[i] & 0xff);
		}
		this.mostSigBits = msb;
		this.leastSigBits = lsb;
	}

	/**
	 * 使用指定的数据构造新的 UUID。
	 *
	 * @param mostSigBits  用于 {@code UUID} 的最高有效 64 位
	 * @param leastSigBits 用于 {@code UUID} 的最低有效 64 位
	 */
	public UUID(final long mostSigBits, final long leastSigBits) {
		this.mostSigBits = mostSigBits;
		this.leastSigBits = leastSigBits;
	}

	/**
	 * 获取类型 4 UUIDv4（伪随机生成的）UUID 的静态工厂。 使用加密的本地线程伪随机数生成器生成该 UUID。
	 *
	 * @return 随机生成的 {@code UUID}
	 */
	public static UUID fastUUID() {
		return randomUUID(false);
	}

	/**
	 * 获取类型 4 UUIDv4（伪随机生成的）UUID 的静态工厂。 使用加密的强伪随机数生成器生成该 UUID。
	 *
	 * @return 随机生成的 {@code UUID}
	 */
	public static UUID randomUUID() {
		return randomUUID(true);
	}

	/**
	 * 获取类型 4 UUIDv4（伪随机生成的）UUID 的静态工厂。 使用加密的强伪随机数生成器生成该 UUID。
	 *
	 * @param isSecure 是否使用{@link SecureRandom}如果是可以获得更安全的随机码，否则可以得到更好的性能
	 * @return 随机生成的 {@code UUID}
	 */
	public static UUID randomUUID(final boolean isSecure) {
		final Random ng = isSecure ? Holder.NUMBER_GENERATOR : RandomUtil.getRandom();

		final byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);

		randomBytes[6] &= 0x0f; /* clear version */
		randomBytes[6] |= 0x40; /* set to version 4 */
		randomBytes[8] &= 0x3f; /* clear variant */
		randomBytes[8] |= (byte) 0x80; /* set to IETF variant */

		return new UUID(randomBytes);
	}

	/**
	 * 根据指定的字节数组获取类型 3 UUIDv3（基于名称的·使用MD5哈希加密）UUID 的静态工厂。
	 *
	 * @param name 用于构造 UUID 的字节数组。
	 * @return 根据指定数组生成的 {@code UUID}
	 */
	public static UUID nameUUIDFromBytes(final byte[] name) {
		final MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported");
		}
		final byte[] md5Bytes = md.digest(name);
		md5Bytes[6] &= 0x0f; /* clear version */
		md5Bytes[6] |= 0x30; /* set to version 3 */
		md5Bytes[8] &= 0x3f; /* clear variant */
		md5Bytes[8] |= (byte) 0x80; /* set to IETF variant */
		return new UUID(md5Bytes);
	}

	/**
	 * 根据 {@link #toString()} 方法中描述的字符串标准表示形式创建{@code UUID}。
	 *
	 * @param name 指定 {@code UUID} 字符串
	 * @return 具有指定值的 {@code UUID}
	 * @throws IllegalArgumentException 如果 name 与 {@link #toString} 中描述的字符串表示形式不符抛出此异常
	 */
	public static UUID fromString(final String name) {
		final String[] components = name.split("-");
		if (components.length != 5) {
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		}
		for (int i = 0; i < 5; i++) {
			components[i] = "0x" + components[i];
		}

		long mostSigBits = Long.decode(components[0]);
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]);
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]);

		long leastSigBits = Long.decode(components[3]);
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]);

		return new UUID(mostSigBits, leastSigBits);
	}

	/**
	 * 返回此 UUID 的 128 位值中的最低有效 64 位。
	 *
	 * @return 此 UUID 的 128 位值中的最低有效 64 位。
	 */
	public long getLeastSignificantBits() {
		return leastSigBits;
	}

	/**
	 * 返回此 UUID 的 128 位值中的最高有效 64 位。
	 *
	 * @return 此 UUID 的 128 位值中最高有效 64 位。
	 */
	public long getMostSignificantBits() {
		return mostSigBits;
	}

	/**
	 * 与此 {@code UUID} 相关联的版本号. 版本号描述此 {@code UUID} 是如何生成的。
	 * <p>
	 * 版本号具有以下含意:
	 * <ul>
	 * <li>UUIDv1 基于时间的 UUID
	 * <li>UUIDv2 DCE 安全 UUID
	 * <li>UUIDv3 基于名称的MD5散列算法的 UUID
	 * <li>UUIDv4 随机生成的 UUID
	 * <li>UUIDv5 基于名称的SHA-1散列算法的 UUID
	 * <li>UUIDv6 基于时间的随机生成的 UUID （UUIDv1 + UUIDv4）
	 * <li>UUIDv7 基于时间戳Unix epoch的 UUID
	 * </ul>
	 *
	 * @return 此 {@code UUID} 的版本号
	 */
	public int version() {
		// Version is bits masked by 0x000000000000F000 in MS long
		return (int) ((mostSigBits >> 12) & 0x0f);
	}

	/**
	 * 与此 {@code UUID} 相关联的变体号。变体号描述 {@code UUID} 的布局。
	 * <p>
	 * 变体号具有以下含意：
	 * <ul>
	 * <li>0 为 NCS 向后兼容保留
	 * <li>2 <a href="http://www.ietf.org/rfc/rfc4122.txt">IETF&nbsp;RFC&nbsp;4122</a>(Leach-Salz), 用于此类
	 * <li>6 保留，微软向后兼容
	 * <li>7 保留供以后定义使用
	 * </ul>
	 *
	 * @return 此 {@code UUID} 相关联的变体号
	 */
	public int variant() {
		// This field is composed of a varying number of bits.
		// 0 - - Reserved for NCS backward compatibility
		// 1 0 - The IETF aka Leach-Salz variant (used by this class)
		// 1 1 0 Reserved, Microsoft backward compatibility
		// 1 1 1 Reserved for future definition.
		return (int) ((leastSigBits >>> (64 - (leastSigBits >>> 62))) & (leastSigBits >> 63));
	}

	/**
	 * 与此 UUID 相关联的时间戳值。
	 *
	 * <p>
	 * 60 位的时间戳值根据此 {@code UUID} 的 time_low、time_mid 和 time_hi 字段构造。<br>
	 * 所得到的时间戳以 100 毫微秒为单位，从 UTC（通用协调时间） 1582 年 10 月 15 日零时开始。
	 *
	 * <p>
	 * 时间戳值仅在在基于时间的 UUID（其 version 类型为 1）中才有意义。<br>
	 * 如果此 {@code UUID} 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
	 *
	 * @return 时间戳值
	 * @throws UnsupportedOperationException 如果此 {@code UUID} 不是 version 为 1 的 UUID。
	 */
	public long timestamp() throws UnsupportedOperationException {
		checkTimeBase();
		return (mostSigBits & 0x0FFFL) << 48//
				| ((mostSigBits >> 16) & 0x0FFFFL) << 32//
				| mostSigBits >>> 32;
	}

	/**
	 * 与此 UUID 相关联的时钟序列值。
	 *
	 * <p>
	 * 14 位的时钟序列值根据此 UUID 的 clock_seq 字段构造。clock_seq 字段用于保证在基于时间的 UUID 中的时间唯一性。
	 * <p>
	 * {@code clockSequence} 值仅在基于时间的 UUID（其 version 类型为 1）中才有意义。 如果此 UUID 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
	 *
	 * @return 此 {@code UUID} 的时钟序列
	 * @throws UnsupportedOperationException 如果此 UUID 的 version 不为 1
	 */
	public int clockSequence() throws UnsupportedOperationException {
		checkTimeBase();
		return (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
	}

	/**
	 * 与此 UUID 相关的节点值。
	 *
	 * <p>
	 * 48 位的节点值根据此 UUID 的 node 字段构造。此字段旨在用于保存机器的 IEEE 802 地址，该地址用于生成此 UUID 以保证空间唯一性。
	 * <p>
	 * 节点值仅在基于时间的 UUID（其 version 类型为 1）中才有意义。<br>
	 * 如果此 UUID 不是基于时间的 UUID，则此方法抛出 UnsupportedOperationException。
	 *
	 * @return 此 {@code UUID} 的节点值
	 * @throws UnsupportedOperationException 如果此 UUID 的 version 不为 1
	 */
	public long node() throws UnsupportedOperationException {
		checkTimeBase();
		return leastSigBits & 0x0000FFFFFFFFFFFFL;
	}

	// Object Inherited Methods

	/**
	 * 返回此{@code UUID} 的字符串表现形式。
	 *
	 * <p>
	 * UUID 的字符串表示形式由此 BNF 描述：
	 *
	 * <pre>
	 * {@code
	 * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
	 * time_low               = 4*<hexOctet>
	 * time_mid               = 2*<hexOctet>
	 * time_high_and_version  = 2*<hexOctet>
	 * variant_and_sequence   = 2*<hexOctet>
	 * node                   = 6*<hexOctet>
	 * hexOctet               = <hexDigit><hexDigit>
	 * hexDigit               = [0-9a-fA-F]
	 * }
	 * </pre>
	 *
	 * @return 此{@code UUID} 的字符串表现形式
	 * @see #toString(boolean)
	 */
	@Override
	public String toString() {
		return toString(false);
	}

	/**
	 * 返回此{@code UUID} 的字符串表现形式。
	 *
	 * <p>
	 * UUID 的字符串表示形式由此 BNF 描述：
	 *
	 * <pre>
	 * {@code
	 * UUID                   = <time_low>-<time_mid>-<time_high_and_version>-<variant_and_sequence>-<node>
	 * time_low               = 4*<hexOctet>
	 * time_mid               = 2*<hexOctet>
	 * time_high_and_version  = 2*<hexOctet>
	 * variant_and_sequence   = 2*<hexOctet>
	 * node                   = 6*<hexOctet>
	 * hexOctet               = <hexDigit><hexDigit>
	 * hexDigit               = [0-9a-fA-F]
	 * }
	 * </pre>
	 *
	 * @param isSimple 是否简单模式，简单模式为不带'-'的UUID字符串
	 * @return 此{@code UUID} 的字符串表现形式
	 */
	public String toString(final boolean isSimple) {
		final StringBuilder builder = StrUtil.builder(isSimple ? 32 : 36);
		// time_low
		builder.append(digits(mostSigBits >> 32, 8));
		if (!isSimple) {
			builder.append('-');
		}
		// time_mid
		builder.append(digits(mostSigBits >> 16, 4));
		if (!isSimple) {
			builder.append('-');
		}
		// time_high_and_version
		builder.append(digits(mostSigBits, 4));
		if (!isSimple) {
			builder.append('-');
		}
		// variant_and_sequence
		builder.append(digits(leastSigBits >> 48, 4));
		if (!isSimple) {
			builder.append('-');
		}
		// node
		builder.append(digits(leastSigBits, 12));

		return builder.toString();
	}

	/**
	 * 返回此 UUID 的哈希码。
	 *
	 * @return UUID 的哈希码值。
	 */
	@Override
	public int hashCode() {
		final long hilo = mostSigBits ^ leastSigBits;
		return ((int) (hilo >> 32)) ^ (int) hilo;
	}

	/**
	 * 将此对象与指定对象比较。
	 * <p>
	 * 当且仅当参数不为 {@code null}、而是一个 UUID 对象、具有与此 UUID 相同的 varriant、包含相同的值（每一位均相同）时，结果才为 {@code true}。
	 *
	 * @param obj 要与之比较的对象
	 * @return 如果对象相同，则返回 {@code true}；否则返回 {@code false}
	 */
	@Override
	public boolean equals(final Object obj) {
		if ((null == obj) || (obj.getClass() != UUID.class)) {
			return false;
		}
		final UUID id = (UUID) obj;
		return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
	}

	// Comparison Operations

	/**
	 * 将此 UUID 与指定的 UUID 比较。
	 *
	 * <p>
	 * 如果两个 UUID 不同，且第一个 UUID 的最高有效字段大于第二个 UUID 的对应字段，则第一个 UUID 大于第二个 UUID。
	 *
	 * @param val 与此 UUID 比较的 UUID
	 * @return 在此 UUID 小于、等于或大于 val 时，分别返回 -1、0 或 1。
	 */
	@Override
	public int compareTo(final UUID val) {
		// The ordering is intentionally set up so that the UUIDs
		// can simply be numerically compared as two numbers
		int compare = Long.compare(this.mostSigBits, val.mostSigBits);
		if(0 == compare){
			compare = Long.compare(this.leastSigBits, val.leastSigBits);
		}
		return compare;
	}

	// ------------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 返回指定数字对应的hex值
	 *
	 * @param val    值
	 * @param digits 位
	 * @return 值
	 */
	private static String digits(final long val, final int digits) {
		final long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	/**
	 * 检查是否为time-based版本UUID
	 */
	private void checkTimeBase() {
		if (version() != 1) {
			throw new UnsupportedOperationException("Not a time-based UUID");
		}
	}
	// ------------------------------------------------------------------------------------------------------------------- Private method end
}
