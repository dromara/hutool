package cn.hutool.bloomfilter.filter;

import cn.hutool.core.util.HashUtil;

public class SDBMFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public SDBMFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public SDBMFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.sdbmHash(str) % size;
	}

}
