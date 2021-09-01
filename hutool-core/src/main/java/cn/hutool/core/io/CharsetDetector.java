package cn.hutool.core.io;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

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
				"UTF-8",
				"GBK",
				"GB2312",
				"GB18030",
				"UTF-16BE",
				"UTF-16LE",
				"UTF-16",
				"BIG5",
				"UNICODE",
				"US-ASCII"};
		DEFAULT_CHARSETS = Convert.convert(Charset[].class, names);
	}

	/**
	 * 探测文件编码
	 *
	 * @param file     文件
	 * @param charsets 需要测试用的编码，null或空使用默认的编码数组
	 * @return 编码
	 * @since 5.6.7
	 */
	public static Charset detect(File file, Charset... charsets) {
		return detect(FileUtil.getInputStream(file), charsets);
	}

	/**
	 * 探测编码<br>
	 * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持reset方法的流
	 *
	 * @param in       流，使用后关闭此流
	 * @param charsets 需要测试用的编码，null或空使用默认的编码数组
	 * @return 编码
	 */
	public static Charset detect(InputStream in, Charset... charsets) {
		return detect(IoUtil.DEFAULT_BUFFER_SIZE, in, charsets);
	}

	/**
	 * 探测编码<br>
	 * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持reset方法的流
	 *
	 * @param bufferSize 自定义缓存大小，即每次检查的长度
	 * @param in         流，使用后关闭此流
	 * @param charsets   需要测试用的编码，null或空使用默认的编码数组
	 * @return 编码
	 * @since 5.7.10
	 */
	public static Charset detect(int bufferSize, InputStream in, Charset... charsets) {
		if (ArrayUtil.isEmpty(charsets)) {
			charsets = DEFAULT_CHARSETS;
		}

		final byte[] buffer = new byte[bufferSize];
		try {
			while (in.read(buffer) > -1) {
				for (Charset charset : charsets) {
					final CharsetDecoder decoder = charset.newDecoder();
					if (identify(buffer, decoder)) {
						return charset;
					}
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
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
