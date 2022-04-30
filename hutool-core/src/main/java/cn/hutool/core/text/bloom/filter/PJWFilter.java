package cn.hutool.core.text.bloom.filter;

import cn.hutool.core.lang.hash.HashUtil;

public class PJWFilter extends FuncFilter {
	private static final long serialVersionUID = 1L;

	public PJWFilter(long maxValue) {
		this(maxValue, DEFAULT_MACHINE_NUM);
	}

	public PJWFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum, HashUtil::pjwHash);
	}
}
