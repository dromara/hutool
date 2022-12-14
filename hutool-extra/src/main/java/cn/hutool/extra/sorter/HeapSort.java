package cn.hutool.extra.sorter;


/**
 * An implementation of the core algorithm of HeapSort.
 * @author zhaosheng
 */
public final class HeapSort implements IndexedSorter {

	public HeapSort() { }

	private static void downHeap(final IndexedSortable s, final int b,
								 int i, final int N) {
		for (int idx = i << 1; idx < N; idx = i << 1) {
			if (idx + 1 < N && s.compare(b + idx, b + idx + 1) < 0) {
				if (s.compare(b + i, b + idx + 1) < 0) {
					s.swap(b + i, b + idx + 1);
				} else return;
				i = idx + 1;
			} else if (s.compare(b + i, b + idx) < 0) {
				s.swap(b + i, b + idx);
				i = idx;
			} else return;
		}
	}

	/**
	 * Sort the given range of items using heap sort.
	 * {@inheritDoc}
	 */
	@Override
	public void sort(IndexedSortable s, int p, int r) {
		sort(s, p, r, null);
	}

	@Override
	public void sort(final IndexedSortable s, final int p, final int r,
					 final Progressable rep) {
		final int N = r - p;
		// build heap w/ reverse comparator, then write in-place from end
		final int t = Integer.highestOneBit(N);
		for (int i = t; i > 1; i >>>= 1) {
			for (int j = i >>> 1; j < i; ++j) {
				downHeap(s, p-1, j, N + 1);
			}
			if (null != rep) {
				rep.progress();
			}
		}
		for (int i = r - 1; i > p; --i) {
			s.swap(p, i);
			downHeap(s, p - 1, 1, i - p + 1);
		}
	}
}
