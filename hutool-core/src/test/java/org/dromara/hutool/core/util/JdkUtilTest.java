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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.JdkUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JdkUtilTest {
	@Test
	public void jvmVersionTest() {
		final int jvmVersion = JdkUtil.JVM_VERSION;
		Assertions.assertTrue(jvmVersion >= 8);
	}

	@Test
	@Disabled
	public void getJvmNameTest() {
		Console.log(JdkUtil.IS_AT_LEAST_JDK17);
	}
}
