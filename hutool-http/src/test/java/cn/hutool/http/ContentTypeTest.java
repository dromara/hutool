package cn.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * ContentType 单元测试
 *
 *
 */
public class ContentTypeTest {

	@Test
	public void testBuild() {
		String result = ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("application/json;charset=UTF-8", result);
	}

	@Test
	public void testGetWithLeadingSpace() {
		String json = " {\n" +
			"     \"name\": \"hutool\"\n" +
			" }";
		ContentType contentType = ContentType.get(json);
		Assert.assertEquals(ContentType.JSON, contentType);
	}
}
