package cn.hutool.core.lang;

import cn.hutool.core.text.StrUtil;

/**
 * 片段默认实现
 *
 * @param <T> 数字类型，用于表示位置index
 * @author looly
 * @since 5.5.3
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	protected T beginIndex;
	protected T endIndex;

	/**
	 * 构造
	 *
	 * @param beginIndex 起始位置
	 * @param endIndex   结束位置
	 */
	public DefaultSegment(final T beginIndex, final T endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	@Override
	public T getBeginIndex() {
		return this.beginIndex;
	}

	@Override
	public T getEndIndex() {
		return this.endIndex;
	}

	@Override
	public String toString() {
		return StrUtil.format("[{}, {}]", beginIndex, endIndex);
	}
}
