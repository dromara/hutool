package cn.hutool.bloomfilter.filter;

import cn.hutool.core.util.HashUtil;

public class FNVFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public FNVFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public FNVFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.fnvHash(str);
	}

}
