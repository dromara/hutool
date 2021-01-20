package cn.hutool.core.lang;

/**
 * 片段默认实现
 *
 * @param <T> 数字类型，用于表示位置index
 * @author looly
 * @since 5.5.3
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	protected T startIndex;
	protected T endIndex;

	/**
	 * 构造
	 * @param startIndex 起始位置
	 * @param endIndex 结束位置
	 */
	public DefaultSegment(T startIndex, T endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public T getStartIndex() {
		return this.startIndex;
	}

	@Override
	public T getEndIndex() {
		return this.endIndex;
	}
}
