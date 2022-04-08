package cn.hutool.bloomfilter.filter;

import cn.hutool.core.util.HashUtil;

public class ELFFilter extends FuncFilter {
	private static final long serialVersionUID = 1L;

	public ELFFilter(long maxValue) {
		this(maxValue, DEFAULT_MACHINE_NUM);
	}

	public ELFFilter(long maxValue, int machineNumber) {
		super(maxValue, machineNumber, HashUtil::elfHash);
	}
}
