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

package org.dromara.hutool.json.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7IZIMTest {
	@Test
	void parseTokenTest() {
		final String n = "payload";
		final byte[] key = "123".getBytes();
		final String sign = JWT.of()
			.setPayload(n, 1L)
			.setKey(key)
			.sign();

		final long payload = JWTUtil.parseToken(sign)
			.setKey(key)
			.getPayload(n, Long.class);


		Assertions.assertEquals(1, payload);
	}
}
