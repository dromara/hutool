package cn.hutool.core.text.finder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * 字符查找器
 *
 * @author looly
 * @since 5.7.14
 */
public class StrFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final CharSequence str;
	private final boolean caseInsensitive;

	/**
	 * 构造
	 *
	 * @param str             被查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 */
	public StrFinder(CharSequence str, boolean caseInsensitive) {
		Assert.notEmpty(str);
		this.str = str;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		return StrUtil.indexOf(text, str, from, caseInsensitive);
	}

	@Override
	public int end(int start) {
		if (start < 0) {
			return -1;
		}
		return start + str.length();
	}
}
