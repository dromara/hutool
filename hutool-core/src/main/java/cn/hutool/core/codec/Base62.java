package cn.hutool.core.codec;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Base62工具类，提供Base62的编码和解码方案<br>
 * 
 * @author Looly
 * @since 4.5.9
 */
public class Base62 {

	private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
	private static final Base62Codec CODEC = Base62Codec.createGmp();

	// -------------------------------------------------------------------- encode
	/**
	 * Base62编码
	 * 
	 * @param source 被编码的Base62字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(CharSequence source) {
		return encode(source, DEFAULT_CHARSET);
	}

	/**
	 * Base62编码
	 * 
	 * @param source 被编码的Base62字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String encode(CharSequence source, Charset charset) {
		return encode(StrUtil.bytes(source, charset));
	}

	/**
	 * Base62编码
	 * 
	 * @param source 被编码的Base62字符串
	 * @return 被加密后的字符串
	 */
	public static String encode(byte[] source) {
		return new String(CODEC.encode(source));
	}

	/**
	 * Base62编码
	 * 
	 * @param in 被编码Base62的流（一般为图片流或者文件流）
	 * @return 被加密后的字符串
	 */
	public static String encode(InputStream in) {
		return encode(IoUtil.readBytes(in));
	}

	/**
	 * Base62编码
	 * 
	 * @param file 被编码Base62的文件
	 * @return 被加密后的字符串
	 */
	public static String encode(File file) {
		return encode(FileUtil.readBytes(file));
	}

	// -------------------------------------------------------------------- decode
	/**
	 * Base62解码
	 * 
	 * @param source 被解码的Base62字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStrGbk(CharSequence source) {
		return decodeStr(source, CharsetUtil.CHARSET_GBK);
	}

	/**
	 * Base62解码
	 * 
	 * @param source 被解码的Base62字符串
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(CharSequence source) {
		return decodeStr(source, DEFAULT_CHARSET);
	}

	/**
	 * Base62解码
	 * 
	 * @param source 被解码的Base62字符串
	 * @param charset 字符集
	 * @return 被加密后的字符串
	 */
	public static String decodeStr(CharSequence source, Charset charset) {
		return StrUtil.str(decode(source), charset);
	}

	/**
	 * Base62解码
	 * 
	 * @param Base62 被解码的Base62字符串
	 * @param destFile 目标文件
	 * @return 目标文件
	 */
	public static File decodeToFile(CharSequence Base62, File destFile) {
		return FileUtil.writeBytes(decode(Base62), destFile);
	}

	/**
	 * Base62解码
	 * 
	 * @param base62Str 被解码的Base62字符串
	 * @param out 写出到的流
	 * @param isCloseOut 是否关闭输出流
	 */
	public static void decodeToStream(CharSequence base62Str, OutputStream out, boolean isCloseOut) {
		IoUtil.write(out, isCloseOut, decode(base62Str));
	}

	/**
	 * Base62解码
	 * 
	 * @param base62Str 被解码的Base62字符串
	 * @return 被加密后的字符串
	 */
	public static byte[] decode(CharSequence base62Str) {
		return decode(StrUtil.bytes(base62Str, DEFAULT_CHARSET));
	}

	/**
	 * 解码Base62
	 * 
	 * @param base62bytes Base62输入
	 * @return 解码后的bytes
	 */
	public static byte[] decode(byte[] base62bytes) {
		return CODEC.decode(base62bytes);
	}
}
