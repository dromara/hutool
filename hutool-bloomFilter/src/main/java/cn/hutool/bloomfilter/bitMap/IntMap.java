package cn.hutool.bloomfilter.bitmap;

import java.io.Serializable;

/**
 * 过滤器BitMap在32位机器上.这个类能发生更好的效果.一般情况下建议使用此类
 *
 * @author loolly
 *
 */
class IntMap implements IntBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	protected final int[] ints;

	/**
	 * 构造
	 */
	IntMap() {
		ints = new int[93750000];
	}

	/**
	 * 构造
	 *
	 * @param size 容量
	 */
	IntMap(int size) {
		ints = new int[size];
	}

	@Override
	public void add(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		ints[r] = ints[r] | (1 << c);
	}

	@Override
	public boolean contains(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		return ((ints[r] >>> c) & 1) == 1;
	}

	@Override
	public void remove(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		ints[r] &= ~(1 << c);
	}

	@Override
	public int getMin() {
		return 0;
	}

	@Override
	public int getMax() {
		return ints.length * MACHINE32 - 1;
	}
}
