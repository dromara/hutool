package cn.hutool.core.text.split;

import cn.hutool.core.regex.PatternPool;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.text.finder.CharFinder;
import cn.hutool.core.text.finder.CharMatcherFinder;
import cn.hutool.core.text.finder.LengthFinder;
import cn.hutool.core.text.finder.PatternFinder;
import cn.hutool.core.text.finder.StrFinder;
import cn.hutool.core.util.CharUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 字符串切分器，封装统一的字符串分割静态方法
 * @author Looly
 * @since 5.7.0
 */
public class SplitUtil {

	//---------------------------------------------------------------------------------------------- Split by char

	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * <p>去除每个元素两边空格，大小写敏感，忽略空串</p>
	 *
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitPath(final CharSequence str) {
		return splitPath(str, 0);
	}

	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * <p>去除每个元素两边空格，大小写敏感，忽略空串</p>
	 *
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitPathToArray(final CharSequence str) {
		return toArray(splitPath(str));
	}

	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * <p>去除每个元素两边空格，大小写敏感，忽略空串</p>
	 *
	 * @param str   被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitPath(final CharSequence str, final int limit) {
		return split(str, CharUtil.SLASH, limit, true, true);
	}

	/**
	 * 切分字符串路径，仅支持Unix分界符：/
	 * <p>去除每个元素两边空格，大小写敏感，忽略空串</p>
	 *
	 * @param str   被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitPathToArray(final CharSequence str, final int limit) {
		return toArray(splitPath(str, limit));
	}

	/**
	 * 切分字符串，大小写敏感，去除每个元素两边空白符
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(final CharSequence str, final char separator, final boolean ignoreEmpty) {
		return split(str, separator, 0, true, ignoreEmpty);
	}

	/**
	 * 切分字符串
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence str, final char separator, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, 0, isTrim, ignoreEmpty);
	}

	/**
	 * 切分字符串，大小写敏感，去除每个元素两边空白符
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitTrim(final CharSequence str, final char separator, final int limit, final boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty, false);
	}

	/**
	 * 切分字符串，大小写敏感
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence str, final char separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}

	/**
	 * 切分字符串，大小写敏感
	 *
	 * @param <R>         切分后的元素类型
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @param mapping     切分后的字符串元素的转换方法
	 * @return 切分后的集合，元素类型是经过 mapping 转换后的
	 * @since 5.7.14
	 */
	public static <R> List<R> split(final CharSequence str, final char separator, final int limit, final boolean ignoreEmpty, final Function<String, R> mapping) {
		return split(str, separator, limit, ignoreEmpty, false, mapping);
	}

	/**
	 * 切分字符串，忽略大小写
	 *
	 * @param text        被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitIgnoreCase(final CharSequence text, final char separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return split(text, separator, limit, isTrim, ignoreEmpty, true);
	}

	/**
	 * 切分字符串
	 *
	 * @param text        被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase  是否忽略大小写
	 * @return 切分后的集合
	 */
	public static List<String> split(final CharSequence text, final char separator, final int limit, final boolean isTrim, final boolean ignoreEmpty, final boolean ignoreCase) {
		return split(text, separator, limit, ignoreEmpty, ignoreCase, trimFunc(isTrim));
	}

