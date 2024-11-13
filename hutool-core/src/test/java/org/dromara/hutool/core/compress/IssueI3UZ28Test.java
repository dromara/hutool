package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI3UZ28Test {
	@Test
	@Disabled
	void unzipWithGBKTest() {
		ZipUtil.unzip(FileUtil.file("d:/test/test.zip"));
	}
}
