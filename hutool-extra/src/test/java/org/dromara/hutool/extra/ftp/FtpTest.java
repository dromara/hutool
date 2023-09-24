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
import org.dromara.hutool.extra.ssh.engine.jsch.JschSftp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FtpTest {

	@Test
	@Disabled
	public void ftpsTest() {
		final FTPSClient ftpsClient = new FTPSClient();
		final Ftp ftp = new Ftp(ftpsClient);

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void cdTest() {
		final Ftp ftp = new Ftp("looly.centos");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void uploadTest() {
		final Ftp ftp = new Ftp("localhost");

		final boolean upload = ftp.uploadFile("/temp", FileUtil.file("d:/test/test.zip"));
		Console.log(upload);

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void reconnectIfTimeoutTest() throws InterruptedException {
		final Ftp ftp = new Ftp("looly.centos");

		Console.log("打印pwd: " + ftp.pwd());

		Console.log("休眠一段时间，然后再次发送pwd命令，抛出异常表明连接超时");
		Thread.sleep(35 * 1000);

		try{
			Console.log("打印pwd: " + ftp.pwd());
		}catch (final FtpException e) {
			e.printStackTrace();
		}

		Console.log("判断是否超时并重连...");
		ftp.reconnectIfTimeout();

		Console.log("打印pwd: " + ftp.pwd());

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolder() {
		final Ftp ftp = new Ftp("looly.centos");
		ftp.recursiveDownloadFolder("/",FileUtil.file("d:/test/download"));

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void recursiveDownloadFolderSftp() {
		final JschSftp ftp = new JschSftp("127.0.0.1", 22, "test", "test");

		ftp.cd("/file/aaa");
		Console.log(ftp.pwd());
		ftp.recursiveDownloadFolder("/",FileUtil.file("d:/test/download"));

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void downloadTest() {
		final Ftp ftp = new Ftp("localhost");

		final List<String> fileNames = ftp.ls("temp/");
		for(final String name: fileNames) {
			ftp.download("",
					name,
					FileUtil.file("d:/test/download/" + name));
		}

		IoUtil.closeQuietly(ftp);
	}

	@Test
	@Disabled
	public void isDirTest() throws Exception {
		try (final Ftp ftp = new Ftp("127.0.0.1", 21)) {
			Console.log(ftp.pwd());
			ftp.isDir("/test");
			Console.log(ftp.pwd());
		}
	}

	@Test
	@Disabled
	public void existSftpTest() {
		try (final JschSftp ftp = new JschSftp("127.0.0.1", 22, "test", "test")) {
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
		try (final Ftp ftp = new Ftp("127.0.0.1", 21)) {
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
