package cn.hutool.bloomfilter.bitMap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.Serializable;

/**
 * 只有一个值的LongBitMap
 *
 * @author wangliang181230
 */
class LongOneMap implements LongBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private final long onlyOneValue;
	private volatile boolean exist = false;


	/**
	 * 构造
	 *
	 * @param onlyOneValue 唯一一个值
	 */
	LongOneMap(long onlyOneValue) {
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


	public long getOnlyOneValue() {
		return onlyOneValue;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ObjectSizeCalculator.getObjectSize(new LongMap(1)));
		System.out.println(ObjectSizeCalculator.getObjectSize(new LongOneMap(1)));
	}
}
