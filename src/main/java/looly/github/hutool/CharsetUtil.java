package looly.github.hutool;

import java.io.UnsupportedEncodingException;

import looly.github.hutool.exceptions.UtilException;

/**
 * 字符集工具类
 * @author xiaoleilu
 *
 */
public class CharsetUtil {
	
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	
	/**
	 * 转换字符串的字符集编码
	 * @param source 字符串
	 * @param srcCharset 源字符集，默认ISO-8859-1
	 * @param newCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, String srcCharset, String newCharset) {
		if(StrUtil.isBlank(srcCharset)) {
			srcCharset = ISO_8859_1;
		}
		
		if(StrUtil.isBlank(newCharset)) {
			srcCharset = UTF_8;
		}
		
		if (StrUtil.isBlank(source) || srcCharset.equals(newCharset)) {
			return source;
		}
		try {
			return new String(source.getBytes(srcCharset), newCharset);
		} catch (UnsupportedEncodingException unex) {
			throw new UtilException(unex);
		}
	}
	
	/**
	 * 将编码的byte数据转换为字符串
	 * @param data 数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(byte[] data, String charset) {
		if(data == null) {
			return null;
		}
		
		if(StrUtil.isBlank(charset)) {
			return new String(data);
		}
		
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}
}
