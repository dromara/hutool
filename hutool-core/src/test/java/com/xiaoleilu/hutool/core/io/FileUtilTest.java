package com.xiaoleilu.hutool.core.io;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;

public class FileUtilTest {
	
	@Test
	public void getAbsolutePathTest(){
		String absolutePath = FileUtil.getAbsolutePath("aaa");
		Assert.assertNotNull(absolutePath);
	}
}
