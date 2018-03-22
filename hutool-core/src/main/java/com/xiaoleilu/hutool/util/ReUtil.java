package com.xiaoleilu.hutool.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Holder;
import com.xiaoleilu.hutool.lang.PatternPool;
import com.xiaoleilu.hutool.lang.Validator;

/**
 * 正则相关工具类<br>
 * 常用正则请见 {@link Validator}
 * 
 * @author xiaoleilu
 */
public class ReUtil {

	/** 正则表达式匹配中文汉字 */
	public final static String RE_CHINESE = "[\u4E00-\u9FFF]";
	/** 正则表达式匹配中文字符串 */
	public final static String RE_CHINESES = RE_CHINESE + "+";

	/** 正则中需要被转义的关键字 */
	public final static Set<Character> RE_KEYS = CollectionUtil.newHashSet(new Character[] { '$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|' });

	/**
	 * 获得匹配的字符串，获得正则中分组0的内容
	 * 
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup0(String regex, String content) {
		return get(regex, content, 0);
	}

	/**
	 * 获得匹配的字符串，获得正则中分组1的内容
	 * 
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup1(String regex, String content) {
		return get(regex, content, 1);
	}

	/**
	 * 获得匹配的字符串
	 * 
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(String regex, String content, int groupIndex) {
		if (null == content || null == regex) {
			return null;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return get(pattern, content, groupIndex);
	}

	/**
	 * 获得匹配的字符串，，获得正则中分组0的内容
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup0(Pattern pattern, String content) {
		return get(pattern, content, 0);
	}

	/**
	 * 获得匹配的字符串，，获得正则中分组1的内容
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串，未匹配返回null
	 * @since 3.1.2
	 */
	public static String getGroup1(Pattern pattern, String content) {
		return get(pattern, content, 1);
	}

	/**
	 * 获得匹配的字符串
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串，未匹配返回null
	 */
	public static String get(Pattern pattern, String content, int groupIndex) {
		if (null == content || null == pattern) {
			return null;
		}

		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}

