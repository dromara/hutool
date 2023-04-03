package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TailerTest {

	@Test
	@Disabled
	public void tailTest() {
		FileUtil.tail(FileUtil.file("d:/test/tail.txt"), CharsetUtil.GBK);
	}

	@Test
	@Disabled
	public void tailWithLinesTest() {
		final Tailer tailer = new Tailer(FileUtil.file("f:/test/test.log"), Tailer.CONSOLE_HANDLER, 2);
		tailer.start();
	}
}
