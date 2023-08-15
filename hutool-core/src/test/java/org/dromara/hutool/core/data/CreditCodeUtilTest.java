/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
