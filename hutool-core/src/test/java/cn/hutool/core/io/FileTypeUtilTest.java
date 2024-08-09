package cn.hutool.core.io;

import cn.hutool.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * 文件类型判断单元测试
 * @author Looly
 *
 */
public class FileTypeUtilTest {

	@Test
	@Disabled
	public void fileTypeUtilTest() {
		final File file = FileUtil.file("hutool.jpg");
		final String type = FileTypeUtil.getType(file);
		assertEquals("jpg", type);

		FileTypeUtil.putFileType("ffd8ffe000104a464946", "new_jpg");
		final String newType = FileTypeUtil.getType(file);
		assertEquals("new_jpg", newType);
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
	public void ofdTest() {
		final File file = FileUtil.file("e:/test.ofd");
		final String hex = IoUtil.readHex64Upper(FileUtil.getInputStream(file));
		Console.log(hex);
		final String type = FileTypeUtil.getType(file);
		Console.log(type);
		assertEquals("ofd", type);
	}


	@Test
	@Disabled
	public void inputStreamAndFilenameTest() {
		final File file = FileUtil.file("e:/laboratory/test.xlsx");
		final String type = FileTypeUtil.getType(file);
		assertEquals("xlsx", type);
	}

	@Test
	@Disabled
	public void getTypeFromInputStream() throws IOException {
		final File file = FileUtil.file("d:/test/pic.jpg");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		inputStream.mark(0);
		final String type = FileTypeUtil.getType(inputStream);

		inputStream.reset();
	}

	@Test
	@Disabled
	public void webpTest(){
		// https://gitee.com/dromara/hutool/issues/I5BGTF
		final File file = FileUtil.file("d:/test/a.webp");
		final BufferedInputStream inputStream = FileUtil.getInputStream(file);
		final String type = FileTypeUtil.getType(inputStream);
		Console.log(type);
	}

	@Test
	public void issueI6MACITest() {
		final File file = FileUtil.file("text.txt");
		final String type = FileTypeUtil.getType(file);
		assertEquals("txt", type);
	}

	@Test
	@Disabled
	public void issue3024Test() {
		String x = FileTypeUtil.getType(FileUtil.getInputStream("d:/test/TEST_WPS_DOC.doc"),true);
		System.out.println(x);
	}
}
