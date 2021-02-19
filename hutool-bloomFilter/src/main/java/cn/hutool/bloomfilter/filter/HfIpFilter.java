package cn.hutool.bloomfilter.filter;

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
		long hash = 0;
		for (int i = 0; i < length; i++) {
			hash += str.charAt(i % 4) ^ str.charAt(i);
		}
		return hash % size;
	}

}
