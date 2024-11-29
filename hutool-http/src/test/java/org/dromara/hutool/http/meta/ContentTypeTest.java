/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.meta;

import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ContentType 单元测试
 *
 */
public class ContentTypeTest {

	@Test
	public void testBuild() {
		final String result = ContentType.build(ContentType.JSON, CharsetUtil.UTF_8);
		assertEquals("application/json;charset=UTF-8", result);
	}

	@Test
	void testGetWithLeadingSpace() {
		final String json = " {\n" +
			"     \"name\": \"hutool\"\n" +
			" }";
		final ContentType contentType = ContentType.get(json);
		assertEquals(ContentType.JSON, contentType);
	}
}
