package cn.hutool.core.text.finder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则查找器
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
	public int start(int from) {
		if (matcher.find(from)) {
			return matcher.start();
		}
		return -1;
	}

	@Override
	public int end(int start) {
		return matcher.end();
	}

	@Override
	public PatternFinder reset() {
		this.matcher.reset();
		return this;
	}
}
