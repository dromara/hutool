package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.func.SerConsumer;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;

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
		try (final BufferedReader reader = ResourceUtil.getUtf8Reader("test_lines.csv")) {
			IoUtil.readLines(reader, (SerConsumer<String>) Assert::assertNotNull);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
