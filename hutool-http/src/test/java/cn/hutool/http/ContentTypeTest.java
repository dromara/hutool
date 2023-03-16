package cn.hutool.http;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.meta.ContentType;
import org.junit.Assert;
import org.junit.Test;

/**
 * ContentType 单元测试
 * <p>
 *
 */
public class ContentTypeTest {

	@Test
	public void testBuild() {
		final String result = ContentType.build(ContentType.JSON, CharsetUtil.UTF_8);
		Assert.assertEquals("application/json;charset=UTF-8", result);
	}
}
