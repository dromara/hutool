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
