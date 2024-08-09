package cn.hutool.core.io.file;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FileNameUtilTest {

	@Test
	public void cleanInvalidTest(){
		String name = FileNameUtil.cleanInvalid("1\n2\n");
		assertEquals("12", name);

		name = FileNameUtil.cleanInvalid("\r1\r\n2\n");
		assertEquals("12", name);
	}

	@Test
	public void mainNameTest() {
		final String s = FileNameUtil.mainName("abc.tar.gz");
		assertEquals("abc", s);
	}
}
