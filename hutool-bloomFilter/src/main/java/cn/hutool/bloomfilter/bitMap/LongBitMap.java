package cn.hutool.bloomfilter.bitMap;

/**
 * LongBitMap接口，用于将某个long值映射到一个数组中，从而判定某个值是否存在
 *
 * @author wangliang181230
 */
public interface LongBitMap extends BitMap {

	long getMin();

	long getMax();

}
