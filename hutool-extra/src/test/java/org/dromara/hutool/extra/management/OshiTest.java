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

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.management.oshi.CpuInfo;
import org.dromara.hutool.extra.management.oshi.OshiUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import oshi.software.os.OSProcess;

/**
 * 测试参考：<a href="https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java">https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java</a>
 */
public class OshiTest {

	@Test
	public void getMemoryTest() {
		final long total = OshiUtil.getMemory().getTotal();
		Assertions.assertTrue(total > 0);
	}

	@Test
	public void getCupInfo() {
		final CpuInfo cpuInfo = OshiUtil.getCpuInfo();
		Assertions.assertNotNull(cpuInfo);
	}

	@Test
	public void getCurrentProcessTest() {
		final OSProcess currentProcess = OshiUtil.getCurrentProcess();
		Assertions.assertEquals("java", currentProcess.getName());
	}

	@Test
	@Disabled
	public void getUsedTest() {
		int i = 0;
		while (i < 1000) {
			Console.log(OshiUtil.getCpuInfo().getUsed());
			i++;
		}
	}
}
