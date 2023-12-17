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

package org.dromara.hutool.core.regex;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.comparator.CompareUtil;
import org.dromara.hutool.core.comparator.StrLengthComparator;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.lang.mutable.Mutable;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类<br>
 * 常用正则请见 {@link Validator}
 *
 * @author Looly
 */
public class ReUtil {

	/**
	 * 正则中需要被转义的关键字
	 */
	public final static Set<Character> RE_KEYS = SetUtil.of('$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|');

	/**
	 * 获得匹配的字符串，获得正则中分组0的内容
	 *
	 * @param regex   匹配的正则
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup0(final String regex, final CharSequence content) {
		return get(regex, content, 0);
	}

	/**
	 * 获得匹配的字符串，获得正则中分组1的内容
	 *
	 * @param regex   匹配的正则
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup1(final String regex, final CharSequence content) {
		return get(regex, content, 1);
	}

	/**
	 * 获得匹配的字符串
	 *
	 * @param regex      匹配的正则
	 * @param content    被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(final String regex, final CharSequence content, final int groupIndex) {
		if (null == content || null == regex) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return get(pattern, content, groupIndex);
	}

	/**
	 * 获得匹配的字符串
	 *
	 * @param regex     匹配的正则
	 * @param content   被匹配的内容
	 * @param groupName 匹配正则的分组名称
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(final String regex, final CharSequence content, final String groupName) {
		if (null == content || null == regex) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return get(pattern, content, groupName);
	}

	/**
	 * 获得匹配的字符串，获得正则中分组0的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup0(final Pattern pattern, final CharSequence content) {
		return get(pattern, content, 0);
	}

	/**
	 * 获得匹配的字符串，获得正则中分组1的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup1(final Pattern pattern, final CharSequence content) {
		return get(pattern, content, 1);
	}

	/**
	 * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推
	 *
	 * @param pattern    编译后的正则模式
	 * @param content    被匹配的内容
	 * @param groupIndex 匹配正则的分组序号，0表示整个匹配内容，1表示第一个括号分组内容，依次类推
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(final Pattern pattern, final CharSequence content, final int groupIndex) {
		if (null == content || null == pattern) {
			return null;
		}

		final MutableObj<String> result = new MutableObj<>();
		get(pattern, content, matcher -> result.set(matcher.group(groupIndex)));
		return result.get();
	}

	/**
	 * 获得匹配的字符串
	 *
	 * @param pattern   匹配的正则
	 * @param content   被匹配的内容
	 * @param groupName 匹配正则的分组名称
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 5.7.15
	 */
	public static String get(final Pattern pattern, final CharSequence content, final String groupName) {
		if (null == content || null == pattern || null == groupName) {
			return null;
		}

		final MutableObj<String> result = new MutableObj<>();
		get(pattern, content, matcher -> result.set(matcher.group(groupName)));
		return result.get();
	}

	/**
	 * 在给定字符串中查找给定规则的字符，如果找到则使用{@link Consumer}处理之<br>
	 * 如果内容中有多个匹配项，则只处理找到的第一个结果。
	 *
	 * @param pattern  匹配的正则
	 * @param content  被匹配的内容
	 * @param consumer 匹配到的内容处理器
	 * @since 5.7.15
	 */
	public static void get(final Pattern pattern, final CharSequence content, final Consumer<Matcher> consumer) {
		if (null == content || null == pattern || null == consumer) {
			return;
		}
		final Matcher m = pattern.matcher(content);
		if (m.find()) {
			consumer.accept(m);
		}
	}

	/**
	 * 获得匹配的字符串匹配到的所有分组
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串数组，按照分组顺序依次列出，未匹配到返回空列表，任何一个参数为null返回null
	 * @since 3.1.0
	 */
	public static List<String> getAllGroups(final Pattern pattern, final CharSequence content) {
		return getAllGroups(pattern, content, true);
	}

	/**
	 * 获得匹配的字符串匹配到的所有分组
	 *
	 * @param pattern    编译后的正则模式
	 * @param content    被匹配的内容
	 * @param withGroup0 是否包括分组0，此分组表示全匹配的信息
	 * @return 匹配后得到的字符串数组，按照分组顺序依次列出，未匹配到返回空列表，任何一个参数为null返回null
	 * @since 4.0.13
	 */
	public static List<String> getAllGroups(final Pattern pattern, final CharSequence content, final boolean withGroup0) {
		return getAllGroups(pattern, content, withGroup0, false);
	}