	/**
	 * 获得匹配的字符串匹配到的所有分组
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @return 匹配后得到的字符串数组，按照分组顺序依次列出，未匹配到返回空列表，任何一个参数为null返回null
	 * @since 3.1.0
	 */
	public static List<String> getAllGroups(Pattern pattern, String content) {
		if (null == content || null == pattern) {
			return null;
		}

		ArrayList<String> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			int groupCount = matcher.groupCount();
			for (int i = 0; i < groupCount; i++) {
				result.add(matcher.group(i));
			}
		}
		return result;
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 * 
	 * @param pattern 匹配正则
	 * @param content 被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 */
	public static String extractMulti(Pattern pattern, String content, String template) {
		if (null == content || null == pattern || null == template) {
			return null;
		}

		HashSet<String> varNums = findAll(PatternPool.GROUP_VAR, template, 1, new HashSet<String>());

		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
				template = template.replace("$" + var, matcher.group(group));
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
	 * @param regex 匹配正则字符串
	 * @param content 被匹配的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMulti(String regex, String content, String template) {
		if (null == content || null == regex || null == template) {
			return null;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return extractMulti(pattern, content, template);
	}

	/**
	 * 从content中匹配出多个值并根据template生成新的字符串<br>
	 * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）<br>
	 * 例如：<br>
	 * content 2013年5月 pattern (.*?)年(.*?)月 template： $1-$2 return 2013-5
	 * 
	 * @param pattern 匹配正则
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 新字符串
	 */
	public static String extractMultiAndDelPre(Pattern pattern, Holder<String> contentHolder, String template) {
		if (null == contentHolder || null == pattern || null == template) {
			return null;
		}

		HashSet<String> varNums = findAll(PatternPool.GROUP_VAR, template, 1, new HashSet<String>());

		final String content = contentHolder.get();
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (String var : varNums) {
				int group = Integer.parseInt(var);
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
	 * @param regex 匹配正则字符串
	 * @param contentHolder 被匹配的内容的Holder，value为内容正文，经过这个方法的原文将被去掉匹配之前的内容
	 * @param template 生成内容模板，变量 $1 表示group1的内容，以此类推
	 * @return 按照template拼接后的字符串
	 */
	public static String extractMultiAndDelPre(String regex, Holder<String> contentHolder, String template) {
		if (null == contentHolder || null == regex || null == template) {
			return null;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return extractMultiAndDelPre(pattern, contentHolder, template);
	}

	/**
	 * 删除匹配的第一个内容
	 * 
	 * @param regex 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delFirst(String regex, String content) {
		if (StrUtil.hasBlank(regex, content)) {
			return content;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
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
	public static String delFirst(Pattern pattern, String content) {
		if (null == pattern || StrUtil.isBlank(content)) {
			return content;
		}

		return pattern.matcher(content).replaceFirst(StrUtil.EMPTY);
	}

	/**
	 * 删除匹配的全部内容
	 * 
	 * @param regex 正则
	 * @param content 被匹配的内容
	 * @return 删除后剩余的内容
	 */
	public static String delAll(String regex, String content) {
		if (StrUtil.hasBlank(regex, content)) {
			return content;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
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
	public static String delAll(Pattern pattern, String content) {
		if (null == pattern || StrUtil.isBlank(content)) {
			return content;
		}

		return pattern.matcher(content).replaceAll(StrUtil.EMPTY);
	}

	/**
	 * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
	 * 
	 * @param regex 定位正则
	 * @param content 被查找的内容
	 * @return 删除前缀后的新内容
	 */
	public static String delPre(String regex, String content) {
		if (null == content || null == regex) {
			return content;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return StrUtil.sub(content, matcher.end(), content.length());
		}
		return content;
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
	 * 
	 * @param regex 正则
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup0(String regex, String content) {
		return findAll(regex, content, 0);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
	 * 
	 * @param regex 正则
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup1(String regex, String content) {
		return findAll(regex, content, 1);
	}

	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param regex 正则
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @return 结果列表
	 * @since 3.0.6
	 */
	public static List<String> findAll(String regex, String content, int group) {
		return findAll(regex, content, group, new ArrayList<String>());
	}

	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param <T> 集合类型
	 * @param regex 正则
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(String regex, String content, int group, T collection) {
		if (null == regex) {
			return null;
		}

		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return findAll(pattern, content, group, collection);
	}

	/**
	 * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 结果列表
	 * @since 3.1.2
	 */
	public static List<String> findAllGroup0(Pattern pattern, String content) {
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
	public static List<String> findAllGroup1(Pattern pattern, String content) {
		return findAll(pattern, content, 1);
	}

	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @return 结果列表
	 * @since 3.0.6
	 */
	public static List<String> findAll(Pattern pattern, String content, int group) {
		return findAll(pattern, content, group, new ArrayList<String>());
	}

	/**
	 * 取得内容中匹配的所有结果
	 * 
	 * @param <T> 集合类型
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @param group 正则的分组
	 * @param collection 返回的集合类型
	 * @return 结果集
	 */
	public static <T extends Collection<String>> T findAll(Pattern pattern, String content, int group, T collection) {
		if (null == pattern || null == content) {
			return null;
		}

		if (null == collection) {
			throw new NullPointerException("Null collection param provided!");
		}

		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			collection.add(matcher.group(group));
		}
		return collection;
	}

	/**
	 * 计算指定字符串中，匹配pattern的个数
	 * 
	 * @param regex 正则表达式
	 * @param content 被查找的内容
	 * @return 匹配个数
	 */
	public static int count(String regex, String content) {
		if (null == regex || null == content) {
			return 0;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
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
	public static int count(Pattern pattern, String content) {
		if (null == pattern || null == content) {
			return 0;
		}

		int count = 0;
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			count++;
		}

		return count;
	}
	
	/**
	 * 指定内容中是否有表达式匹配的内容
	 * 
	 * @param regex 正则表达式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 * @since 3.3.1
	 */
	public static boolean contains(String regex, String content) {
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
	public static boolean contains(Pattern pattern, String content) {
		if (null == pattern || null == content) {
			return false;
		}
		return pattern.matcher(content).find();
	}

	/**
	 * 从字符串中获得第一个整数
	 * 
	 * @param StringWithNumber 带数字的字符串
	 * @return 整数
	 */
	public static Integer getFirstNumber(String StringWithNumber) {
		return Convert.toInt(get(PatternPool.NUMBERS, StringWithNumber, 0), null);
	}

	/**
	 * 给定内容是否匹配正则
	 * 
	 * @param regex 正则
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(String regex, String content) {
		if (content == null) {
			// 提供null的字符串为不匹配
			return false;
		}

		if (StrUtil.isEmpty(regex)) {
			// 正则不存在则为全匹配
			return true;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
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
	public static boolean isMatch(Pattern pattern, String content) {
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
	 * @param content 文本
	 * @param regex 正则
	 * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
	 * @return 处理后的文本
	 */
	public static String replaceAll(String content, String regex, String replacementTemplate) {
		final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return replaceAll(content, pattern, replacementTemplate);
	}

	/**
	 * 正则替换指定值<br>
	 * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
	 * 
	 * @param content 文本
	 * @param pattern {@link Pattern}
	 * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
	 * @return 处理后的文本
	 * @since 3.0.4
	 */
	public static String replaceAll(String content, Pattern pattern, String replacementTemplate) {
		if (StrUtil.isEmpty(content)) {
			return content;
		}

		final Matcher matcher = pattern.matcher(content);
		boolean result = matcher.find();
		if (result) {
			final Set<String> varNums = findAll(PatternPool.GROUP_VAR, replacementTemplate, 1, new HashSet<String>());
			final StringBuffer sb = new StringBuffer();
			do {
				String replacement = replacementTemplate;
				for (String var : varNums) {
					int group = Integer.parseInt(var);
					replacement = replacement.replace("$" + var, matcher.group(group));
				}
				matcher.appendReplacement(sb, escape(replacement));
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		return content;
	}

	/**
	 * 转义字符，将正则的关键字转义
	 * 
	 * @param c 字符
	 * @return 转义后的文本
	 */
	public static String escape(char c) {
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
	public static String escape(String content) {
		if (StrUtil.isBlank(content)) {
			return content;
		}

		final StringBuilder builder = new StringBuilder();
		int len = content.length();
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