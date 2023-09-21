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

package org.dromara.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest() {
		final int a = 100;
		final Boolean aBoolean = Convert.toBoolean(a);
		Assertions.assertTrue(aBoolean);

		final int b = 0;
		final Boolean bBoolean = Convert.toBoolean(b);
		Assertions.assertFalse(bBoolean);
	}

	@Test
	public void issueI65P8ATest() {
		final Boolean bool = Convert.toBoolean("", Boolean.TRUE);
		Assertions.assertFalse(bool);
	}

}
