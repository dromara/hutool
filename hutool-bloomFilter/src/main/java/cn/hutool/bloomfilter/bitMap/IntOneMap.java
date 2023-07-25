package cn.hutool.bloomfilter.bitMap;

import java.io.Serializable;

/**
 * 只有一个值的IntBitMap
 *
 * @author wangliang181230
 */
class IntOneMap implements IntBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private final int onlyOneValue;
	private volatile boolean exist = false;


	/**
	 * 构造
	 *
	 * @param onlyOneValue 唯一一个值
	 */
	IntOneMap(int onlyOneValue) {
		this.onlyOneValue = onlyOneValue;
	}


	@Override
	public void add(long i) {
		if (i == onlyOneValue) {
			this.exist = true;
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


	public int getOnlyOneValue() {
		return onlyOneValue;
	}
}
