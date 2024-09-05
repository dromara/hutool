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

import io.jsonwebtoken.Jwts;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.json.jwt.signers.AlgorithmUtil;
import org.dromara.hutool.json.jwt.signers.JWTSigner;
import org.dromara.hutool.json.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

/**
 *https://github.com/dromara/hutool/issues/3205
 */
public class Issue3205Test {
	@Test
	public void es256Test() {
		final String id = "es256";
		final KeyPair keyPair = KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id));
		final JWTSigner signer = JWTSignerUtil.createSigner(id, keyPair);

		final JWT jwt = JWT.of()
			.setPayload("sub", "1234567890")
			.setPayload("name", "looly")
			.setPayload("admin", true)
			.setExpiresAt(DateUtil.tomorrow())
			.setSigner(signer);

		final String token = jwt.sign();

		final boolean signed = Jwts.parser().verifyWith(keyPair.getPublic()).build().isSigned(token);

		Assertions.assertTrue(signed);
	}
}
