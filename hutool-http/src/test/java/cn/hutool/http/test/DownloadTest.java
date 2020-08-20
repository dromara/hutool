package cn.hutool.http.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

/**
 * 下载单元测试
 * 
 * @author looly
 */
public class DownloadTest {

	@Test
	@Ignore
	public void downloadPicTest() {
		String url = "http://wx.qlogo.cn/mmopen/vKhlFcibVUtNBVDjcIowlg0X8aJfHXrTNCEFBukWVH9ta99pfEN88lU39MKspCUCOP3yrFBH3y2NbV7sYtIIlon8XxLwAEqv2/0";
		HttpUtil.downloadFile(url, "e:/pic/t3.jpg");
		Console.log("ok");
	}

	@Test
	@Ignore
	public void downloadSizeTest() {
		String url = "https://res.t-io.org/im/upload/img/67/8948/1119501/88097554/74541310922/85/231910/366466 - 副本.jpg";
		HttpRequest.get(url).setSSLProtocol("TLSv1.2").executeAsync().writeBody("e:/pic/366466.jpg");
	}

	@Test
	@Ignore
	public void downloadTest1() {
		long size = HttpUtil.downloadFile("http://explorer.bbfriend.com/crossdomain.xml", "e:/temp/");
		System.out.println("Download size: " + size);
	}

	@Test
	@Ignore
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
	@Ignore
	public void downloadFileFromUrlTest1() {
		File file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", "d:/download/temp");
		Assert.assertNotNull(file);
		Assert.assertTrue(file.isFile());
		Assert.assertTrue(file.length() > 0);
	}
	
	@Test
	@Ignore
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
			
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertTrue(file.isFile());
			Assert.assertTrue(file.length() > 0);
			Assert.assertTrue(file.getName().length() > 0);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IORuntimeException);
		} finally {
			FileUtil.del(file);
		}
	}
	
	@Test
	@Ignore
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
			
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertTrue(file.isFile());
			Assert.assertTrue(file.length() > 0);
			Assert.assertTrue(file.getName().length() > 0);
		} finally {
			FileUtil.del(file);
		}
	}
	
	@Test
	@Ignore
	public void downloadFileFromUrlTest4() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp"), 1);
			
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertTrue(file.isFile());
			Assert.assertTrue(file.length() > 0);
			Assert.assertTrue(file.getName().length() > 0);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IORuntimeException);
		} finally {
			FileUtil.del(file);
		}
	}
	
	
	@Test
	@Ignore
	public void downloadFileFromUrlTest5() {
		File file = null;
		try {
			file = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp", UUID.randomUUID().toString()));
			
			Assert.assertNotNull(file);
			Assert.assertTrue(file.exists());
			Assert.assertTrue(file.isFile());
			Assert.assertTrue(file.length() > 0);
		} finally {
			FileUtil.del(file);
		}
		
		File file1 = null;
		try {
			file1 = HttpUtil.downloadFileFromUrl("http://groovy-lang.org/changelogs/changelog-3.0.5.html", FileUtil.file("d:/download/temp"));
			
			Assert.assertNotNull(file1);
			Assert.assertTrue(file1.exists());
			Assert.assertTrue(file1.isFile());
			Assert.assertTrue(file1.length() > 0);
		} finally {
			FileUtil.del(file1);
		}
	}
}
