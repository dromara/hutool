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

		final boolean signed = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().isSigned(token);

		Assertions.assertTrue(signed);
	}
}
