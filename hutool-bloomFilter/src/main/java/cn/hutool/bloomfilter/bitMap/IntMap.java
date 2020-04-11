package cn.hutool.bloomfilter.bitMap;

import java.io.Serializable;

/**
 * 过滤器BitMap在32位机器上.这个类能发生更好的效果.一般情况下建议使用此类
 * 
 * @author loolly
 *
 */
public class IntMap implements BitMap, Serializable {
	private static final long serialVersionUID = 1L;

	private final int[] ints;

	/**
	 * 构造
	 */
	public IntMap() {
		ints = new int[93750000];
	}

	/**
	 * 构造
	 * 
	 * @param size 容量
	 */
	public IntMap(int size) {
		ints = new int[size];
	}

	@Override
	public void add(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		ints[r] = (int) (ints[r] | (1 << c));
	}

	@Override
	public boolean contains(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		return ((int) ((ints[r] >>> c)) & 1) == 1;
	}

	@Override
	public void remove(long i) {
		int r = (int) (i / BitMap.MACHINE32);
		int c = (int) (i % BitMap.MACHINE32);
		ints[r] &= ~(1 << c);
	}

}