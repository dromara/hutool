package cn.hutool.core.text.finder;

import cn.hutool.core.lang.Assert;

import java.io.Serializable;

/**
 * 文本查找抽象类
 *
 * @author looly
 * @since 5.7.14
 */
public abstract class TextFinder implements Finder, Serializable {
	private static final long serialVersionUID = 1L;

	protected CharSequence text;
	protected int endIndex = -1;

	/**
	 * 设置被查找的文本
	 *
	 * @param text 文本
	 * @return this
	 */
	public TextFinder setText(CharSequence text) {
		this.text = Assert.notNull(text, "Text must be not null!");
		return this;
	}

	/**
	 * 设置查找的结束位置<br>
	 * 如果从前向后查找，结束位置最大为text.length()<br>
	 * 如果从后向前，结束位置为-1
	 *
	 * @param endIndex 结束位置（不包括）
	 * @return this
	 */
	public TextFinder setEndIndex(int endIndex) {
		this.endIndex = endIndex;
		return this;
	}

	/**
	 * 获取有效结束位置<br>
	 * 如果{@link #endIndex}小于0，在反向模式下是开头（-1），正向模式是结尾（text.length()）
	 *
	 * @param negative 是否从后向前查找模式
	 * @return 有效结束位置
	 */
	protected int getValidEndIndex(boolean negative) {
		if(negative && -1 == endIndex){
			// 反向查找模式下，-1表示0前面的位置，即字符串反向末尾的位置
			return -1;
		}
		final int limit;
		if (endIndex < 0) {
			limit = endIndex + text.length();
		} else {
			limit = Math.min(endIndex, text.length());
		}
		return limit;
	}
}
