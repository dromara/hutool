package cn.hutool.bloomfilter.bitMap;

/**
 * BitMap接口，用于将某个int或long值映射到一个数组中，从而判定某个值是否存在
 *
 * @author looly
 *
 */
public interface BitMap{

	int MACHINE32 = 32;
	int MACHINE64 = 64;

	/**
	 * 加入值
	 *
	 * @param i 值
	 */
	void add(long i);

	/**
	 * 检查是否包含值
	 *
	 * @param i 值
	 * @return 是否包含
	 */
	boolean contains(long i);

	/**
	 * 移除值
	 *
	 * @param i 值
	 */
	void remove(long i);
}