package cn.hutool.core.io.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.url.URLUtil;
import cn.hutool.core.regex.ReUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.SystemUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件名相关工具类
 *
 * @author looly
 * @since 5.4.1
 */
public class FileNameUtil {

	/**
	 * .java文件扩展名
	 */
	public static final String EXT_JAVA = ".java";
	/**
	 * .class文件扩展名
	 */
	public static final String EXT_CLASS = ".class";
	/**
	 * .jar文件扩展名
	 */
	public static final String EXT_JAR = ".jar";

	/**
	 * 在Jar中的路径jar的扩展名形式
	 */
	public static final String EXT_JAR_PATH = ".jar!";

	/**
	 * 类Unix路径分隔符
	 */
	public static final char UNIX_SEPARATOR = CharUtil.SLASH;
	/**
	 * Windows路径分隔符
	 */
	public static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;

	/**
	 * Windows下文件名中的无效字符
	 */
	private static final Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|\r\n]");

	/**
	 * 特殊后缀
	 */
	private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};


	// -------------------------------------------------------------------------------------------- name start

	/**
	 * 返回文件名
	 *
	 * @param file 文件
	 * @return 文件名
	 * @since 4.1.13
	 */
	public static String getName(final File file) {
		return (null != file) ? file.getName() : null;
	}

	/**
	 * 返回文件名<br>
	 * <pre>
	 * "d:/test/aaa" 返回 "aaa"
	 * "/test/aaa.jpg" 返回 "aaa.jpg"
	 * </pre>
	 *
	 * @param filePath 文件
	 * @return 文件名
	 * @since 4.1.13
	 */
	public static String getName(final String filePath) {
		if (null == filePath) {
			return null;
		}
		int len = filePath.length();
		if (0 == len) {
			return filePath;
		}
		if (CharUtil.isFileSeparator(filePath.charAt(len - 1))) {
			// 以分隔符结尾的去掉结尾分隔符
			len--;
		}

		int begin = 0;
		char c;
		for (int i = len - 1; i > -1; i--) {
			c = filePath.charAt(i);
			if (CharUtil.isFileSeparator(c)) {
				// 查找最后一个路径分隔符（/或者\）
				begin = i + 1;
				break;
			}
		}

		return filePath.substring(begin, len);
	}

	/**
	 * 获取文件后缀名，扩展名不带“.”
	 *
	 * @param file 文件
	 * @return 扩展名
	 * @see #extName(File)
	 * @since 5.3.8
	 */
	public static String getSuffix(final File file) {
		return extName(file);
	}

	/**
	 * 获得文件后缀名，扩展名不带“.”
	 *
	 * @param fileName 文件名
	 * @return 扩展名
	 * @see #extName(String)
	 * @since 5.3.8
	 */
	public static String getSuffix(final String fileName) {
		return extName(fileName);
	}

	/**
	 * 增加临时扩展名
	 *
	 * @param fileName 文件名
	 * @param suffix 临时扩展名，如果为空，使用`.temp`
	 * @return 临时文件名
	 */
	public static String addTempSuffix(final String fileName, String suffix){
		if (StrUtil.isBlank(suffix)) {
			suffix = ".temp";
		} else {
			suffix = StrUtil.addPrefixIfNot(suffix, StrUtil.DOT);
		}

		return fileName + suffix;
	}

	/**
	 * 返回主文件名
	 *
	 * @param file 文件
	 * @return 主文件名
	 * @see #mainName(File)
	 * @since 5.3.8
	 */
	public static String getPrefix(final File file) {
		return mainName(file);
	}

	/**
	 * 返回主文件名
	 *
	 * @param fileName 完整文件名
	 * @return 主文件名
	 * @see #mainName(String)
	 * @since 5.3.8
	 */
	public static String getPrefix(final String fileName) {
		return mainName(fileName);
	}

	/**
	 * 返回主文件名
	 *
	 * @param file 文件
	 * @return 主文件名
	 */
	public static String mainName(final File file) {
		if (file.isDirectory()) {
			return file.getName();
		}
		return mainName(file.getName());
	}

	/**
	 * 返回主文件名
	 *
	 * @param fileName 完整文件名
	 * @return 主文件名
	 */
	public static String mainName(final String fileName) {
		if (null == fileName) {
			return null;
		}
		int len = fileName.length();
		if (0 == len) {
			return fileName;
		}

		//issue#2642，多级扩展名的主文件名
		for (final CharSequence specialSuffix : SPECIAL_SUFFIX) {
			if(StrUtil.endWith(fileName, "." + specialSuffix)){
				return StrUtil.subPre(fileName, len - specialSuffix.length() - 1);
			}
		}

		if (CharUtil.isFileSeparator(fileName.charAt(len - 1))) {
			len--;
		}

		int begin = 0;
		int end = len;
		char c;
		for (int i = len - 1; i >= 0; i--) {
			c = fileName.charAt(i);
			if (len == end && CharUtil.DOT == c) {
				// 查找最后一个文件名和扩展名的分隔符：.
				end = i;
			}
			// 查找最后一个路径分隔符（/或者\），如果这个分隔符在.之后，则继续查找，否则结束
			if (CharUtil.isFileSeparator(c)) {
				begin = i + 1;
				break;
			}
		}

		return fileName.substring(begin, end);
	}

	/**
	 * 获取文件扩展名（后缀名），扩展名不带“.”
	 *
	 * @param file 文件
	 * @return 扩展名
	 */
	public static String extName(final File file) {
		if (null == file) {
			return null;
		}
		if (file.isDirectory()) {
			return null;
		}
		return extName(file.getName());
	}

	/**
	 * 获得文件的扩展名（后缀名），扩展名不带“.”
	 *
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String extName(final String fileName) {
		if (fileName == null) {
			return null;
		}
		final int index = fileName.lastIndexOf(StrUtil.DOT);
		if (index == -1) {
			return StrUtil.EMPTY;
		} else {
			// issue#I4W5FS@Gitee
			final int secondToLastIndex = fileName.substring(0, index).lastIndexOf(StrUtil.DOT);
			final String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
			if (StrUtil.containsAny(substr, SPECIAL_SUFFIX)) {
				return substr;
			}

			final String ext = fileName.substring(index + 1);
			// 扩展名中不能包含路径相关的符号
			return StrUtil.containsAny(ext, UNIX_SEPARATOR, WINDOWS_SEPARATOR) ? StrUtil.EMPTY : ext;
		}
	}

	/**
	 * 清除文件名中的在Windows下不支持的非法字符，包括： \ / : * ? " &lt; &gt; |
	 *
	 * @param fileName 文件名（必须不包括路径，否则路径符将被替换）
	 * @return 清理后的文件名
	 * @since 3.3.1
	 */
	public static String cleanInvalid(final String fileName) {
		return StrUtil.isBlank(fileName) ? fileName : ReUtil.delAll(FILE_NAME_INVALID_PATTERN_WIN, fileName);
	}

	/**
	 * 文件名中是否包含在Windows下不支持的非法字符，包括： \ / : * ? " &lt; &gt; |
	 *
	 * @param fileName 文件名（必须不包括路径，否则路径符将被替换）
	 * @return 是否包含非法字符
	 * @since 3.3.1
	 */
	public static boolean containsInvalid(final String fileName) {
		return (false == StrUtil.isBlank(fileName)) && ReUtil.contains(FILE_NAME_INVALID_PATTERN_WIN, fileName);
	}

	/**
	 * 根据文件名检查文件类型，忽略大小写
	 *
	 * @param fileName 文件名，例如hutool.png
	 * @param extNames 被检查的扩展名数组，同一文件类型可能有多种扩展名，扩展名不带“.”
	 * @return 是否是指定扩展名的类型
	 * @since 5.5.2
	 */
	public static boolean isType(final String fileName, final String... extNames) {
		return StrUtil.equalsAnyIgnoreCase(extName(fileName), extNames);
	}

	/**
	 * 修复路径<br>
	 * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
	 * <ol>
	 * <li>1. 统一用 /</li>
	 * <li>2. 多个 / 转换为一个 /</li>
	 * <li>3. 去除左边空格</li>
	 * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
	 * </ol>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * "/foo//" =》 "/foo/"
	 * "/foo/./" =》 "/foo/"
	 * "/foo/../bar" =》 "/bar"
	 * "/foo/../bar/" =》 "/bar/"
	 * "/foo/../bar/../baz" =》 "/baz"
	 * "/../" =》 "/"
	 * "foo/bar/.." =》 "foo"
	 * "foo/../bar" =》 "bar"
	 * "foo/../../bar" =》 "bar"
	 * "//server/foo/../bar" =》 "/server/bar"
	 * "//server/../bar" =》 "/bar"
	 * "C:\\foo\\..\\bar" =》 "C:/bar"
	 * "C:\\..\\bar" =》 "C:/bar"
	 * "~/foo/../bar/" =》 "~/bar/"
	 * "~/../bar" =》 普通用户运行是'bar的home目录'，ROOT用户运行是'/bar'
	 * </pre>
	 *
	 * @param path 原路径
	 * @return 修复后的路径
	 */
	public static String normalize(final String path) {
		if (path == null) {
			return null;
		}

		// 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
		String pathToUse = StrUtil.removePrefixIgnoreCase(path, URLUtil.CLASSPATH_URL_PREFIX);
		// 去除file:前缀
		pathToUse = StrUtil.removePrefixIgnoreCase(pathToUse, URLUtil.FILE_URL_PREFIX);

		// 识别home目录形式，并转换为绝对路径
		if (StrUtil.startWith(pathToUse, '~')) {
			pathToUse = SystemUtil.getUserHomePath() + pathToUse.substring(1);
		}

		// 统一使用斜杠
		pathToUse = pathToUse.replaceAll("[/\\\\]+", StrUtil.SLASH);
		// 去除开头空白符，末尾空白符合法，不去除
		pathToUse = StrUtil.trimStart(pathToUse);
		//兼容Windows下的共享目录路径（原始路径如果以\\开头，则保留这种路径）
		if (path.startsWith("\\\\")) {
			pathToUse = "\\" + pathToUse;
		}

		String prefix = StrUtil.EMPTY;
		final int prefixIndex = pathToUse.indexOf(StrUtil.COLON);
		if (prefixIndex > -1) {
			// 可能Windows风格路径
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (StrUtil.startWith(prefix, CharUtil.SLASH)) {
				// 去除类似于/C:这类路径开头的斜杠
				prefix = prefix.substring(1);
			}
			if (false == prefix.contains(StrUtil.SLASH)) {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			} else {
				// 如果前缀中包含/,说明非Windows风格path
				prefix = StrUtil.EMPTY;
			}
		}
		if (pathToUse.startsWith(StrUtil.SLASH)) {
			prefix += StrUtil.SLASH;
			pathToUse = pathToUse.substring(1);
		}

		final List<String> pathList = StrUtil.split(pathToUse, CharUtil.SLASH);

		final List<String> pathElements = new LinkedList<>();
		int tops = 0;
		String element;
		for (int i = pathList.size() - 1; i >= 0; i--) {
			element = pathList.get(i);
			// 只处理非.的目录，即只处理非当前目录
			if (false == StrUtil.DOT.equals(element)) {
				if (StrUtil.DOUBLE_DOT.equals(element)) {
					tops++;
				} else {
					if (tops > 0) {
						// 有上级目录标记时按照个数依次跳过
						tops--;
					} else {
						// Normal path element found.
						pathElements.add(0, element);
					}
				}
			}
		}

		// issue#1703@Github
		if (tops > 0 && StrUtil.isEmpty(prefix)) {
			// 只有相对路径补充开头的..，绝对路径直接忽略之
			while (tops-- > 0) {
				//遍历完节点发现还有上级标注（即开头有一个或多个..），补充之
				// Normal path element found.
				pathElements.add(0, StrUtil.DOUBLE_DOT);
			}
		}

		return prefix + CollUtil.join(pathElements, StrUtil.SLASH);
	}
	// -------------------------------------------------------------------------------------------- name end
}
