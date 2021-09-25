package cn.hutool.core.text.finder;

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

	/**
	 * 设置被查找的文本
	 *
	 * @param text 文本
	 * @return this
	 */
	public TextFinder setText(CharSequence text) {
		this.text = text;
		return this;
	}
}
