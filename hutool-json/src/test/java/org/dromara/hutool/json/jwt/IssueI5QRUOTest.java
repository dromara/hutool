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

import java.util.LinkedHashMap;
import java.util.Map;

public class IssueI5QRUOTest {

	@Test
	public void createTokenTest(){
		// https://jwt.io/

		// 自定义header顺序
		final Map<String, Object> header = new LinkedHashMap<String, Object>(){
			private static final long serialVersionUID = 1L;
			{
				put(JWTHeader.ALGORITHM, "HS384");
				put(JWTHeader.TYPE, "JWT");
			}
		};

		final Map<String, Object> payload = new LinkedHashMap<String, Object>(){
			private static final long serialVersionUID = 1L;
			{
				put("sub", "1234567890");
				put("name", "John Doe");
				put("iat", 1516239022);
			}
		};

		final String token = JWTUtil.createToken(header, payload, "123456".getBytes());
		Assertions.assertEquals("eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
				"3Ywq9NlR3cBST4nfcdbR-fcZ8374RHzU50X6flKvG-tnWFMalMaHRm3cMpXs1NrZ", token);

		final boolean verify = JWT.of(token).setKey("123456".getBytes()).verify();
		Assertions.assertTrue(verify);
	}
}
