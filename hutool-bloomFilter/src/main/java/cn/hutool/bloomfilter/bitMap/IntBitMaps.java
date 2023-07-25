package cn.hutool.bloomfilter.bitmap;

import static cn.hutool.bloomfilter.bitmap.BitMap.MACHINE32;

/**
 * 用于创建IntBitMap的工具类
 *
 * @author wangliang181230
 */
public class IntBitMaps {

	private IntBitMaps() {
		throw new UnsupportedOperationException("Instantiation of IntBitMaps class is not allowed");
	}


	public static IntBitMap create() {
		return new IntMap();
	}

	public static IntBitMap create(int size) {
		return new IntMap(size);
	}

	public static IntBitMap create(int size, long min) {
		if (min == 0) {
			return new IntMap(size);
		} else {
			return new IntRangeMap(size, min);
		}
	}

	public static IntBitMap createOfRange(long min, long max) {
		// 交换值
		if (min > max) {
			long temp = min;
			min = max;
			max = temp;
		} else if (min == max) {
			return new SingleValueBitMap(min);
		}

		long total = max - min + 1L;
		if (total <= 0 || total > IntBitMap.MAX_TOTAL) {
			throw new IllegalArgumentException("The range[" + min + "," + max + "] is too bigger.");
		}
		int size = computeSize(total);

		if (min == 0) {
			return new IntMap(size, max);
		} else {
			return new IntRangeMap(size, min, max);
		}
	}

	public static int computeSize(long max) {
		return (int) (max / MACHINE32 + (max % MACHINE32 > 0 ? 1 : 0));
	}

	public static int computeSize(long min, long max) {
		long total = max - min + 1;
		return computeSize(total);
	}

	public static long computeTotal(int size) {
		return (long) size * MACHINE32;
	}

	public static long computeMax(int size) {
		return computeTotal(size) - 1;
	}

	public static long computeMax(int size, long min) {
		return computeMax(size) + min;
	}

}
