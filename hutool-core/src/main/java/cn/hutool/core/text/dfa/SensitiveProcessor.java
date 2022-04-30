package cn.hutool.core.text.dfa;

/**
 * @author 肖海斌
 * 敏感词过滤处理器，默认按字符数替换成*
 */
public interface SensitiveProcessor {

	/**
	 * 敏感词过滤处理
	 * @param foundWord 敏感词匹配到的内容
	 * @return 敏感词过滤后的内容，默认按字符数替换成*
	 */
	default String process(final FoundWord foundWord) {
		final int length = foundWord.getFoundWord().length();
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append("*");
		}
		return sb.toString();
	}
}
