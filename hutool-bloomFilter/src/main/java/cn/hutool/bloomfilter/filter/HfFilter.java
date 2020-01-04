package cn.hutool.bloomfilter.filter;


import java.util.stream.IntStream;

public class HfFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public HfFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}
	
	public HfFilter(long maxValue) {
		super(maxValue);
	}
	
	@Override
	public long hash(String str) {
		int length = str.length() ;
		long hash = IntStream.range(0, length).mapToLong(i -> str.charAt(i) * 3 * i).sum();

		if (hash < 0) {
			hash = -hash;
		}

		return hash % size;
	}

}
