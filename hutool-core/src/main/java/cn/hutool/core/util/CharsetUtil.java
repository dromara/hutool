package cn.hutool.core.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类
 *
 * @author xiaoleilu
 */
public class CharsetUtil {

	/**
	 * ISO-8859-1
	 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	/**
	 * UTF-8
	 */
	public static final String UTF_8 = "UTF-8";
	/**
	 * GBK
	 */
	public static final String GBK = "GBK";

	/**
	 * ISO-8859-1
	 */
	public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
	/**
	 * UTF-8
	 */
	public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
	/**
	 * GBK
	 */
	public static final Charset CHARSET_GBK;

	static {
		//避免不支持GBK的系统中运行报错 issue#731
		Charset _CHARSET_GBK = null;
		try {
			_CHARSET_GBK = Charset.forName(GBK);
		} catch (UnsupportedCharsetException e) {
			//ignore
		}
		CHARSET_GBK = _CHARSET_GBK;
	}

	/**
	 * 转换为Charset对象
	 *
	 * @param charsetName 字符集，为空则返回默认字符集
	 * @return Charset
	 * @throws UnsupportedCharsetException 编码不支持
	 */
	public static Charset charset(String charsetName) throws UnsupportedCharsetException {
		return StrUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
	}

	/**
	 * 解析字符串编码为Charset对象，解析失败返回系统默认编码
	 *
	 * @param charsetName    字符集，为空则返回默认字符集
	 * @return Charset
	 * @since 5.2.6
	 */
	public static Charset parse(String charsetName) {
		return parse(charsetName, Charset.defaultCharset());
	}

	/**
	 * 解析字符串编码为Charset对象，解析失败返回默认编码
	 *
	 * @param charsetName    字符集，为空则返回默认字符集
	 * @param defaultCharset 解析失败使用的默认编码
	 * @return Charset
	 * @since 5.2.6
	 */
	public static Charset parse(String charsetName, Charset defaultCharset) {
		if (StrUtil.isBlank(charsetName)) {
			return defaultCharset;
		}

		Charset result;
		try {
			result = Charset.forName(charsetName);
		} catch (UnsupportedCharsetException e) {
			result = defaultCharset;
		}

		return result;
	}

	/**
	 * 转换字符串的字符集编码
	 *
	 * @param source      字符串
	 * @param srcCharset  源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, String srcCharset, String destCharset) {
		return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
	}

	/**
	 * 转换字符串的字符集编码<br>
	 * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br>
	 * 此方法用于纠正因读取使用编码错误导致的乱码问题。<br>
	 * 例如，在Servlet请求中客户端用GBK编码了请求参数，我们使用UTF-8读取到的是乱码，此时，使用此方法即可还原原编码的内容
	 * <pre>
	 * 客户端 -》 GBK编码 -》 Servlet容器 -》 UTF-8解码 -》 乱码
	 * 乱码 -》 UTF-8编码 -》 GBK解码 -》 正确内容
	 * </pre>
	 *
	 * @param source      字符串
	 * @param srcCharset  源字符集，默认ISO-8859-1
	 * @param destCharset 目标字符集，默认UTF-8
	 * @return 转换后的字符集
	 */
	public static String convert(String source, Charset srcCharset, Charset destCharset) {
		if (null == srcCharset) {
			srcCharset = StandardCharsets.ISO_8859_1;
		}

		if (null == destCharset) {
			destCharset = StandardCharsets.UTF_8;
		}

		if (StrUtil.isBlank(source) || srcCharset.equals(destCharset)) {
			return source;
		}
		return new String(source.getBytes(srcCharset), destCharset);
	}

	/**
	 * 转换文件编码<br>
	 * 此方法用于转换文件编码，读取的文件实际编码必须与指定的srcCharset编码一致，否则导致乱码
	 *
	 * @param file        文件
	 * @param srcCharset  原文件的编码，必须与文件内容的编码保持一致
	 * @param destCharset 转码后的编码
	 * @return 被转换编码的文件
	 * @since 3.1.0
	 */
	public static File convert(File file, Charset srcCharset, Charset destCharset) {
		final String str = FileUtil.readString(file, srcCharset);
		return FileUtil.writeString(str, file, destCharset);
	}

	/**
	 * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取 {@link CharsetUtil#defaultCharsetName()}
	 *
	 * @return 系统字符集编码
	 * @see CharsetUtil#defaultCharsetName()
	 * @since 3.1.2
	 */
	public static String systemCharsetName() {
		return systemCharset().name();
	}

	/**
	 * 系统字符集编码，如果是Windows，则默认为GBK编码，否则取 {@link CharsetUtil#defaultCharsetName()}
	 *
	 * @return 系统字符集编码
	 * @see CharsetUtil#defaultCharsetName()
	 * @since 3.1.2
	 */
	public static Charset systemCharset() {
		return FileUtil.isWindows() ? CHARSET_GBK : defaultCharset();
	}

	/**
	 * 系统默认字符集编码
	 *
	 * @return 系统字符集编码
	 */
	public static String defaultCharsetName() {
		return defaultCharset().name();
	}

	/**
	 * 系统默认字符集编码
	 *
	 * @return 系统字符集编码
	 */
	public static Charset defaultCharset() {
		return Charset.defaultCharset();
	}
}
