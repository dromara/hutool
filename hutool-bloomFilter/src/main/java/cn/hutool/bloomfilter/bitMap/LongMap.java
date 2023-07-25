package cn.hutool.bloomfilter.bitMap;

import java.io.Serializable;

/**
 * 过滤器BitMap在64位机器上.这个类能发生更好的效果.一般机器不建议使用
 *
 * @author loolly
 */
class LongMap implements LongBitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private static final long MIN = 0;


	protected final long[] longs;
	protected long max;


	/**
	 * 构造
	 */
	LongMap() {
		this(93750000);
	}

	/**
	 * 构造
	 *
	 * @param size 容量
	 */
	LongMap(int size) {
		this(size, LongBitMaps.computeMax(size));
	}

	/**
	 * 构造
	 *
	 * @param size 容量
	 * @param max  最大值
	 */
	LongMap(int size, long max) {
		longs = new long[size];
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
		int r = (int) (i / BitMap.MACHINE64);
		int c = (int) (i & (BitMap.MACHINE64 - 1));
		longs[r] = longs[r] | (1L << c);
	}

	@Override
	public boolean contains(long i) {
		if (i < getMin() || i > getMax()) {
			return false;
		}

		return this.doContains(i);
	}

	protected boolean doContains(long i) {
		int r = (int) (i / BitMap.MACHINE64);
		int c = (int) (i & (BitMap.MACHINE64 - 1));
		return ((longs[r] >>> c) & 1) == 1;
	}

	@Override
	public void remove(long i) {
		if (i < getMin() || i > getMax()) {
			return;
		}

		this.doRemove(i);
	}

	protected void doRemove(long i) {
		int r = (int) (i / BitMap.MACHINE64);
		int c = (int) (i & (BitMap.MACHINE64 - 1));
		longs[r] &= ~(1L << c);
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
