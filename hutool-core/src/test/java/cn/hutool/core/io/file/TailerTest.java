package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;

public class TailerTest {

	@Test
	@Ignore
	public void tailTest() {
		FileUtil.tail(FileUtil.file("e:/tail.txt"), CharsetUtil.CHARSET_GBK);
	}

	@Test
	@Ignore
	public void tailWithLinesTest() {
		Tailer tailer = new Tailer(FileUtil.file("f:/test/test.log"), Tailer.CONSOLE_HANDLER, 2);
		tailer.start();
	}

	@Test
	@Ignore
	public void testDir() {
		System.out.println(FileUtil.getClassDir(FileUtil.class));
		System.out.println(FileUtil.getClassDir(Data.class));
		System.out.println(FileUtil.getClassDir(Test.class));
	}
}
