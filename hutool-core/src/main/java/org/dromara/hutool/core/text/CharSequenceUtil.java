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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.comparator.VersionComparator;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.finder.CharFinder;
import org.dromara.hutool.core.text.finder.CharMatcherFinder;
import org.dromara.hutool.core.text.finder.Finder;
import org.dromara.hutool.core.text.finder.StrFinder;
import org.dromara.hutool.core.text.placeholder.StrFormatter;
import org.dromara.hutool.core.text.replacer.RangeReplacerByChar;
import org.dromara.hutool.core.text.replacer.RangeReplacerByStr;
import org.dromara.hutool.core.text.replacer.SearchReplacer;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link CharSequence} 相关工具类封装，包括但不限于：
 * <ul>
 *     <li>字符串补充前缀或后缀：addXXX</li>
 *     <li>字符串补充长度：padXXX</li>
 *     <li>字符串包含关系：containsXXX</li>
 *     <li>字符串默认值：defaultIfXXX</li>
 *     <li>字符串查找：indexOf</li>
 *     <li>字符串判断以什么结尾：endWith</li>
 *     <li>字符串判断以什么开始：startWith</li>
 *     <li>字符串匹配：equals</li>
 *     <li>字符串格式化：format</li>
 *     <li>字符串去除：removeXXX</li>
 *     <li>字符串重复：repeat</li>
 *     <li>获取子串：sub</li>
 *     <li>去除两边的指定字符串（只去除一次）：strip</li>
 *     <li>去除两边的指定所有字符：trim</li>
 *     <li>去除两边的指定所有字符包装和去除包装：wrap</li>
 * </ul>
 * <p>
 * 需要注意的是，strip、trim、wrap（unWrap）的策略不同：
 * <ul>
 *     <li>strip： 强调去除两边或某一边的指定字符串，这个字符串不会重复去除，如果一边不存在，另一边不影响去除</li>
 *     <li>trim：  强调去除两边指定字符，如果这个字符有多个，全部去除，例如去除两边所有的空白符。</li>
 *     <li>unWrap：强调去包装，要求包装的前后字符都要存在，只有一个则不做处理，如去掉双引号包装。</li>
 * </ul>
 *
 * @author looly
 * @since 5.5.3
 */
public class CharSequenceUtil extends StrValidator {

	/**
	 * 未找到的的位置表示，用-1表示
	 *
	 * @see Finder#INDEX_NOT_FOUND
	 */
	public static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;

	/**
	 * 字符串常量：空格符 {@code " "}
	 */
	public static final String SPACE = " ";

	/**
	 * {@link CharSequence} 转为字符串，null安全
	 *
	 * @param cs {@link CharSequence}
	 * @return 字符串
	 */
	public static String str(final CharSequence cs) {
		return null == cs ? null : cs.toString();
	}

	// region ----- defaultIf

	/**
	 * 当给定字符串为null时，转换为Empty
	 *
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyIfNull(final CharSequence str) {
		return ObjUtil.defaultIfNull(str, EMPTY).toString();
	}

	/**
	 * 当给定字符串为空字符串时，转换为{@code null}
	 *
	 * @param <T> 字符串类型
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static <T extends CharSequence> T nullIfEmpty(final T str) {
		return isEmpty(str) ? null : str;
	}

	/**
	 * 如果给定对象为{@code null}或者 "" 返回默认值
	 *
	 * <pre>
	 *   defaultIfEmpty(null, null)      = null
	 *   defaultIfEmpty(null, "")        = ""
	 *   defaultIfEmpty("", "zz")      = "zz"
	 *   defaultIfEmpty(" ", "zz")      = " "
	 *   defaultIfEmpty("abc", *)        = "abc"
	 * </pre>
	 *
	 * @param <T>          对象类型（必须实现CharSequence接口）
	 * @param str          被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}或者 ""返回的默认值，可以为{@code null}或者 ""
	 * @return 被检查对象为{@code null}或者 ""返回默认值，否则返回原值
	 * @since 5.0.4
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultValue) {
		return isEmpty(str) ? defaultValue : str;
	}

	/**
	 * 如果给定对象为{@code null}或者{@code ""}返回defaultHandler处理的结果, 否则返回自定义handler处理后的返回值
	 *
	 * @param <T>             被检查对象类型
	 * @param <V>             结果类型
	 * @param str             String 类型
	 * @param handler         非empty的处理方法
	 * @param defaultSupplier empty时的处理方法
	 * @return 处理后的返回值
	 */
	public static <T extends CharSequence, V> V defaultIfEmpty(final T str, final Function<T, V> handler, final Supplier<? extends V> defaultSupplier) {
		if (isNotEmpty(str)) {
			return handler.apply(str);
		}
		return defaultSupplier.get();
	}

	/**
	 * 如果给定对象为{@code null}或者""或者空白符返回默认值
	 *
	 * <pre>
	 *   defaultIfBlank(null, null)      = null
	 *   defaultIfBlank(null, "")        = ""
	 *   defaultIfBlank("", "zz")      = "zz"
	 *   defaultIfBlank(" ", "zz")      = "zz"
	 *   defaultIfBlank("abc", *)        = "abc"
	 * </pre>
	 *
	 * @param <T>          对象类型（必须实现CharSequence接口）
	 * @param str          被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}或者 ""或者空白符返回的默认值，可以为{@code null}或者 ""或者空白符
	 * @return 被检查对象为{@code null}或者 ""或者空白符返回默认值，否则返回原值
	 * @since 5.0.4
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultValue) {
		return isBlank(str) ? defaultValue : str;
	}

	/**
	 * 如果被检查对象为 {@code null} 或 "" 或 空白字符串时，返回默认值（由 defaultValueSupplier 提供）；否则直接返回
	 *
	 * @param str             被检查对象
	 * @param handler         非blank的处理方法
	 * @param defaultSupplier 默认值提供者
	 * @param <T>             对象类型（必须实现CharSequence接口）
	 * @param <V>             结果类型
	 * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
	 * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
	 * @since 5.7.20
	 */
	public static <T extends CharSequence, V> V defaultIfBlank(final T str, final Function<T, V> handler, final Supplier<? extends V> defaultSupplier) {
		if (isBlank(str)) {
			return defaultSupplier.get();
		}
		return handler.apply(str);
	}
	// endregion

