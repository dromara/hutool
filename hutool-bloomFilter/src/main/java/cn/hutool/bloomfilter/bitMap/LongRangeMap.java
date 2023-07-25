package cn.hutool.bloomfilter.bitMap;

/**
 * 允许设置值范围的变种LongMap
 *
 * @author wangliang181230
 */
class LongRangeMap extends LongMap {
	private static final long serialVersionUID = 1L;

	// the range
	private final long min;
	private final long max;


	/**
	 * 构造
	 *
	 * @param min  最小值
	 * @param size 容量
	 */
	LongRangeMap(long min, int size) {
		super(size);
		this.min = min;
		this.max = min + (long) size * MACHINE64 - 1;
	}


	@Override
	public void add(long i) {
		if (i < min || i > max) {
			return;
		}

		i -= min;
		super.add(i);
	}

	@Override
	public boolean contains(long i) {
		if (i < min || i > max) {
			return false;
		}

		i -= min;
		return super.contains(i);
	}

	@Override
	public void remove(long i) {
		if (i < min || i > max) {
			return;
		}

		i -= min;
		super.remove(i);
	}


	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}
}
