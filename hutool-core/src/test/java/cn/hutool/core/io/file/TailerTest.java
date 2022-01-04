package cn.hutool.core.io.file;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;

public class TailerTest {

	@Test
	@Disabled
	public void tailTest() {
		FileUtil.tail(FileUtil.file("d:/test/tail.txt"), CharsetUtil.CHARSET_GBK);
	}

	@Test
	@Disabled
	public void tailWithLinesTest() {
		Tailer tailer = new Tailer(FileUtil.file("f:/test/test.log"), Tailer.CONSOLE_HANDLER, 2);
		tailer.start();
	}
}
