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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.lang.Console;
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

	@Test
	@Disabled
	public void issueIAB5LWTest() {
		final String s = RuntimeUtil.execForStr("cmd /c netstat -aon | findstr 8080");
		Console.log(s);
	}
}
