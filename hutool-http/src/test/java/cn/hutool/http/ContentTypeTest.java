package cn.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ContentType 单元测试
 *
 *
 */
public class ContentTypeTest {

	@Test
	public void testBuild() {
		String result = ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8);
		Assertions.assertEquals("application/json;charset=UTF-8", result);
	}
}
