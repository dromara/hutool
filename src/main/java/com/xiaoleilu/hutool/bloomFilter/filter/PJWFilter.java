package com.xiaoleilu.hutool.bloomFilter.filter;

import com.xiaoleilu.hutool.Hashs;

public class PJWFilter extends AbstractFilter {

	public PJWFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public PJWFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		return Hashs.pjwHash(str) % size;
	}

}
