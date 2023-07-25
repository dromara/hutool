package cn.hutool.bloomfilter.bitMap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

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
		this.exist = (i == onlyOneValue);
	}

	@Override
	public boolean contains(long i) {
		return this.exist && i == onlyOneValue;
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

	public static void main(String[] args) throws Exception {
		System.out.println(ObjectSizeCalculator.getObjectSize(new IntMap(1)));
		System.out.println(ObjectSizeCalculator.getObjectSize(new IntOneMap(1)));
	}
}
