package cn.hutool.core.io;

import cn.hutool.core.util.ArrayUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 编码探测器
 *
 * @author looly
 * @since 5.4.7
 */
public class CharsetDetector {

	/**
	 * 默认的参与测试的编码
	 */
	private static final Charset[] DEFAULT_CHARSETS;

	static {
		String[] names = {
				"US-ASCII",
				"UTF-8",
				"GBK",
				"GB2312",
				"BIG5",
				"GB18030",
				"UTF-16BE",
				"UTF-16LE",
				"UTF-16",
				"UNICODE"};
		final List<Charset> list = new ArrayList<>();
		for (String name : names) {
			try {
				list.add(Charset.forName(name));
			} catch (UnsupportedCharsetException ignore) {
				//ignore
			}
		}
		DEFAULT_CHARSETS = list.toArray(new Charset[0]);
	}

	/**
	 * 探测编码
	 *
	 * @param in       流，使用后关闭此流
	 * @param charsets 需要测试用的编码，null或空使用默认的编码数组
	 * @return 编码
	 */
	public static Charset detect(InputStream in, Charset... charsets) {
		if (ArrayUtil.isEmpty(charsets)) {
			charsets = DEFAULT_CHARSETS;
		}
		for (Charset charset : charsets) {
			charset = detectCharset(in, charset);
			if (null != charset) {
				return charset;
			}
		}
		return null;
	}

	/**
	 * 判断编码
	 *
	 * @param in      流
	 * @param charset 编码
	 * @return 编码
	 */
	private static Charset detectCharset(InputStream in, Charset charset) {
		try (BufferedInputStream input = IoUtil.toBuffered(in)) {
			CharsetDecoder decoder = charset.newDecoder();

			byte[] buffer = new byte[512];
			while (input.read(buffer) > -1) {
				if (identify(buffer, decoder)) {
					return charset;
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return null;
	}

	/**
	 * 通过try的方式测试指定bytes是否可以被解码，从而判断是否为指定编码
	 *
	 * @param bytes   测试的bytes
	 * @param decoder 解码器
	 * @return 是否是指定编码
	 */
	private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
		try {
			decoder.decode(ByteBuffer.wrap(bytes));
		} catch (CharacterCodingException e) {
			return false;
		}
		return true;
	}
}
