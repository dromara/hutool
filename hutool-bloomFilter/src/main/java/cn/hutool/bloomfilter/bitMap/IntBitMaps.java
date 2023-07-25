package cn.hutool.bloomfilter.bitMap;

import static cn.hutool.bloomfilter.bitMap.BitMap.MACHINE32;

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

	public static IntBitMap create(int min, int size) {
		if (min == 0) {
			return new IntMap(size);
		} else {
			return new IntRangeMap(min, size);
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

		int size = (max - min + 1) / MACHINE32;
		return create(min, size);
	}

}
