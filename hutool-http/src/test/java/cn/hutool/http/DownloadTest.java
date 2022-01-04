package cn.hutool.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

/**
 * 下载单元测试
 *
 * @author looly
 */
public class DownloadTest {

	@Test
	@Disabled
	public void downloadPicTest() {
		String url = "http://wx.qlogo.cn/mmopen/vKhlFcibVUtNBVDjcIowlg0X8aJfHXrTNCEFBukWVH9ta99pfEN88lU39MKspCUCOP3yrFBH3y2NbV7sYtIIlon8XxLwAEqv2/0";
		HttpUtil.downloadFile(url, "e:/pic/t3.jpg");
		Console.log("ok");
	}

	@Test
	@Disabled
	public void downloadSizeTest() {
		String url = "https://res.t-io.org/im/upload/img/67/8948/1119501/88097554/74541310922/85/231910/366466 - 副本.jpg";
		HttpRequest.get(url).setSSLProtocol("TLSv1.2").executeAsync().writeBody("e:/pic/366466.jpg");
	}

	@Test
	@Disabled
	public void downloadTest1() {
		long size = HttpUtil.downloadFile("http://explorer.bbfriend.com/crossdomain.xml", "e:/temp/");
		System.out.println("Download size: " + size);
	}

	@Test
	@Disabled
	public void downloadTest() {
		// 带进度显示的文件下载
		HttpUtil.downloadFile("http://mirrors.sohu.com/centos/7/isos/x86_64/CentOS-7-x86_64-DVD-1810.iso", FileUtil.file("d:/"), new StreamProgress() {

			final long time = System.currentTimeMillis();

			@Override
			public void start() {
				Console.log("开始下载。。。。");
			}

			@Override
			public void progress(long progressSize) {
				long speed = progressSize / (System.currentTimeMillis() - time) * 1000;
				Console.log("已下载：{}, 速度：{}/s", FileUtil.readableFileSize(progressSize), FileUtil.readableFileSize(speed));
			}

			@Override
			public void finish() {
				Console.log("下载完成！");
			}
		});
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest1() {
		File file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", "d:/download/temp");
		Assertions.assertNotNull(file);
		Assertions.assertTrue(file.isFile());
		Assertions.assertTrue(file.length() > 0);
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest2() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.4.0/hutool-all-5.4.0-sources.jar", FileUtil.file("d:/download/temp"), 1, new StreamProgress() {
				@Override
				public void start() {
					System.out.println("start");
				}

				@Override
				public void progress(long progressSize) {
					System.out.println("download size:" + progressSize);
				}

				@Override
				public void finish() {
					System.out.println("end");
				}
			});

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertTrue(file.getName().length() > 0);
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IORuntimeException);
		} finally {
			FileUtil.del(file);
		}
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest3() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.4.0/hutool-all-5.4.0-sources.jar", FileUtil.file("d:/download/temp"), new StreamProgress() {
				@Override
				public void start() {
					System.out.println("start");
				}

				@Override
				public void progress(long progressSize) {
					System.out.println("download size:" + progressSize);
				}

				@Override
				public void finish() {
					System.out.println("end");
				}
			});

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertTrue(file.getName().length() > 0);
		} finally {
			FileUtil.del(file);
		}
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest4() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp"), 1);

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertTrue(file.getName().length() > 0);
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IORuntimeException);
		} finally {
			FileUtil.del(file);
		}
	}


	@Test
	@Disabled
	public void downloadFileFromUrlTest5() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp", UUID.randomUUID().toString()));

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
		} finally {
			FileUtil.del(file);
		}

		File file1 = null;
		try {
			file1 = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp"));

			Assertions.assertNotNull(file1);
			Assertions.assertTrue(file1.exists());
			Assertions.assertTrue(file1.isFile());
			Assertions.assertTrue(file1.length() > 0);
		} finally {
			FileUtil.del(file1);
		}
	}
}
