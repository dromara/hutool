package cn.hutool.core.io.file;

import org.junit.Assert;
import org.junit.Test;

public class FileNameUtilTest {

	@Test
	public void cleanInvalidTest(){
		String name = FileNameUtil.cleanInvalid("1\n2\n");
		Assert.assertEquals("12", name);

		name = FileNameUtil.cleanInvalid("\r1\r\n2\n");
		Assert.assertEquals("12", name);
	}
}
