package cn.hutool.bloomfilter.bitmap;

/**
 * 允许设置值范围的变种IntMap
 *
 * @author wangliang181230
 */
class IntRangeMap extends IntMap {
	private static final long serialVersionUID = 1L;

	// the range
	private final int min;
	private final int max;


	/**
	 * 构造
	 *
	 * @param min  最小值
	 * @param size 容量
	 */
	IntRangeMap(int min, int size) {
		super(size);
		this.min = min;
		this.max = min + size * MACHINE32 - 1;
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

	@Override
	public int getMin() {
		return min;
	}

	@Override
	public int getMax() {
		return max;
	}
}