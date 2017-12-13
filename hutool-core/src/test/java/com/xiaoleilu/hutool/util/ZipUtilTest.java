package com.xiaoleilu.hutool.util;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.ZipUtil;

/**
 * {@link ZipUtil}单元测试
 * @author Looly
 *
 */
public class ZipUtilTest {
	
	@Test
	@Ignore
	public void zipDirTest() {
		ZipUtil.zip("d:/aaa/");
	}
	
	@Test
	@Ignore
	public void unzipTest() {
		File unzip = ZipUtil.unzip("E:\\aaa\\RongGenetor V1.0.0.zip", "e:\\aaa");
		Console.log(unzip);
		File unzip2 = ZipUtil.unzip("E:\\aaa\\RongGenetor V1.0.0.zip", "e:\\aaa");
		Console.log(unzip2);
	}
	
	@Test
	@Ignore
	public void unzipChineseTest() {
		ZipUtil.unzip("d:/中文.zip", CharsetUtil.CHARSET_GBK);
	}
}
