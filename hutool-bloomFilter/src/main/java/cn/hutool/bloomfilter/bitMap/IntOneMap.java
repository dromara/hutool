package cn.hutool.bloomfilter.bitmap;

import java.io.Serializable;

/**
 * 只有一个值的IntBitMap
 *
 * @author wangliang181230
 */
class IntOneMap implements IntBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private final long onlyOneValue;
	private volatile boolean exist = false;


	/**
	 * 构造
	 *
	 * @param onlyOneValue 唯一一个值
	 */
	IntOneMap(long onlyOneValue) {
		this.onlyOneValue = onlyOneValue;
	}


	@Override
	public void add(long i) {
		if (i == onlyOneValue) {
			this.exist = true;
		} else {
			throw new IllegalArgumentException("The value " + i + " is out the range [" + onlyOneValue + "," + onlyOneValue + "].");
		}
	}

	@Override
	public boolean contains(long i) {
		return i == onlyOneValue && this.exist;
	}

	@Override
	public void remove(long i) {
		if (i == onlyOneValue) {
			this.exist = false;
		}
	}

	@Override
	public long getMin() {
		return onlyOneValue;
	}

	@Override
	public long getMax() {
		return onlyOneValue;
	}

	public long getOnlyOneValue() {
		return onlyOneValue;
	}
}
