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

package org.dromara.hutool.setting.props;

import org.dromara.hutool.setting.props.PropsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class PropsUtilTest {

	@Test
	public void getTest() {
		final String driver = PropsUtil.get("test").getStr("driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getFirstFoundTest() {
		final String driver = Objects.requireNonNull(PropsUtil.getFirstFound("test2", "test")).getStr("driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
