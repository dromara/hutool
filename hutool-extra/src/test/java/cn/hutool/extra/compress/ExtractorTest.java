package cn.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class ExtractorTest {

	@Test
	@Ignore
	public void zipTest() {
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/c_1344112734760931330_20201230104703032.zip"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Ignore
	public void sevenZTest() {
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/compress/test.7z"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Ignore
	public void tgzTest() {
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				"tgz",
				FileUtil.file("d:/test/test.tgz"));

		extractor.extract(FileUtil.file("d:/test/tgz/"));
	}

	@Test
	@Ignore
	public void sevenZTest2() {
		File targetDir = FileUtil.file("d:/test/sevenZ2/");
		FileUtil.clean(targetDir);
		//
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("D:/System-Data/Downloads/apache-tomcat-10.0.27.7z"));

		extractor.extract(targetDir, 1);
	}

	@Test
	@Ignore
	public void zipTest2() {
		File targetDir = FileUtil.file("d:/test/zip2/");
		FileUtil.clean(targetDir);
		//
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("D:/System-Data/Downloads/apache-tomcat-10.0.27.zip"));

		extractor.extract(targetDir, 1);
	}
}
