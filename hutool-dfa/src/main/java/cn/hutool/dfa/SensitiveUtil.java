package cn.hutool.dfa;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词工具类
 *
 * @author Looly
 */
public final class SensitiveUtil {

	public static final char DEFAULT_SEPARATOR = StrUtil.C_COMMA;
	private static final WordTree sensitiveTree = new WordTree();

	/**
	 * @return 是否已经被初始化
	 */
	public static boolean isInited() {
		return false == sensitiveTree.isEmpty();
	}

	/**
	 * 初始化敏感词树
	 *
	 * @param isAsync        是否异步初始化
	 * @param sensitiveWords 敏感词列表
	 */
	public static void init(final Collection<String> sensitiveWords, boolean isAsync) {
		if (isAsync) {
			ThreadUtil.execAsync(() -> {
				init(sensitiveWords);
				return true;
			});
		} else {
			init(sensitiveWords);
		}
	}

	/**
	 * 初始化敏感词树
	 *
	 * @param sensitiveWords 敏感词列表
	 */
	public static void init(Collection<String> sensitiveWords) {
		sensitiveTree.clear();
		sensitiveTree.addWords(sensitiveWords);
//		log.debug("Sensitive init finished, sensitives: {}", sensitiveWords);
	}

	/**
	 * 初始化敏感词树
	 *
	 * @param sensitiveWords 敏感词列表组成的字符串
	 * @param isAsync        是否异步初始化
	 * @param separator      分隔符
	 */
	public static void init(String sensitiveWords, char separator, boolean isAsync) {
		if (StrUtil.isNotBlank(sensitiveWords)) {
			init(StrUtil.split(sensitiveWords, separator), isAsync);
		}
	}

	/**
	 * 初始化敏感词树，使用逗号分隔每个单词
	 *
	 * @param sensitiveWords 敏感词列表组成的字符串
	 * @param isAsync        是否异步初始化
	 */
	public static void init(String sensitiveWords, boolean isAsync) {
		init(sensitiveWords, DEFAULT_SEPARATOR, isAsync);
	}

	/**
	 * 设置字符过滤规则，通过定义字符串过滤规则，过滤不需要的字符<br>
	 * 当accept为false时，此字符不参与匹配
	 *
	 * @param charFilter 过滤函数
	 * @since 5.4.4
	 */
	public static void setCharFilter(Filter<Character> charFilter) {
		if (charFilter != null) {
			sensitiveTree.setCharFilter(charFilter);
		}
	}

	/**
	 * 是否包含敏感词
	 *
	 * @param text 文本
	 * @return 是否包含
	 */
	public static boolean containsSensitive(String text) {
		return sensitiveTree.isMatch(text);
	}

	/**
	 * 是否包含敏感词
	 *
	 * @param obj bean，会被转为JSON字符串
	 * @return 是否包含
	 */
	public static boolean containsSensitive(Object obj) {
		return sensitiveTree.isMatch(JSONUtil.toJsonStr(obj));
	}

	/**
	 * 查找敏感词，返回找到的第一个敏感词
	 *
	 * @param text 文本
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundFirstSensitive(String)}
	 */
	@Deprecated
	public static String getFindedFirstSensitive(String text) {
		return sensitiveTree.match(text);
	}

	/**
	 * 查找敏感词，返回找到的第一个敏感词
	 *
	 * @param text 文本
	 * @return 敏感词
	 * @since 5.5.3
	 */
	public static FoundWord getFoundFirstSensitive(String text) {
		return sensitiveTree.matchWord(text);
	}

	/**
	 * 查找敏感词，返回找到的第一个敏感词
	 *
	 * @param obj bean，会被转为JSON字符串
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundFirstSensitive(Object)}
	 */
	@Deprecated
	public static String getFindedFirstSensitive(Object obj) {
		return sensitiveTree.match(JSONUtil.toJsonStr(obj));
	}

