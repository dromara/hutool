package cn.hutool.jwt;

import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.Assert;
import org.junit.Test;

public class JWTTest {

	@Test
	public void createHs256Test(){
		byte[] key = "1234567890".getBytes();
		JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setKey(key);

		String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
				"536690902d931d857d2f47d337ec81048ee09a8e71866bcc8404edbbcbf4cc40";

		String token = jwt.sign();
		Assert.assertEquals(token, token);

		Assert.assertTrue(JWT.of(rightToken).setKey(key).verify());
	}

	@Test
	public void parseTest(){
		String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
				"536690902d931d857d2f47d337ec81048ee09a8e71866bcc8404edbbcbf4cc40";

		final JWT jwt = JWT.of(rightToken);

		//header
		Assert.assertEquals("JWT", jwt.getHeader(JWTHeader.TYPE));
		Assert.assertEquals("HS256", jwt.getHeader(JWTHeader.ALGORITHM));
		Assert.assertNull(jwt.getHeader(JWTHeader.CONTENT_TYPE));

		//payload
		Assert.assertEquals("1234567890", jwt.getPayload("sub"));
		Assert.assertEquals("looly", jwt.getPayload("name"));
		Assert.assertEquals(true, jwt.getPayload("admin"));
	}

	@Test
	public void createNoneTest(){
		JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setSigner(JWTSignerUtil.none());

		String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9.";

		String token = jwt.sign();
		Assert.assertEquals(token, token);

		Assert.assertTrue(JWT.of(rightToken).setSigner(JWTSignerUtil.none()).verify());
	}

	/**
	 * 必须定义签名器
	 */
	@Test(expected = JWTException.class)
	public void needSignerTest(){
		JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true);

		jwt.sign();
	}
}
