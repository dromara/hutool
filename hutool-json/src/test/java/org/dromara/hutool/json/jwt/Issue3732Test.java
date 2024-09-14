package org.dromara.hutool.json.jwt;

import org.dromara.hutool.json.jwt.signers.JWTSigner;
import org.dromara.hutool.json.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3732Test {
	@Test
	void hmacTest() {
		final JWTSigner SIGNER = JWTSignerUtil.hs256("6sf2f5j2a62a3s8f9032hsf".getBytes());
		final Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");

		final Map<String, Object> payload = new HashMap<>();
		payload.put("name", "test");
		payload.put("role", "admin");

		// 创建 JWT token
		final String token = JWTUtil.createToken(headers, payload, SIGNER);
		assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJuYW1lIjoidGVzdCJ9.pD3Xz41rtXvU3G1c_yS7ir01FXmDvtjjAOU2HYd8MdA", token);
	}
}
