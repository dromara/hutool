/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RuntimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 命令行单元测试
 * @author looly
 *
 */
public class RuntimeUtilTest {

	@Test
	@Disabled
	public void execTest() {
		final String str = RuntimeUtil.execForStr("ipconfig");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest() {
		final String str = RuntimeUtil.execForStr("cmd /c dir");
		Console.log(str);
	}

	@Test
	@Disabled
	public void execCmdTest2() {
		final String str = RuntimeUtil.execForStr("cmd /c", "cd \"C:\\Program Files (x86)\"", "chdir");
		Console.log(str);
	}

	@Test
	public void getUsableMemoryTest(){
		Assertions.assertTrue(RuntimeUtil.getUsableMemory() > 0);
	}

	@Test
	public void getPidTest(){
		final int pid = RuntimeUtil.getPid();
		Assertions.assertTrue(pid > 0);
	}

	@Test
	public void getProcessorCountTest(){
		final int cpu = RuntimeUtil.getProcessorCount();
		Assertions.assertTrue(cpu > 0);
	}

	@Test
	@Disabled
	void pingTest() {
		final String s = RuntimeUtil.execForStr("cmd /c ping " + "hutool.cn");
		Console.log(s);
	}
}
