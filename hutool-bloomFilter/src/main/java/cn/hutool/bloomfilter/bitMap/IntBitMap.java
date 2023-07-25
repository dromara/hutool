package cn.hutool.bloomfilter.bitmap;

/**
 * IntBitMap接口，用于将某个int值映射到一个数组中，从而判定某个值是否存在
 *
 * @author wangliang181230
 */
public interface IntBitMap extends BitMap {

	long MAX_TOTAL = (long) Integer.MAX_VALUE * MACHINE32;

	// 当min=0时，max的最大值
	long MAX_VALUE = MAX_TOTAL - 1;


	long getMin();

	long getMax();

}
