/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text.split;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.finder.*;
import org.dromara.hutool.core.text.CharUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 字符串切分器，封装统一的字符串分割静态方法
 *
 * @author Looly
 * @since 5.7.0
 */
public class SplitUtil {

	/**
	 * 无限制切分个数
	 */
	public static final int UNLIMITED = -1;

	// region ----- split to

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项，并转为结果类型
	 *
	 * @param <T>        结果类型
	 * @param str        被切分的字符串
	 * @param separator  分隔符字符
	 * @param resultType 结果类型的类，可以是数组或集合
	 * @return long数组
	 */
	public static <T> T splitTo(final CharSequence str, final CharSequence separator, final Class<T> resultType) {
		return Convert.convert(resultType, splitTrim(str, separator));
	}
	// endregion

	// region ----- Split by String or char

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 * @since 3.2.0
	 */
	public static List<String> splitTrim(final CharSequence str, final CharSequence separator) {
		return split(str, separator, true, true);
	}

	/**
	 * 切分字符串，如果分隔符不存在则返回原字符串<br>
	 * 此方法不会去除切分字符串后每个元素两边的空格，不忽略空串
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符
	 * @return 字符串
	 */
	public static String[] splitToArray(final CharSequence str, final CharSequence separator) {
		if (str == null) {
			return new String[]{};
		}
		return toArray(split(str, separator));
	}

	/**
	 * 切分字符串，如果分隔符不存在则返回原字符串<br>
	 * 此方法不会去除切分字符串后每个元素两边的空格，不忽略空串
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符
	 * @return 字符串
	 */
	public static List<String> split(final CharSequence str, final CharSequence separator) {
		return split(str, separator, false, false);
	}

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
	public static List<String> split(final CharSequence str, final CharSequence separator,
									 final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, UNLIMITED, isTrim, ignoreEmpty, false);
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
	public static List<String> split(final CharSequence str, final CharSequence separator,
									 final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return split(str, separator, limit, isTrim, ignoreEmpty, false);
	}

	/**
	 * 切分字符串<br>
	 * 如果提供的字符串为{@code null}，则返回一个空的{@link ArrayList}<br>
	 * 如果提供的字符串为""，则当ignoreEmpty时返回空的{@link ArrayList}，否则返回只有一个""元素的{@link ArrayList}
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase  是否忽略大小写
	 * @return 切分后的集合
	 */
	public static List<String> split(final CharSequence str, final CharSequence separator,
									 final int limit, final boolean isTrim, final boolean ignoreEmpty, final boolean ignoreCase) {
		return split(str, separator, limit, ignoreEmpty, ignoreCase, trimFunc(isTrim));
	}

	/**
	 * 切分字符串<br>
	 * 如果提供的字符串为{@code null}，则返回一个空的{@link ArrayList}<br>
	 * 如果提供的字符串为""，则当ignoreEmpty时返回空的{@link ArrayList}，否则返回只有一个""元素的{@link ArrayList}
	 *
	 * @param <R>         元素类型
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符串
	 * @param limit       限制分片数，小于等于0表示无限制
	 * @param ignoreEmpty 是否忽略空串
	 * @param ignoreCase  是否忽略大小写
	 * @param mapping     切分后字段映射函数
	 * @return 切分后的集合
	 */
	public static <R> List<R> split(final CharSequence str, final CharSequence separator,
									final int limit, final boolean ignoreEmpty, final boolean ignoreCase,
									final Function<String, R> mapping) {
		if (null == str) {
			return ListUtil.zero();
		} else if (0 == str.length() && ignoreEmpty) {
			return ListUtil.zero();
		}
		Assert.notEmpty(separator, "Separator must be not empty!");

		// 查找分隔符的方式
		final TextFinder finder = separator.length() == 1 ?
				new CharFinder(separator.charAt(0), ignoreCase) :
				StrFinder.of(separator, ignoreCase);

		final SplitIter splitIter = new SplitIter(str, finder, limit, ignoreEmpty);
		return splitIter.toList(mapping);
	}
	// endregion

	// region ----- Split by XXX

	/**
	 * 切分路径字符串<br>
	 * 如果为空字符串或者null 则返回空集合<br><br>
	 * 空路径会被忽略
	 *
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 */
	public static List<String> splitPath(final CharSequence str) {
		return splitPath(str, UNLIMITED);
	}

