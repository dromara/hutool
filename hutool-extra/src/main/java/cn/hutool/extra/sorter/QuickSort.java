package cn.hutool.extra.sorter;


/**
 * 快速排序
 *
 * @author zhaosheng
 */
public final class QuickSort implements IndexedSorter {

	private static final IndexedSorter alt = new HeapSort();

	public QuickSort() {
	}

	private static void fix(IndexedSortable s, int p, int r) {
		if (s.compare(p, r) > 0) {
			s.swap(p, r);
		}
	}

	/**
	 * Deepest recursion before giving up and doing a heapsort.
	 * Returns 2 * ceil(log(n)).
	 */
	protected static int getMaxDepth(int x) {
		if (x <= 0)
			throw new IllegalArgumentException("Undefined for " + x);
		return (32 - Integer.numberOfLeadingZeros(x - 1)) << 2;
	}

	/**
	 * 使用快速排查对指定的数据范围排序，如果递归尝试小于{@link #getMaxDepth}则使用{@link HeapSort}.
	 */
	@Override
	public void sort(IndexedSortable s, int p, int r) {
		sort(s, p, r, null);
	}

	@Override
	public void sort(final IndexedSortable s, int p, int r,
					 final Progressable rep) {
		sortInternal(s, p, r, rep, getMaxDepth(r - p));
	}

	private static void sortInternal(final IndexedSortable s, int p, int r,
									 final Progressable rep, int depth) {
		if (null != rep) {
			rep.progress();
		}
		while (true) {
			if (r - p < 13) {
				for (int i = p; i < r; ++i) {
					for (int j = i; j > p && s.compare(j - 1, j) > 0; --j) {
						s.swap(j, j - 1);
					}
				}
				return;
			}
			if (--depth < 0) {
				// give up
				alt.sort(s, p, r, rep);
				return;
			}

			// select, move pivot into first position
			fix(s, (p + r) >>> 1, p);
			fix(s, (p + r) >>> 1, r - 1);
			fix(s, p, r - 1);

			// Divide
			int i = p;
			int j = r;
			int ll = p;
			int rr = r;
			int cr;
			while (true) {
				while (++i < j) {
					if ((cr = s.compare(i, p)) > 0) break;
					if (0 == cr && ++ll != i) {
						s.swap(ll, i);
					}
				}
				while (--j > i) {
					if ((cr = s.compare(p, j)) > 0) break;
					if (0 == cr && --rr != j) {
						s.swap(rr, j);
					}
				}
				if (i < j) s.swap(i, j);
				else break;
			}
			j = i;
			// swap pivot- and all eq values- into position
			while (ll >= p) {
				s.swap(ll--, --i);
			}
			while (rr < r) {
				s.swap(rr++, j++);
			}

			// Conquer
			// Recurse on smaller interval first to keep stack shallow
			assert i != j;
			if (i - p < r - j) {
				sortInternal(s, p, i, rep, depth);
				p = j;
			} else {
				sortInternal(s, j, r, rep, depth);
				r = i;
			}
		}
	}

}
