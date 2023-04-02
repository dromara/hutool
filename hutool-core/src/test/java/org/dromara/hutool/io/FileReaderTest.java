package org.dromara.hutool.io;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.dromara.hutool.io.file.FileReader;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取测试
 * @author Looly
 *
 */
public class FileReaderTest {

	@Test
	public void fileReaderTest(){
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final String result = fileReader.readString();
		Assertions.assertNotNull(result);
	}

	@Test
	public void readLinesTest() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines();
		Assertions.assertEquals(6, strings.size());
	}

	@Test
	public void readLinesTest2() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines(new ArrayList<>(), StrUtil::isNotBlank);
		Assertions.assertEquals(5, strings.size());
	}
}
