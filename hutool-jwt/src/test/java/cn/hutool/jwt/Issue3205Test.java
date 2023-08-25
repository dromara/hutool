package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.Test;

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

		final JWT jwt = JWT.create()
			.setPayload("sub", "1234567890")
			.setPayload("name", "looly")
			.setPayload("admin", true)
			.setExpiresAt(DateUtil.tomorrow())
			.setSigner(signer);

		final String token = jwt.sign();

		final boolean signed = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().isSigned(token);

		Assert.assertTrue(signed);
	}
}
