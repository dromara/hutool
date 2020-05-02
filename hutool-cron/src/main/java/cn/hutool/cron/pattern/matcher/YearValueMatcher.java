package cn.hutool.cron.pattern.matcher;

import java.util.List;

/**
 * 年匹配<br>
 * 考虑年数字太大，不适合boolean数组，单独使用列表遍历匹配
 * @author Looly
 *
 */
public class YearValueMatcher implements ValueMatcher{
	
	private final List<Integer> valueList;
	
	public YearValueMatcher(List<Integer> intValueList) {
		this.valueList = intValueList;
	}

	@Override
	public boolean match(Integer t) {
		return valueList.contains(t);
	}
}
