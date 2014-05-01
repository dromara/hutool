package looly.github.hutool.bloomFilter.filter;

import looly.github.hutool.Hashs;


public class TianlFilter extends AbstractFilter {

	public TianlFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public TianlFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return Hashs.tianlHash(str) % size;
	}

}
