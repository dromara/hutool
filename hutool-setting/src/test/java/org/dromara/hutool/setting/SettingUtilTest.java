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

package org.dromara.hutool.setting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingUtilTest {

	@Test
	public void getTest() {
		final String driver = SettingUtil.get("test").getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getTest2() {
		final String driver = SettingUtil.get("example/example").getStrByGroup("key", "demo");
		Assertions.assertEquals("value", driver);
	}

	@Test
	public void getFirstFoundTest() {
		//noinspection ConstantConditions
		final String driver = SettingUtil.getFirstFound("test2", "test")
				.getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
