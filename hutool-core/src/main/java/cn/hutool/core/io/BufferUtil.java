package cn.hutool.core.io;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * {@link ByteBuffer} 工具类<br>
 * 此工具来自于 t-io 项目以及其它项目的相关部分收集<br>
 * ByteBuffer的相关介绍见：https://www.cnblogs.com/ruber/p/6857159.html
 *
 * @author tanyaowu, looly
 * @since 4.0.0
 */
public class BufferUtil {

	/**
	 * 拷贝到一个新的ByteBuffer
	 *
	 * @param src   源ByteBuffer
	 * @param start 起始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的ByteBuffer
	 */
	public static ByteBuffer copy(ByteBuffer src, int start, int end) {
		return copy(src, ByteBuffer.allocate(end - start));
	}

	/**
	 * 拷贝ByteBuffer
	 *
	 * @param src  源ByteBuffer
	 * @param dest 目标ByteBuffer
	 * @return 目标ByteBuffer
	 */
	public static ByteBuffer copy(ByteBuffer src, ByteBuffer dest) {
		return copy(src, dest, Math.min(src.limit(), dest.remaining()));
	}

	/**
	 * 拷贝ByteBuffer
	 *
	 * @param src    源ByteBuffer
	 * @param dest   目标ByteBuffer
	 * @param length 长度
	 * @return 目标ByteBuffer
	 */
	public static ByteBuffer copy(ByteBuffer src, ByteBuffer dest, int length) {
		return copy(src, src.position(), dest, dest.position(), length);
	}

	/**
	 * 拷贝ByteBuffer
	 *
	 * @param src       源ByteBuffer
	 * @param srcStart  源开始的位置
	 * @param dest      目标ByteBuffer
	 * @param destStart 目标开始的位置
	 * @param length    长度
	 * @return 目标ByteBuffer
	 */
	public static ByteBuffer copy(ByteBuffer src, int srcStart, ByteBuffer dest, int destStart, int length) {
		System.arraycopy(src.array(), srcStart, dest.array(), destStart, length);
		return dest;
	}

	/**
	 * 读取剩余部分并转为UTF-8编码字符串
	 *
	 * @param buffer ByteBuffer
	 * @return 字符串
	 * @since 4.5.0
	 */
	public static String readUtf8Str(ByteBuffer buffer) {
		return readStr(buffer, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取剩余部分并转为字符串
	 *
	 * @param buffer  ByteBuffer
	 * @param charset 编码
	 * @return 字符串
	 * @since 4.5.0
	 */
	public static String readStr(ByteBuffer buffer, Charset charset) {
		return StrUtil.str(readBytes(buffer), charset);
	}

	/**
	 * 读取剩余部分bytes<br>
	 *
	 * @param buffer ByteBuffer
	 * @return bytes
	 */
	public static byte[] readBytes(ByteBuffer buffer) {
		final int remaining = buffer.remaining();
		byte[] ab = new byte[remaining];
		buffer.get(ab);
		return ab;
	}

	/**
	 * 读取指定长度的bytes<br>
	 * 如果长度不足，则读取剩余部分，此时buffer必须为读模式
	 *
	 * @param buffer    ByteBuffer
	 * @param maxLength 最大长度
	 * @return bytes
	 */
	public static byte[] readBytes(ByteBuffer buffer, int maxLength) {
		final int remaining = buffer.remaining();
		if (maxLength > remaining) {
			maxLength = remaining;
		}
		byte[] ab = new byte[maxLength];
		buffer.get(ab);
		return ab;
	}

	/**
	 * 读取指定区间的数据
	 *
	 * @param buffer {@link ByteBuffer}
	 * @param start  开始位置
	 * @param end    结束位置
	 * @return bytes
	 */
	public static byte[] readBytes(ByteBuffer buffer, int start, int end) {
		byte[] bs = new byte[end - start];
		System.arraycopy(buffer.array(), start, bs, 0, bs.length);
		return bs;
	}

	/**
	 * 一行的末尾位置，查找位置时位移ByteBuffer到结束位置
	 *
	 * @param buffer {@link ByteBuffer}
	 * @return 末尾位置，未找到或达到最大长度返回-1
	 */
	public static int lineEnd(ByteBuffer buffer) {
		return lineEnd(buffer, buffer.remaining());
	}

	/**
	 * 一行的末尾位置，查找位置时位移ByteBuffer到结束位置<br>
	 * 支持的换行符如下：
	 *
	 * <pre>
	 * 1. \r\n
	 * 2. \n
	 * </pre>
	 *
	 * @param buffer    {@link ByteBuffer}
	 * @param maxLength 读取最大长度
	 * @return 末尾位置，未找到或达到最大长度返回-1
	 */
	public static int lineEnd(ByteBuffer buffer, int maxLength) {
		int primitivePosition = buffer.position();
		boolean canEnd = false;
		int charIndex = primitivePosition;
		byte b;
		while (buffer.hasRemaining()) {
			b = buffer.get();
			charIndex++;
			if (b == StrUtil.C_CR) {
				canEnd = true;
			} else if (b == StrUtil.C_LF) {
				return canEnd ? charIndex - 2 : charIndex - 1;
			} else {
				// 只有\r无法确认换行
				canEnd = false;
			}

			if (charIndex - primitivePosition > maxLength) {
				// 查找到尽头，未找到，还原位置
				buffer.position(primitivePosition);
				throw new IndexOutOfBoundsException(StrUtil.format("Position is out of maxLength: {}", maxLength));
			}
		}

		// 查找到buffer尽头，未找到，还原位置
		buffer.position(primitivePosition);
		// 读到结束位置
		return -1;
	}

	/**
	 * 读取一行，如果buffer中最后一部分并非完整一行，则返回null<br>
	 * 支持的换行符如下：
	 *
	 * <pre>
	 * 1. \r\n
	 * 2. \n
	 * </pre>
	 *
	 * @param buffer  ByteBuffer
	 * @param charset 编码
	 * @return 一行
	 */
	public static String readLine(ByteBuffer buffer, Charset charset) {
		final int startPosition = buffer.position();
		final int endPosition = lineEnd(buffer);

		if (endPosition > startPosition) {
			byte[] bs = readBytes(buffer, startPosition, endPosition);
			return StrUtil.str(bs, charset);
		} else if (endPosition == startPosition) {
			return StrUtil.EMPTY;
		}

		return null;
	}

	/**
	 * 创建新Buffer
	 *
	 * @param data 数据
	 * @return {@link ByteBuffer}
	 * @since 4.5.0
	 */
	public static ByteBuffer create(byte[] data) {
		return ByteBuffer.wrap(data);
	}

	/**
	 * 从字符串创建新Buffer
	 *
	 * @param data    数据
	 * @param charset 编码
	 * @return {@link ByteBuffer}
	 * @since 4.5.0
	 */
	public static ByteBuffer create(CharSequence data, Charset charset) {
		return create(StrUtil.bytes(data, charset));
	}

	/**
	 * 从字符串创建新Buffer，使用UTF-8编码
	 *
	 * @param data 数据
	 * @return {@link ByteBuffer}
	 * @since 4.5.0
	 */
	public static ByteBuffer createUtf8(CharSequence data) {
		return create(StrUtil.utf8Bytes(data));
	}

	/**
	 * 创建{@link CharBuffer}
	 *
	 * @param capacity 容量
	 * @return {@link CharBuffer}
	 * @since 5.5.7
	 */
	public static CharBuffer createCharBuffer(int capacity) {
		return CharBuffer.allocate(capacity);
	}
}
