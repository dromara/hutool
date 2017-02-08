package com.xiaoleilu.hutool.cron.matcher;

import java.util.List;

public class IntArrayValueMatcher implements ValueMatcher{
	
	Integer[] values;
	
	public IntArrayValueMatcher(List<Integer> intValueList) {
		this.values = intValueList.toArray(new Integer[intValueList.size()]);
	}

	@Override
	public boolean match(Integer t) {
		if(null == t){
			return false;
		}
		for(int i = 0; i < values.length; i++){
			if(t.equals(values[i])){
				return true;
			}
		}
		return false;
	}

}
