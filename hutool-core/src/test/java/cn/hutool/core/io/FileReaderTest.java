package cn.hutool.core.io;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.io.file.FileReader;

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
		Assert.assertNotNull(result);
	}

	@Test
	public void readLinesTest() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines();
		Assert.assertEquals(6, strings.size());
	}

	@Test
	public void readLinesTest2() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines(new ArrayList<>(), StrUtil::isNotBlank);
		Assert.assertEquals(5, strings.size());
	}
}
