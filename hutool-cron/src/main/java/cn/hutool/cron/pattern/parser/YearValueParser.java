package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.pattern.matcher.ValueMatcher;
import cn.hutool.cron.pattern.matcher.YearValueMatcher;

import java.util.List;

/**
 * 年值处理<br>
 * 年的限定在1970-2099年
 *
 * @author Looly
 */
public class YearValueParser extends AbsValueParser {

	public YearValueParser() {
		super(1970, 2099);
	}

	@Override
	protected ValueMatcher buildValueMatcher(List<Integer> values) {
		//考虑年数字太大，不适合boolean数组，单独使用列表遍历匹配
		return new YearValueMatcher(values);
	}
}
