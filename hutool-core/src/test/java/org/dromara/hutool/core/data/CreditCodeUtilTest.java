/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.data.CreditCodeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreditCodeUtilTest {

	@Test
	public void isCreditCodeBySimple() {
		final String testCreditCode = "91310115591693856A";
		Assertions.assertTrue(CreditCodeUtil.isCreditCodeSimple(testCreditCode));
	}

	@Test
	public void isCreditCode() {
		final String testCreditCode = "91310110666007217T";
		Assertions.assertTrue(CreditCodeUtil.isCreditCode(testCreditCode));
	}

	@Test
	public void isCreditCode2() {
		// 由于早期部分试点地区推行 法人和其他组织统一社会信用代码 较早，会存在部分代码不符合国家标准的情况。
		// 见：https://github.com/bluesky335/IDCheck
		final String testCreditCode = "91350211M00013FA1N";
		Assertions.assertFalse(CreditCodeUtil.isCreditCode(testCreditCode));
	}

	@Test
	public void randomCreditCode() {
		final String s = CreditCodeUtil.randomCreditCode();
		Assertions.assertTrue(CreditCodeUtil.isCreditCode(s));
	}
}
