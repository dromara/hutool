package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.Assert;
import org.junit.Test;

public class JWTTest {

	@Test
	public void createHs256Test(){
		final byte[] key = "1234567890".getBytes();
		final JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setExpiresAt(DateUtil.parse("2022-01-01"))
				.setKey(key);

		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Imxvb2x5IiwiYWRtaW4iOnRydWUsImV4cCI6MTY0MDk2NjQwMH0." +
				"bXlSnqVeJXWqUIt7HyEhgKNVlIPjkumHlAwFY-5YCtk";

		final String token = jwt.sign();
		Assert.assertEquals(rightToken, token);

		Assert.assertTrue(JWT.of(rightToken).setKey(key).verify());
	}

	@Test
	public void parseTest(){
		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
				"U2aQkC2THYV9L0fTN-yBBI7gmo5xhmvMhATtu8v0zEA";

		final JWT jwt = JWT.of(rightToken);

		Assert.assertTrue(jwt.setKey("1234567890".getBytes()).verify());

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
		final JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setSigner(JWTSignerUtil.none());

		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9.";

		final String token = jwt.sign();
		Assert.assertEquals(token, token);

		Assert.assertTrue(JWT.of(rightToken).setSigner(JWTSignerUtil.none()).verify());
	}

	/**
	 * 必须定义签名器
	 */
	@Test(expected = JWTException.class)
	public void needSignerTest(){
		final JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true);

		jwt.sign();
	}

	@Test
	public void verifyTest(){
		final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2MjQwMDQ4MjIsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV_op5LoibLkuozlj7ciLCJzeXNfbWVudV8xIiwiUk9MRV_op5LoibLkuIDlj7ciLCJzeXNfbWVudV8yIl0sImp0aSI6ImQ0YzVlYjgwLTA5ZTctNGU0ZC1hZTg3LTVkNGI5M2FhNmFiNiIsImNsaWVudF9pZCI6ImhhbmR5LXNob3AifQ." +
				"aixF1eKlAKS_k3ynFnStE7-IRGiD5YaqznvK2xEjBew";

		final boolean verify = JWT.of(token).setKey(StrUtil.utf8Bytes("123456")).verify();
		Assert.assertTrue(verify);
	}

	@Test
	public void getLongTest(){
		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"
				+ ".eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJhZG1pbiIsImRldmljZSI6ImRlZmF1bHQtZGV2aWNlIiwiZWZmIjoxNjc4Mjg1NzEzOTM1LCJyblN0ciI6IkVuMTczWFhvWUNaaVZUWFNGOTNsN1pabGtOalNTd0pmIn0"
				+ ".wRe2soTaWYPhwcjxdzesDi1BgEm9D61K-mMT3fPc4YM"
				+ "";

		final JWT jwt = JWTUtil.parseToken(rightToken);

		Assert.assertEquals(
				"{\"loginType\":\"login\",\"loginId\":\"admin\",\"device\":\"default-device\"," +
						"\"eff\":1678285713935,\"rnStr\":\"En173XXoYCZiVTXSF93l7ZZlkNjSSwJf\"}",
				jwt.getPayloads().toString());
		Assert.assertEquals(Long.valueOf(1678285713935L), jwt.getPayloads().getLong("eff"));
	}
}
