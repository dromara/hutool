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

import lombok.Data;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.date.format.GlobalCustomFormat;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
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

		final JSONObject payloadsData = JSONUtil.parseObj(jwtToken, JSONConfig.of().setDateFormat(GlobalCustomFormat.FORMAT_SECONDS));

		final String token = JWTUtil.createToken(payloadsData, "123".getBytes(StandardCharsets.UTF_8));
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

		final JSONObject payloadsData = JSONUtil.parseObj(jwtToken, JSONConfig.of().setDateFormat(GlobalCustomFormat.FORMAT_SECONDS));

		final String token = JWTUtil.createToken(payloadsData, "123".getBytes(StandardCharsets.UTF_8));
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
