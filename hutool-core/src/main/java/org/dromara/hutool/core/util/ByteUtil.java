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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.io.buffer.FastByteBuffer;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * 对数字和字节进行转换。<br>
 * 假设数据存储是以大端模式存储的：<br>
 * <ul>
 *     <li>byte: 字节类型 占8位二进制 00000000</li>
 *     <li>char: 字符类型 占2个字节 16位二进制 byte[0] byte[1]</li>
 *     <li>int : 整数类型 占4个字节 32位二进制 byte[0] byte[1] byte[2] byte[3]</li>
 *     <li>long: 长整数类型 占8个字节 64位二进制 byte[0] byte[1] byte[2] byte[3] byte[4] byte[5]</li>
 *     <li>long: 长整数类型 占8个字节 64位二进制 byte[0] byte[1] byte[2] byte[3] byte[4] byte[5] byte[6] byte[7]</li>
 *     <li>float: 浮点数(小数) 占4个字节 32位二进制 byte[0] byte[1] byte[2] byte[3]</li>
 *     <li>double: 双精度浮点数(小数) 占8个字节 64位二进制 byte[0] byte[1] byte[2] byte[3] byte[4]byte[5] byte[6] byte[7]</li>
 * </ul>
 * 注：注释来自Hanlp，代码提供来自pr#1492@Github
 *
 * @author looly, hanlp, FULaBUla
 * @since 5.6.3
 */
public class ByteUtil {

	/**
	 * 默认字节序：大端在前，小端在后
	 */
	public static final ByteOrder DEFAULT_ORDER = ByteOrder.LITTLE_ENDIAN;
	/**
	 * CPU的字节序
	 */
	public static final ByteOrder CPU_ENDIAN = "little".equals(System.getProperty("sun.cpu.endian")) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

	/**
	 * 编码字符串，编码为UTF-8
	 *
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] toUtf8Bytes(final CharSequence str) {
		return toBytes(str, CharsetUtil.UTF_8);
	}

	/**
	 * 编码字符串
	 *
	 * @param str     字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] toBytes(final CharSequence str, final Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * int转byte
	 *
	 * @param intValue int值
	 * @return byte值
	 */
	public static byte toByte(final int intValue) {
		return (byte) intValue;
	}

