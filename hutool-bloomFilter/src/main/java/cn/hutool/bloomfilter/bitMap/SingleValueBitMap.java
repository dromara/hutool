package cn.hutool.bloomfilter.bitmap;

import java.io.Serializable;

/**
 * 只有一个值的BitMap
 *
 * @author wangliang181230
 */
class SingleValueBitMap implements IntBitMap, LongBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private final long value;
	private volatile boolean exist = false;


	/**
	 * 构造
	 *
	 * @param value 唯一一个值
	 */
	SingleValueBitMap(long value) {
		this.value = value;
	}


	@Override
	public void add(long i) {
		if (i == value) {
			this.exist = true;
		} else {
			throw new IllegalArgumentException("The value " + i + " is out the range [" + value + "," + value + "].");
		}
	}

	@Override
	public boolean contains(long i) {
		return i == value && this.exist;
	}

	@Override
	public void remove(long i) {
		if (i == value) {
			this.exist = false;
		}
	}

	@Override
	public long getMin() {
		return value;
	}

	@Override
	public long getMax() {
		return value;
	}


	public long getValue() {
		return value;
	}
}