	// region ----- trim

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * <ul>
	 *     <li>去除字符串空格罗列相关如下：</li>
	 *     <li>{@link StrUtil#trimPrefix(CharSequence)}去除头部空格</li>
	 *     <li>{@link StrUtil#trimSuffix(CharSequence)}去除尾部空格</li>
	 *     <li>{@link StrUtil#cleanBlank(CharSequence)}去除头部、尾部、中间空格</li>
	 * </ul>
	 *
	 * <pre>
	 * trim(null)          = null
	 * trim(&quot;&quot;)            = &quot;&quot;
	 * trim(&quot;     &quot;)       = &quot;&quot;
	 * trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去头尾空白的字符串，如果原字串为{@code null}，则返回{@code null}
	 */
	public static String trim(final CharSequence str) {
		return StrTrimer.TRIM_BLANK.apply(str);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，返回{@code ""}。
	 *
	 * <pre>
	 * trimToEmpty(null)          = ""
	 * trimToEmpty("")            = ""
	 * trimToEmpty("     ")       = ""
	 * trimToEmpty("abc")         = "abc"
	 * trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为null返回""
	 * @since 3.1.1
	 */
	public static String trimToEmpty(final CharSequence str) {
		return str == null ? EMPTY : trim(str);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}或者""，返回{@code null}。
	 *
	 * <pre>
	 * trimToNull(null)          = null
	 * trimToNull("")            = null
	 * trimToNull("     ")       = null
	 * trimToNull("abc")         = "abc"
	 * trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为空返回null
	 * @since 3.2.1
	 */
	public static String trimToNull(final CharSequence str) {
		final String trimStr = trim(str);
		return EMPTY.equals(trimStr) ? null : trimStr;
	}

	/**
	 * 除去字符串头部的空白，如果字符串是{@code null}，则返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trimPrefix(null)         = null
	 * trimPrefix(&quot;&quot;)           = &quot;&quot;
	 * trimPrefix(&quot;abc&quot;)        = &quot;abc&quot;
	 * trimPrefix(&quot;  abc&quot;)      = &quot;abc&quot;
	 * trimPrefix(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * trimPrefix(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
	 */
	public static String trimPrefix(final CharSequence str) {
		return StrTrimer.TRIM_PREFIX_BLANK.apply(str);
	}

	/**
	 * 除去字符串尾部的空白，如果字符串是{@code null}，则返回{@code null}。
	 *
	 * <p>
	 * 注意，和{@link String#trim()}不同，此方法使用{@link CharUtil#isBlankChar(char)} 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trimSuffix(null)       = null
	 * trimSuffix(&quot;&quot;)         = &quot;&quot;
	 * trimSuffix(&quot;abc&quot;)      = &quot;abc&quot;
	 * trimSuffix(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * trimSuffix(&quot;abc  &quot;)    = &quot;abc&quot;
	 * trimSuffix(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 * @return 除去空白的字符串，如果原字串为{@code null}或结果字符串为{@code ""}，则返回 {@code null}
	 */
	public static String trimSuffix(final CharSequence str) {
		return StrTrimer.TRIM_SUFFIX_BLANK.apply(str);
	}

	/**
	 * 除去字符串头尾部的空白符，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * @param str  要处理的字符串
	 * @param mode 去除模式，可选去除头部、尾部、两边
	 * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
	 */
	public static String trim(final CharSequence str, final StrTrimer.TrimMode mode) {
		return new StrTrimer(mode, CharUtil::isBlankChar).apply(str);
	}

	/**
	 * 按照断言，除去字符串头尾部的断言为真的字符，如果字符串是{@code null}，依然返回{@code null}。
	 *
	 * @param str       要处理的字符串
	 * @param mode      去除模式，可选去除头部、尾部、两边
	 * @param predicate 断言是否过掉字符，返回{@code true}表述过滤掉，{@code false}表示不过滤
	 * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
	 */
	public static String trim(final CharSequence str, final StrTrimer.TrimMode mode, final Predicate<Character> predicate) {
		return new StrTrimer(mode, predicate).apply(str);
	}
	// endregion

	// region ----- startWith

	/**
	 * 字符串是否以给定字符开始
	 *
	 * @param str 字符串
	 * @param c   字符
	 * @return 是否开始
	 */
	public static boolean startWith(final CharSequence str, final char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(0);
	}

	/**
	 * 是否以指定字符串开头
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(final CharSequence str, final CharSequence prefix) {
		return startWith(str, prefix, false);
	}

	/**
	 * 是否以指定字符串开头，忽略相等字符串的情况
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头并且两个字符串不相等
	 */
	public static boolean startWithIgnoreEquals(final CharSequence str, final CharSequence prefix) {
		return startWith(str, prefix, false, true);
	}

	/**
	 * 是否以指定字符串开头，忽略大小写
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
		return startWith(str, prefix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串开始<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param prefixes 需要检测的开始字符串
	 * @return 给定字符串是否以任何一个字符串开始
	 * @since 3.0.6
	 */
	public static boolean startWithAny(final CharSequence str, final CharSequence... prefixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
			return false;
		}

		for (final CharSequence suffix : prefixes) {
			if (startWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 给定字符串是否以任何一个字符串开始（忽略大小写）<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param suffixes 需要检测的开始字符串
	 * @return 给定字符串是否以任何一个字符串开始
	 * @since 6.0.0
	 */
	public static boolean startWithAnyIgnoreCase(final CharSequence str, final CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (final CharSequence suffix : suffixes) {
			if (startWith(str, suffix, true)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str        被监测字符串
	 * @param prefix     开头字符串
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 * @since 5.4.3
	 */
	public static boolean startWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
		return startWith(str, prefix, ignoreCase, false);
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false<br>
	 * <pre>
	 *     CharSequenceUtil.startWith("123", "123", false, true);   -- false
	 *     CharSequenceUtil.startWith("ABCDEF", "abc", true, true); -- true
	 *     CharSequenceUtil.startWith("abc", "abc", true, true);    -- false
	 * </pre>
	 *
	 * @param str          被监测字符串
	 * @param prefix       开头字符串
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @return 是否以指定字符串开头
	 * @since 5.4.3
	 */
	public static boolean startWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase, final boolean ignoreEquals) {
		return new StrRegionMatcher(ignoreCase, ignoreEquals, true)
			.test(str, prefix);
	}
	// endregion

	// region ----- endWith

	/**
	 * 字符串是否以给定字符结尾
	 *
	 * @param str 字符串
	 * @param c   字符
	 * @return 是否结尾
	 */
	public static boolean endWith(final CharSequence str, final char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(str.length() - 1);
	}

	/**
	 * 是否以指定字符串结尾
	 *
	 * @param str    被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(final CharSequence str, final CharSequence suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * 是否以指定字符串结尾，忽略大小写
	 *
	 * @param str    被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
		return endWith(str, suffix, true);
	}

	/**
	 * 给定字符串是否以任何一个字符串结尾<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param suffixes 需要检测的结尾字符串
	 * @return 给定字符串是否以任何一个字符串结尾
	 * @since 3.0.6
	 */
	public static boolean endWithAny(final CharSequence str, final CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (final CharSequence suffix : suffixes) {
			if (endWith(str, suffix, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
	 * 给定字符串和数组为空都返回false
	 *
	 * @param str      给定字符串
	 * @param suffixes 需要检测的结尾字符串
	 * @return 给定字符串是否以任何一个字符串结尾
	 * @since 5.5.9
	 */
	public static boolean endWithAnyIgnoreCase(final CharSequence str, final CharSequence... suffixes) {
		if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
			return false;
		}

		for (final CharSequence suffix : suffixes) {
			if (endWith(str, suffix, true)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否以指定字符串结尾<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str        被监测字符串
	 * @param suffix     结尾字符串
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
		return endWith(str, suffix, ignoreCase, false);
	}

	/**
	 * 是否以指定字符串结尾<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str          被监测字符串
	 * @param suffix       结尾字符串
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @return 是否以指定字符串结尾
	 * @since 5.8.0
	 */
	public static boolean endWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase, final boolean ignoreEquals) {
		return new StrRegionMatcher(ignoreCase, ignoreEquals, false)
			.test(str, suffix);
	}
	// endregion

	// region ----- contains

	/**
	 * 指定字符是否在字符串中出现过
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 是否包含
	 * @since 3.1.2
	 */
	public static boolean contains(final CharSequence str, final char searchChar) {
		return indexOf(str, searchChar) > -1;
	}

	/**
	 * 指定字符串是否在字符串中出现过
	 *
	 * @param str       字符串
	 * @param searchStr 被查找的字符串
	 * @return 是否包含
	 * @since 5.1.1
	 */
	public static boolean contains(final CharSequence str, final CharSequence searchStr) {
		if (null == str || null == searchStr) {
			return false;
		}
		return str.toString().contains(searchStr);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 * @since 3.2.0
	 */
	public static boolean containsAny(final CharSequence str, final CharSequence... testStrs) {
		return null != getContainsStr(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符列表中的任意一个字符
	 *
	 * @param str       指定字符串
	 * @param testChars 需要检查的字符数组
	 * @return 是否包含任意一个字符
	 * @since 4.1.11
	 */
	public static boolean containsAny(final CharSequence str, final char... testChars) {
		if (isNotEmpty(str)) {
			final int len = str.length();
			for (int i = 0; i < len; i++) {
				if (ArrayUtil.contains(testChars, str.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查指定字符串中是否只包含给定的字符<br>
	 * <ul>
	 *     <li>str 是 null，testChars 也是 null，直接返回 true</li>
	 *     <li>str 是 null，testChars 不是 null，直接返回 true</li>
	 *     <li>str 不是 null，testChars 是 null，直接返回 false</li>
	 * </ul>
	 *
	 * @param str       字符串
	 * @param testChars 检查的字符
	 * @return 字符串含有非检查的字符，返回false
	 * @since 4.4.1
	 */
	public static boolean containsOnly(final CharSequence str, final char... testChars) {
		if (isNotEmpty(str)) {
			final int len = str.length();
			for (int i = 0; i < len; i++) {
				if (!ArrayUtil.contains(testChars, str.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 给定字符串是否包含空白符（空白符包括空格、制表符、全角空格和不间断空格）<br>
	 * 如果给定字符串为null或者""，则返回false
	 *
	 * @param str 字符串
	 * @return 是否包含空白符
	 * @since 4.0.8
	 */
	public static boolean containsBlank(final CharSequence str) {
		if (null == str) {
			return false;
		}
		final int length = str.length();
		if (0 == length) {
			return false;
		}

		for (int i = 0; i < length; i += 1) {
			if (CharUtil.isBlankChar(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 * @since 3.2.0
	 */
	public static String getContainsStr(final CharSequence str, final CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (final CharSequence checkStr : testStrs) {
			if (contains(str, checkStr)) {
				return checkStr.toString();
			}
		}
		return null;
	}

	/**
	 * 是否包含特定字符，忽略大小写，如果给定两个参数都为{@code null}，返回true
	 *
	 * @param str     被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(final CharSequence str, final CharSequence testStr) {
		if (null == str) {
			// 如果被监测字符串和
			return null == testStr;
		}
		return indexOfIgnoreCase(str, testStr) > -1;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 * @since 3.2.0
	 */
	public static boolean containsAnyIgnoreCase(final CharSequence str, final CharSequence... testStrs) {
		return null != getContainsStrIgnoreCase(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 * @since 3.2.0
	 */
	public static String getContainsStrIgnoreCase(final CharSequence str, final CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (final CharSequence testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	/**
	 * 检查指定字符串中是否含给定的所有字符串
	 *
	 * @param str       字符串
	 * @param testChars 检查的字符
	 * @return 字符串含有非检查的字符，返回false
	 * @since 4.4.1
	 */
	public static boolean containsAll(final CharSequence str, final CharSequence... testChars) {
		if (isBlank(str) || ArrayUtil.isEmpty(testChars)) {
			return false;
		}
		for (final CharSequence testChar : testChars) {
			if (!contains(str, testChar)) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region ----- indexOf

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 位置
	 */
	public static int indexOf(final CharSequence str, final char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @return 位置
	 */
	public static int indexOf(final CharSequence str, final char searchChar, final int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param text       字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 */
	public static int indexOf(final CharSequence text, final char searchChar, final int start, final int end) {
		if (isEmpty(text)) {
			return INDEX_NOT_FOUND;
		}
		return new CharFinder(searchChar).setText(text).setEndIndex(end).start(start);
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param text    字符串
	 * @param matcher 被查找的字符匹配器
	 * @param start   起始位置，如果小于0，从0开始查找
	 * @param end     终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 * @since 6.0.0
	 */
	public static int indexOf(final CharSequence text, final Predicate<Character> matcher, final int start, final int end) {
		if (isEmpty(text)) {
			return INDEX_NOT_FOUND;
		}
		return new CharMatcherFinder(matcher).setText(text).setEndIndex(end).start(start);
	}

	/**
	 * 指定范围内查找字符串，忽略大小写<br>
	 *
	 * <pre>
	 * indexOfIgnoreCase(null, *, *)          = -1
	 * indexOfIgnoreCase(*, null, *)          = -1
	 * indexOfIgnoreCase("", "", 0)           = 0
	 * indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * 指定范围内查找字符串
	 *
	 * <pre>
	 * indexOfIgnoreCase(null, *, *)          = -1
	 * indexOfIgnoreCase(*, null, *)          = -1
	 * indexOfIgnoreCase("", "", 0)           = 0
	 * indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, final int fromIndex) {
		return indexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 指定范围内查找字符串
	 *
	 * @param text       字符串，空则返回-1
	 * @param searchStr  需要查找位置的字符串，空则返回-1
	 * @param from       起始位置（包含）
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOf(final CharSequence text, final CharSequence searchStr, final int from, final boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		return StrFinder.of(searchStr, ignoreCase).setText(text).start(from);
	}

	/**
	 * 指定范围内查找字符串，忽略大小写
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * 指定范围内查找字符串，忽略大小写<br>
	 * fromIndex 为搜索起始位置，从后往前计数
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置，从后往前计数
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, final int fromIndex) {
		return lastIndexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 指定范围内查找字符串<br>
	 * fromIndex 为搜索起始位置，从后往前计数
	 *
	 * @param text       字符串
	 * @param searchStr  需要查找位置的字符串
	 * @param from       起始位置，从后往前计数
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOf(final CharSequence text, final CharSequence searchStr, final int from, final boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		return StrFinder.of(searchStr, ignoreCase)
			.setText(text).setNegative(true).start(from);
	}

	/**
	 * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置。
	 *
	 * <p>
	 * 如果 str=null 或 searchStr=null 或 ordinal&ge;0 则返回-1<br>
	 * 此方法来自：Apache-Commons-Lang
	 * <p>
	 * 例子（*代表任意字符）：
	 *
	 * <pre>
	 * ordinalIndexOf(null, *, *)          = -1
	 * ordinalIndexOf(*, null, *)          = -1
	 * ordinalIndexOf("", "", *)           = 0
	 * ordinalIndexOf("aabaabaa", "a", 1)  = 0
	 * ordinalIndexOf("aabaabaa", "a", 2)  = 1
	 * ordinalIndexOf("aabaabaa", "b", 1)  = 2
	 * ordinalIndexOf("aabaabaa", "b", 2)  = 5
	 * ordinalIndexOf("aabaabaa", "ab", 1) = 1
	 * ordinalIndexOf("aabaabaa", "ab", 2) = 4
	 * ordinalIndexOf("aabaabaa", "", 1)   = 0
	 * ordinalIndexOf("aabaabaa", "", 2)   = 0
	 * </pre>
	 *
	 * @param str       被检查的字符串，可以为null
	 * @param searchStr 被查找的字符串，可以为null
	 * @param ordinal   第几次出现的位置
	 * @return 查找到的位置
	 * @since 3.2.3
	 */
	public static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return 0;
		}
		int found = 0;
		int index = INDEX_NOT_FOUND;
		do {
			index = indexOf(str, searchStr, index + 1, false);
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}
	// endregion

	// region ----- remove

	/**
	 * 移除字符串中所有给定字符串<br>
	 * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
	 *
	 * @param str         字符串
	 * @param strToRemove 被移除的字符串
	 * @return 移除后的字符串
	 */
	public static String removeAll(final CharSequence str, final CharSequence strToRemove) {
		// strToRemove如果为空， 也不用继续后面的逻辑
		if (isEmpty(str) || isEmpty(strToRemove)) {
			return str(str);
		}
		return str.toString().replace(strToRemove, EMPTY);
	}

	/**
	 * 移除字符串中所有给定字符串，当某个字符串出现多次，则全部移除<br>
	 * 例：removeAny("aa-bb-cc-dd", "a", "b") =》 --cc-dd
	 *
	 * @param str          字符串
	 * @param strsToRemove 被移除的字符串
	 * @return 移除后的字符串
	 * @since 5.3.8
	 */
	public static String removeAny(final CharSequence str, final CharSequence... strsToRemove) {
		String result = str(str);
		if (isNotEmpty(str)) {
			for (final CharSequence strToRemove : strsToRemove) {
				result = removeAll(result, strToRemove);
			}
		}
		return result;
	}

	/**
	 * 去除字符串中指定的多个字符，如有多个则全部去除
	 *
	 * @param str   字符串
	 * @param chars 字符列表
	 * @return 去除后的字符
	 * @since 4.2.2
	 */
	public static String removeAll(final CharSequence str, final char... chars) {
		if (null == str || ArrayUtil.isEmpty(chars)) {
			return str(str);
		}
		return filter(str, (c) -> !ArrayUtil.contains(chars, c));
	}

	/**
	 * 去除所有换行符，包括：
	 *
	 * <pre>
	 * 1. \r
	 * 1. \n
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 处理后的字符串
	 * @since 4.2.2
	 */
	public static String removeAllLineBreaks(final CharSequence str) {
		return removeAll(str, CharUtil.CR, CharUtil.LF);
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, preLength=3 =》 return name
	 *
	 * @param str       被处理的字符串
	 * @param preLength 去掉的长度
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(final CharSequence str, final int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			final char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.toString().substring(preLength + 1);
			}
			return String.valueOf(first);
		} else {
			return str.toString();
		}
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, prefix=set =》 return name
	 *
	 * @param str    被处理的字符串
	 * @param prefix 前缀
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(final CharSequence str, final CharSequence prefix) {
		return lowerFirst(removePrefix(str, prefix));
	}

	/**
	 * 去掉指定前缀
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefix(final CharSequence str, final CharSequence prefix) {
		return removePrefix(str, prefix, false);
	}

	/**
	 * 忽略大小写去掉指定前缀
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefixIgnoreCase(final CharSequence str, final CharSequence prefix) {
		return removePrefix(str, prefix, true);
	}

	/**
	 * 去掉指定前缀
	 *
	 * @param str        字符串
	 * @param prefix     前缀
	 * @param ignoreCase 是否忽略大小写
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefix(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (startWith(str, prefix, ignoreCase)) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(final CharSequence str, final CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 截取前半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀，并小写首字母
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSufAndLowerFirst(final CharSequence str, final CharSequence suffix) {
		return lowerFirst(removeSuffix(str, suffix));
	}

	/**
	 * 忽略大小写去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffixIgnoreCase(final CharSequence str, final CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (endWithIgnoreCase(str, suffix)) {
			return subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * 清理空白字符
	 *
	 * @param str 被清理的字符串
	 * @return 清理后的字符串
	 */
	public static String cleanBlank(final CharSequence str) {
		return filter(str, c -> !CharUtil.isBlankChar(c));
	}
	// endregion

	// region ----- strip

	/**
	 * 去除两边的指定字符串
	 *
	 * @param str            被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String strip(final CharSequence str, final CharSequence prefixOrSuffix) {
		if (equals(str, prefixOrSuffix)) {
			// 对于去除相同字符的情况单独处理
			return EMPTY;
		}
		return strip(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串<br>
	 * 两边字符如果存在，则去除，不存在不做处理
	 *
	 * @param str    被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String strip(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		if (isEmpty(str)) {
			return str(str);
		}

		int from = 0;
		int to = str.length();

		final String str2 = str.toString();
		if (startWith(str2, prefix)) {
			from = prefix.length();
		}
		if (endWith(str2, suffix)) {
			to -= suffix.length();
		}

		return str2.substring(Math.min(from, to), Math.max(from, to));
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 *
	 * @param str            被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(final CharSequence str, final CharSequence prefixOrSuffix) {
		return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 *
	 * @param str    被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		int from = 0;
		int to = str.length();

		final String str2 = str.toString();
		if (startWithIgnoreCase(str2, prefix)) {
			from = prefix.length();
		}
		if (endWithIgnoreCase(str2, suffix)) {
			to -= suffix.length();
		}
		return str2.substring(from, to);
	}
	// endregion

	// region ----- add

	/**
	 * 如果给定字符串不是以prefix开头的，在开头补充 prefix
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 补充后的字符串
	 * @see #prependIfMissing(CharSequence, CharSequence, CharSequence...)
	 */
	public static String addPrefixIfNot(final CharSequence str, final CharSequence prefix) {
		return prependIfMissing(str, prefix, prefix);
	}

	/**
	 * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 补充后的字符串
	 * @see #appendIfMissing(CharSequence, CharSequence, CharSequence...)
	 */
	public static String addSuffixIfNot(final CharSequence str, final CharSequence suffix) {
		return appendIfMissing(str, suffix);
	}
	// endregion

	/**
	 * 将字符串切分为N等份
	 *
	 * @param str        字符串
	 * @param partLength 每等份的长度
	 * @return 切分后的数组
	 * @since 3.0.6
	 */
	public static String[] cut(final CharSequence str, final int partLength) {
		if (null == str) {
			return null;
		}
		final int len = str.length();
		if (len < partLength) {
			return new String[]{str.toString()};
		}
		final int part = NumberUtil.count(len, partLength);
		final String[] array = new String[part];

		final String str2 = str.toString();
		for (int i = 0; i < part; i++) {
			array[i] = str2.substring(i * partLength, (i == part - 1) ? len : (partLength + i * partLength));
		}
		return array;
	}

	// region ----- sub

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 =》 c <br>
	 * abcdefgh 2 -3 =》 cde <br>
	 *
	 * @param str              String
	 * @param fromIndexInclude 开始的index（包括）
	 * @param toIndexExclude   结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(final CharSequence str, int fromIndexInclude, int toIndexExclude) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int len = str.length();

		if (fromIndexInclude < 0) {
			fromIndexInclude = len + fromIndexInclude;
			if (fromIndexInclude < 0) {
				fromIndexInclude = 0;
			}
		} else if (fromIndexInclude > len) {
			fromIndexInclude = len;
		}

		if (toIndexExclude < 0) {
			toIndexExclude = len + toIndexExclude;
			if (toIndexExclude < 0) {
				toIndexExclude = len;
			}
		} else if (toIndexExclude > len) {
			toIndexExclude = len;
		}

		if (toIndexExclude < fromIndexInclude) {
			final int tmp = fromIndexInclude;
			fromIndexInclude = toIndexExclude;
			toIndexExclude = tmp;
		}

		if (fromIndexInclude == toIndexExclude) {
			return EMPTY;
		}

		return str.toString().substring(fromIndexInclude, toIndexExclude);
	}

	/**
	 * 通过CodePoint截取字符串，可以截断Emoji
	 *
	 * @param str       String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex   结束的index（不包括）
	 * @return 字串
	 */
	public static String subByCodePoint(final CharSequence str, final int fromIndex, final int toIndex) {
		if (isEmpty(str)) {
			return str(str);
		}

		if (fromIndex < 0 || fromIndex > toIndex) {
			throw new IllegalArgumentException();
		}

		if (fromIndex == toIndex) {
			return EMPTY;
		}

		final StringBuilder sb = new StringBuilder();
		final int subLen = toIndex - fromIndex;
		str.toString().codePoints().skip(fromIndex).limit(subLen).forEach(v -> sb.append(Character.toChars(v)));
		return sb.toString();
	}

	/**
	 * 截取部分字符串，这里一个汉字的长度认为是2
	 *
	 * @param str    字符串
	 * @param len    bytes切割到的位置（包含）
	 * @param suffix 切割后加上后缀
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subPreGbk(final CharSequence str, final int len, final CharSequence suffix) {
		return subPreGbk(str, len, true) + suffix;
	}

	/**
	 * 截取部分字符串，这里一个汉字的长度认为是2<br>
	 * 可以自定义halfUp，如len为10，如果截取后最后一个字符是半个字符，{@code true}表示保留，则长度是11，否则长度9
	 *
	 * @param str    字符串
	 * @param len    bytes切割到的位置（包含）
	 * @param halfUp 遇到截取一半的GBK字符，是否保留。
	 * @return 切割后的字符串
	 * @since 5.7.17
	 */
	public static String subPreGbk(final CharSequence str, int len, final boolean halfUp) {
		if (isEmpty(str)) {
			return str(str);
		}

		int counterOfDoubleByte = 0;
		final byte[] b = ByteUtil.toBytes(str, CharsetUtil.GBK);
		if (b.length <= len) {
			return str.toString();
		}
		for (int i = 0; i < len; i++) {
			if (b[i] < 0) {
				counterOfDoubleByte++;
			}
		}

		if (counterOfDoubleByte % 2 != 0) {
			if (halfUp) {
				len += 1;
			} else {
				len -= 1;
			}
		}
		return new String(b, 0, len, CharsetUtil.GBK);
	}

	/**
	 * 切割指定位置之前部分的字符串
	 * <p>安全的subString,允许：string为null，允许string长度小于toIndexExclude长度</p>
	 * <pre>{@code
	 *      Assert.assertEquals(subPre(null, 3), null);
	 * 		Assert.assertEquals(subPre("ab", 3), "ab");
	 * 		Assert.assertEquals(subPre("abc", 3), "abc");
	 * 		Assert.assertEquals(subPre("abcd", 3), "abc");
	 * 		Assert.assertEquals(subPre("abcd", -3), "a");
	 * 		Assert.assertEquals(subPre("ab", 3), "ab");
	 * }</pre>
	 *
	 * @param string         字符串
	 * @param toIndexExclude 切割到的位置（不包括）
	 * @return 切割后的剩余的前半部分字符串
	 */
	public static String subPre(final CharSequence string, final int toIndexExclude) {
		return sub(string, 0, toIndexExclude);
	}

	/**
	 * 切割指定位置之后部分的字符串
	 *
	 * @param string    字符串
	 * @param fromIndex 切割开始的位置（包括）
	 * @return 切割后后剩余的后半部分字符串
	 */
	public static String subSuf(final CharSequence string, final int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 切割指定长度的后部分的字符串
	 *
	 * <pre>
	 * subSufByLength("abcde", 3)      =    "cde"
	 * subSufByLength("abcde", 0)      =    ""
	 * subSufByLength("abcde", -5)     =    ""
	 * subSufByLength("abcde", -1)     =    ""
	 * subSufByLength("abcde", 5)       =    "abcde"
	 * subSufByLength("abcde", 10)     =    "abcde"
	 * subSufByLength(null, 3)               =    null
	 * </pre>
	 *
	 * @param string 字符串
	 * @param length 切割长度
	 * @return 切割后后剩余的后半部分字符串
	 * @since 4.0.1
	 */
	public static String subSufByLength(final CharSequence string, final int length) {
		if (isEmpty(string)) {
			return null;
		}
		if (length <= 0) {
			return EMPTY;
		}
		return sub(string, -length, string.length());
	}

	/**
	 * 截取字符串,从指定位置开始,截取指定长度的字符串<br>
	 * 当fromIndex为正数时，这个index指的是插空位置，如下：
	 * <pre>
	 *     0   1   2   3   4
	 *       A   B   C   D
	 * </pre>
	 * 当fromIndex为负数时，为反向插空位置，其中-1表示最后一个字符之前的位置：
	 * <pre>
	 *       -3   -2   -1   length
	 *     A    B    C    D
	 * </pre>
	 *
	 * @param input     原始字符串
	 * @param fromIndex 开始的index,包括，可以为负数
	 * @param length    要截取的长度
	 * @return 截取后的字符串
	 * author weibaohui
	 */
	public static String subByLength(final String input, final int fromIndex, final int length) {
		if (isEmpty(input)) {
			return null;
		}
		if (length <= 0) {
			return EMPTY;
		}

		final int toIndex;
		if (fromIndex < 0) {
			toIndex = fromIndex - length;
		} else {
			toIndex = fromIndex + length;
		}
		return sub(input, fromIndex, toIndex);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * subBefore(null, *, false)      = null
	 * subBefore("", *, false)        = ""
	 * subBefore("abc", "a", false)   = ""
	 * subBefore("abcba", "b", false) = "a"
	 * subBefore("abc", "c", false)   = "ab"
	 * subBefore("abc", "d", false)   = "abc"
	 * subBefore("abc", "", false)    = ""
	 * subBefore("abc", null, false)  = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subBefore(final CharSequence string, final CharSequence separator, final boolean isLastSeparator) {
		if (isEmpty(string) || separator == null) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final String sep = separator.toString();
		if (sep.isEmpty()) {
			return EMPTY;
		}
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * subBefore(null, *, false)      = null
	 * subBefore("", *, false)        = ""
	 * subBefore("abc", 'a', false)   = ""
	 * subBefore("abcba", 'b', false) = "a"
	 * subBefore("abc", 'c', false)   = "ab"
	 * subBefore("abc", 'd', false)   = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subBefore(final CharSequence string, final char separator, final boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}

		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * subAfter(null, *, false)      = null
	 * subAfter("", *, false)        = ""
	 * subAfter(*, null, false)      = ""
	 * subAfter("abc", "a", false)   = "bc"
	 * subAfter("abcba", "b", false) = "cba"
	 * subAfter("abc", "c", false)   = ""
	 * subAfter("abc", "d", false)   = ""
	 * subAfter("abc", "", false)    = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subAfter(final CharSequence string, final CharSequence separator, final boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		if (separator == null) {
			return EMPTY;
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * subAfter(null, *, false)      = null
	 * subAfter("", *, false)        = ""
	 * subAfter("abc", 'a', false)   = "bc"
	 * subAfter("abcba", 'b', false) = "cba"
	 * subAfter("abc", 'c', false)   = ""
	 * subAfter("abc", 'd', false)   = ""
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subAfter(final CharSequence string, final char separator, final boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * subBetween("wx[b]yz", "[", "]") = "b"
	 * subBetween(null, *, *)          = null
	 * subBetween(*, null, *)          = null
	 * subBetween(*, *, null)          = null
	 * subBetween("", "", "")          = ""
	 * subBetween("", "", "]")         = null
	 * subBetween("", "[", "]")        = null
	 * subBetween("yabcz", "", "")     = ""
	 * subBetween("yabcz", "y", "z")   = "abc"
	 * subBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 *
	 * @param str    被切割的字符串
	 * @param before 截取开始的字符串标识
	 * @param after  截取到的字符串标识
	 * @return 截取后的字符串
	 * @since 3.1.1
	 */
	public static String subBetween(final CharSequence str, final CharSequence before, final CharSequence after) {
		if (str == null || before == null || after == null) {
			return null;
		}

		final String str2 = str.toString();
		final String before2 = before.toString();
		final String after2 = after.toString();

		final int start = str2.indexOf(before2);
		if (start != INDEX_NOT_FOUND) {
			final int end = str2.indexOf(after2, start + before2.length());
			if (end != INDEX_NOT_FOUND) {
				return str2.substring(start + before2.length(), end);
			}
		}
		return null;
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * subBetween(null, *)            = null
	 * subBetween("", "")             = ""
	 * subBetween("", "tag")          = null
	 * subBetween("tagabctag", null)  = null
	 * subBetween("tagabctag", "")    = ""
	 * subBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 *
	 * @param str            被切割的字符串
	 * @param beforeAndAfter 截取开始和结束的字符串标识
	 * @return 截取后的字符串
	 * @since 3.1.1
	 */
	public static String subBetween(final CharSequence str, final CharSequence beforeAndAfter) {
		return subBetween(str, beforeAndAfter, beforeAndAfter);
	}

	/**
	 * 截取指定字符串多段中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * subBetweenAll("wx[b]y[z]", "[", "]") 		= ["b","z"]
	 * subBetweenAll(null, *, *)          			= []
	 * subBetweenAll(*, null, *)          			= []
	 * subBetweenAll(*, *, null)          			= []
	 * subBetweenAll("", "", "")          			= []
	 * subBetweenAll("", "", "]")         			= []
	 * subBetweenAll("", "[", "]")        			= []
	 * subBetweenAll("yabcz", "", "")     			= []
	 * subBetweenAll("yabcz", "y", "z")   			= ["abc"]
	 * subBetweenAll("yabczyabcz", "y", "z")   		= ["abc","abc"]
	 * subBetweenAll("[yabc[zy]abcz]", "[", "]");   = ["zy"]           重叠时只截取内部，
	 * </pre>
	 *
	 * @param str    被切割的字符串
	 * @param prefix 截取开始的字符串标识
	 * @param suffix 截取到的字符串标识
	 * @return 截取后的字符串
	 * @author dahuoyzs
	 * @since 5.2.5
	 */
	public static String[] subBetweenAll(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		if (hasEmpty(str, prefix, suffix) ||
			// 不包含起始字符串，则肯定没有子串
			!contains(str, prefix)) {
			return new String[0];
		}

		final List<String> result = new LinkedList<>();
		final String[] split = SplitUtil.splitToArray(str, prefix);
		if (prefix.equals(suffix)) {
			// 前后缀字符相同，单独处理
			for (int i = 1, length = split.length - 1; i < length; i += 2) {
				result.add(split[i]);
			}
		} else {
			int suffixIndex;
			String fragment;
			for (int i = 1; i < split.length; i++) {
				fragment = split[i];
				suffixIndex = fragment.indexOf(suffix.toString());
				if (suffixIndex > 0) {
					result.add(fragment.substring(0, suffixIndex));
				}
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * 截取指定字符串多段中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * subBetweenAll(null, *)          			= []
	 * subBetweenAll(*, null)          			= []
	 * subBetweenAll(*, *)          			= []
	 * subBetweenAll("", "")          			= []
	 * subBetweenAll("", "#")         			= []
	 * subBetweenAll("gotanks", "")     		= []
	 * subBetweenAll("#gotanks#", "#")   		= ["gotanks"]
	 * subBetweenAll("#hello# #world#!", "#")   = ["hello", "world"]
	 * subBetweenAll("#hello# world#!", "#");   = ["hello"]
	 * </pre>
	 *
	 * @param str             被切割的字符串
	 * @param prefixAndSuffix 截取开始和结束的字符串标识
	 * @return 截取后的字符串
	 * @author gotanks
	 * @since 5.5.0
	 */
	public static String[] subBetweenAll(final CharSequence str, final CharSequence prefixAndSuffix) {
		return subBetweenAll(str, prefixAndSuffix, prefixAndSuffix);
	}
	// endregion

	// region ----- repeat

	/**
	 * 重复某个字符
	 *
	 * <pre>
	 * repeat('e', 0)  = ""
	 * repeat('e', 3)  = "eee"
	 * repeat('e', -2) = ""
	 * </pre>
	 *
	 * @param c     被重复的字符
	 * @param count 重复的数目，如果小于等于0则返回""
	 * @return 重复字符字符串
	 */
	public static String repeat(final char c, final int count) {
		if (count <= 0) {
			return EMPTY;
		}
		return StrRepeater.of(count).repeat(c);
	}

	/**
	 * 重复某个字符串
	 *
	 * @param str   被重复的字符
	 * @param count 重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(final CharSequence str, final int count) {
		if (null == str) {
			return null;
		}
		return StrRepeater.of(count).repeat(str);
	}

	/**
	 * 重复某个字符串到指定长度
	 * <ul>
	 *     <li>如果指定长度非指定字符串的整数倍，截断到固定长度</li>
	 *     <li>如果指定长度小于字符串本身的长度，截断之</li>
	 * </ul>
	 *
	 * @param str    被重复的字符
	 * @param padLen 指定长度
	 * @return 重复字符字符串
	 * @since 4.3.2
	 */
	public static String repeatByLength(final CharSequence str, final int padLen) {
		if (null == str) {
			return null;
		}
		if (padLen <= 0) {
			return EMPTY;
		}
		return StrRepeater.of(padLen).repeatByLength(str);
	}

	/**
	 * 重复某个字符串并通过分界符连接
	 *
	 * <pre>
	 * repeatAndJoin("?", 5, ",")   = "?,?,?,?,?"
	 * repeatAndJoin("?", 0, ",")   = ""
	 * repeatAndJoin("?", 5, null) = "?????"
	 * </pre>
	 *
	 * @param str       被重复的字符串
	 * @param count     数量
	 * @param delimiter 分界符
	 * @return 连接后的字符串
	 * @since 4.0.1
	 */
	public static String repeatAndJoin(final CharSequence str, final int count, final CharSequence delimiter) {
		if (count <= 0) {
			return EMPTY;
		}
		return StrRepeater.of(count).repeatAndJoin(str, delimiter);
	}
	// endregion

	// region ----- equals

	/**
	 * 比较两个字符串（大小写敏感）。
	 *
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equals(final CharSequence str1, final CharSequence str2) {
		return equals(str1, str2, false);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 *
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
		return equals(str1, str2, true);
	}

	/**
	 * 比较两个字符串是否相等，规则如下
	 * <ul>
	 *     <li>str1和str2都为{@code null}</li>
	 *     <li>忽略大小写使用{@link String#equalsIgnoreCase(String)}判断相等</li>
	 *     <li>不忽略大小写使用{@link String#contentEquals(CharSequence)}判断相等</li>
	 * </ul>
	 *
	 * @param str1       要比较的字符串1
	 * @param str2       要比较的字符串2
	 * @param ignoreCase 是否忽略大小写
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 * @since 3.2.0
	 */
	public static boolean equals(final CharSequence str1, final CharSequence str2, final boolean ignoreCase) {
		if (null == str1) {
			// 只有两个都为null才判断相等
			return str2 == null;
		}
		if (null == str2) {
			// 字符串2空，字符串1非空，直接false
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

	/**
	 * 给定字符串是否与提供的中任意一个字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAnyIgnoreCase(final CharSequence str1, final CharSequence... strs) {
		return equalsAny(str1, true, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAny(final CharSequence str1, final CharSequence... strs) {
		return equalsAny(str1, false, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 *
	 * @param str1       给定需要检查的字符串
	 * @param ignoreCase 是否忽略大小写
	 * @param strs       需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAny(final CharSequence str1, final boolean ignoreCase, final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return false;
		}

		for (final CharSequence str : strs) {
			if (equals(str1, str, ignoreCase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串指定位置的字符是否与给定字符相同<br>
	 * 如果字符串为null，返回false<br>
	 * 如果给定的位置大于字符串长度，返回false<br>
	 * 如果给定的位置小于0，返回false
	 *
	 * @param str      字符串
	 * @param position 位置
	 * @param c        需要对比的字符
	 * @return 字符串指定位置的字符是否与给定字符相同
	 * @since 3.3.1
	 */
	public static boolean equalsCharAt(final CharSequence str, final int position, final char c) {
		if (null == str || position < 0) {
			return false;
		}
		return str.length() > position && c == str.charAt(position);
	}

	/**
	 * 截取第一个字串的部分字符，与第二个字符串比较（长度一致），判断截取的子串是否相同<br>
	 * 任意一个字符串为null返回false
	 *
	 * @param str1       第一个字符串
	 * @param offset1    第一个字符串开始的位置
	 * @param str2       第二个字符串
	 * @param ignoreCase 是否忽略大小写
	 * @return 子串是否相同
	 * @see String#regionMatches(boolean, int, String, int, int)
	 * @since 3.2.1
	 */
	public static boolean isSubEquals(final CharSequence str1, final int offset1,
									  final CharSequence str2, final boolean ignoreCase) {
		return isSubEquals(str1, offset1, str2, 0, str2.length(), ignoreCase);
	}

	/**
	 * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
	 * 任意一个字符串为null返回false
	 *
	 * @param str1       第一个字符串
	 * @param offset1    第一个字符串开始的位置
	 * @param str2       第二个字符串
	 * @param offset2    第二个字符串开始的位置
	 * @param length     截取长度
	 * @param ignoreCase 是否忽略大小写
	 * @return 子串是否相同
	 * @see String#regionMatches(boolean, int, String, int, int)
	 * @since 3.2.1
	 */
	public static boolean isSubEquals(final CharSequence str1, final int offset1,
									  final CharSequence str2, final int offset2, final int length,
									  final boolean ignoreCase) {
		if (null == str1 || null == str2) {
			return false;
		}

		return str1.toString().regionMatches(ignoreCase, offset1, str2.toString(), offset2, length);
	}
	// endregion

	// region ----- format

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
	 * @param params   参数值
	 * @return 格式化后的文本，如果模板为null，返回"null"
	 */
	public static String format(final CharSequence template, final Object... params) {
		if (null == template) {
			return NULL;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		return StrFormatter.format(template.toString(), params);
	}

	/**
	 * 有序的格式化文本，使用{number}做为占位符<br>
	 * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
	 *
	 * @param pattern   文本格式
	 * @param arguments 参数
	 * @return 格式化后的文本
	 */
	public static String indexedFormat(final CharSequence pattern, final Object... arguments) {
		return MessageFormat.format(pattern.toString(), arguments);
	}
	// endregion

	// region ----- wrap

	/**
	 * 包装指定字符串<br>
	 * 当前缀和后缀一致时使用此方法
	 *
	 * @param str             被包装的字符串
	 * @param prefixAndSuffix 前缀和后缀
	 * @return 包装后的字符串
	 * @since 3.1.0
	 */
	public static String wrap(final CharSequence str, final CharSequence prefixAndSuffix) {
		return wrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 包装指定字符串
	 *
	 * @param str    被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrap(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		return emptyIfNull(prefix).concat(emptyIfNull(str)).concat(emptyIfNull(suffix));
	}

	/**
	 * 使用单个字符包装多个字符串
	 *
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs            多个字符串
	 * @return 包装的字符串数组
	 * @since 5.4.1
	 */
	public static String[] wrapAllWithPair(final CharSequence prefixAndSuffix, final CharSequence... strs) {
		return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs   多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAll(final CharSequence prefix, final CharSequence suffix, final CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrap(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
	 *
	 * @param str    被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrapIfMissing(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		int len = 0;
		if (isNotEmpty(str)) {
			len += str.length();
		}
		if (isNotEmpty(prefix)) {
			len += prefix.length();
		}
		if (isNotEmpty(suffix)) {
			len += suffix.length();
		}
		final StringBuilder sb = new StringBuilder(len);
		if (isNotEmpty(prefix) && !startWith(str, prefix)) {
			sb.append(prefix);
		}
		if (isNotEmpty(str)) {
			sb.append(str);
		}
		if (isNotEmpty(suffix) && !endWith(str, suffix)) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * 使用成对的字符包装多个字符串，如果已经包装，则不再包装
	 *
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs            多个字符串
	 * @return 包装的字符串数组
	 * @since 5.4.1
	 */
	public static String[] wrapAllWithPairIfMissing(final CharSequence prefixAndSuffix, final CharSequence... strs) {
		return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串，如果已经包装，则不再包装
	 *
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs   多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAllIfMissing(final CharSequence prefix, final CharSequence suffix, final CharSequence... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrapIfMissing(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串<br>
	 * 此方法要求prefix和suffix都存在，如果只有一个，不做去除。
	 *
	 * @param str    字符串
	 * @param prefix 前置字符串
	 * @param suffix 后置字符串
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(final CharSequence str, final String prefix, final String suffix) {
		if (isWrap(str, prefix, suffix)) {
			return sub(str, prefix.length(), str.length() - suffix.length());
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 *
	 * @param str    字符串
	 * @param prefix 前置字符
	 * @param suffix 后置字符
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(final CharSequence str, final char prefix, final char suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
			return sub(str, 1, str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 *
	 * @param str             字符串
	 * @param prefixAndSuffix 前置和后置字符
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(final CharSequence str, final char prefixAndSuffix) {
		return unWrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 指定字符串是否被包装
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(final CharSequence str, final CharSequence prefix, final CharSequence suffix) {
		if (ArrayUtil.hasNull(str, prefix, suffix)) {
			return false;
		}
		if (str.length() < (prefix.length() + suffix.length())) {
			return false;
		}

		final String str2 = str.toString();
		return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 *
	 * @param str     字符串
	 * @param wrapper 包装字符串
	 * @return 是否被包装
	 */
	public static boolean isWrap(final CharSequence str, final String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 *
	 * @param str     字符串
	 * @param wrapper 包装字符
	 * @return 是否被包装
	 */
	public static boolean isWrap(final CharSequence str, final char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被包装
	 *
	 * @param str        字符串
	 * @param prefixChar 前缀
	 * @param suffixChar 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(final CharSequence str, final char prefixChar, final char suffixChar) {
		if (null == str || str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}
	// endregion

	// region ----- pad

	/**
	 * 补充字符串以满足指定长度，如果提供的字符串大于指定长度，截断之
	 * 同：leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * padPre(null, *, *);//null
	 * padPre("1", 3, "ABC");//"AB1"
	 * padPre("123", 2, "ABC");//"12"
	 * padPre("1039", -1, "0");//"103"
	 * </pre>
	 *
	 * @param str    字符串
	 * @param length 长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(final CharSequence str, final int length, final CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subPre(str, length);
		}

		return repeatByLength(padStr, length - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之
	 * 同：leftPad (org.apache.commons.lang3.leftPad)
	 *
	 * <pre>
	 * padPre(null, *, *);//null
	 * padPre("1", 3, '0');//"001"
	 * padPre("123", 2, '0');//"12"
	 * </pre>
	 *
	 * @param str     字符串
	 * @param length  长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(final CharSequence str, final int length, final char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subPre(str, length);
		}

		return repeat(padChar, length - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度，如果提供的字符串大于指定长度，截断之
	 *
	 * <pre>
	 * padAfter(null, *, *);//null
	 * padAfter("1", 3, '0');//"100"
	 * padAfter("123", 2, '0');//"23"
	 * padAfter("123", -1, '0')//"" 空串
	 * </pre>
	 *
	 * @param str     字符串，如果为{@code null}，直接返回null
	 * @param length  长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padAfter(final CharSequence str, final int length, final char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return sub(str, strLen - length, strLen);
		}

		return str.toString().concat(repeat(padChar, length - strLen));
	}

	/**
	 * 补充字符串以满足最小长度
	 *
	 * <pre>
	 * padAfter(null, *, *);//null
	 * padAfter("1", 3, "ABC");//"1AB"
	 * padAfter("123", 2, "ABC");//"23"
	 * </pre>
	 *
	 * @param str    字符串，如果为{@code null}，直接返回null
	 * @param length 长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String padAfter(final CharSequence str, final int length, final CharSequence padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == length) {
			return str.toString();
		} else if (strLen > length) {
			//如果提供的字符串大于指定长度，截断之
			return subSufByLength(str, length);
		}

		return str.toString().concat(repeatByLength(padStr, length - strLen));
	}
	// region

	// region ----- center

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * center(null, *)   = null
	 * center("", 4)     = "    "
	 * center("ab", -1)  = "ab"
	 * center("ab", 4)   = " ab "
	 * center("abcd", 2) = "abcd"
	 * center("a", 4)    = " a  "
	 * </pre>
	 *
	 * @param str  字符串
	 * @param size 指定长度
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String center(final CharSequence str, final int size) {
		return center(str, size, CharUtil.SPACE);
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * center(null, *, *)     = null
	 * center("", 4, ' ')     = "    "
	 * center("ab", -1, ' ')  = "ab"
	 * center("ab", 4, ' ')   = " ab "
	 * center("abcd", 2, ' ') = "abcd"
	 * center("a", 4, ' ')    = " a  "
	 * center("a", 4, 'y')   = "yayy"
	 * center("abc", 7, ' ')   = "  abc  "
	 * </pre>
	 *
	 * @param str     字符串
	 * @param size    指定长度
	 * @param padChar 两边补充的字符
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String center(CharSequence str, final int size, final char padChar) {
		if (str == null || size <= 0) {
			return str(str);
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padChar);
		str = padAfter(str, size, padChar);
		return str.toString();
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 *
	 * <pre>
	 * center(null, *, *)     = null
	 * center("", 4, " ")     = "    "
	 * center("ab", -1, " ")  = "ab"
	 * center("ab", 4, " ")   = " ab "
	 * center("abcd", 2, " ") = "abcd"
	 * center("a", 4, " ")    = " a  "
	 * center("a", 4, "yz")   = "yayz"
	 * center("abc", 7, null) = "  abc  "
	 * center("abc", 7, "")   = "  abc  "
	 * </pre>
	 *
	 * @param str    字符串
	 * @param size   指定长度
	 * @param padStr 两边补充的字符串
	 * @return 补充后的字符串
	 */
	public static String center(CharSequence str, final int size, CharSequence padStr) {
		if (str == null || size <= 0) {
			return str(str);
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padStr);
		str = padAfter(str, size, padStr);
		return str.toString();
	}
	// endregion

	// ------------------------------------------------------------------------ count

	/**
	 * 统计指定内容中包含指定字符串的数量<br>
	 * 参数为 {@code null} 或者 "" 返回 {@code 0}.
	 *
	 * <pre>
	 * count(null, *)       = 0
	 * count("", *)         = 0
	 * count("abba", null)  = 0
	 * count("abba", "")    = 0
	 * count("abba", "a")   = 2
	 * count("abba", "ab")  = 1
	 * count("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param content      被查找的字符串
	 * @param strForSearch 需要查找的字符串
	 * @return 查找到的个数
	 */
	public static int count(final CharSequence content, final CharSequence strForSearch) {
		if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		final String content2 = content.toString();
		final String strForSearch2 = strForSearch.toString();
		while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
			count++;
			idx += strForSearch.length();
		}
		return count;
	}

	/**
	 * 统计指定内容中包含指定字符的数量
	 *
	 * @param content       内容
	 * @param charForSearch 被统计的字符
	 * @return 包含数量
	 */
	public static int count(final CharSequence content, final char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		final int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	// region ----- compare

	/**
	 * 比较两个字符串，用于排序
	 *
	 * <pre>
	 * compare(null, null, *)     = 0
	 * compare(null , "a", true)  &lt; 0
	 * compare(null , "a", false) &gt; 0
	 * compare("a", null, true)   &gt; 0
	 * compare("a", null, false)  &lt; 0
	 * compare("abc", "abc", *)   = 0
	 * compare("a", "b", *)       &lt; 0
	 * compare("b", "a", *)       &gt; 0
	 * compare("a", "B", *)       &gt; 0
	 * compare("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       字符串1
	 * @param str2       字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compare(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareTo(str2.toString());
	}

	/**
	 * 比较两个字符串，用于排序，大小写不敏感
	 *
	 * <pre>
	 * compareIgnoreCase(null, null, *)     = 0
	 * compareIgnoreCase(null , "a", true)  &lt; 0
	 * compareIgnoreCase(null , "a", false) &gt; 0
	 * compareIgnoreCase("a", null, true)   &gt; 0
	 * compareIgnoreCase("a", null, false)  &lt; 0
	 * compareIgnoreCase("abc", "abc", *)   = 0
	 * compareIgnoreCase("abc", "ABC", *)   = 0
	 * compareIgnoreCase("a", "b", *)       &lt; 0
	 * compareIgnoreCase("b", "a", *)       &gt; 0
	 * compareIgnoreCase("a", "B", *)       &lt; 0
	 * compareIgnoreCase("A", "b", *)       &lt; 0
	 * compareIgnoreCase("ab", "abc", *)    &lt; 0
	 * </pre>
	 *
	 * @param str1       字符串1
	 * @param str2       字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compareIgnoreCase(final CharSequence str1, final CharSequence str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareToIgnoreCase(str2.toString());
	}

	/**
	 * 比较两个版本<br>
	 * null版本排在最小：即：
	 *
	 * <pre>
	 * compareVersion(null, "v1") &lt; 0
	 * compareVersion("v1", "v1")  = 0
	 * compareVersion(null, null)   = 0
	 * compareVersion("v1", null) &gt; 0
	 * compareVersion("1.0.0", "1.0.2") &lt; 0
	 * compareVersion("1.0.2", "1.0.2a") &lt; 0
	 * compareVersion("1.13.0", "1.12.1c") &gt; 0
	 * compareVersion("V0.0.20170102", "V0.0.20170101") &gt; 0
	 * </pre>
	 *
	 * @param version1 版本1
	 * @param version2 版本2
	 * @return 排序值。负数：version1 &lt; version2，正数：version1 &gt; version2, 0：version1 == version2
	 * @since 4.0.2
	 */
	public static int compareVersion(final CharSequence version1, final CharSequence version2) {
		return VersionComparator.INSTANCE.compare(str(version1), str(version2));
	}
	// endregion

	// region ----- append and prepend

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 不忽略大小写
	 *
	 * @param str      被检查的字符串
	 * @param suffix   需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final CharSequence str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 忽略大小写
	 *
	 * @param str      被检查的字符串
	 * @param suffix   需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissingIgnoreCase(final CharSequence str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
	 *
	 * @param str          被检查的字符串
	 * @param suffix       需要添加到结尾的字符串，不参与检查匹配
	 * @param ignoreCase   检查结尾时是否忽略大小写
	 * @param testSuffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final CharSequence str, final CharSequence suffix, final boolean ignoreCase, final CharSequence... testSuffixes) {
		if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
			return str(str);
		}
		if (ArrayUtil.isNotEmpty(testSuffixes)) {
			for (final CharSequence testSuffix : testSuffixes) {
				if (endWith(str, testSuffix, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return str.toString().concat(suffix.toString());
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在前面添加起始字符串<br>
	 * 不忽略大小写
	 *
	 * @param str      被检查的字符串
	 * @param prefix   需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final CharSequence str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
	 * 忽略大小写
	 *
	 * @param str      被检查的字符串
	 * @param prefix   需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissingIgnoreCase(final CharSequence str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串
	 *
	 * @param str        被检查的字符串
	 * @param prefix     需要添加到首部的字符串
	 * @param ignoreCase 检查结尾时是否忽略大小写
	 * @param prefixes   需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final CharSequence str, final CharSequence prefix, final boolean ignoreCase, final CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase)) {
			return str(str);
		}
		if (ArrayUtil.isNotEmpty(prefixes)) {
			for (final CharSequence s : prefixes) {
				if (startWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return prefix.toString().concat(str.toString());
	}
	// endregion

	// region ----- replace

	/**
	 * 替换字符串中第一个指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacedStr 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 */
	public static String replaceFirst(final CharSequence str, final CharSequence searchStr, final CharSequence replacedStr, final boolean ignoreCase) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int startInclude = indexOf(str, searchStr, 0, ignoreCase);
		if (INDEX_NOT_FOUND == startInclude) {
			return str(str);
		}
		return replace(str, startInclude, startInclude + searchStr.length(), replacedStr);
	}

	/**
	 * 替换字符串中最后一个指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacedStr 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 */
	public static String replaceLast(final CharSequence str, final CharSequence searchStr, final CharSequence replacedStr, final boolean ignoreCase) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int lastIndex = lastIndexOf(str, searchStr, str.length(), ignoreCase);
		if (INDEX_NOT_FOUND == lastIndex) {
			return str(str);
		}
		return replace(str, lastIndex, searchStr, replacedStr, ignoreCase);
	}

	/**
	 * 替换字符串中的指定字符串，忽略大小写
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @return 替换后的字符串
	 * @since 4.0.3
	 */
	public static String replaceIgnoreCase(final CharSequence str, final CharSequence searchStr, final CharSequence replacement) {
		return replace(str, 0, searchStr, replacement, true);
	}

	/**
	 * 替换字符串中的指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @return 替换后的字符串
	 * @since 4.0.3
	 */
	public static String replace(final CharSequence str, final CharSequence searchStr, final CharSequence replacement) {
		return replace(str, 0, searchStr, replacement, false);
	}

	/**
	 * 替换字符串中的指定字符串
	 *
	 * @param str         字符串
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 * @since 4.0.3
	 */
	public static String replace(final CharSequence str, final CharSequence searchStr, final CharSequence replacement, final boolean ignoreCase) {
		return replace(str, 0, searchStr, replacement, ignoreCase);
	}

	/**
	 * 替换字符串中的指定字符串<br>
	 * 如果指定字符串出现多次，则全部替换
	 *
	 * @param str         字符串
	 * @param fromIndex   开始位置（包括）
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 * @return 替换后的字符串
	 * @since 4.0.3
	 */
	public static String replace(final CharSequence str, final int fromIndex, final CharSequence searchStr, final CharSequence replacement, final boolean ignoreCase) {
		if (isEmpty(str) || isEmpty(searchStr)) {
			return str(str);
		}
		return new SearchReplacer(fromIndex, searchStr, replacement, ignoreCase).apply(str);
	}

	/**
	 * 替换指定字符串的指定区间内字符为固定字符，替换后字符串长度不变<br>
	 * 如替换的区间长度为10，则替换后的字符重复10次<br>
	 * 此方法使用{@link String#codePoints()}完成拆分替换
	 *
	 * @param str          字符串
	 * @param beginInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @param replacedChar 被替换的字符
	 * @return 替换后的字符串
	 * @since 3.2.1
	 */
	public static String replace(final CharSequence str, final int beginInclude, final int endExclude, final char replacedChar) {
		return new RangeReplacerByChar(beginInclude, endExclude, replacedChar).apply(str);
	}

	/**
	 * 替换指定字符串的指定区间内字符为指定字符串，字符串只重复一次<br>
	 * 此方法使用{@link String#codePoints()}完成拆分替换
	 *
	 * @param str          字符串
	 * @param beginInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @param replacedStr  被替换的字符串
	 * @return 替换后的字符串
	 * @since 3.2.1
	 */
	public static String replace(final CharSequence str, final int beginInclude, final int endExclude, final CharSequence replacedStr) {
		return new RangeReplacerByStr(beginInclude, endExclude, replacedStr).apply(str);
	}

	/**
	 * 替换所有正则匹配的文本，并使用自定义函数决定如何替换<br>
	 * replaceFun可以提取出匹配到的内容的不同部分，然后经过重新处理、组装变成新的内容放回原位。
	 * <pre class="code">
	 *     replace(this.content, "(\\d+)", parameters -&gt; "-" + parameters.group(1) + "-")
	 *     // 结果为："ZZZaaabbbccc中文-1234-"
	 * </pre>
	 *
	 * @param str        要替换的字符串
	 * @param pattern    用于匹配的正则式
	 * @param replaceFun 决定如何替换的函数
	 * @return 替换后的字符串
	 * @see ReUtil#replaceAll(CharSequence, Pattern, SerFunction)
	 * @since 4.2.2
	 */
	public static String replace(final CharSequence str, final Pattern pattern, final SerFunction<Matcher, String> replaceFun) {
		return ReUtil.replaceAll(str, pattern, replaceFun);
	}

	/**
	 * 替换所有正则匹配的文本，并使用自定义函数决定如何替换
	 *
	 * @param str        要替换的字符串
	 * @param regex      用于匹配的正则式
	 * @param replaceFun 决定如何替换的函数
	 * @return 替换后的字符串
	 * @see ReUtil#replaceAll(CharSequence, String, SerFunction)
	 * @since 4.2.2
	 */
	public static String replace(final CharSequence str, final String regex, final SerFunction<Matcher, String> replaceFun) {
		return ReUtil.replaceAll(str, regex, replaceFun);
	}

	/**
	 * 替换指定字符串的指定区间内字符为"*"
	 * 俗称：脱敏功能，后面其他功能，可以见：DesensitizedUtil(脱敏工具类)
	 *
	 * <pre>
	 * hide(null,*,*)=null
	 * hide("",0,*)=""
	 * hide("jackduan@163.com",-1,4)   ****duan@163.com
	 * hide("jackduan@163.com",2,3)    ja*kduan@163.com
	 * hide("jackduan@163.com",3,2)    jackduan@163.com
	 * hide("jackduan@163.com",16,16)  jackduan@163.com
	 * hide("jackduan@163.com",16,17)  jackduan@163.com
	 * </pre>
	 *
	 * @param str          字符串
	 * @param startInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @return 替换后的字符串
	 * @since 4.1.14
	 */
	public static String hide(final CharSequence str, final int startInclude, final int endExclude) {
		return replace(str, startInclude, endExclude, '*');
	}

	/**
	 * 替换字符字符数组中所有的字符为replacedStr<br>
	 * 提供的chars为所有需要被替换的字符，例如："\r\n"，则"\r"和"\n"都会被替换，哪怕他们单独存在
	 *
	 * @param str         被检查的字符串
	 * @param chars       需要替换的字符列表，用一个字符串表示这个字符列表
	 * @param replacedStr 替换成的字符串
	 * @return 新字符串
	 * @since 3.2.2
	 */
	public static String replaceChars(final CharSequence str, final String chars, final CharSequence replacedStr) {
		if (isEmpty(str) || isEmpty(chars)) {
			return str(str);
		}
		return replaceChars(str, chars.toCharArray(), replacedStr);
	}

	/**
	 * 替换字符字符数组中所有的字符为replacedStr
	 *
	 * @param str         被检查的字符串
	 * @param chars       需要替换的字符列表
	 * @param replacedStr 替换成的字符串
	 * @return 新字符串
	 * @since 3.2.2
	 */
	public static String replaceChars(final CharSequence str, final char[] chars, final CharSequence replacedStr) {
		if (isEmpty(str) || ArrayUtil.isEmpty(chars)) {
			return str(str);
		}

		final Set<Character> set = new HashSet<>(chars.length);
		for (final char c : chars) {
			set.add(c);
		}
		final int strLen = str.length();
		final StringBuilder builder = new StringBuilder();
		char c;
		for (int i = 0; i < strLen; i++) {
			c = str.charAt(i);
			builder.append(set.contains(c) ? replacedStr : c);
		}
		return builder.toString();
	}

	/**
	 * 按照给定逻辑替换指定位置的字符，如字符大小写转换等
	 *
	 * @param str         字符串
	 * @param index       位置，-1表示最后一个字符
	 * @param replaceFunc 替换逻辑，给定原字符，返回新字符
	 * @return 替换后的字符串
	 * @since 6.0.0
	 */
	public static String replaceAt(final CharSequence str, int index, final Function<Character, Character> replaceFunc) {
		if (str == null) {
			return null;
		}

		// 支持负数
		final int length = str.length();
		if (index < 0) {
			index += length;
		}

		final String string = str.toString();
		if (index < 0 || index >= length) {
			return string;
		}

		// 检查转换前后是否有编码，无变化则不转换，返回原字符串
		final char c = string.charAt(index);
		final Character newC = replaceFunc.apply(c);
		if (c == newC) {
			// 无变化，返回原字符串
			return string;
		}

		// 此处不复用传入的CharSequence，防止修改原对象
		final StringBuilder builder = new StringBuilder(str);
		builder.setCharAt(index, replaceFunc.apply(c));
		return builder.toString();
	}
	// endregion

	// region ----- length

	/**
	 * 获取字符串的长度，如果为null返回0
	 *
	 * @param cs a 字符串
	 * @return 字符串的长度，如果为null返回0
	 * @since 4.3.2
	 */
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 获取字符串的Unicode字符长度，如果为{@code null}返回0<br>
	 * Unicode字符长度指实际Unicode字符个数，如emoji算一个字符
	 *
	 * @param cs a 字符串
	 * @return 字符串的长度，如果为{@code null}返回0
	 * @since 6.0.0
	 */
	public static int codeLength(final CharSequence cs) {
		return cs == null ? 0 : cs.toString().codePointCount(0, cs.length());
	}

	/**
	 * 给定字符串转为bytes后的byte数（byte长度）
	 *
	 * @param cs      字符串
	 * @param charset 编码
	 * @return byte长度
	 * @since 4.5.2
	 */
	public static int byteLength(final CharSequence cs, final Charset charset) {
		return cs == null ? 0 : cs.toString().getBytes(charset).length;
	}

	/**
	 * 给定字符串数组的总长度<br>
	 * null字符长度定义为0
	 *
	 * @param strs 字符串数组
	 * @return 总长度
	 * @since 4.0.1
	 */
	public static int totalLength(final CharSequence... strs) {
		int totalLength = 0;
		for (final CharSequence str : strs) {
			totalLength += length(str);
		}
		return totalLength;
	}

	/**
	 * 限制字符串长度，如果超过指定长度，截取指定长度并在末尾加"..."
	 *
	 * @param string 字符串
	 * @param length 最大长度
	 * @return 切割后的剩余的前半部分字符串+"..."
	 * @since 4.0.10
	 */
	public static String limitLength(final CharSequence string, final int length) {
		Assert.isTrue(length > 0);
		if (null == string) {
			return null;
		}
		if (string.length() <= length) {
			return string.toString();
		}
		return sub(string, 0, length) + "...";
	}

	/**
	 * 截断字符串，使用UTF8编码为字节后不超过maxBytes长度
	 *
	 * @param str            原始字符串
	 * @param maxBytesLength 最大字节数
	 * @param appendDots     截断后是否追加省略号(...)
	 * @return 限制后的长度
	 */
	public static String limitByteLengthUtf8(final CharSequence str, final int maxBytesLength, final boolean appendDots) {
		return limitByteLength(str, CharsetUtil.UTF_8, maxBytesLength, 4, appendDots);
	}

	/**
	 * 截断字符串，使用其按照指定编码为字节后不超过maxBytes长度<br>
	 * 此方法用于截取总bytes数不超过指定长度，如果字符出没有超出原样输出，如果超出了，则截取掉超出部分，并可选添加...，
	 * 但是添加“...”后总长度也不超过限制长度。
	 *
	 * @param str            原始字符串
	 * @param charset        指定编码
	 * @param maxBytesLength 最大字节数
	 * @param factor         速算因子，取该编码下单个字符的最大可能字节数
	 * @param appendDots     截断后是否追加省略号(...)
	 * @return 限制后的长度
	 */
	public static String limitByteLength(final CharSequence str, final Charset charset, final int maxBytesLength,
										 final int factor, final boolean appendDots) {
		//字符数*速算因子<=最大字节数
		if (str == null || str.length() * factor <= maxBytesLength) {
			return str(str);
		}
		final byte[] sba = ByteUtil.toBytes(str, charset);
		if (sba.length <= maxBytesLength) {
			return str(str);
		}
		//限制字节数
		final int limitBytes;
		if (appendDots) {
			limitBytes = maxBytesLength - "...".getBytes(charset).length;
		} else {
			limitBytes = maxBytesLength;
		}
		final ByteBuffer bb = ByteBuffer.wrap(sba, 0, limitBytes);
		final CharBuffer cb = CharBuffer.allocate(limitBytes);
		final CharsetDecoder decoder = charset.newDecoder();
		//忽略被截断的字符
		decoder.onMalformedInput(CodingErrorAction.IGNORE);
		decoder.decode(bb, cb, true);
		decoder.flush(cb);
		final String result = new String(cb.array(), 0, cb.position());
		if (appendDots) {
			return result + "...";
		}
		return result;
	}
	// endregion

	// region ----- firstXXX

	/**
	 * 返回第一个非{@code null} 元素
	 *
	 * @param strs 多个元素
	 * @param <T>  元素类型
	 * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonNull(final T... strs) {
		return ArrayUtil.firstNonNull(strs);
	}

	/**
	 * 返回第一个非empty 元素
	 *
	 * @param strs 多个元素
	 * @param <T>  元素类型
	 * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
	 * @see #isNotEmpty(CharSequence)
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonEmpty(final T... strs) {
		return ArrayUtil.firstMatch(CharSequenceUtil::isNotEmpty, strs);
	}

	/**
	 * 返回第一个非blank 元素
	 *
	 * @param strs 多个元素
	 * @param <T>  元素类型
	 * @return 第一个非空元素，如果给定的数组为空或者都为空，返回{@code null}
	 * @see #isNotBlank(CharSequence)
	 * @since 5.4.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CharSequence> T firstNonBlank(final T... strs) {
		return ArrayUtil.firstMatch(CharSequenceUtil::isNotBlank, strs);
	}
	// endregion

	// region ----- lower and upper

	/**
	 * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get =》 return getName
	 *
	 * @param str       被处理的字符串
	 * @param preString 添加的首部
	 * @return 处理后的字符串
	 */
	public static String upperFirstAndAddPre(final CharSequence str, final String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}

	/**
	 * 大写对应下标字母
	 *
	 * <pre>例如: str = name,index = 1, return nAme</pre>
	 *
	 * @param str   字符串
	 * @param index 下标，支持负数，-1表示最后一个字符
	 * @return 字符串
	 */
	public static String upperAt(final CharSequence str, int index) {
		if (null == str) {
			return null;
		}

		// 支持负数
		final int length = str.length();
		if (index < 0) {
			index += length;
		}

		final String string = str.toString();
		if (index < 0 || index >= length) {
			return string;
		}

		final char c = str.charAt(index);
		if (!Character.isLowerCase(c)) {
			// 非小写不转换，某些字符非小写也非大写，一并略过
			return string;
		}

		// 此处不复用传入的CharSequence，防止修改原对象
		final StringBuilder builder = new StringBuilder(str);
		builder.setCharAt(index, Character.toUpperCase(c));

		return builder.toString();
	}

	/**
	 * 小写对应下标字母<br>
	 * 例如: str = NAME,index = 1, return NaME
	 *
	 * @param str   字符串
	 * @param index 下标，支持负数，-1表示最后一个字符
	 * @return 字符串
	 */
	public static String lowerAt(final CharSequence str, int index) {
		if (str == null) {
			return null;
		}

		// 支持负数
		final int length = str.length();
		if (index < 0) {
			index += length;
		}

		final String string = str.toString();
		if (index < 0 || index >= length) {
			return string;
		}

		final char c = str.charAt(index);
		if (!Character.isUpperCase(c)) {
			// 非大写不转换，某些字符非小写也非大写，一并略过
			return string;
		}

		// 此处不复用传入的CharSequence，防止修改原对象
		final StringBuilder builder = new StringBuilder(str);
		builder.setCharAt(index, Character.toLowerCase(c));

		return builder.toString();
	}

	/**
	 * 大写首字母<br>
	 * 例如：str = name, return Name
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String upperFirst(final CharSequence str) {
		return upperAt(str, 0);
	}

	/**
	 * 小写首字母<br>
	 * 例如：str = Name, return name
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String lowerFirst(final CharSequence str) {
		return lowerAt(str, 0);
	}
	// endregion

	// region ----- filter

	/**
	 * 过滤字符串
	 *
	 * @param str       字符串
	 * @param predicate 过滤器，{@link Predicate#test(Object)}为{@code true}保留字符
	 * @return 过滤后的字符串
	 * @since 5.4.0
	 */
	public static String filter(final CharSequence str, final Predicate<Character> predicate) {
		if (str == null || predicate == null) {
			return str(str);
		}

		final int len = str.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (predicate.test(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	// endregion

	// region ----- case

	/**
	 * 给定字符串中的字母是否全部为大写，判断依据如下：
	 *
	 * <pre>
	 * 1. 大写字母包括A-Z
	 * 2. 其它非字母的Unicode符都算作大写
	 * </pre>
	 *
	 * @param str 被检查的字符串
	 * @return 是否全部为大写
	 * @since 4.2.2
	 */
	public static boolean isUpperCase(final CharSequence str) {
		return NamingCase.isUpperCase(str);
	}

	/**
	 * 给定字符串中的字母是否全部为小写，判断依据如下：
	 *
	 * <pre>
	 * 1. 小写字母包括a-z
	 * 2. 其它非字母的Unicode符都算作小写
	 * </pre>
	 *
	 * @param str 被检查的字符串
	 * @return 是否全部为小写
	 * @since 4.2.2
	 */
	public static boolean isLowerCase(final CharSequence str) {
		return NamingCase.isLowerCase(str);
	}

	/**
	 * 切换给定字符串中的大小写。大写转小写，小写转大写。
	 *
	 * <pre>
	 * swapCase(null)                 = null
	 * swapCase("")                   = ""
	 * swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 交换后的字符串
	 * @since 4.3.2
	 */
	public static String swapCase(final String str) {
		return NamingCase.swapCase(str);
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 * 例如：
	 *
	 * <pre>
	 * HelloWorld=》hello_world
	 * Hello_World=》hello_world
	 * HelloWorld_test=》hello_world_test
	 * </pre>
	 *
	 * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
	 * @return 转换后下划线方式命名的字符串
	 * @see NamingCase#toUnderlineCase(CharSequence)
	 */
	public static String toUnderlineCase(final CharSequence str) {
		return NamingCase.toUnderlineCase(str);
	}

	/**
	 * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 *
	 * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
	 * @param symbol 连接符
	 * @return 转换后符号连接方式命名的字符串
	 * @see NamingCase#toSymbolCase(CharSequence, char)
	 * @since 4.0.10
	 */
	public static String toSymbolCase(final CharSequence str, final char symbol) {
		return NamingCase.toSymbolCase(str, symbol);
	}

	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 例如：hello_world=》helloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 * @see NamingCase#toCamelCase(CharSequence)
	 */
	public static String toCamelCase(final CharSequence name) {
		return NamingCase.toCamelCase(name);
	}

	/**
	 * 将连接符方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 例如：hello_world=》helloWorld; hello-world=》helloWorld
	 *
	 * @param name   转换前的下划线大写方式命名的字符串
	 * @param symbol 连接符
	 * @return 转换后的驼峰式命名的字符串
	 * @see NamingCase#toCamelCase(CharSequence, char)
	 */
	public static String toCamelCase(final CharSequence name, final char symbol) {
		return NamingCase.toCamelCase(name, symbol);
	}
	// endregion

	/**
	 * 创建StringBuilder对象<br>
	 * 如果对象本身为{@link StringBuilder}，直接返回，否则新建
	 *
	 * @param str {@link CharSequence}
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(final CharSequence str) {
		return str instanceof StringBuilder ? (StringBuilder) str : new StringBuilder(str);
	}

	/**
	 * 创建StringBuilder对象
	 *
	 * @param strs 初始字符串列表
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder(final CharSequence... strs) {
		final StringBuilder sb = new StringBuilder();
		for (final CharSequence str : strs) {
			sb.append(str);
		}
		return sb;
	}

	// ------------------------------------------------------------------------ getter and setter

	/**
	 * 获得set或get或is方法对应的标准属性名<br>
	 * 例如：setName 返回 name
	 *
	 * <pre>
	 * getName =》name
	 * setName =》name
	 * isName  =》name
	 * </pre>
	 *
	 * @param getOrSetMethodName Get或Set方法名
	 * @return 如果是set或get方法名，返回field， 否则null
	 */
	public static String getGeneralField(final CharSequence getOrSetMethodName) {
		final String getOrSetMethodNameStr = getOrSetMethodName.toString();
		if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
			return removePreAndLowerFirst(getOrSetMethodName, 3);
		} else if (getOrSetMethodNameStr.startsWith("is")) {
			return removePreAndLowerFirst(getOrSetMethodName, 2);
		}
		return null;
	}

	/**
	 * 生成set方法名<br>
	 * 例如：name 返回 setName
	 *
	 * @param fieldName 属性名
	 * @return setXxx
	 */
	public static String genSetter(final CharSequence fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}

	/**
	 * 生成get方法名
	 *
	 * @param fieldName 属性名
	 * @return getXxx
	 */
	public static String genGetter(final CharSequence fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}

	// ------------------------------------------------------------------------ other

	/**
	 * 连接多个字符串为一个
	 *
	 * @param isNullToEmpty 是否null转为""
	 * @param strs          字符串数组
	 * @return 连接后的字符串
	 * @since 4.1.0
	 */
	public static String concat(final boolean isNullToEmpty, final CharSequence... strs) {
		final StringBuilder sb = new StringBuilder();
		for (final CharSequence str : strs) {
			sb.append(isNullToEmpty ? emptyIfNull(str) : str);
		}
		return sb.toString();
	}

	/**
	 * 将给定字符串，变成 "xxx...xxx" 形式的字符串
	 *
	 * <ul>
	 *     <li>abcdefgh  9 -》 abcdefgh</li>
	 *     <li>abcdefgh  8 -》 abcdefgh</li>
	 *     <li>abcdefgh  7 -》 ab...gh</li>
	 *     <li>abcdefgh  6 -》 ab...h</li>
	 *     <li>abcdefgh  5 -》 a...h</li>
	 *     <li>abcdefgh  4 -》 a..h</li>
	 *     <li>abcdefgh  3 -》 a.h</li>
	 *     <li>abcdefgh  2 -》 a.</li>
	 *     <li>abcdefgh  1 -》 a</li>
	 *     <li>abcdefgh  0 -》 abcdefgh</li>
	 *     <li>abcdefgh -1 -》 abcdefgh</li>
	 * </ul>
	 *
	 * @param str       字符串
	 * @param maxLength 结果的最大长度
	 * @return 截取后的字符串
	 */
	public static String brief(final CharSequence str, final int maxLength) {
		if (null == str) {
			return null;
		}
		final int strLength = str.length();
		if (maxLength <= 0 || strLength <= maxLength) {
			return str.toString();
		}

		// since 5.7.5，特殊长度
		switch (maxLength) {
			case 1:
				return String.valueOf(str.charAt(0));
			case 2:
				return str.charAt(0) + ".";
			case 3:
				return str.charAt(0) + "." + str.charAt(strLength - 1);
			case 4:
				return str.charAt(0) + ".." + str.charAt(strLength - 1);
		}

		final int suffixLength = (maxLength - 3) / 2;
		final int preLength = suffixLength + (maxLength - 3) % 2; // suffixLength 或 suffixLength + 1
		final String str2 = str.toString();
		return format("{}...{}",
			str2.substring(0, preLength),
			str2.substring(strLength - suffixLength));
	}

	/**
	 * 以 conjunction 为分隔符将多个对象转换为字符串
	 *
	 * @param conjunction 分隔符 {@link StrPool#COMMA}
	 * @param objs        数组
	 * @return 连接后的字符串
	 * @see ArrayUtil#join(Object, CharSequence)
	 */
	public static String join(final CharSequence conjunction, final Object... objs) {
		return ArrayUtil.join(objs, conjunction);
	}

	/**
	 * 以 conjunction 为分隔符将多个对象转换为字符串
	 *
	 * @param <T>         元素类型
	 * @param conjunction 分隔符 {@link StrPool#COMMA}
	 * @param iterable    集合
	 * @return 连接后的字符串
	 * @see CollUtil#join(Iterable, CharSequence)
	 * @since 5.6.6
	 */
	public static <T> String join(final CharSequence conjunction, final Iterable<T> iterable) {
		return CollUtil.join(iterable, conjunction);
	}

	/**
	 * 检查字符串是否都为数字组成
	 *
	 * @param str 字符串
	 * @return 是否都为数字组成
	 * @since 5.7.3
	 */
	public static boolean isNumeric(final CharSequence str) {
		return isAllCharMatch(str, Character::isDigit);
	}

	/**
	 * 循环位移指定位置的字符串为指定距离<br>
	 * 当moveLength大于0向右位移，小于0向左位移，0不位移<br>
	 * 当moveLength大于字符串长度时采取循环位移策略，即位移到头后从头（尾）位移，例如长度为10，位移13则表示位移3
	 *
	 * @param str          字符串
	 * @param startInclude 起始位置（包括）
	 * @param endExclude   结束位置（不包括）
	 * @param moveLength   移动距离，负数表示左移，正数为右移
	 * @return 位移后的字符串
	 * @since 4.0.7
	 */
	public static String move(final CharSequence str, final int startInclude, final int endExclude, int moveLength) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int len = str.length();
		if (Math.abs(moveLength) > len) {
			// 循环位移，当越界时循环
			moveLength = moveLength % len;
		}
		final StringBuilder strBuilder = new StringBuilder(len);
		if (moveLength > 0) {
			final int endAfterMove = Math.min(endExclude + moveLength, str.length());
			strBuilder.append(str.subSequence(0, startInclude))//
				.append(str.subSequence(endExclude, endAfterMove))//
				.append(str.subSequence(startInclude, endExclude))//
				.append(str.subSequence(endAfterMove, str.length()));
		} else if (moveLength < 0) {
			final int startAfterMove = Math.max(startInclude + moveLength, 0);
			strBuilder.append(str.subSequence(0, startAfterMove))//
				.append(str.subSequence(startInclude, endExclude))//
				.append(str.subSequence(startAfterMove, startInclude))//
				.append(str.subSequence(endExclude, str.length()));
		} else {
			return str(str);
		}
		return strBuilder.toString();
	}

	/**
	 * 检查给定字符串的所有字符是否都一样
	 *
	 * @param str 字符出啊
	 * @return 给定字符串的所有字符是否都一样
	 * @since 5.7.3
	 */
	public static boolean isCharEquals(final CharSequence str) {
		Assert.notEmpty(str, "Str to check must be not empty!");
		return count(str, str.charAt(0)) == str.length();
	}

	/**
	 * 对字符串归一化处理，如 "Á" 可以使用 "u00C1"或 "u0041u0301"表示，实际测试中两个字符串并不equals<br>
	 * 因此使用此方法归一为一种表示形式，默认按照W3C通常建议的，在NFC中交换文本。
	 *
	 * @param str 归一化的字符串
	 * @return 归一化后的字符串
	 * @see Normalizer#normalize(CharSequence, Normalizer.Form)
	 * @since 5.7.16
	 */
	public static String normalize(final CharSequence str) {
		return Normalizer.normalize(str, Normalizer.Form.NFC);
	}

	/**
	 * 在给定字符串末尾填充指定字符，以达到给定长度<br>
	 * 如果字符串本身的长度大于等于length，返回原字符串
	 *
	 * @param str       字符串
	 * @param fixedChar 补充的字符
	 * @param length    补充到的长度
	 * @return 补充后的字符串
	 * @since 5.8.0
	 */
	public static String fixLength(final CharSequence str, final char fixedChar, final int length) {
		final int fixedLength = length - str.length();
		if (fixedLength <= 0) {
			return str.toString();
		}
		return str + repeat(fixedChar, fixedLength);
	}

	/**
	 * 获取两个字符串的公共前缀<br>
	 * <pre>{@code
	 *     commonPrefix("abb", "acc") // "a"
	 * }</pre>
	 *
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 字符串1和字符串2的公共前缀
	 */
	public static CharSequence commonPrefix(final CharSequence str1, final CharSequence str2) {
		if (isEmpty(str1) || isEmpty(str2)) {
			return EMPTY;
		}
		final int minLength = Math.min(str1.length(), str2.length());
		int index = 0;
		for (; index < minLength; index++) {
			if (str1.charAt(index) != str2.charAt(index)) {
				break;
			}
		}
		return str1.subSequence(0, index);
	}

	/**
	 * 获取两个字符串的公共后缀<br>
	 * <pre>{@code
	 *     commonSuffix("aba", "cba") // "ba"
	 * }</pre>
	 *
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 字符串1和字符串2的公共后缀
	 */
	public static CharSequence commonSuffix(final CharSequence str1, final CharSequence str2) {
		if (isEmpty(str1) || isEmpty(str2)) {
			return EMPTY;
		}
		int str1Index = str1.length() - 1;
		int str2Index = str2.length() - 1;
		for (; str1Index >= 0 && str2Index >= 0; str1Index--, str2Index--) {

			if (str1.charAt(str1Index) != str2.charAt(str2Index)) {
				break;
			}

		}
		return str1.subSequence(str1Index + 1, str1.length());
	}
}
