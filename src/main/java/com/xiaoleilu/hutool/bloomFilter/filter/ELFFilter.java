package com.xiaoleilu.hutool.bloomFilter.filter;

import com.xiaoleilu.hutool.Hashs;

public class ELFFilter extends AbstractFilter {

	public ELFFilter(long maxValue, int MACHINENUM) {
		super(maxValue, MACHINENUM);
	}
	
	public ELFFilter(long maxValue) {
		super(maxValue);
	}
	
	@Override
	public long hash(String str) {
		return Hashs.ELFHash(str) % size;
	}

}
