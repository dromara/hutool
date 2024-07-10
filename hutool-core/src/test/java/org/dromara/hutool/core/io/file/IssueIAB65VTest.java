package org.dromara.hutool.core.io.file;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/IAB65V
 */
public class IssueIAB65VTest {
	@Test
	public void getAbsolutePathTest() {
		final String path = "D:\\test\\personal\n";

		final File file = FileUtil.file(path);
		if(FileUtil.isWindows()){
			// 换行符自动去除
			assertEquals("D:\\test\\personal", file.toString());
		}
	}
}
