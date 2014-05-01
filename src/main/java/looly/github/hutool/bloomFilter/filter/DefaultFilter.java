package looly.github.hutool.bloomFilter.filter;

import looly.github.hutool.Hashs;

/**
 * 默认Bloom过滤器，使用Java自带的Hash算法
 * @author loolly
 *
 */
public class DefaultFilter extends AbstractFilter {

	public DefaultFilter(long maxValue, int MACHINENUM) {
		super(maxValue, MACHINENUM);
	}
	
	public DefaultFilter(long maxValue) {
		super(maxValue);
	}
	
	@Override
	public long hash(String str) {
		return Hashs.javaDefaultHash(str) % size;
	}
}
