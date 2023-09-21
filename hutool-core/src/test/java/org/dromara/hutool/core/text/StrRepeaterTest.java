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

package org.dromara.hutool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrRepeaterTest {

	@Test
	public void repeatByLengthTest() {
		// 如果指定长度非指定字符串的整数倍，截断到固定长度
		final String ab = StrRepeater.of(5).repeatByLength("ab");
		Assertions.assertEquals("ababa", ab);
	}

	@Test
	public void repeatByLengthTest2() {
		// 如果指定长度小于字符串本身的长度，截断之
		final String ab = StrRepeater.of(2).repeatByLength("abcde");
		Assertions.assertEquals("ab", ab);
	}
}
