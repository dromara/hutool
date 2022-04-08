package cn.hutool.core.text.finder;

import cn.hutool.core.lang.Assert;

/**
 * 固定长度查找器<br>
 * 给定一个长度，查找的位置为from + length，一般用于分段截取
 *
 * @since 5.7.14
 * @author looly
 */
public class LengthFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final int length;

	/**
	 * 构造
	 * @param length 长度
	 */
	public LengthFinder(int length) {
		this.length = length;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int limit = getValidEndIndex();
		int result;
		if(negative){
			result = from - length;
			if(result > limit){
				return result;
			}
		} else {
			result = from + length;
			if(result < limit){
				return result;
			}
		}
		return -1;
	}

	@Override
	public int end(int start) {
		return start;
	}
}
