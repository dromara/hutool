/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.cron.pattern.matcher;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 所有值匹配，始终返回{@code true}
 *
 * @author Looly
 */
public class AlwaysTrueMatcher implements PartMatcher {

	/**
	 * 单例
	 */
	public static AlwaysTrueMatcher INSTANCE = new AlwaysTrueMatcher();

	@Override
	public boolean test(final Integer t) {
		return true;
	}

	@Override
	public int nextAfter(final int value) {
		return value;
	}

	@Override
	public String toString() {
		return StrUtil.format("[Matcher]: always true.");
	}
}
