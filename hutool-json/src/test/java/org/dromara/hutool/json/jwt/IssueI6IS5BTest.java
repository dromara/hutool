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

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * https://gitee.com/dromara/hutool/issues/I6IS5B
 */
public class IssueI6IS5BTest {

	@Test
	public void payloadToBeanTest() {
		final LocalDateTime iat = TimeUtil.of(DateUtil.parse("2023-03-03"));
		final JwtToken jwtToken = new JwtToken();
		jwtToken.setIat(iat);
		final String token = JWTUtil.createToken(JSONUtil.parseObj(jwtToken), "123".getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.SXU_mm1wT5lNoK-Dq5Y8f3BItv_44zuAlyeWLqajpXg", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		Assertions.assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken o = payloads.toBean(JwtToken.class);
		Assertions.assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken {
		private LocalDateTime iat;
	}

	@Test
	public void payloadToBeanTest2() {
		final Date iat = DateUtil.parse("2023-03-03");
		final JwtToken2 jwtToken = new JwtToken2();
		jwtToken.setIat(iat);
		final String token = JWTUtil.createToken(JSONUtil.parseObj(jwtToken), "123".getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2Nzc3NzI4MDB9.SXU_mm1wT5lNoK-Dq5Y8f3BItv_44zuAlyeWLqajpXg", token);
		final JSONObject payloads = JWTUtil.parseToken(token).getPayloads();
		Assertions.assertEquals("{\"iat\":1677772800}", payloads.toString());
		final JwtToken2 o = payloads.toBean(JwtToken2.class);
		Assertions.assertEquals(iat, o.getIat());
	}

	@Data
	static class JwtToken2 {
		private Date iat;
	}
}
