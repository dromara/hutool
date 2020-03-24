package cn.hutool.core.lang.hash;

/**
 * 128位数字表示，分高位和低位
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class Number128 {

	private long lowValue;
	private long highValue;

	/**
	 * 构造
	 *
	 * @param lowValue  低位
	 * @param highValue 高位
	 */
	public Number128(long lowValue, long highValue) {
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	public long getLowValue() {
		return lowValue;
	}

	public long getHighValue() {
		return highValue;
	}

	public void setLowValue(long lowValue) {
		this.lowValue = lowValue;
	}

	public void setHighValue(long hiValue) {
		this.highValue = hiValue;
	}

	public long[] getLongArray() {
		return new long[]{lowValue, highValue};
	}
}
