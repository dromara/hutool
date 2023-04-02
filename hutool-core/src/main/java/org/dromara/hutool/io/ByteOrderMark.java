package org.dromara.hutool.io;

import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.util.CharsetUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Byte Order Mark (BOM) 头描述<br>
 * BOM定义：<a href="http://www.unicode.org/unicode/faq/utf_bom.html">http://www.unicode.org/unicode/faq/utf_bom.html</a>
 * <ul>
 * 	<li>EF BB BF = UTF-8</li>
 * 	<li>FE FF = UTF-16BE, big-endian</li>
 * 	<li>FF FE =  UTF-16LE, little-endian</li>
 * 	<li>00 00 FE FF = UTF-32BE, big-endian</li>
 * 	<li>FF FE 00 00 = UTF-32LE, little-endian</li>
 * </ul>
 *
 * <p>来自：Apache-commons-io</p>
 *
 * @author Apache-commons-io
 */
public class ByteOrderMark implements Predicate<byte[]>, Comparable<ByteOrderMark>, Serializable {
	private static final long serialVersionUID = 1L;

	// region ----- BOMs
	/**
	 * UTF-8 BOM.
	 */
	public static final ByteOrderMark UTF_8 = new ByteOrderMark(CharsetUtil.NAME_UTF_8,
			(byte) 0xEF, (byte) 0xBB, (byte) 0xBF);

	/**
	 * UTF-16BE BOM (Big-Endian).
	 */
	public static final ByteOrderMark UTF_16BE = new ByteOrderMark("UTF-16BE",
			(byte) 0xFE, (byte) 0xFF);

	/**
	 * UTF-16LE BOM (Little-Endian).
	 */
	public static final ByteOrderMark UTF_16LE = new ByteOrderMark("UTF-16LE",
			(byte) 0xFF, (byte) 0xFE);

	/**
	 * UTF-32BE BOM (Big-Endian).
	 */
	public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE",
			(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF);

	/**
	 * UTF-32LE BOM (Little-Endian).
	 */
	public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE",
			(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00);

	/**
	 * 预定义的所有BOM信息
	 */
	public static final ByteOrderMark[] ALL = new ByteOrderMark[]{
			UTF_32BE,
			UTF_32LE,
			UTF_8,
			UTF_16BE,
			UTF_16LE
	};
	// endregion

	private final String charsetName;
	private final byte[] bytes;

	/**
	 * 构造
	 *
	 * @param charsetName BOM定义的编码名称
	 * @param bytes       BOM bytes
	 * @throws IllegalArgumentException 编码名称为空或者bytes为空
	 */
	public ByteOrderMark(final String charsetName, final byte... bytes) {
		if (ArrayUtil.isEmpty(bytes)) {
			throw new IllegalArgumentException("No bytes specified");
		}
		this.charsetName = Assert.notEmpty(charsetName, "No charsetName specified");
		this.bytes = new byte[bytes.length];
		System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
	}

	/**
	 * 获取BOM头定义的编码名称.
	 *
	 * @return 编码名称
	 */
	public String getCharsetName() {
		return charsetName;
	}

	/**
	 * 获取BOM头byte数
	 *
	 * @return BOM头byte数
	 */
	public int length() {
		return bytes.length;
	}

	/**
	 * 获取指定位置的byte值
	 *
	 * @param pos The position
	 * @return The specified byte
	 */
	public int get(final int pos) {
		return bytes[pos];
	}

	/**
	 * Gets a copy of the BOM's bytes.
	 *
	 * @return a copy of the BOM's bytes
	 */
	public byte[] getBytes() {
		return Arrays.copyOfRange(bytes, 0, bytes.length);
	}

	/**
	 * 是否匹配头部BOM信息<br>
	 * 当提供的长度小于BOM需要检查的长度时，返回{code false}
	 *
	 * @param headBytes 头部bytes
	 * @return 是否匹配头部BOM信息
	 */
	@Override
	public boolean test(final byte[] headBytes) {
		if (headBytes.length < bytes.length) {
			return false;
		}
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != headBytes[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ByteOrderMark)) {
			return false;
		}
		final ByteOrderMark bom = (ByteOrderMark) obj;
		return Arrays.equals(this.bytes, bom.bytes);
	}

	@Override
	public int hashCode() {
		int hashCode = getClass().hashCode();
		for (final int b : bytes) {
			hashCode += b;
		}
		return hashCode;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append('[');
		builder.append(charsetName);
		builder.append(": ");
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0) {
				builder.append(",");
			}
			builder.append("0x");
			builder.append(Integer.toHexString(0xFF & bytes[i]).toUpperCase());
		}
		builder.append(']');
		return builder.toString();
	}

	@Override
	public int compareTo(final ByteOrderMark o) {
		// 按照长度倒序
		return Integer.compare(o.length(), this.length());
	}
}
