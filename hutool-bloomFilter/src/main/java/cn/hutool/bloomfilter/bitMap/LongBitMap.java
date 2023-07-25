package cn.hutool.bloomfilter.bitmap;

/**
 * LongBitMap接口，用于将某个long值映射到一个数组中，从而判定某个值是否存在
 *
 * @author wangliang181230
 */
public interface LongBitMap extends BitMap {

	long MAX_TOTAL = (long) Integer.MAX_VALUE * MACHINE64;

	// 当min=0时，max的最大值
	long MAX_VALUE = MAX_TOTAL - 1;


	long getMin();

	long getMax();

}
