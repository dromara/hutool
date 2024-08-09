package cn.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import static org.junit.jupiter.api.Assertions.*;
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
		assertEquals("application/json;charset=UTF-8", result);
	}

	@Test
	public void testGetWithLeadingSpace() {
		String json = " {\n" +
			"     \"name\": \"hutool\"\n" +
			" }";
		ContentType contentType = ContentType.get(json);
		assertEquals(ContentType.JSON, contentType);
	}
}
