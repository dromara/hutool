package cn.hutool.core.text.finder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则查找器<br>
 * 通过传入正则表达式，查找指定字符串中匹配正则的开始和结束位置
 *
 * @author looly
 * @since 5.7.14
 */
public class PatternFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final Pattern pattern;
	private Matcher matcher;

	/**
	 * 构造
	 *
	 * @param regex           被查找的正则表达式
	 * @param caseInsensitive 是否忽略大小写
	 */
	public PatternFinder(String regex, boolean caseInsensitive) {
		this(Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0));
	}

	/**
	 * 构造
	 *
	 * @param pattern 被查找的正则{@link Pattern}
	 */
	public PatternFinder(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public TextFinder setText(CharSequence text) {
		this.matcher = pattern.matcher(text);
		return super.setText(text);
	}

	@Override
	public TextFinder setNegative(boolean negative) {
		throw new UnsupportedOperationException("Negative is invalid for Pattern!");
	}

	@Override
	public int start(int from) {
		if (matcher.find(from)) {
			// 只有匹配到的字符串结尾在limit范围内，才算找到
			if(matcher.end() <= getValidEndIndex()){
				return matcher.start();
			}
		}
		return INDEX_NOT_FOUND;
	}

	@Override
	public int end(int start) {
		final int end = matcher.end();
		final int limit;
		if(endIndex < 0){
			limit = text.length();
		}else{
			limit = Math.min(endIndex, text.length());
		}
		return end <= limit ? end : INDEX_NOT_FOUND;
	}

	@Override
	public PatternFinder reset() {
		this.matcher.reset();
		return this;
	}
}