	/**
	 * 切分字符串<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param <R>         切分后的元素类型
	 * @param text        被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase  是否忽略大小写
	 * @param mapping     切分后的字符串元素的转换方法
	 * @return 切分后的集合，元素类型是经过 mapping 转换后的
	 * @since 5.7.14
	 */
	public static <R> List<R> split(final CharSequence text, final char separator, final int limit, final boolean ignoreEmpty,
									final boolean ignoreCase, final Function<String, R> mapping) {
		if (StrUtil.isEmpty(text)) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new CharFinder(separator, ignoreCase), limit, ignoreEmpty);
		return splitIter.toList(mapping);
	}

	/**
	 * 切分字符串为字符串数组
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(final CharSequence str, final char separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}

	//---------------------------------------------------------------------------------------------- Split by String

	/**
	 * 切分字符串，不忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence str, final String separator, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, -1, isTrim, ignoreEmpty, false);
	}

	/**
	 * 切分字符串，去除每个元素两边空格，不忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(final CharSequence str, final String separator, final boolean ignoreEmpty) {
		return split(str, separator, true, ignoreEmpty);
	}

	/**
	 * 切分字符串，不忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence str, final String separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}

	/**
	 * 切分字符串，去除每个元素两边空格，不忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrim(final CharSequence str, final String separator, final int limit, final boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty);
	}

	/**
	 * 切分字符串，忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitIgnoreCase(final CharSequence str, final String separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, true);
	}

	/**
	 * 切分字符串，去除每个元素两边空格，忽略大小写
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> splitTrimIgnoreCase(final CharSequence str, final String separator, final int limit, final boolean ignoreEmpty) {
		return split(str, separator, limit, true, ignoreEmpty, true);
	}

	/**
	 * 切分字符串<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param text        被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase  是否忽略大小写
	 * @return 切分后的集合
	 * @since 3.2.1
	 */
	public static List<String> split(final CharSequence text, final String separator, final int limit, final boolean isTrim, final boolean ignoreEmpty, final boolean ignoreCase) {
		if (StrUtil.isEmpty(text)) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new StrFinder(separator, ignoreCase), limit, ignoreEmpty);
		return splitIter.toList(isTrim);
	}

	/**
	 * 切分字符串为字符串数组
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(final CharSequence str, final String separator, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return toArray(split(str, separator, limit, isTrim, ignoreEmpty));
	}

	//---------------------------------------------------------------------------------------------- Split by Whitespace

	/**
	 * 使用空白符切分字符串<br>
	 * 切分后的字符串两边不包含空白符，空串或空白符串并不做为元素之一<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param text  被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence text, final int limit) {
		if (StrUtil.isBlank(text)) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new CharMatcherFinder(CharUtil::isBlankChar), limit, true);
		return splitIter.toList(false);
	}

	/**
	 * 切分字符串为字符串数组
	 *
	 * @param str   被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(final CharSequence str, final int limit) {
		return toArray(split(str, limit));
	}
	//---------------------------------------------------------------------------------------------- Split by regex

	/**
	 * 通过正则切分字符串
	 *
	 * @param text           字符串
	 * @param separatorRegex 分隔符正则
	 * @param limit          限制分片数，小于等于0表示无限制
	 * @param isTrim         是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty    是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitByRegex(final CharSequence text, final String separatorRegex, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		final Pattern pattern = PatternPool.get(separatorRegex);
		return split(text, pattern, limit, isTrim, ignoreEmpty);
	}

	/**
	 * 通过正则切分字符串<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param text             字符串
	 * @param separatorPattern 分隔符正则{@link Pattern}
	 * @param limit            限制分片数，小于等于0表示无限制
	 * @param isTrim           是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty      是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(final CharSequence text, final Pattern separatorPattern, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		if (StrUtil.isEmpty(text)) {
			return new ArrayList<>(0);
		}
		final SplitIter splitIter = new SplitIter(text, new PatternFinder(separatorPattern), limit, ignoreEmpty);
		return splitIter.toList(isTrim);
	}

	/**
	 * 通过正则切分字符串为字符串数组
	 *
	 * @param str              被切分的字符串
	 * @param separatorPattern 分隔符正则{@link Pattern}
	 * @param limit            限制分片数，小于等于0表示无限制
	 * @param isTrim           是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty      是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static String[] splitToArray(final CharSequence str, final Pattern separatorPattern, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return toArray(split(str, separatorPattern, limit, isTrim, ignoreEmpty));
	}
	//---------------------------------------------------------------------------------------------- Split by length

	/**
	 * 根据给定长度，将给定字符串截取为多个部分
	 *
	 * @param text 字符串
	 * @param len  每一个小节的长度，必须大于0
	 * @return 截取后的字符串数组
	 */
	public static String[] splitByLength(final CharSequence text, final int len) {
		if (StrUtil.isEmpty(text)) {
			return new String[0];
		}
		final SplitIter splitIter = new SplitIter(text, new LengthFinder(len), -1, false);
		return splitIter.toArray(false);
	}
	//---------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * List转Array
	 *
	 * @param list List
	 * @return Array
	 */
	private static String[] toArray(final List<String> list) {
		return list.toArray(new String[0]);
	}

	/**
	 * Trim函数
	 *
	 * @param isTrim 是否trim
	 * @return {@link Function}
	 */
	public static Function<String, String> trimFunc(final boolean isTrim) {
		return isTrim ? StrUtil::trim : Function.identity();
	}
	//---------------------------------------------------------------------------------------------------------- Private method end
}
