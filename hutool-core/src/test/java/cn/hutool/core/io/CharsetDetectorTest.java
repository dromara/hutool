package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.Charset;

public class CharsetDetectorTest {

	@Test
	public void detectTest(){
		// 测试多个Charset对同一个流的处理是否有问题
		final Charset detect = CharsetDetector.detect(ResourceUtil.getStream("test.xml"),
				CharsetUtil.CHARSET_GBK, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(CharsetUtil.CHARSET_UTF_8, detect);
	}

	@Test
	@Ignore
	public void issue2547() {
		final Charset detect = CharsetDetector.detect(IoUtil.DEFAULT_LARGE_BUFFER_SIZE,
				ResourceUtil.getStream("d:/test/default.txt"));
		Assert.assertEquals(CharsetUtil.CHARSET_UTF_8, detect);
	}
}
