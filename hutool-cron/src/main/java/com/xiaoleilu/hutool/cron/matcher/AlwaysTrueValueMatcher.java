package com.xiaoleilu.hutool.cron.matcher;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 值匹配，始终返回<code>true</code>
 * @author Looly
 *
 */
public class AlwaysTrueValueMatcher implements ValueMatcher{

	@Override
	public boolean match(Integer t) {
		return true;
	}
	
	@Override
	public String toString() {
		return StrUtil.format("[Matcher]: always true.");
	}
}
