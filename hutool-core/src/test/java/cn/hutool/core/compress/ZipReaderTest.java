package cn.hutool.core.compress;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ZipUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ZipReaderTest {

	@Test
	@Disabled
	public void unzipTest() {
		File unzip = ZipUtil.unzip("d:/java.zip", "d:/test/java");
		Console.log(unzip);
	}
}
