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

	public static LongBitMap create(int min, int size) {
		if (min == 0) {
			return new LongMap(size);
		} else {
			return new LongRangeMap(min, size);
		}
	}

	public static LongBitMap createOfRange(int min, int max) {
		// 交换值
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		} else if (min == max) {
			return new LongOneMap(min);
		}

		int size = (max - min + 1) / MACHINE64;
		return create(min, size);
	}

}
