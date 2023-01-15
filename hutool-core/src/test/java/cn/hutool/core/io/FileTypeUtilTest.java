package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * 文件类型判断单元测试
 *
 * @author Looly
 */
public class FileTypeUtilTest {

	@Test
	public void getTypeTest() {
		final String type = FileTypeUtil.getType(ResourceUtil.getStream("hutool.jpg"));
		Assert.assertEquals("jpg", type);
	}

	@Test
	public void customTypeTest() {
		final File file = FileUtil.file("hutool.jpg");
		final String type = FileTypeUtil.getType(file);
		Assert.assertEquals("jpg", type);

		final String oldType = FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
		Assert.assertNull(oldType);

		final String newType = FileTypeUtil.getType(file);
		Assert.assertEquals("new_jpg", newType);

		FileTypeUtil.removeFileType("ffd8ffe000104a464946");
		final String type2 = FileTypeUtil.getType(file);
		Assert.assertEquals("jpg", type2);
	}

	@Test
	@Ignore
	public void emptyTest() {
		final File file = FileUtil.file("d:/empty.txt");
		final String type = FileTypeUtil.getType(file);
		Console.log(type);
	}

	@Test
	@Ignore
	public void docTest() {
		final File file = FileUtil.file("f:/test/test.doc");
		final String type = FileTypeUtil.getType(file);
		Console.log(type);
	}

	@Test
	@Ignore
	public void inputStreamAndFilenameTest() {
		final File file = FileUtil.file("e:/laboratory/test.xlsx");
		final String type = FileTypeUtil.getType(file);
		Assert.assertEquals("xlsx", type);
	}

	@Test
	@Ignore
	public void getTypeFromInputStream() throws IOException {
		final File file = FileUtil.file("d:/test/pic.jpg");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		inputStream.mark(0);
		final String type = FileTypeUtil.getType(inputStream);
		Assert.assertEquals("jpg", type);

		inputStream.reset();
	}

	@Test
	@Ignore
	public void webpTest() {
		// https://gitee.com/dromara/hutool/issues/I5BGTF
		final File file = FileUtil.file("d:/test/a.webp");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		final String type = FileTypeUtil.getType(inputStream);
		Console.log(type);
	}
}
