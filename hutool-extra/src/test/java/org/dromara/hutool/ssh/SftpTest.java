package org.dromara.hutool.ssh;

import org.dromara.hutool.util.CharsetUtil;
import org.junit.Before;
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

	@Before
	@Disabled
	public void init() {
		sshjSftp = new SshjSftp("ip", 22, "test", "test", CharsetUtil.UTF_8);
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
