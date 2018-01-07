package cn.hutool.core.io;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;

/**
 * 文件类型判断单元测试
 * @author Looly
 *
 */
public class FileTypeUtilTest {
	
	@Test
	public void fileTypeUtilTest() {
		File file = FileUtil.file("hutool.jpg");
		String type = FileTypeUtil.getType(file);
		Assert.assertEquals("jpg", type);
		
		FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
		String newType = FileTypeUtil.getType(file);
		Assert.assertEquals("new_jpg", newType);
	}
	
	@Test
	@Ignore
	public void emptyTest() {
		File file = FileUtil.file("d:/empty.txt");
		String type = FileTypeUtil.getType(file);
		Console.log(type);
	}
}
