package cn.hutool.core.text.bloom;

/**
 * 组合BloomFilter 实现 <br>
 * 1.构建hash算法 <br>
 * 2.散列hash映射到数组的bit位置 <br>
 * 3.验证<br>
 * 此实现方式可以指定Hash算法
 *
 * @author Ansj
 */
public class CombinedBloomFilter implements BloomFilter {
	private static final long serialVersionUID = 1L;

	private final BloomFilter[] filters;

	/**
	 * 使用自定的多个过滤器建立BloomFilter
	 *
	 * @param filters Bloom过滤器列表
	 */
	public CombinedBloomFilter(final BloomFilter... filters) {
		this.filters = filters;
	}

	/**
	 * 增加字符串到Filter映射中
	 *
	 * @param str 字符串
	 */
	@Override
	public boolean add(final String str) {
		boolean flag = false;
		for (final BloomFilter filter : filters) {
			flag |= filter.add(str);
		}
		return flag;
	}

	/**
	 * 是否可能包含此字符串，此处存在误判
	 *
	 * @param str 字符串
	 * @return 是否存在
	 */
	@Override
	public boolean contains(final String str) {
		for (final BloomFilter filter : filters) {
			if (filter.contains(str) == false) {
				return false;
			}
		}
		return true;
	}
}
