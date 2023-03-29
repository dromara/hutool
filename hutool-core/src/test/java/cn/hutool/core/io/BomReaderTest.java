package cn.hutool.core.io;

import cn.hutool.core.io.file.FileUtil;
import org.junit.Assert;
import org.junit.Test;

public class BomReaderTest {
	@Test
	public void readTest() {
		final BomReader bomReader = FileUtil.getBOMReader(FileUtil.file("with_bom.txt"));
		final String read = IoUtil.read(bomReader, true);
		Assert.assertEquals("此文本包含BOM头信息，用于测试BOM头读取", read);
	}
}
