package cn.hutool.bloomfilter.bitMap;

/**
 * BitMap接口，用于将某个int或long值映射到一个数组中，从而判定某个值是否存在
 * 
 * @author looly
 *
 */
public interface BitMap {

	public final int MACHINE32 = 32;
	public final int MACHINE64 = 64;

	/**
	 * 加入值
	 * 
	 * @param i 值
	 */
	public void add(long i);

	/**
	 * 检查是否包含值
	 * 
	 * @param i 值
	 */
	public boolean contains(long i);

	/**
	 * 移除值
	 * 
	 * @param i 值
	 */
	public void remove(long i);
}