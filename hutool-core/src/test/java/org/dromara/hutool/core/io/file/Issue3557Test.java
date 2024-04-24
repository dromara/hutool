package org.dromara.hutool.core.io.file;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.StandardCopyOption;

public class Issue3557Test {
	@Test
	@Disabled
	public void copyFileTest() {
		// 如果只是文件不存在，则不会报错
		// 如果文件所在目录不存在，则会报错
		FileUtil.copy(FileUtil.getInputStream("d:/test/aaa.xlsx"), FileUtil.file("d:/test2/aaa_copy.xlsx"), StandardCopyOption.REPLACE_EXISTING);
	}

	@Test
	@Disabled
	public void copyFileTest2() {
		FileUtil.copy(FileUtil.file("d:/test/aaa.xlsx"), FileUtil.file("d:/test2/aaa_copy.xlsx"), true);
	}
}