	/**
	 * 查找敏感词，返回找到的第一个敏感词
	 *
	 * @param obj bean，会被转为JSON字符串
	 * @return 敏感词
	 */
	public static FoundWord getFoundFirstSensitive(Object obj) {
		return sensitiveTree.matchWord(JSONUtil.toJsonStr(obj));
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词
	 *
	 * @param text 文本
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundAllSensitive(String)}
	 */
	@Deprecated
	public static List<String> getFindedAllSensitive(String text) {
		return sensitiveTree.matchAll(text);
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词
	 *
	 * @param text 文本
	 * @return 敏感词
	 * @since 5.5.3
	 */
	public static List<FoundWord> getFoundAllSensitive(String text) {
		return sensitiveTree.matchAllWords(text);
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词<br>
	 * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 *
	 * @param text           文本
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundAllSensitive(String, boolean, boolean)}
	 */
	@Deprecated
	public static List<String> getFindedAllSensitive(String text, boolean isDensityMatch, boolean isGreedMatch) {
		return sensitiveTree.matchAll(text, -1, isDensityMatch, isGreedMatch);
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词<br>
	 * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 *
	 * @param text           文本
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 敏感词
	 */
	public static List<FoundWord> getFoundAllSensitive(String text, boolean isDensityMatch, boolean isGreedMatch) {
		return sensitiveTree.matchAllWords(text, -1, isDensityMatch, isGreedMatch);
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词
	 *
	 * @param bean 对象，会被转为JSON
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundAllSensitive(Object)}
	 */
	@Deprecated
	public static List<String> getFindedAllSensitive(Object bean) {
		return sensitiveTree.matchAll(JSONUtil.toJsonStr(bean));
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词
	 *
	 * @param bean 对象，会被转为JSON
	 * @return 敏感词
	 * @since 5.5.3
	 */
	public static List<FoundWord> getFoundAllSensitive(Object bean) {
		return sensitiveTree.matchAllWords(JSONUtil.toJsonStr(bean));
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词<br>
	 * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 *
	 * @param bean           对象，会被转为JSON
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 敏感词
	 * @deprecated 请使用 {@link #getFoundAllSensitive(Object, boolean, boolean)}
	 */
	@Deprecated
	public static List<String> getFindedAllSensitive(Object bean, boolean isDensityMatch, boolean isGreedMatch) {
		return sensitiveTree.matchAll(JSONUtil.toJsonStr(bean), -1, isDensityMatch, isGreedMatch);
	}

	/**
	 * 查找敏感词，返回找到的所有敏感词<br>
	 * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 *
	 * @param bean           对象，会被转为JSON
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 敏感词
	 * @since 5.5.3
	 */
	public static List<FoundWord> getFoundAllSensitive(Object bean, boolean isDensityMatch, boolean isGreedMatch) {
		return getFoundAllSensitive(JSONUtil.toJsonStr(bean), isDensityMatch, isGreedMatch);
	}

	/**
	 * 敏感词过滤
	 *
	 * @param bean               对象，会被转为JSON
	 * @param isGreedMatch       贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 * @param sensitiveProcessor 敏感词处理器，默认按匹配内容的字符数替换成*
	 * @param <T>                bean的class类型
	 * @return 敏感词过滤处理后的bean对象
	 */
	public static <T> T sensitiveFilter(T bean, boolean isGreedMatch, SensitiveProcessor sensitiveProcessor) {
		String jsonText = JSONUtil.toJsonStr(bean);
		@SuppressWarnings("unchecked")
		final Class<T> c = (Class<T>) bean.getClass();
		return JSONUtil.toBean(sensitiveFilter(jsonText, isGreedMatch, sensitiveProcessor), c);
	}

	/**
	 * 处理过滤文本中的敏感词，默认替换成*
	 *
	 * @param text               文本
	 * @param isGreedMatch       贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 * @param sensitiveProcessor 敏感词处理器，默认按匹配内容的字符数替换成*
	 * @return 敏感词过滤处理后的文本
	 */
	public static String sensitiveFilter(String text, boolean isGreedMatch, SensitiveProcessor sensitiveProcessor) {
		if (StrUtil.isEmpty(text)) {
			return text;
		}

		//敏感词过滤场景下，不需要密集匹配
		List<FoundWord> foundWordList = getFoundAllSensitive(text, false, isGreedMatch);
		if (CollUtil.isEmpty(foundWordList)) {
			return text;
		}
		sensitiveProcessor = sensitiveProcessor == null ? new SensitiveProcessor() {
		} : sensitiveProcessor;
		Map<Integer, FoundWord> foundWordMap = new HashMap<>(foundWordList.size());
		foundWordList.forEach(foundWord -> foundWordMap.put(foundWord.getStartIndex(), foundWord));
		int length = text.length();
		StringBuilder textStringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			FoundWord fw = foundWordMap.get(i);
			if (fw != null) {
				textStringBuilder.append(sensitiveProcessor.process(fw));
				i = fw.getEndIndex();
			} else {
				textStringBuilder.append(text.charAt(i));
			}
		}
		return textStringBuilder.toString();
	}
}
