package cn.hutool.bloomfilter.filter;

import java.util.stream.IntStream;

public class HfIpFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public HfIpFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public HfIpFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		int length = str.length();
		long hash = IntStream.range(0, length).mapToLong(i -> str.charAt(i % 4) ^ str.charAt(i)).sum();
		return hash % size;
	}

}