	/**
	 * 获得匹配的字符串匹配到的所有分组
	 *
	 * @param pattern    编译后的正则模式
	 * @param content    被匹配的内容
	 * @param withGroup0 是否包括分组0，此分组表示全匹配的信息
	 * @param findAll    是否查找所有匹配到的内容，{@code false}表示只读取第一个匹配到的内容
	 * @return 匹配后得到的字符串数组，按照分组顺序依次列出，未匹配到返回空列表，任何一个参数为null返回null
	 * @since 4.0.13
	 */
	public static List<String> getAllGroups(final Pattern pattern, final CharSequence content, final boolean withGroup0, final boolean findAll) {
		if (null == content || null == pattern) {
			return null;
		}

		final ArrayList<String> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			final int startGroup = withGroup0 ? 0 : 1;
			final int groupCount = matcher.groupCount();
			for (int i = startGroup; i <= groupCount; i++) {
				result.add(matcher.group(i));
			}

			if (!findAll) {
				break;
			}
		}
		return result;
	}

	/**
	 * 根据给定正则查找字符串中的匹配项，返回所有匹配的分组名对应分组值<br>
	 * <pre>
	 * pattern: (?&lt;year&gt;\\d+)-(?&lt;month&gt;\\d+)-(?&lt;day&gt;\\d+)
	 * content: 2021-10-11
	 * result : year: 2021, month: 10, day: 11
	 * </pre>
	 *
	 * <p>jdk9+之后，因为此方法无效</p>
	 *
	 * @param pattern 匹配的正则
	 * @param content 被匹配的内容
	 * @return 命名捕获组，key为分组名，value为对应值
	 * @since 5.7.15
	 */
	public static Map<String, String> getAllGroupNames(final Pattern pattern, final CharSequence content) {
		if (null == content || null == pattern) {
			return null;
		}
		final Matcher m = pattern.matcher(content);
		final Map<String, String> result = MapUtil.newHashMap(m.groupCount());
		if (m.find()) {
			// 通过反射获取 namedGroups 方法
			final Map<String, Integer> map =
				//MethodHandleUtil.invokeSpecial(pattern, "namedGroups");
				MethodUtil.invoke(pattern, "namedGroups");
			map.forEach((key, value) -> result.put(key, m.group(value)));
		}
		return result;
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 *
	 * @param pattern  匹配正则
	 * @param content  被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 */
	public static String extractMulti(final Pattern pattern, final CharSequence content, String template) {
		if (null == content || null == pattern || null == template) {
			return null;
		}

		//提取模板中的编号
		final TreeSet<Integer> varNums = new TreeSet<>((o1, o2) -> CompareUtil.compare(o2, o1));
		final Matcher matcherForTemplate = PatternPool.GROUP_VAR.matcher(template);
		while (matcherForTemplate.find()) {
			varNums.add(Integer.parseInt(matcherForTemplate.group(1)));
		}

		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (final Integer group : varNums) {
				template = template.replace("$" + group, matcher.group(group));
			}
			return template;
		}
		return null;
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 *
	 * @param regex    匹配正则字符串
	 * @param content  被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMulti(final String regex, final CharSequence content, final String template) {
		if (null == content || null == regex || null == template) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return extractMulti(pattern, content, template);
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 *
	 * @param pattern       匹配正则
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template      生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 * @since 5.8.0
	 */
	public static String extractMultiAndDelPre(final Pattern pattern, final Mutable<CharSequence> contentHolder, String template) {
		if (null == contentHolder || null == pattern || null == template) {
			return null;
		}

		final HashSet<String> varNums = findAll(PatternPool.GROUP_VAR, template, 1, new HashSet<>());

		final CharSequence content = contentHolder.get();
		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (final String var : varNums) {
				final int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
			}
			contentHolder.set(StrUtil.sub(content, matcher.end(), content.length()));
			return template;
		}
		return null;
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 *
	 * @param regex         匹配正则字符串
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template      生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMultiAndDelPre(final String regex, final Mutable<CharSequence> contentHolder, final String template) {
		if (null == contentHolder || null == regex || null == template) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return extractMultiAndDelPre(pattern, contentHolder, template);
	}

	/**
	 * 删除匹配的第一个内容
	 *
	 * @param regex   正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(final String regex, final CharSequence content) {
		if (ArrayUtil.hasBlank(regex, content)) {
			return StrUtil.str(content);
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return delFirst(pattern, content);
	}

	/**
	 * 删除匹配的第一个内容
	 *
	 * @param pattern 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(final Pattern pattern, final CharSequence content) {
		return replaceFirst(pattern, content, StrUtil.EMPTY);
	}

	/**
	 * 替换匹配的第一个内容
	 *
	 * @param pattern     正则
	 * @param content     被匹配的内容
	 * @param replacement 替换的内容
	 * @return 替换后剩余的内容
	 * @since 5.6.5
	 */
	public static String replaceFirst(final Pattern pattern, final CharSequence content, final String replacement) {
		if (null == pattern || StrUtil.isEmpty(content)) {
			return StrUtil.str(content);
		}

		return pattern.matcher(content).replaceFirst(replacement);
	}

	/**
	 * 删除匹配的最后一个内容
	 *
	 * @param regex 正则
	 * @param str   被匹配的内容
	 * @return 删除后剩余的内容
	 * @since 5.6.5
	 */
	public static String delLast(final String regex, final CharSequence str) {
		if (ArrayUtil.hasBlank(regex, str)) {
			return StrUtil.str(str);
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return delLast(pattern, str);
	}

	/**
	 * 删除匹配的最后一个内容
	 *
	 * @param pattern 正则
	 * @param str     被匹配的内容
	 * @return 删除后剩余的内容
	 * @since 5.6.5
	 */
	public static String delLast(final Pattern pattern, final CharSequence str) {
		if (null != pattern && StrUtil.isNotEmpty(str)) {
			final MatchResult matchResult = lastIndexOf(pattern, str);
			if (null != matchResult) {
				return StrUtil.subPre(str, matchResult.start()) + StrUtil.subSuf(str, matchResult.end());
			}
		}

		return StrUtil.str(str);
	}

	/**
	 * 删除匹配的全部内容
	 *
	 * @param regex   正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delAll(final String regex, final CharSequence content) {
		if (StrUtil.hasEmpty(regex, content)) {
			return StrUtil.str(content);
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return delAll(pattern, content);
	}

	/**
	 * 删除匹配的全部内容
	 *
	 * @param pattern 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delAll(final Pattern pattern, final CharSequence content) {
		if (null == pattern || StrUtil.isEmpty(content)) {
			return StrUtil.str(content);
		}

		return pattern.matcher(content).replaceAll(StrUtil.EMPTY);
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 *
	 * @param regex   定位正则
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(final String regex, final CharSequence content) {
		if (null == content || null == regex) {
			return StrUtil.str(content);
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return delPre(pattern, content);
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 *
	 * @param pattern 定位正则模式
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(final Pattern pattern, final CharSequence content) {
		if (null == content || null == pattern) {
			return StrUtil.str(content);
		}

		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return StrUtil.sub(content, matcher.end(), content.length());
		}
		return StrUtil.str(content);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
	 *
	 * @param regex   正则
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup0(final String regex, final CharSequence content) {
		return findAll(regex, content, 0);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
	 *
	 * @param regex   正则
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup1(final String regex, final CharSequence content) {
		return findAll(regex, content, 1);
	}

	/**
	 * 取得内容中匹配的所有结果
	 *
	 * @param regex   正则
	 * @param content 被查找的内容
	 * @param group   正则的分组
	 * @return 结果列表
	 * @since 3.0.6
	 */
	public static List<String> findAll(final String regex, final CharSequence content, final int group) {
		return findAll(regex, content, group, new ArrayList<>());
	}

	/**
	 * 取得内容中匹配的所有结果
	 *
	 * @param <T>        集合类型
	 * @param regex      正则
	 * @param content    被查找的内容
	 * @param group      正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(final String regex, final CharSequence content, final int group, final T collection) {
		if (null == regex) {
			return collection;
		}

		return findAll(PatternPool.get(regex, Pattern.DOTALL), content, group, collection);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup0(final Pattern pattern, final CharSequence content) {
		return findAll(pattern, content, 0);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup1(final Pattern pattern, final CharSequence content) {
		return findAll(pattern, content, 1);
	}

	/**
	 * 取得内容中匹配的所有结果
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @param group   正则的分组
	 * @return 结果列表
	 * @since 3.0.6
	 */
	public static List<String> findAll(final Pattern pattern, final CharSequence content, final int group) {
		return findAll(pattern, content, group, new ArrayList<>());
	}

	/**
	 * 取得内容中匹配的所有结果
	 *
	 * @param <T>        集合类型
	 * @param pattern    编译后的正则模式
	 * @param content    被查找的内容
	 * @param group      正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(final Pattern pattern, final CharSequence content, final int group, final T collection) {
		if (null == pattern || null == content) {
			return null;
		}
		Assert.notNull(collection, "Collection must be not null !");

		findAll(pattern, content, (matcher) -> collection.add(matcher.group(group)));
		return collection;
	}

	/**
	 * 取得内容中匹配的所有结果，使用{@link Consumer}完成匹配结果处理
	 *
	 * @param pattern  编译后的正则模式
	 * @param content  被查找的内容
	 * @param consumer 匹配结果处理函数
	 * @since 5.7.15
	 */
	public static void findAll(final Pattern pattern, final CharSequence content, final Consumer<Matcher> consumer) {
		if (null == pattern || null == content) {
			return;
		}

		final Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			consumer.accept(matcher);
		}
	}

	/**
	 * 计算指定字符串中，匹配pattern的个数
	 *
	 * @param regex   正则表达式
	 * @param content 被查找的内容
	 * @return 匹配个数
	 */
	public static int count(final String regex, final CharSequence content) {
		if (null == regex || null == content) {
			return 0;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return count(pattern, content);
	}

	/**
	 * 计算指定字符串中，匹配pattern的个数
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 匹配个数
	 */
	public static int count(final Pattern pattern, final CharSequence content) {
		if (null == pattern || null == content) {
			return 0;
		}

		int count = 0;
		final Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			count++;
		}

		return count;
	}

	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param regex   正则表达式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 * @since 3.3.1
	 */
	public static boolean contains(final String regex, final CharSequence content) {
		if (null == regex || null == content) {
			return false;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return contains(pattern, content);
	}

	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 * @since 3.3.1
	 */
	public static boolean contains(final Pattern pattern, final CharSequence content) {
		if (null == pattern || null == content) {
			return false;
		}
		return pattern.matcher(content).find();
	}

	/**
	 * 找到指定正则匹配到字符串的开始位置
	 *
	 * @param regex   正则
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 * @since 5.6.5
	 */
	public static MatchResult indexOf(final String regex, final CharSequence content) {
		if (null == regex || null == content) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return indexOf(pattern, content);
	}

	/**
	 * 找到指定模式匹配到字符串的开始位置
	 *
	 * @param pattern 模式
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 * @since 5.6.5
	 */
	public static MatchResult indexOf(final Pattern pattern, final CharSequence content) {
		if (null != pattern && null != content) {
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				return matcher.toMatchResult();
			}
		}

		return null;
	}

	/**
	 * 找到指定正则匹配到第一个字符串的位置
	 *
	 * @param regex   正则
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 * @since 5.6.5
	 */
	public static MatchResult lastIndexOf(final String regex, final CharSequence content) {
		if (null == regex || null == content) {
			return null;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return lastIndexOf(pattern, content);
	}

	/**
	 * 找到指定模式匹配到最后一个字符串的位置
	 *
	 * @param pattern 模式
	 * @param content 字符串
	 * @return 位置，{@code null}表示未找到
	 * @since 5.6.5
	 */
	public static MatchResult lastIndexOf(final Pattern pattern, final CharSequence content) {
		MatchResult result = null;
		if (null != pattern && null != content) {
			final Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				result = matcher.toMatchResult();
			}
		}

		return result;
	}

	/**
	 * 从字符串中获得第一个整数
	 *
	 * @param stringWithNumber 带数字的字符串
	 * @return 整数
	 */
	public static Integer getFirstNumber(final CharSequence stringWithNumber) {
		return Convert.toInt(get(PatternPool.NUMBERS, stringWithNumber, 0), null);
	}

	/**
	 * 给定内容是否匹配正则
	 *
	 * @param regex   正则
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(final String regex, final CharSequence content) {
		if (content == null) {
			// 提供null的字符串为不匹配
			return false;
		}

		if (StrUtil.isEmpty(regex)) {
			// 正则不存在则为全匹配
			return true;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return isMatch(pattern, content);
	}

	/**
	 * 给定内容是否匹配正则
	 *
	 * @param pattern 模式
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(final Pattern pattern, final CharSequence content) {
		if (content == null || pattern == null) {
			// 提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}

	/**
	 * 正则替换指定值<br>
	 * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
	 *
	 * <p>
	 * 例如：原字符串是：中文1234，我想把1234换成(1234)，则可以：
	 *
	 * <pre>
	 * ReUtil.replaceAll("中文1234", "(\\d+)", "($1)"))
	 *
	 * 结果：中文(1234)
	 * </pre>
	 *
	 * @param content             文本
	 * @param regex               正则
	 * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
	 * @return 处理后的文本
	 */
	public static String replaceAll(final CharSequence content, final String regex, final String replacementTemplate) {
		final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return replaceAll(content, pattern, replacementTemplate);
	}

	/**
	 * 正则替换指定值<br>
	 * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
	 *
	 * @param content             文本
	 * @param pattern             {@link Pattern}
	 * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
	 * @return 处理后的文本
	 * @since 3.0.4
	 */
	public static String replaceAll(final CharSequence content, final Pattern pattern, final String replacementTemplate) {
		if (StrUtil.isEmpty(content)) {
			return StrUtil.str(content);
		}

		// replacementTemplate字段不能为null，否则无法抉择如何处理结果
		Assert.notNull(replacementTemplate, "ReplacementTemplate must be not null !");

		final Matcher matcher = pattern.matcher(content);
		boolean result = matcher.find();
		if (result) {
			final Set<String> varNums = findAll(PatternPool.GROUP_VAR, replacementTemplate, 1,
					new TreeSet<>(StrLengthComparator.INSTANCE.reversed()));
			final StringBuffer sb = new StringBuffer();
			do {
				String replacement = replacementTemplate;
				for (final String var : varNums) {
					final int group = Integer.parseInt(var);
					replacement = replacement.replace("$" + var, matcher.group(group));
				}
				matcher.appendReplacement(sb, escape(replacement));
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		return StrUtil.str(content);
	}

	/**
	 * 替换所有正则匹配的文本，并使用自定义函数决定如何替换<br>
	 * replaceFun可以通过{@link Matcher}提取出匹配到的内容的不同部分，然后经过重新处理、组装变成新的内容放回原位。
	 * <pre class="code">
	 *     replaceAll(this.content, "(\\d+)", parameters -&gt; "-" + parameters.group(1) + "-")
	 *     // 结果为："ZZZaaabbbccc中文-1234-"
	 * </pre>
	 *
	 * @param str        要替换的字符串
	 * @param regex      用于匹配的正则式
	 * @param replaceFun 决定如何替换的函数
	 * @return 替换后的文本
	 * @since 4.2.2
	 */
	public static String replaceAll(final CharSequence str, final String regex, final SerFunction<Matcher, String> replaceFun) {
		return replaceAll(str, Pattern.compile(regex), replaceFun);
	}

	/**
	 * 替换所有正则匹配的文本，并使用自定义函数决定如何替换<br>
	 * replaceFun可以通过{@link Matcher}提取出匹配到的内容的不同部分，然后经过重新处理、组装变成新的内容放回原位。
	 * <pre class="code">
	 *     replaceAll(this.content, "(\\d+)", parameters -&gt; "-" + parameters.group(1) + "-")
	 *     // 结果为："ZZZaaabbbccc中文-1234-"
	 * </pre>
	 *
	 * @param str        要替换的字符串
	 * @param pattern    用于匹配的正则式
	 * @param replaceFun 决定如何替换的函数,可能被多次调用（当有多个匹配时）
	 * @return 替换后的字符串
	 * @since 4.2.2
	 */
	public static String replaceAll(final CharSequence str, final Pattern pattern, final SerFunction<Matcher, String> replaceFun) {
		if (StrUtil.isEmpty(str)) {
			return StrUtil.str(str);
		}

		final Matcher matcher = pattern.matcher(str);
		final StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buffer, replaceFun.apply(matcher));
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	/**
	 * 转义字符，将正则的关键字转义
	 *
	 * @param c 字符
	 * @return 转义后的文本
	 */
	public static String escape(final char c) {
		final StringBuilder builder = new StringBuilder();
		if (RE_KEYS.contains(c)) {
			builder.append('\\');
		}
		builder.append(c);
		return builder.toString();
	}

	/**
	 * 转义字符串，将正则的关键字转义
	 *
	 * @param content 文本
	 * @return 转义后的文本
	 */
	public static String escape(final CharSequence content) {
		if (StrUtil.isBlank(content)) {
			return StrUtil.str(content);
		}

		final StringBuilder builder = new StringBuilder();
		final int len = content.length();
		char current;
		for (int i = 0; i < len; i++) {
			current = content.charAt(i);
			if (RE_KEYS.contains(current)) {
				builder.append('\\');
			}
			builder.append(current);
		}
		return builder.toString();
	}
}
