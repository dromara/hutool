package org.dromara.hutool.io;

import org.dromara.hutool.io.file.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BomReaderTest {
	@Test
	public void readTest() {
		final BomReader bomReader = FileUtil.getBOMReader(FileUtil.file("with_bom.txt"));
		final String read = IoUtil.read(bomReader, true);
		Assertions.assertEquals("此文本包含BOM头信息，用于测试BOM头读取", read);
	}
}
