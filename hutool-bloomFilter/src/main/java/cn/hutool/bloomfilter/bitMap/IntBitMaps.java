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

	public static IntBitMap create(int size, int min) {
		if (min == 0) {
			return new IntMap(size);
		} else {
			return new IntRangeMap(size, min);
		}
	}

	public static IntBitMap createOfRange(int min, int max) {
		// 交换值
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		} else if (min == max) {
			return new IntOneMap(min);
		}

		int diff = max - min;
		int size = diff / MACHINE32 + (diff % MACHINE32 > 0 ? 1 : 0);

		if (min == 0) {
			return new IntMap(size, max);
		} else {
			return new IntRangeMap(size, min, max);
		}
	}

}
