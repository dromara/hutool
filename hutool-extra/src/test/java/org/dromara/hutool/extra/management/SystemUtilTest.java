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

import org.dromara.hutool.core.util.SystemUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SystemUtilTest {

	@Test
	@Disabled
	public void dumpTest() {
		ManagementUtil.dumpSystemInfo();
	}

	@Test
	public void getCurrentPidTest() {
		final long pid = ManagementUtil.getCurrentPID();
		Assertions.assertTrue(pid > 0);
	}

	@Test
	public void getJavaInfoTest() {
		final JavaInfo javaInfo = ManagementUtil.getJavaInfo();
		Assertions.assertNotNull(javaInfo);
	}

	@Test
	public void getJavaRuntimeInfoTest() {
		final JavaRuntimeInfo info = ManagementUtil.getJavaRuntimeInfo();
		Assertions.assertNotNull(info);
	}

	@Test
	public void getOsInfoTest() {
		final OsInfo osInfo = ManagementUtil.getOsInfo();
		Assertions.assertNotNull(osInfo);
	}

	@Test
	public void getHostInfo() {
		final HostInfo hostInfo = ManagementUtil.getHostInfo();
		Assertions.assertNotNull(hostInfo);
	}

	@Test
	public void getUserInfoTest(){
		// https://gitee.com/dromara/hutool/issues/I3NM39
		final UserInfo userInfo = ManagementUtil.getUserInfo();
		Assertions.assertTrue(userInfo.getTempDir().endsWith(File.separator));
	}

	@Test
	public void getOsVersionTest(){
		String s = SystemUtil.get("os.name");
		Assertions.assertNotNull(s);
		s = SystemUtil.get("os.version");
		Assertions.assertNotNull(s);
	}
}
