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

package org.dromara.hutool.extra.ssh;

import org.dromara.hutool.extra.ssh.engine.sshj.SshjSftp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * 基于sshj 框架SFTP 封装测试.
 *
 * @author youyongkun
 * @since 5.7.18
 */
public class SftpTest {

	private SshjSftp sshjSftp;

	@BeforeEach
	@Disabled
	public void init() {
		sshjSftp = SshjSftp.of("ip", 22, "test", "test");
	}

	@Test
	@Disabled
	public void lsTest() {
		final List<String> files = sshjSftp.ls("/");
		if (files != null && !files.isEmpty()) {
			files.forEach(System.out::print);
		}
	}

	@Test
	@Disabled
	public void downloadTest() {
		sshjSftp.recursiveDownloadFolder("/home/test/temp", new File("C:\\Users\\akwangl\\Downloads\\temp"));
	}

	@Test
	@Disabled
	public void uploadTest() {
		sshjSftp.uploadFile("/home/test/temp/", new File("C:\\Users\\akwangl\\Downloads\\temp\\辽宁_20190718_104324.CIME"));
	}

	@Test
	@Disabled
	public void mkDirTest() {
		final boolean flag = sshjSftp.mkdir("/home/test/temp");
		System.out.println("是否创建成功: " + flag);
	}

	@Test
	@Disabled
	public void mkDirsTest() {
		// 在当前目录下批量创建目录
		sshjSftp.mkDirs("/home/test/temp");
	}

	@Test
	@Disabled
	public void delDirTest() {
		sshjSftp.delDir("/home/test/temp");
	}
}
