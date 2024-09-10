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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI6LBZATest {
	@Test
	public void parseJSONStringTest() {
		final String a = "\"a\"";
		final Object parse = JSONUtil.parse(a);
		assertEquals(JSONPrimitive.class, parse.getClass());
		assertEquals(a, parse.toString());
	}

	@Test
	public void parseJSONStringTest2() {
		final String a = "'a'";
		final Object parse = JSONUtil.parse(a);
		assertEquals(JSONPrimitive.class, parse.getClass());
		assertEquals("\"a\"", parse.toString());
	}

	@Test
	public void parseJSONErrorTest() {
		final String a = "a";
		final Object parse = JSONUtil.parse(a);
		assertEquals(JSONPrimitive.class, parse.getClass());
		assertEquals("\"a\"", parse.toString());
	}

	@Test
	public void parseJSONNumberTest() {
		final String a = "123";
		final JSON parse = JSONUtil.parse(a);
		assertEquals(JSONPrimitive.class, parse.getClass());
		assertEquals(123, ((JSONPrimitive)parse).getValue());
		assertEquals(Integer.class, ((JSONPrimitive)parse).getValue().getClass());
	}
}
