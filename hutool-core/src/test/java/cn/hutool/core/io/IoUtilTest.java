package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class IoUtilTest {

	@Test
	public void readBytesTest() {
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"));
		Assert.assertEquals(22807, bytes.length);
	}

	@Test
	public void readBytesWithLengthTest() {
		// 读取固定长度
		final int limit = RandomUtil.randomInt(22807);
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"), limit);
		Assert.assertEquals(limit, bytes.length);
	}

	@Test
	public void readLinesTest() {
		try (BufferedReader reader = ResourceUtil.getUtf8Reader("test_lines.csv");) {
			IoUtil.readLines(reader, (LineHandler) Assert::assertNotNull);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Test
	public void copyToByteArrayTest() throws Exception {
		try(InputStream is1 = ResourceUtil.getStream("hutool.jpg");
			InputStream is2 = ResourceUtil.getStream("hutool.jpg")){
			byte[] copiedBytes = IoUtil.copyToByteArray(is1);
			byte[] readBytes = IoUtil.readBytes(is2);
			Assert.assertArrayEquals(readBytes, copiedBytes);
		}
	}

	@Test
	public void copyToStringTest() throws Exception {
		String str = "abc123";
		try(InputStream is1 = new ByteArrayInputStream(str.getBytes(Charset.defaultCharset()));
			InputStream is2 = new ByteArrayInputStream(str.getBytes(Charset.defaultCharset()))){
			String copiedString = IoUtil.copyToString(is1, Charset.defaultCharset());
			String readString = IoUtil.read(is2, Charset.defaultCharset());
			Assert.assertEquals(readString, copiedString);
		}


	}

}
