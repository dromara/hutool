package cn.hutool.core.io;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * 文件类型判断单元测试
 * @author Looly
 *
 */
public class FileTypeUtilTest {

	@Test
	@Ignore
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

	@Test
	@Ignore
	public void docTest() {
		File file = FileUtil.file("f:/test/test.doc");
		String type = FileTypeUtil.getType(file);
		Console.log(type);
	}

	@Test
	@Ignore
	public void ofdTest() {
		File file = FileUtil.file("e:/test.ofd");
		String hex = IoUtil.readHex28Upper(FileUtil.getInputStream(file));
		Console.log(hex);
		String type = FileTypeUtil.getType(file);
		Console.log(type);
		Assert.assertEquals("ofd", type);
	}


	@Test
	@Ignore
	public void inputStreamAndFilenameTest() {
		File file = FileUtil.file("e:/laboratory/test.xlsx");
		String type = FileTypeUtil.getType(file);
		Assert.assertEquals("xlsx", type);
	}

	@Test
	@Ignore
	public void getTypeFromInputStream() throws IOException {
		File file = FileUtil.file("d:/test/pic.jpg");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		inputStream.mark(0);
		String type = FileTypeUtil.getType(inputStream);

		inputStream.reset();
	}

	@Test
	@Ignore
	public void webpTest(){
		// https://gitee.com/dromara/hutool/issues/I5BGTF
		final File file = FileUtil.file("d:/test/a.webp");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		final String type = FileTypeUtil.getType(inputStream);
		Console.log(type);
	}

}
