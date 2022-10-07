package cn.hutool.core.codec;

/**
 * 128位数字表示，分高位和低位
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class Number128 extends Number {
	private static final long serialVersionUID = 1L;

	private long lowValue;
	private long highValue;

	/**
	 * 构造
	 *
	 * @param lowValue  低位
	 * @param highValue 高位
	 */
	public Number128(final long lowValue, final long highValue) {
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	/**
	 * 获取低位值
	 *
	 * @return 地位值
	 */
	public long getLowValue() {
		return lowValue;
	}

	/**
	 * 设置低位值
	 *
	 * @param lowValue 低位值
	 */
	public void setLowValue(final long lowValue) {
		this.lowValue = lowValue;
	}

	/**
	 * 获取高位值
	 *
	 * @return 高位值
	 */
	public long getHighValue() {
		return highValue;
	}

	/**
	 * 设置高位值
	 *
	 * @param hiValue 高位值
	 */
	public void setHighValue(final long hiValue) {
		this.highValue = hiValue;
	}

	/**
	 * 获取高低位数组，long[0]：低位，long[1]：高位
	 *
	 * @return 高低位数组，long[0]：低位，long[1]：高位
	 */
	public long[] getLongArray() {
		return new long[]{lowValue, highValue};
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return this.lowValue;
	}

	@Override
	public float floatValue() {
		return longValue();
	}

	@Override
	public double doubleValue() {
		return longValue();
	}
}
