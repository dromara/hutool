package cn.hutool.cron.pattern.matcher;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 年匹配<br>
 * 考虑年数字太大，不适合boolean数组，单独使用{@link LinkedHashSet}匹配
 *
 * @author Looly
 */
public class YearValueMatcher implements PartMatcher {

	private final LinkedHashSet<Integer> valueList;

	public YearValueMatcher(Collection<Integer> intValueList) {
		this.valueList = new LinkedHashSet<>(intValueList);
	}

	@Override
	public boolean match(Integer t) {
		return valueList.contains(t);
	}

	@Override
	public int nextAfter(int value) {
		for (Integer year : valueList) {
			if (year >= value) {
				return year;
			}
		}

		// 年无效，此表达式整体无效
		return -1;
	}
}