	/**
	 * byte转无符号int
	 *
	 * @param byteValue byte值
	 * @return 无符号int值
	 * @since 3.2.0
	 */
	public static int toUnsignedInt(final byte byteValue) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return byteValue & 0xFF;
	}

	/**
	 * byte数组转short<br>
	 * 默认以小端序转换
	 *
	 * @param bytes byte数组
	 * @return short值
	 */
	public static short toShort(final byte[] bytes) {
		return toShort(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转short<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组，长度必须为2
	 * @param byteOrder 端序
	 * @return short值
	 */
	public static short toShort(final byte[] bytes, final ByteOrder byteOrder) {
		return toShort(bytes, 0, byteOrder);
	}

	/**
	 * byte数组转short<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组，长度必须大于2
	 * @param start     开始位置
	 * @param byteOrder 端序
	 * @return short值
	 */
	public static short toShort(final byte[] bytes, final int start, final ByteOrder byteOrder) {
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			//小端模式，数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中
			return (short) (bytes[start] & 0xff | (bytes[start + 1] & 0xff) << Byte.SIZE);
		} else {
			return (short) (bytes[start + 1] & 0xff | (bytes[start] & 0xff) << Byte.SIZE);
		}
	}

	/**
	 * short转byte数组<br>
	 * 默认以小端序转换
	 *
	 * @param shortValue short值
	 * @return byte数组
	 */
	public static byte[] toBytes(final short shortValue) {
		return toBytes(shortValue, DEFAULT_ORDER);
	}

	/**
	 * short转byte数组<br>
	 * 自定义端序
	 *
	 * @param shortValue short值
	 * @param byteOrder  端序
	 * @return byte数组
	 */
	public static byte[] toBytes(final short shortValue, final ByteOrder byteOrder) {
		final byte[] b = new byte[Short.BYTES];
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			b[0] = (byte) (shortValue & 0xff);
			b[1] = (byte) ((shortValue >> Byte.SIZE) & 0xff);
		} else {
			b[1] = (byte) (shortValue & 0xff);
			b[0] = (byte) ((shortValue >> Byte.SIZE) & 0xff);
		}
		return b;
	}

	/**
	 * byte[]转int值<br>
	 * 默认以小端序转换
	 *
	 * @param bytes byte数组
	 * @return int值
	 */
	public static int toInt(final byte[] bytes) {
		return toInt(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte[]转int值<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return int值
	 */
	public static int toInt(final byte[] bytes, final ByteOrder byteOrder) {
		return toInt(bytes, 0, byteOrder);
	}

	/**
	 * byte[]转int值<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组
	 * @param start     开始位置（包含）
	 * @param byteOrder 端序
	 * @return int值
	 * @since 5.7.21
	 */
	public static int toInt(final byte[] bytes, final int start, final ByteOrder byteOrder) {
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			return bytes[start] & 0xFF | //
				(bytes[1 + start] & 0xFF) << 8 | //
				(bytes[2 + start] & 0xFF) << 16 | //
				(bytes[3 + start] & 0xFF) << 24; //
		} else {
			return bytes[3 + start] & 0xFF | //
				(bytes[2 + start] & 0xFF) << 8 | //
				(bytes[1 + start] & 0xFF) << 16 | //
				(bytes[start] & 0xFF) << 24; //
		}

	}

	/**
	 * int转byte数组<br>
	 * 默认以小端序转换
	 *
	 * @param intValue int值
	 * @return byte数组
	 */
	public static byte[] toBytes(final int intValue) {
		return toBytes(intValue, DEFAULT_ORDER);
	}

	/**
	 * int转byte数组<br>
	 * 自定义端序
	 *
	 * @param intValue  int值
	 * @param byteOrder 端序
	 * @return byte数组
	 */
	public static byte[] toBytes(final int intValue, final ByteOrder byteOrder) {

		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			return new byte[]{ //
				(byte) (intValue & 0xFF), //
				(byte) ((intValue >> 8) & 0xFF), //
				(byte) ((intValue >> 16) & 0xFF), //
				(byte) ((intValue >> 24) & 0xFF) //
			};

		} else {
			return new byte[]{ //
				(byte) ((intValue >> 24) & 0xFF), //
				(byte) ((intValue >> 16) & 0xFF), //
				(byte) ((intValue >> 8) & 0xFF), //
				(byte) (intValue & 0xFF) //
			};
		}

	}

	/**
	 * long转byte数组<br>
	 * 默认以小端序转换<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param longValue long值
	 * @return byte数组
	 */
	public static byte[] toBytes(final long longValue) {
		return toBytes(longValue, DEFAULT_ORDER);
	}

	/**
	 * long转byte数组<br>
	 * 自定义端序<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param longValue long值
	 * @param byteOrder 端序
	 * @return byte数组
	 */
	public static byte[] toBytes(long longValue, final ByteOrder byteOrder) {
		final byte[] result = new byte[Long.BYTES];
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			for (int i = 0; i < result.length; i++) {
				result[i] = (byte) (longValue & 0xFF);
				longValue >>= Byte.SIZE;
			}
		} else {
			for (int i = (result.length - 1); i >= 0; i--) {
				result[i] = (byte) (longValue & 0xFF);
				longValue >>= Byte.SIZE;
			}
		}
		return result;
	}

	/**
	 * byte数组转long<br>
	 * 默认以小端序转换<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param bytes byte数组
	 * @return long值
	 */
	public static long toLong(final byte[] bytes) {
		return toLong(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转long<br>
	 * 自定义端序<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return long值
	 */
	public static long toLong(final byte[] bytes, final ByteOrder byteOrder) {
		return toLong(bytes, 0, byteOrder);
	}

	/**
	 * byte数组转long<br>
	 * 自定义端序<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param bytes     byte数组
	 * @param start     计算数组开始位置
	 * @param byteOrder 端序
	 * @return long值
	 * @since 5.7.21
	 */
	public static long toLong(final byte[] bytes, final int start, final ByteOrder byteOrder) {
		long values = 0;
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			for (int i = (Long.BYTES - 1); i >= 0; i--) {
				values <<= Byte.SIZE;
				values |= (bytes[i + start] & 0xffL);
			}
		} else {
			for (int i = 0; i < Long.BYTES; i++) {
				values <<= Byte.SIZE;
				values |= (bytes[i + start] & 0xffL);
			}
		}

		return values;
	}

	/**
	 * float转byte数组，默认以小端序转换<br>
	 *
	 * @param floatValue float值
	 * @return byte数组
	 * @since 5.7.18
	 */
	public static byte[] toBytes(final float floatValue) {
		return toBytes(floatValue, DEFAULT_ORDER);
	}

	/**
	 * float转byte数组，自定义端序<br>
	 *
	 * @param floatValue float值
	 * @param byteOrder  端序
	 * @return byte数组
	 * @since 5.7.18
	 */
	public static byte[] toBytes(final float floatValue, final ByteOrder byteOrder) {
		return toBytes(Float.floatToIntBits(floatValue), byteOrder);
	}

	/**
	 * byte数组转float<br>
	 * 默认以小端序转换<br>
	 *
	 * @param bytes byte数组
	 * @return float值
	 * @since 5.7.18
	 */
	public static float toFloat(final byte[] bytes) {
		return toFloat(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转float<br>
	 * 自定义端序<br>
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return float值
	 * @since 5.7.18
	 */
	public static float toFloat(final byte[] bytes, final ByteOrder byteOrder) {
		return Float.intBitsToFloat(toInt(bytes, byteOrder));
	}

	/**
	 * double转byte数组<br>
	 * 默认以小端序转换<br>
	 *
	 * @param doubleValue double值
	 * @return byte数组
	 */
	public static byte[] toBytes(final double doubleValue) {
		return toBytes(doubleValue, DEFAULT_ORDER);
	}

	/**
	 * double转byte数组<br>
	 * 自定义端序<br>
	 * from: <a href="https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java">https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java</a>
	 *
	 * @param doubleValue double值
	 * @param byteOrder   端序
	 * @return byte数组
	 */
	public static byte[] toBytes(final double doubleValue, final ByteOrder byteOrder) {
		return toBytes(Double.doubleToLongBits(doubleValue), byteOrder);
	}

	/**
	 * byte数组转Double<br>
	 * 默认以小端序转换<br>
	 *
	 * @param bytes byte数组
	 * @return long值
	 */
	public static double toDouble(final byte[] bytes) {
		return toDouble(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转double<br>
	 * 自定义端序<br>
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return long值
	 */
	public static double toDouble(final byte[] bytes, final ByteOrder byteOrder) {
		return Double.longBitsToDouble(toLong(bytes, byteOrder));
	}

	/**
	 * 将{@link Number}转换为
	 *
	 * @param number 数字
	 * @return bytes
	 */
	public static byte[] toBytes(final Number number) {
		return toBytes(number, DEFAULT_ORDER);
	}

	/**
	 * 将{@link Number}转换为
	 *
	 * @param number    数字
	 * @param byteOrder 端序
	 * @return bytes
	 */
	public static byte[] toBytes(final Number number, final ByteOrder byteOrder) {
		if (number instanceof Byte) {
			return new byte[]{number.byteValue()};
		} else if (number instanceof Double) {
			return toBytes(number.doubleValue(), byteOrder);
		} else if (number instanceof Long) {
			return toBytes(number.longValue(), byteOrder);
		} else if (number instanceof Integer) {
			return ByteUtil.toBytes(number.intValue(), byteOrder);
		} else if (number instanceof Short) {
			return ByteUtil.toBytes(number.shortValue(), byteOrder);
		} else if (number instanceof Float) {
			return toBytes(number.floatValue(), byteOrder);
		} else if (number instanceof BigInteger) {
			return ((BigInteger) number).toByteArray();
		} else {
			return toBytes(number.doubleValue(), byteOrder);
		}
	}

	/**
	 * byte数组转换为指定类型数字
	 *
	 * @param <T>         数字类型
	 * @param bytes       byte数组
	 * @param targetClass 目标数字类型
	 * @param byteOrder   端序
	 * @return 转换后的数字
	 * @throws IllegalArgumentException 不支持的数字类型，如用户自定义数字类型
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T toNumber(final byte[] bytes, final Class<T> targetClass, final ByteOrder byteOrder) throws IllegalArgumentException {
		final Number number;
		if (Byte.class == targetClass) {
			number = bytes[0];
		} else if (Short.class == targetClass) {
			number = toShort(bytes, byteOrder);
		} else if (Integer.class == targetClass) {
			number = toInt(bytes, byteOrder);
		} else if (AtomicInteger.class == targetClass) {
			number = new AtomicInteger(toInt(bytes, byteOrder));
		} else if (Long.class == targetClass) {
			number = toLong(bytes, byteOrder);
		} else if (AtomicLong.class == targetClass) {
			number = new AtomicLong(toLong(bytes, byteOrder));
		} else if (LongAdder.class == targetClass) {
			final LongAdder longValue = new LongAdder();
			longValue.add(toLong(bytes, byteOrder));
			number = longValue;
		} else if (Float.class == targetClass) {
			number = toFloat(bytes, byteOrder);
		} else if (Double.class == targetClass) {
			number = toDouble(bytes, byteOrder);
		} else if (DoubleAdder.class == targetClass) {
			final DoubleAdder doubleAdder = new DoubleAdder();
			doubleAdder.add(toDouble(bytes, byteOrder));
			number = doubleAdder;
		} else if (BigDecimal.class == targetClass) {
			number = NumberUtil.toBigDecimal(toDouble(bytes, byteOrder));
		} else if (BigInteger.class == targetClass) {
			number = BigInteger.valueOf(toLong(bytes, byteOrder));
		} else if (Number.class == targetClass) {
			// 用户没有明确类型具体类型，默认Double
			number = toDouble(bytes, byteOrder);
		} else {
			// 用户自定义类型不支持
			throw new IllegalArgumentException("Unsupported Number type: " + targetClass.getName());
		}

		return (T) number;
	}

	/**
	 * 以无符号字节数组的形式返回传入值。
	 *
	 * @param value 需要转换的值
	 * @return 无符号bytes
	 * @since 4.5.0
	 */
	public static byte[] toUnsignedByteArray(final BigInteger value) {
		final byte[] bytes = value.toByteArray();

		if (bytes[0] == 0) {
			final byte[] tmp = new byte[bytes.length - 1];
			System.arraycopy(bytes, 1, tmp, 0, tmp.length);

			return tmp;
		}

		return bytes;
	}

	/**
	 * 以无符号字节数组的形式返回传入值。
	 *
	 * @param length bytes长度
	 * @param value  需要转换的值
	 * @return 无符号bytes
	 * @since 4.5.0
	 */
	public static byte[] toUnsignedByteArray(final int length, final BigInteger value) {
		final byte[] bytes = value.toByteArray();
		if (bytes.length == length) {
			return bytes;
		}

		final int start = bytes[0] == 0 ? 1 : 0;
		final int count = bytes.length - start;

		if (count > length) {
			throw new IllegalArgumentException("standard length exceeded for value");
		}

		final byte[] tmp = new byte[length];
		System.arraycopy(bytes, start, tmp, tmp.length - count, count);
		return tmp;
	}

	/**
	 * 无符号bytes转{@link BigInteger}
	 *
	 * @param buf buf 无符号bytes
	 * @return {@link BigInteger}
	 * @since 4.5.0
	 */
	public static BigInteger fromUnsignedByteArray(final byte[] buf) {
		return new BigInteger(1, buf);
	}

	/**
	 * 无符号bytes转{@link BigInteger}
	 *
	 * @param buf    无符号bytes
	 * @param off    起始位置
	 * @param length 长度
	 * @return {@link BigInteger}
	 */
	public static BigInteger fromUnsignedByteArray(final byte[] buf, final int off, final int length) {
		byte[] mag = buf;
		if (off != 0 || length != buf.length) {
			mag = new byte[length];
			System.arraycopy(buf, off, mag, 0, length);
		}
		return new BigInteger(1, mag);
	}

	/**
	 * 连接多个byte[]
	 *
	 * @param byteArrays 多个byte[]
	 * @return 连接后的byte[]
	 * @since 6.0.0
	 */
	public static byte[] concat(final byte[]... byteArrays) {
		int totalLength = 0;
		for (final byte[] byteArray : byteArrays) {
			totalLength += byteArray.length;
		}

		final FastByteBuffer buffer = new FastByteBuffer(totalLength);
		for (final byte[] byteArray : byteArrays) {
			buffer.append(byteArray);
		}
		return buffer.toArrayZeroCopyIfPossible();
	}

	/**
	 * 统计byte中位数为1的个数
	 *
	 * @param buf 无符号bytes
	 * @return 为 1 的个数
	 * @see Integer#bitCount(int)
	 */
	public static int bitCount(final byte[] buf) {
		int sum = 0;
		for (final byte b : buf) {
			sum += Integer.bitCount((b & 0xFF));
		}
		return sum;
	}

	/**
	 * 统计无符号bytes转为bit位数为1的索引集合
	 *
	 * @param bytes 无符号bytes
	 * @return 位数为1的索引集合
	 */
	public static List<Integer> toUnsignedBitIndex(final byte[] bytes) {
		final List<Integer> idxList = new LinkedList<>();
		final StringBuilder sb = new StringBuilder();
		for (final byte b : bytes) {
			sb.append(StrUtil.padPre(Integer.toBinaryString((b & 0xFF)), 8, "0"));
		}
		final String bitStr = sb.toString();
		for (int i = 0; i < bitStr.length(); i++) {
			if (bitStr.charAt(i) == '1') {
				idxList.add(i);
			}
		}
		return idxList;
	}
}
