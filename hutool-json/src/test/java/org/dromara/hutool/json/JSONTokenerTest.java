/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JSONTokenerTest {
	@Test
	void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.getUtf8Reader("issue1200.json"));
		assertNotNull(jsonObject);
	}

	@Test
	void nextTest() {
		final JSONTokener jsonTokener = new JSONTokener("{\"ab\": \"abc\"}");
		final char c = jsonTokener.nextTokenChar();
		assertEquals('{', c);
		assertEquals("ab", jsonTokener.nextString());
		final char c2 = jsonTokener.nextTokenChar();
		assertEquals(':', c2);
		assertEquals("abc", jsonTokener.nextString());


		IoUtil.closeQuietly(jsonTokener);
	}

	/**
	 * 兼容非包装符包装的value和key
	 */
	@Test
	void nextWithoutWrapperTest() {
		final JSONTokener jsonTokener = new JSONTokener("{ab: abc}");
		final char c = jsonTokener.nextTokenChar();
		assertEquals('{', c);
		assertEquals("ab", jsonTokener.nextString());
		final char c2 = jsonTokener.nextTokenChar();
		assertEquals(':', c2);
		assertEquals("abc", jsonTokener.nextString());


		IoUtil.closeQuietly(jsonTokener);
	}
}
