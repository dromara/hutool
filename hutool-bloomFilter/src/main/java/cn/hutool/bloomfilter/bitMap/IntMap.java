package cn.hutool.bloomfilter.bitMap;

/**
 * 过滤器BitMap在32位机器上.这个类能发生更好的效果.一般情况下建议使用此类
 * @author loolly
 *
 */
public class IntMap implements BitMap {

	private static final int MAX = Integer.MAX_VALUE;

	public IntMap() {
		ints = new int[93750000];
	}

	public IntMap(int size) {
		ints = new int[size];
	}

	private int[] ints = null;

	public void add(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		ints[r] = (int) (ints[r] | (1 << c));
	}

	public boolean contains(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		if (((int) ((ints[r] >>> c)) & 1) == 1) {
			return true;
		}
		return false;
	}

	public void remove(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		ints[r] = (int) (ints[r] & (((1 << (c + 1)) - 1) ^ MAX));
	}

}