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
