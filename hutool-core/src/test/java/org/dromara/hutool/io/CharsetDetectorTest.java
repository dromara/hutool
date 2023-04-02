package org.dromara.hutool.io;

import org.dromara.hutool.io.resource.ResourceUtil;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class CharsetDetectorTest {

	@Test
	public void detectTest(){
		// 测试多个Charset对同一个流的处理是否有问题
		final Charset detect = CharsetDetector.detect(ResourceUtil.getStream("test.xml"),
				CharsetUtil.GBK, CharsetUtil.UTF_8);
		Assertions.assertEquals(CharsetUtil.UTF_8, detect);
	}
}
