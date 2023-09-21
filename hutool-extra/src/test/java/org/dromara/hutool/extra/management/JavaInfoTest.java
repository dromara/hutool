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

package org.dromara.hutool.extra.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @see JavaInfo
 */
public class JavaInfoTest {

	@Test
	public void isJavaVersionAtLeastTest() {
		final int versionInt = ManagementUtil.getJavaInfo().getVersionIntSimple();
		Assertions.assertTrue(versionInt >= 8);
		final boolean javaVersionAtLeast1 = ManagementUtil.getJavaInfo().isJavaVersionAtLeast(1.8f);
		Assertions.assertTrue(javaVersionAtLeast1);
	}
}
