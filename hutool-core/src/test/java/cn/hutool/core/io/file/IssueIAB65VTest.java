package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/IAB65V
 */
public class IssueIAB65VTest {
	@Test
	public void getAbsolutePathTest() {
		String path = "D:\\test\\personal\n";

		File file = FileUtil.file(path);
		if(FileUtil.isWindows()){
			// 换行符自动去除
			assertEquals("D:\\test\\personal", file.toString());
		}
	}
}
