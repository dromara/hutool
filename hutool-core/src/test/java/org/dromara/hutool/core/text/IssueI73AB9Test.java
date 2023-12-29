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

public class IssueI73AB9Test {

	/**
	 * https://gitee.com/dromara/hutool/issues/I73AB9
	 */
	@Test
	void subWithLengthTest() {
		final String str = "7814A103447E";
		String s = StrUtil.subByLength(str, -4, 2);
		Assertions.assertEquals("03", s);
		s = StrUtil.subByLength(str, -2, 2);
		Assertions.assertEquals("44", s);
	}
}
