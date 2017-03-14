package com.xiaoleilu.hutool.core.io;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileTypeUtil;
import com.xiaoleilu.hutool.io.FileUtil;

/**
 * 文件类型判断单元测试
 * @author Looly
 *
 */
public class FileTypeUtilTest {
	
	@Test
	public void fileTypeUtilTest() throws IOException{
		File file = FileUtil.file("hutool.jpg");
		String type = FileTypeUtil.getType(file);
		Assert.assertEquals("jpg", type);
		
		FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
		String newType = FileTypeUtil.getType(file);
		Assert.assertEquals("new_jpg", newType);
	}
}
