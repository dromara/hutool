package cn.hutool.extra.ssh;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.BeforeAll;
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
@Disabled
public class SftpTest {

	private static SshjSftp sshjSftp;

	@BeforeAll
	public static void init() {
		sshjSftp = new SshjSftp("ip", 22, "test", "test", CharsetUtil.CHARSET_UTF_8);
	}

	@Test
	public void lsTest() {
		List<String> files = sshjSftp.ls("/");
		if (files != null && !files.isEmpty()) {
			files.forEach(System.out::print);
		}
	}

	@Test
	public void downloadTest() {
		sshjSftp.recursiveDownloadFolder("/home/test/temp", new File("C:\\Users\\akwangl\\Downloads\\temp"));
	}

	@Test
	public void uploadTest() {
		sshjSftp.upload("/home/test/temp/", new File("C:\\Users\\akwangl\\Downloads\\temp\\辽宁_20190718_104324.CIME"));
	}

	@Test
	public void mkDirTest() {
		boolean flag = sshjSftp.mkdir("/home/test/temp");
		System.out.println("是否创建成功: " + flag);
	}

	@Test
	public void mkDirsTest() {
		// 在当前目录下批量创建目录
		sshjSftp.mkDirs("/home/test/temp");
	}

	@Test
	public void delDirTest() {
		sshjSftp.delDir("/home/test/temp");
	}
}
