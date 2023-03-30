package cn.hutool.core.io;

import cn.hutool.core.io.file.FileTypeUtil;
import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
		Assertions.assertEquals("jpg", type);
	}

	@Test
	public void customTypeTest() {
		final File file = FileUtil.file("hutool.jpg");
		final String type = FileTypeUtil.getType(file);
		Assertions.assertEquals("jpg", type);

		final String oldType = FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
		Assertions.assertNull(oldType);

		final String newType = FileTypeUtil.getType(file);
		Assertions.assertEquals("new_jpg", newType);

		FileTypeUtil.removeFileType("ffd8ffe000104a464946");
		final String type2 = FileTypeUtil.getType(file);
		Assertions.assertEquals("jpg", type2);
	}

	@Test
	@Disabled
	public void emptyTest() {
		final File file = FileUtil.file("d:/empty.txt");
		final String type = FileTypeUtil.getType(file);
		Console.log(type);
	}

	@Test
	@Disabled
	public void docTest() {
		final File file = FileUtil.file("f:/test/test.doc");
		final String type = FileTypeUtil.getType(file);
		Console.log(type);
	}

	@Test
	@Disabled
	public void inputStreamAndFilenameTest() {
		final File file = FileUtil.file("e:/laboratory/test.xlsx");
		final String type = FileTypeUtil.getType(file);
		Assertions.assertEquals("xlsx", type);
	}

	@Test
	@Disabled
	public void getTypeFromInputStream() throws IOException {
		final File file = FileUtil.file("d:/test/pic.jpg");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		inputStream.mark(0);
		final String type = FileTypeUtil.getType(inputStream);
		Assertions.assertEquals("jpg", type);

		inputStream.reset();
	}

	@Test
	@Disabled
	public void webpTest() {
		// https://gitee.com/dromara/hutool/issues/I5BGTF
		final File file = FileUtil.file("d:/test/a.webp");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		final String type = FileTypeUtil.getType(inputStream);
		Console.log(type);
	}

	@Test
	public void issueI6MACITest() {
		final File file = FileUtil.file("1.txt");
		final String type = FileTypeUtil.getType(file);
		Assertions.assertEquals("txt", type);
	}
}
