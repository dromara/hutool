package cn.hutool.jwt;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class IssueI5QRUOTest {

	@Test
	public void createTokenTest(){
		// https://jwt.io/

		// 自定义header顺序
		final Map<String, Object> header = new LinkedHashMap<String, Object>(){
			{
				put(JWTHeader.ALGORITHM, "HS384");
				put(JWTHeader.TYPE, "JWT");
			}
		};

		final Map<String, Object> payload = new LinkedHashMap<String, Object>(){
			{
				put("sub", "1234567890");
				put("name", "John Doe");
				put("iat", 1516239022);
			}
		};

		final String token = JWTUtil.createToken(header, payload, "123456".getBytes());
		Assert.assertEquals("eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
				"3Ywq9NlR3cBST4nfcdbR-fcZ8374RHzU50X6flKvG-tnWFMalMaHRm3cMpXs1NrZ", token);

		final boolean verify = JWT.of(token).setKey("123456".getBytes()).verify();
		Assert.assertTrue(verify);
	}
}
