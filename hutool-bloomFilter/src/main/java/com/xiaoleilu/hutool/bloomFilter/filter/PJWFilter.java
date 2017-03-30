package com.xiaoleilu.hutool.bloomFilter.filter;

import com.xiaoleilu.hutool.util.HashUtil;

public class PJWFilter extends AbstractFilter {

	public PJWFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public PJWFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return HashUtil.pjwHash(str) % size;
	}

}
