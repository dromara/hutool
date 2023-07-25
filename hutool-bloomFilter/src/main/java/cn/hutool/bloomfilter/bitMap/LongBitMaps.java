package cn.hutool.bloomfilter.bitMap;

import static cn.hutool.bloomfilter.bitMap.BitMap.MACHINE64;

/**
 * 用于创建LongBitMap的工具类
 *
 * @author wangliang181230
 */
public class LongBitMaps {

	private LongBitMaps() {
		throw new UnsupportedOperationException("Instantiation of LongBitMaps class is not allowed");
	}


	public static LongBitMap create() {
		return new LongMap();
	}

	public static LongBitMap create(int size) {
		return new LongMap(size);
	}

	public static LongBitMap create(int size, long min) {
		if (min == 0) {
			return new LongMap(size);
		} else {
			return new LongRangeMap(size, min);
		}
	}

	public static LongBitMap createOfRange(long min, long max) {
		// 交换值
		if (min > max) {
			long temp = min;
			min = max;
			max = temp;
		} else if (min == max) {
			return new SingleValueBitMap(min);
		}

		long total = max - min + 1L;
		if (total <= 0 || total > LongBitMap.MAX_TOTAL) {
			throw new IllegalArgumentException("The range[" + min + "," + max + "] is too bigger.");
		}
		int size = computeSize(total);


		if (min == 0) {
			return new LongMap(size, max);
		} else {
			return new LongRangeMap(size, min, max);
		}
	}

	public static int computeSize(long max) {
		return (int) (max / MACHINE64 + (max % MACHINE64 > 0 ? 1 : 0));
	}

	public static int computeSize(long min, long max) {
		// 交换值
		if (min > max) {
			long temp = min;
			min = max;
			max = temp;
		}

		long total = max - min + 1;
		if (total <= 0 && total <= min) {
			throw new IllegalArgumentException("The range[" + min + "," + max + "] is too bigger.");
		}

		return computeSize(total);
	}

	public static long computeTotal(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Invalid size: " + size);
		}
		return (long) size * MACHINE64;
	}

	public static long computeMax(int size) {
		return computeTotal(size) - 1;
	}

	public static long computeMax(int size, long min) {
		long max = computeMax(size) + min;
		if (max <= min) {
			throw new IllegalArgumentException("Maximum value out of range: " + max);
		}
		return max;
	}
}
