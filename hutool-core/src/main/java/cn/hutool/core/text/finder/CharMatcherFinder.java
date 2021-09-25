package cn.hutool.core.text.finder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Matcher;

/**
 * 字符匹配查找器
 *
 * @since 5.7.14
 * @author looly
 */
public class CharMatcherFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final Matcher<Character> matcher;

	/**
	 * 构造
	 * @param matcher 被查找的字符匹配器
	 */
	public CharMatcherFinder(Matcher<Character> matcher) {
		this.matcher = matcher;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int length = text.length();
		for (int i = from; i < length; i++) {
			if(matcher.match(text.charAt(i))){
				return i;
			}
		}
		return -1;
	}

	@Override
	public int end(int start) {
		if(start < 0){
			return -1;
		}
		return start + 1;
	}
}
