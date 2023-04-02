package org.dromara.hutool;

import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.meta.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ContentType 单元测试
 *
 */
public class ContentTypeTest {

	@Test
	public void testBuild() {
		final String result = ContentType.build(ContentType.JSON, CharsetUtil.UTF_8);
		Assertions.assertEquals("application/json;charset=UTF-8", result);
	}
}
