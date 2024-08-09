package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class CharsetDetectorTest {

	@Test
	public void detectTest(){
		// 测试多个Charset对同一个流的处理是否有问题
		final Charset detect = CharsetDetector.detect(ResourceUtil.getStream("test.xml"),
				CharsetUtil.CHARSET_GBK, CharsetUtil.CHARSET_UTF_8);
		assertEquals(CharsetUtil.CHARSET_UTF_8, detect);
	}

	@Test
	@Disabled
	public void issue2547() {
		final Charset detect = CharsetDetector.detect(IoUtil.DEFAULT_LARGE_BUFFER_SIZE,
				ResourceUtil.getStream("d:/test/default.txt"));
		assertEquals(CharsetUtil.CHARSET_UTF_8, detect);
	}
}