	/**
	 * 切分路径字符串<br>
	 * 如果为空字符串或者null 则返回空集合<br>
	 * 空路径会被忽略
	 *
	 * @param str   被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitPath(final CharSequence str, final int limit) {
		if (StrUtil.isBlank(str)) {
			return ListUtil.zero();
		}

		final SplitIter splitIter = new SplitIter(str,
				new CharMatcherFinder(
						(c) -> c == CharUtil.SLASH || c == CharUtil.BACKSLASH),
				// 路径中允许空格
				limit, true);
		return splitIter.toList(false);
	}

	/**
	 * 使用空白符切分字符串<br>
	 * 切分后的字符串两边不包含空白符，空串或空白符串并不做为元素之一<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param str 被切分的字符串
	 * @return 切分后的集合
	 */
	public static List<String> splitByBlank(final CharSequence str) {
		return splitByBlank(str, UNLIMITED);
	}

	/**
	 * 使用空白符切分字符串<br>
	 * 切分后的字符串两边不包含空白符，空串或空白符串并不做为元素之一<br>
	 * 如果为空字符串或者null 则返回空集合
	 *
	 * @param str   被切分的字符串
	 * @param limit 限制分片数，小于等于0表示无限制
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitByBlank(final CharSequence str, final int limit) {
		if (StrUtil.isBlank(str)) {
			return ListUtil.zero();
		}
		final SplitIter splitIter = new SplitIter(str, new CharMatcherFinder(CharUtil::isBlankChar), limit, true);
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
	public static String[] splitByBlankToArray(final CharSequence str, final int limit) {
		return toArray(splitByBlank(str, limit));
	}

	/**
	 * 通过正则切分字符串，规则如下：
	 * <ul>
	 *     <li>当提供的str为{@code null}时，返回new ArrayList(0)</li>
	 *     <li>当提供的str为{@code ""}时，返回[""]</li>
	 *     <li>当提供的separatorRegex为empty(null or "")时，返回[str]，即只有原串一个元素的数组</li>
	 * </ul>
	 *
	 * @param str            字符串
	 * @param separatorRegex 分隔符正则
	 * @param limit          限制分片数，小于等于0表示无限制
	 * @param isTrim         是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty    是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitByRegex(final CharSequence str, final String separatorRegex, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return splitByRegex(str,
			// 给定字符串或正则为empty，就不再需要解析pattern
			(StrUtil.isEmpty(str) || StrUtil.isEmpty(separatorRegex)) ? null : PatternPool.get(separatorRegex),
			limit, isTrim, ignoreEmpty);
	}

	/**
	 * 通过正则切分字符串，规则如下：
	 * <ul>
	 *     <li>当提供的str为{@code null}时，返回new ArrayList(0)</li>
	 *     <li>当提供的str为{@code ""}时，返回[""]</li>
	 *     <li>当提供的separatorRegex为empty(null or "")时，返回[str]，即只有原串一个元素的数组</li>
	 * </ul>
	 *
	 * @param str              字符串
	 * @param separatorPattern 分隔符正则{@link Pattern}
	 * @param limit            限制分片数，小于等于0表示无限制
	 * @param isTrim           是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty      是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> splitByRegex(final CharSequence str, final Pattern separatorPattern, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		if (null == str) {
			return ListUtil.zero();
		}
		if(0 == str.length()){
			return ignoreEmpty ? ListUtil.zero() : ListUtil.of(StrUtil.EMPTY);
		}
		if(null == separatorPattern){
			final String result = str.toString();
			if(StrUtil.isEmpty(result)){
				return ignoreEmpty ? ListUtil.zero() : ListUtil.of(StrUtil.EMPTY);
			}
			return ListUtil.of(result);
		}
		final SplitIter splitIter = new SplitIter(str, new PatternFinder(separatorPattern), limit, ignoreEmpty);
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
	public static String[] splitByRegexToArray(final CharSequence str, final Pattern separatorPattern, final int limit, final boolean isTrim, final boolean ignoreEmpty) {
		return toArray(splitByRegex(str, separatorPattern, limit, isTrim, ignoreEmpty));
	}

	/**
	 * 根据给定长度，将给定字符串截取为多个部分
	 *
	 * @param str 字符串
	 * @param len 每一个小节的长度，必须大于0
	 * @return 截取后的字符串数组
	 */
	public static String[] splitByLength(final CharSequence str, final int len) {
		if (StrUtil.isEmpty(str)) {
			return new String[0];
		}
		final SplitIter splitIter = new SplitIter(str, new LengthFinder(len), -1, false);
		return splitIter.toArray(false);
	}
	// endregion

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
