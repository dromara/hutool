package cn.hutool.bloomfilter.bitMap;

/**
 * LongBitMap接口，用于将某个long值映射到一个数组中，从而判定某个值是否存在
 *
 * @author wangliang181230
 */
public interface LongBitMap extends BitMap {

	/**
	 * 一个LongBitMap可以存放的最大数字量，但要注意内存溢出
 	 */
	long MAX_TOTAL = (long) Integer.MAX_VALUE * MACHINE64;

	/**
	 * 当min=0时，max的最大值
	 */
	long MAX_VALUE = MAX_TOTAL - 1;


	long getMin();

	long getMax();

}
