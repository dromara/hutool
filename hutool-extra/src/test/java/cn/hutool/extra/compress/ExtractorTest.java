package cn.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ExtractorTest {

	@Test
	@Disabled
	public void zipTest(){
		Extractor extractor = CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/c_1344112734760931330_20201230104703032.zip"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}

	@Test
	@Disabled
	public void sevenZTest(){
		Extractor extractor = 	CompressUtil.createExtractor(
				CharsetUtil.defaultCharset(),
				FileUtil.file("d:/test/compress/test.7z"));

		extractor.extract(FileUtil.file("d:/test/compress/test2/"));
	}
}
