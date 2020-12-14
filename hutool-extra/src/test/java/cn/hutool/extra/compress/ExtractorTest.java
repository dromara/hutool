package cn.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import org.junit.Ignore;
import org.junit.Test;

public class ExtractorTest {

	@Test
	@Ignore
	public void zipTest(){
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/compress/test.zip"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Ignore
	public void sevenZTest(){
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/compress/test.7z"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}
}
