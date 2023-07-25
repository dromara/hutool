package cn.hutool.bloomfilter.bitMap;

import java.io.Serializable;

/**
 * 过滤器BitMap在32位机器上.这个类能发生更好的效果.一般情况下建议使用此类
 *
 * @author loolly
 */
class IntMap implements IntBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	// Integer.MAX_VALUE / MACHINE32 + (Integer.MAX_VALUE % MACHINE32 > 0 ? 1 : 0)
	public static final int DEFAULT_SIZE = IntBitMaps.computeSize(Integer.MAX_VALUE);

	private static final long MIN = 0;


	protected final int[] ints;
	protected long max;

	/**
	 * 构造
	 */
	IntMap() {
		this(DEFAULT_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param size 容量
	 */
	IntMap(int size) {
		this(size, IntBitMaps.computeMax(size));
	}

	/**
	 * 构造
	 *
	 * @param size 容量
	 * @param max  最大值
	 */
	IntMap(int size, long max) {
		ints = new int[size];
		this.max = max;
	}


	@Override
	public void add(long i) {
		if (i < getMin() || i > getMax()) {
			throw new IllegalArgumentException("The value " + i + " is out the range [" + MIN + "," + max + "].");
		}

		this.doAdd(i);
	}

	protected void doAdd(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		ints[r] = ints[r] | (1 << c);
	}

	@Override
	public boolean contains(long i) {
		if (i < getMin() || i > getMax()) {
			return false;
		}

		return this.doContains(i);
	}

	protected boolean doContains(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		return ((ints[r] >>> c) & 1) == 1;
	}

	@Override
	public void remove(long i) {
		if (i < getMin() || i > getMax()) {
			return;
		}

		this.doRemove(i);
	}

	protected void doRemove(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i & (BitMap.MACHINE32 - 1));
		ints[r] &= ~(1 << c);
	}


	@Override
	public long getMin() {
		return MIN;
	}

	@Override
	public long getMax() {
		return max;
	}
}
