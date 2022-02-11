package cn.hutool.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
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

	public static final ByteOrder DEFAULT_ORDER = ByteOrder.LITTLE_ENDIAN;
	/**
	 * CPU的字节序
	 */
	public static final ByteOrder CPU_ENDIAN = "little".equals(System.getProperty("sun.cpu.endian")) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

	/**
	 * int转byte
	 *
	 * @param intValue int值
	 * @return byte值
	 */
	public static byte intToByte(int intValue) {
		return (byte) intValue;
	}

	/**
	 * byte转无符号int
	 *
	 * @param byteValue byte值
	 * @return 无符号int值
	 * @since 3.2.0
	 */
	public static int byteToUnsignedInt(byte byteValue) {
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
	public static short bytesToShort(byte[] bytes) {
		return bytesToShort(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转short<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组，长度必须为2
	 * @param byteOrder 端序
	 * @return short值
	 */
	public static short bytesToShort(byte[] bytes, ByteOrder byteOrder) {
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			//小端模式，数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中
			return (short) (bytes[0] & 0xff | (bytes[1] & 0xff) << Byte.SIZE);
		} else {
			return (short) (bytes[1] & 0xff | (bytes[0] & 0xff) << Byte.SIZE);
		}
	}

	/**
	 * short转byte数组<br>
	 * 默认以小端序转换
	 *
	 * @param shortValue short值
	 * @return byte数组
	 */
	public static byte[] shortToBytes(short shortValue) {
		return shortToBytes(shortValue, DEFAULT_ORDER);
	}

	/**
	 * short转byte数组<br>
	 * 自定义端序
	 *
	 * @param shortValue short值
	 * @param byteOrder  端序
	 * @return byte数组
	 */
	public static byte[] shortToBytes(short shortValue, ByteOrder byteOrder) {
		byte[] b = new byte[Short.BYTES];
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
	public static int bytesToInt(byte[] bytes) {
		return bytesToInt(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte[]转int值<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return int值
	 */
	public static int bytesToInt(byte[] bytes, ByteOrder byteOrder) {
		return bytesToInt(bytes, 0, byteOrder);
	}

	/**
	 * byte[]转int值<br>
	 * 自定义端序
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return int值
	 * @since 5.7.21
	 */
	public static int bytesToInt(byte[] bytes, int start, ByteOrder byteOrder) {
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
	public static byte[] intToBytes(int intValue) {
		return intToBytes(intValue, DEFAULT_ORDER);
	}

	/**
	 * int转byte数组<br>
	 * 自定义端序
	 *
	 * @param intValue  int值
	 * @param byteOrder 端序
	 * @return byte数组
	 */
	public static byte[] intToBytes(int intValue, ByteOrder byteOrder) {

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
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param longValue long值
	 * @return byte数组
	 */
	public static byte[] longToBytes(long longValue) {
		return longToBytes(longValue, DEFAULT_ORDER);
	}

	/**
	 * long转byte数组<br>
	 * 自定义端序<br>
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param longValue long值
	 * @param byteOrder 端序
	 * @return byte数组
	 */
	public static byte[] longToBytes(long longValue, ByteOrder byteOrder) {
		byte[] result = new byte[Long.BYTES];
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
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param bytes byte数组
	 * @return long值
	 */
	public static long bytesToLong(byte[] bytes) {
		return bytesToLong(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转long<br>
	 * 自定义端序<br>
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return long值
	 */
	public static long bytesToLong(byte[] bytes, ByteOrder byteOrder) {
		return bytesToLong(bytes, 0, byteOrder);
	}

	/**
	 * byte数组转long<br>
	 * 自定义端序<br>
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param bytes     byte数组
	 * @param start     计算数组开始位置
	 * @param byteOrder 端序
	 * @return long值
	 * @since 5.7.21
	 */
	public static long bytesToLong(byte[] bytes, int start, ByteOrder byteOrder) {
		long values = 0;
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			for (int i = (Long.BYTES - 1); i >= 0; i--) {
				values <<= Byte.SIZE;
				values |= (bytes[i + start] & 0xff);
			}
		} else {
			for (int i = 0; i < Long.BYTES; i++) {
				values <<= Byte.SIZE;
				values |= (bytes[i + start] & 0xff);
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
	public static byte[] floatToBytes(float floatValue) {
		return floatToBytes(floatValue, DEFAULT_ORDER);
	}

	/**
	 * float转byte数组，自定义端序<br>
	 *
	 * @param floatValue float值
	 * @param byteOrder  端序
	 * @return byte数组
	 * @since 5.7.18
	 */
	public static byte[] floatToBytes(float floatValue, ByteOrder byteOrder) {
		return intToBytes(Float.floatToIntBits(floatValue), byteOrder);
	}

	/**
	 * byte数组转float<br>
	 * 默认以小端序转换<br>
	 *
	 * @param bytes byte数组
	 * @return float值
	 * @since 5.7.18
	 */
	public static double bytesToFloat(byte[] bytes) {
		return bytesToFloat(bytes, DEFAULT_ORDER);
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
	public static float bytesToFloat(byte[] bytes, ByteOrder byteOrder) {
		return Float.intBitsToFloat(bytesToInt(bytes, byteOrder));
	}

	/**
	 * double转byte数组<br>
	 * 默认以小端序转换<br>
	 *
	 * @param doubleValue double值
	 * @return byte数组
	 */
	public static byte[] doubleToBytes(double doubleValue) {
		return doubleToBytes(doubleValue, DEFAULT_ORDER);
	}

	/**
	 * double转byte数组<br>
	 * 自定义端序<br>
	 * from: https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
	 *
	 * @param doubleValue double值
	 * @param byteOrder   端序
	 * @return byte数组
	 */
	public static byte[] doubleToBytes(double doubleValue, ByteOrder byteOrder) {
		return longToBytes(Double.doubleToLongBits(doubleValue), byteOrder);
	}

	/**
	 * byte数组转Double<br>
	 * 默认以小端序转换<br>
	 *
	 * @param bytes byte数组
	 * @return long值
	 */
	public static double bytesToDouble(byte[] bytes) {
		return bytesToDouble(bytes, DEFAULT_ORDER);
	}

	/**
	 * byte数组转double<br>
	 * 自定义端序<br>
	 *
	 * @param bytes     byte数组
	 * @param byteOrder 端序
	 * @return long值
	 */
	public static double bytesToDouble(byte[] bytes, ByteOrder byteOrder) {
		return Double.longBitsToDouble(bytesToLong(bytes, byteOrder));
	}

	/**
	 * 将{@link Number}转换为
	 *
	 * @param number 数字
	 * @return bytes
	 */
	public static byte[] numberToBytes(Number number) {
		return numberToBytes(number, DEFAULT_ORDER);
	}

	/**
	 * 将{@link Number}转换为
	 *
	 * @param number    数字
	 * @param byteOrder 端序
	 * @return bytes
	 */
	public static byte[] numberToBytes(Number number, ByteOrder byteOrder) {
		if (number instanceof Double) {
			return doubleToBytes((Double) number, byteOrder);
		} else if (number instanceof Long) {
			return longToBytes((Long) number, byteOrder);
		} else if (number instanceof Integer) {
			return intToBytes((Integer) number, byteOrder);
		} else if (number instanceof Short) {
			return shortToBytes((Short) number, byteOrder);
		} else if (number instanceof Float) {
			return floatToBytes((Float) number, byteOrder);
		} else {
			return doubleToBytes(number.doubleValue(), byteOrder);
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
	public static <T extends Number> T bytesToNumber(byte[] bytes, Class<T> targetClass, ByteOrder byteOrder) throws IllegalArgumentException {
		Number number;
		if (Byte.class == targetClass) {
			number = bytes[0];
		} else if (Short.class == targetClass) {
			number = bytesToShort(bytes, byteOrder);
		} else if (Integer.class == targetClass) {
			number = bytesToInt(bytes, byteOrder);
		} else if (AtomicInteger.class == targetClass) {
			number = new AtomicInteger(bytesToInt(bytes, byteOrder));
		} else if (Long.class == targetClass) {
			number = bytesToLong(bytes, byteOrder);
		} else if (AtomicLong.class == targetClass) {
			number = new AtomicLong(bytesToLong(bytes, byteOrder));
		} else if (LongAdder.class == targetClass) {
			final LongAdder longValue = new LongAdder();
			longValue.add(bytesToLong(bytes, byteOrder));
			number = longValue;
		} else if (Float.class == targetClass) {
			number = bytesToFloat(bytes, byteOrder);
		} else if (Double.class == targetClass) {
			number = bytesToDouble(bytes, byteOrder);
		} else if (DoubleAdder.class == targetClass) {
			final DoubleAdder doubleAdder = new DoubleAdder();
			doubleAdder.add(bytesToDouble(bytes, byteOrder));
			number = doubleAdder;
		} else if (BigDecimal.class == targetClass) {
			number = NumberUtil.toBigDecimal(bytesToDouble(bytes, byteOrder));
		} else if (BigInteger.class == targetClass) {
			number = BigInteger.valueOf(bytesToLong(bytes, byteOrder));
		} else if (Number.class == targetClass) {
			// 用户没有明确类型具体类型，默认Double
			number = bytesToDouble(bytes, byteOrder);
		} else {
			// 用户自定义类型不支持
			throw new IllegalArgumentException("Unsupported Number type: " + targetClass.getName());
		}

		return (T) number;
	}
}
