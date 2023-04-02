package org.dromara.hutool.ofd;

import org.dromara.hutool.io.file.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class OfdWriterTest {

	@Test
	@Disabled
	public void writeTest(){
		final OfdWriter ofdWriter = new OfdWriter(FileUtil.file("d:/test/test.ofd"));
		ofdWriter.addText(null, "测试文本");
		ofdWriter.close();
	}
}
