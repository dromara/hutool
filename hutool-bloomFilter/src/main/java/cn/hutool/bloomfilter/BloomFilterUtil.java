package cn.hutool.bloomfilter;

/**
 * 布隆过滤器工具
 * 
 * @author looly
 * @since 4.1.5
 */
public class BloomFilterUtil {

	/**
	 * 创建一个BitSet实现的布隆过滤器，过滤器的容量为c * n 个bit.
	 * 
	 * @param c 当前过滤器预先开辟的最大包含记录,通常要比预计存入的记录多一倍.
	 * @param n 当前过滤器预计所要包含的记录.
	 * @param k 哈希函数的个数，等同每条记录要占用的bit数.
	 * @return BitSetBloomFilter
	 */
	public static BitSetBloomFilter createBitSet(int c, int n, int k) {
		return new BitSetBloomFilter(c, n, k);
	}

	/**
	 * 创建BitMap实现的布隆过滤器
	 * 
	 * @param m BitMap的大小
	 * @return BitMapBloomFilter
	 */
	public static BitMapBloomFilter createBitMap(int m) {
		return new BitMapBloomFilter(m);
	}
}
