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
