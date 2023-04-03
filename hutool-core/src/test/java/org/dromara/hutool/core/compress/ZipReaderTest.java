package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ZipReaderTest {

	@Test
	@Disabled
	public void unzipTest() {
		final File unzip = ZipUtil.unzip("d:/java.zip", "d:/test/java");
		Console.log(unzip);
	}
}
