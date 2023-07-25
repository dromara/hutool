package cn.hutool.bloomfilter.bitmap;

import static cn.hutool.bloomfilter.bitmap.BitMap.MACHINE64;

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
			return new LongOneMap(min);
		}

		long diff = max - min;
		int size = (int) diff / MACHINE64 + (diff % MACHINE64 > 0 ? 1 : 0);

		if (min == 0) {
			return new LongMap(size, max);
		} else {
			return new LongRangeMap(size, min, max);
		}
	}

}
