package cn.hutool.bloomfilter.bitMap;

/**
 * 用于创建IntMap的工具类
 *
 * @author wangliang181230
 */
public class IntBitMaps {

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
		}

		int size = max - min + 1;
		return create(min, size);
	}

}
