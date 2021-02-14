package cn.hutool.bloomfilter.filter;


public class JSFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	public JSFilter(long maxValue, int machineNum) {
		super(maxValue, machineNum);
	}

	public JSFilter(long maxValue) {
		super(maxValue);
	}

	@Override
	public long hash(String str) {
		int hash = 1315423911;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}

		if(hash<0) {
			hash*=-1 ;
		}

		return hash % size;
	}

}
