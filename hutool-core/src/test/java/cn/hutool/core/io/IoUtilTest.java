package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;

public class IoUtilTest {

	@Test
	public void readBytesTest(){
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"));
		Assert.assertEquals(22807, bytes.length);
	}

	@Test
	public void readLinesTest(){
		try(BufferedReader reader = ResourceUtil.getUtf8Reader("test_lines.csv");){
			IoUtil.readLines(reader, (LineHandler) Assert::assertNotNull);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
