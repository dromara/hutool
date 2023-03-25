package cn.hutool.core.text;

import java.io.Serializable;
import java.util.function.BiPredicate;

/**
 * 字符串区域匹配器，用于匹配字串是头部匹配还是尾部匹配
 *
 * @author looly
 * @since 6.0.0
 */
public class StrRegionMatcher implements BiPredicate<CharSequence, CharSequence>, Serializable {
	private static final long serialVersionUID = 1L;

	private final boolean ignoreCase;
	private final boolean ignoreEquals;
	private final boolean isPrefix;

	/**
	 * 构造
	 *
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @param isPrefix     {@code true}表示检查开头匹配，{@code false}检查末尾匹配
	 */
	public StrRegionMatcher(final boolean ignoreCase, final boolean ignoreEquals, final boolean isPrefix) {
		this.ignoreCase = ignoreCase;
		this.ignoreEquals = ignoreEquals;
		this.isPrefix = isPrefix;
	}

	@Override
	public boolean test(final CharSequence str, final CharSequence strToCheck) {
		if (null == str || null == strToCheck) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == strToCheck;
		}

		final int toffset = isPrefix ? 0 : str.length() - strToCheck.length();
		final boolean matches = str.toString()
				.regionMatches(ignoreCase, toffset, strToCheck.toString(), 0, strToCheck.length());

		if (matches) {
			return (false == ignoreEquals) || (false == StrUtil.equals(str, strToCheck, ignoreCase));
		}
		return false;
	}
}
