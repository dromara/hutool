package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.FileResource;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ZipWriterTest {

	@Test
	@Disabled
	public void zipDirTest() {
		ZipUtil.zip(new File("d:/test"));
	}

	@Test
	@Disabled
	public void addTest(){
		final ZipWriter writer = ZipWriter.of(FileUtil.file("d:/test/test.zip"), CharsetUtil.UTF_8);
		writer.add(new FileResource("d:/test/qr_c.png"));
		writer.close();
	}
}
