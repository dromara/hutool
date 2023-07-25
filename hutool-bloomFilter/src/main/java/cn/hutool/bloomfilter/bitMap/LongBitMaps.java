package cn.hutool.bloomfilter.bitMap;

/**
 * 用于创建LongMap的工具类
 *
 * @author wangliang181230
 */
public class LongBitMaps {

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
		}

		int size = max - min + 1;
		return create(min, size);
	}

}
