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

package org.dromara.hutool.extra.ftp;

import org.apache.commons.net.ftp.FTPSClient;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.ssh.engine.jsch.JschSftp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class FtpTest {

	@Test
	@Disabled
	public void ftpsTest() {
		final FTPSClient ftpsClient = new FTPSClient();
		final CommonsFtp ftp = new CommonsFtp(ftpsClient);

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void cdTest() {
		final CommonsFtp ftp = CommonsFtp.of("looly.centos");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void uploadTest() {
		final CommonsFtp ftp = CommonsFtp.of("localhost");

		final boolean upload = ftp.uploadFile("/temp", FileUtil.file("d:/test/test.zip"));
		Console.log(upload);

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void reconnectIfTimeoutTest() throws InterruptedException {
		final CommonsFtp ftp = CommonsFtp.of("looly.centos");

		Console.log("打印pwd: " + ftp.pwd());

		Console.log("休眠一段时间，然后再次发送pwd命令，抛出异常表明连接超时");
		Thread.sleep(35 * 1000);

		try {
			Console.log("打印pwd: " + ftp.pwd());
		} catch (final FtpException e) {
			Console.error(e, e.getMessage());
		}

		Console.log("判断是否超时并重连...");
		ftp.reconnectIfTimeout();

		Console.log("打印pwd: " + ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolder() {
		final CommonsFtp ftp = CommonsFtp.of("looly.centos");
		ftp.recursiveDownloadFolder("/", FileUtil.file("d:/test/download"));

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolderSftp() {
		final JschSftp ftp = JschSftp.of("127.0.0.1", 22, "test", "test");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());
		ftp.recursiveDownloadFolder("/", FileUtil.file("d:/test/download"));

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void downloadTest() {
		final CommonsFtp ftp = CommonsFtp.of("localhost");

		final List<String> fileNames = ftp.ls("temp/");
		for (final String name : fileNames) {
			ftp.download("",
				name,
				FileUtil.file("d:/test/download/" + name));
		}

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void isDirTest() throws Exception {
		try (final CommonsFtp ftp = CommonsFtp.of("127.0.0.1", 21)) {
			Console.log(ftp.pwd());
			ftp.isDir("/test");
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void readTest() throws Exception {
		try (final CommonsFtp ftp = CommonsFtp.of("localhost");
			 final BufferedReader reader = new BufferedReader(new InputStreamReader(ftp.getFileStream("d://test/read/", "test.txt")))) {
			String line;
			while (StrUtil.isNotBlank(line = reader.readLine())) {
				Console.log(line);
			}
		}
	}

	@Test
	@Disabled
	public void existSftpTest() {
		try (final JschSftp ftp = JschSftp.of("127.0.0.1", 22, "test", "test")) {
			Console.log(ftp.pwd());
			Console.log(ftp.exist(null));
			Console.log(ftp.exist(""));
			Console.log(ftp.exist("."));
			Console.log(ftp.exist(".."));
			Console.log(ftp.exist("/"));
			Console.log(ftp.exist("a"));
			Console.log(ftp.exist("/home/test"));
			Console.log(ftp.exist("/home/test/"));
			Console.log(ftp.exist("/home/test//////"));
			Console.log(ftp.exist("/home/test/file1"));
			Console.log(ftp.exist("/home/test/file1/"));
			Console.log(ftp.exist("///////////"));
			Console.log(ftp.exist("./"));
			Console.log(ftp.exist("./file1"));
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void existFtpTest() throws Exception {
		try (final CommonsFtp ftp = CommonsFtp.of("127.0.0.1", 21)) {
			Console.log(ftp.pwd());
			Console.log(ftp.exist(null));
			Console.log(ftp.exist(""));
			Console.log(ftp.exist("."));
			Console.log(ftp.exist(".."));
			Console.log(ftp.exist("/"));
			Console.log(ftp.exist("a"));
			Console.log(ftp.exist("/test"));
			Console.log(ftp.exist("/test/"));
			Console.log(ftp.exist("/test//////"));
			Console.log(ftp.exist("/test/.."));
			Console.log(ftp.exist("/test/."));
			Console.log(ftp.exist("/file1"));
			Console.log(ftp.exist("/file1/"));
			Console.log(ftp.exist("///////////"));
			Console.log(ftp.exist("./"));
			Console.log(ftp.exist("./file1"));
			Console.log(ftp.exist("./2/3/4/.."));
			Console.log(ftp.pwd());
		}
	}
}
