package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.util.StrUtil;

/**
 * 所有值匹配，始终返回{@code true}
 *
 * @author Looly
 */
public class AlwaysTrueMatcher implements PartMatcher {

	public static AlwaysTrueMatcher INSTANCE = new AlwaysTrueMatcher();

	@Override
	public boolean match(Integer t) {
		return true;
	}

	@Override
	public int nextAfter(int value) {
		return value;
	}

	@Override
	public String toString() {
		return StrUtil.format("[Matcher]: always true.");
	}
}
