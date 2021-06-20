package cn.hutool.bloomfilter.filter;

import cn.hutool.core.util.HashUtil;


public class TianlFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public TianlFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public TianlFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.tianlHash(str) % size;
	}

}
