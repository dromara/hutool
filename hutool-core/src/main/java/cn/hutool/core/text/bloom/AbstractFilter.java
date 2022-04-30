package cn.hutool.core.text.bloom;

import java.util.BitSet;

/**
 * 抽象Bloom过滤器
 *
 * @author looly
 */
public abstract class AbstractFilter implements BloomFilter {
	private static final long serialVersionUID = 1L;

	private final BitSet bitSet;
	protected int size;


	/**
	 * 构造
	 *
	 * @param size 容量
	 */
	public AbstractFilter(final int size) {
		this.size = size;
		this.bitSet = new BitSet(size);
	}

	@Override
	public boolean contains(final String str) {
		return bitSet.get(Math.abs(hash(str)));
	}

	@Override
	public boolean add(final String str) {
		final int hash = Math.abs(hash(str));
		if (bitSet.get(hash)) {
			return false;
		}

		bitSet.set(hash);
		return true;
	}

	/**
	 * 自定义Hash方法
	 *
	 * @param str 字符串
	 * @return HashCode
	 */
	public abstract int hash(String str);
}
