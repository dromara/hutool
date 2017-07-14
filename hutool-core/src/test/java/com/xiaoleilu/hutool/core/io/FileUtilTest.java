package com.xiaoleilu.hutool.core.io;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;

public class FileUtilTest {
	
	@Test
	public void getAbsolutePathTest(){
		String absolutePath = FileUtil.getAbsolutePath("aaa");
		Assert.assertNotNull(absolutePath);
	}

	@Test
	public void copyTest() throws Exception {
		String dir = FileUtilTest.class.getResource("/").getFile();
		File srcFile = FileUtil.file(dir, "hutool.jpg");
		File destFile = FileUtil.file(dir, "hutool.copy.jpg");

		FileUtil.copy(srcFile, destFile, true);
	}
}
