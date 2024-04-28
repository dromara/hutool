package cn.hutool.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class IssueI9K494Test {
	@Test
	@Ignore
	public void unzipTest() {
		FileInputStream inputStream = IoUtil.toStream(FileUtil.file("d:/test/unzip5616889482468994725.zip"));
		ZipUtil.unzip(inputStream, FileUtil.file("d:/test/"), StandardCharsets.UTF_8);
	}

	@Test
	@Ignore
	public void unzipTest2() {
		FileInputStream inputStream = IoUtil.toStream(FileUtil.file("d:/test/test.zip"));
		ZipUtil.unzip(inputStream, FileUtil.file("d:/test/"), StandardCharsets.UTF_8);
	}
}
