package cn.hutool.core.util;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;

/**
 * {@link ZipUtil}单元测试
 * @author Looly
 *
 */
public class ZipUtilTest {
	
	
	@Test
	@Ignore
	public void zipDirTest() {
		ZipUtil.zip(new File("e:/picTest/picSubTest"));
	}
	
	@Test
	@Ignore
	public void unzipTest() {
		File unzip = ZipUtil.unzip("f:/test/apache-maven-3.6.2.zip", "f:\\test");
		Console.log(unzip);
	}
	
	@Test
	@Ignore
	public void unzipTest2() {
		File unzip = ZipUtil.unzip("f:/test/各种资源.zip", "f:/test/各种资源", CharsetUtil.CHARSET_GBK);
		Console.log(unzip);
	}
	
	@Test
	@Ignore
	public void unzipFromStreamTest() {
		File unzip = ZipUtil.unzip(FileUtil.getInputStream("e:/test/antlr.zip"), FileUtil.file("e:/test/"), CharsetUtil.CHARSET_UTF_8);
		Console.log(unzip);
	}
	
	@Test
	@Ignore
	public void unzipChineseTest() {
		ZipUtil.unzip("d:/测试.zip");
	}
	
	@Test
	@Ignore
	public void unzipFileBytesTest() {
		byte[] fileBytes = ZipUtil.unzipFileBytes(FileUtil.file("e:/02 电力相关设备及服务2-241-.zip"), CharsetUtil.CHARSET_GBK, "images/CE-EP-HY-MH01-ES-0001.jpg");
		Assert.assertNotNull(fileBytes);
	}
	
	@Test
	public void gzipTest() {
		String data = "我是一个需要压缩的很长很长的字符串";
		byte[] bytes = StrUtil.utf8Bytes(data);
		byte[] gzip = ZipUtil.gzip(bytes);
		
		//保证gzip长度正常
		Assert.assertEquals(68, gzip.length);
		
		byte[] unGzip = ZipUtil.unGzip(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip));
	}
	
	@Test
	public void zlibTest() {
		String data = "我是一个需要压缩的很长很长的字符串";
		byte[] bytes = StrUtil.utf8Bytes(data);
		byte[] gzip = ZipUtil.zlib(bytes, 0);
		
		//保证zlib长度正常
		Assert.assertEquals(62, gzip.length);
		byte[] unGzip = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip));
		
		gzip = ZipUtil.zlib(bytes, 9);
		//保证zlib长度正常
		Assert.assertEquals(56, gzip.length);
		byte[] unGzip2 = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip2));
	}
}
