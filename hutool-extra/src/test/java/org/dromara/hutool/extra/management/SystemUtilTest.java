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
