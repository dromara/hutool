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

	@Test
	public void mainNameTest() {
		final String s = FileNameUtil.mainName("abc.tar.gz");
		Assert.assertEquals("abc", s);
	}

	@Test
	public void normalizeTest() {
		Assert.assertEquals("/foo/", FileNameUtil.normalize("/foo//"));
		Assert.assertEquals("/foo/", FileNameUtil.normalize("/foo/./"));
		Assert.assertEquals("/bar", FileNameUtil.normalize("/foo/../bar"));
		Assert.assertEquals("/bar/", FileNameUtil.normalize("/foo/../bar/"));
		Assert.assertEquals("/baz", FileNameUtil.normalize("/foo/../bar/../baz"));
		Assert.assertEquals("/", FileNameUtil.normalize("/../"));
		Assert.assertEquals("foo", FileNameUtil.normalize("foo/bar/.."));
		Assert.assertEquals("../bar", FileNameUtil.normalize("foo/../../bar"));
		Assert.assertEquals("bar", FileNameUtil.normalize("foo/../bar"));
		Assert.assertEquals("/server/bar", FileNameUtil.normalize("//server/foo/../bar"));
		Assert.assertEquals("/bar", FileNameUtil.normalize("//server/../bar"));
		Assert.assertEquals("C:/bar", FileNameUtil.normalize("C:\\foo\\..\\bar"));
		//
		Assert.assertEquals("C:/bar", FileNameUtil.normalize("C:\\..\\bar"));
		Assert.assertEquals("../../bar", FileNameUtil.normalize("../../bar"));
		Assert.assertEquals("C:/bar", FileNameUtil.normalize("/C:/bar"));
		Assert.assertEquals("C:", FileNameUtil.normalize("C:"));
		Assert.assertEquals("\\/192.168.1.1/Share/", FileNameUtil.normalize("\\\\192.168.1.1\\Share\\"));
	}

	@Test
	public void normalizeBlankTest() {
		Assert.assertEquals("C:/aaa ", FileNameUtil.normalize("C:\\aaa "));
	}
}
