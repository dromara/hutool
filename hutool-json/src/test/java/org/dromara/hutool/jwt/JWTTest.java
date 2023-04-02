package org.dromara.hutool.jwt;

import org.dromara.hutool.date.DatePattern;
import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.util.ByteUtil;
import org.dromara.hutool.jwt.signers.AlgorithmUtil;
import org.dromara.hutool.jwt.signers.JWTSigner;
import org.dromara.hutool.jwt.signers.JWTSignerUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class JWTTest {

	@Test
	public void createHs256Test(){
		final byte[] key = "1234567890".getBytes();
		final JWT jwt = JWT.of()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setExpiresAt(DateUtil.parse("2022-01-01"))
				.setKey(key);

		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Imxvb2x5IiwiYWRtaW4iOnRydWUsImV4cCI6MTY0MDk2NjQwMH0." +
				"bXlSnqVeJXWqUIt7HyEhgKNVlIPjkumHlAwFY-5YCtk";

		final String token = jwt.sign();
		Assertions.assertEquals(rightToken, token);

		Assertions.assertTrue(JWT.of(rightToken).setKey(key).verify());
	}

	@Test
	public void parseTest(){
		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
				"U2aQkC2THYV9L0fTN-yBBI7gmo5xhmvMhATtu8v0zEA";

		final JWT jwt = JWT.of(rightToken);

		Assertions.assertTrue(jwt.setKey("1234567890".getBytes()).verify());

		//header
		Assertions.assertEquals("JWT", jwt.getHeader(JWTHeader.TYPE));
		Assertions.assertEquals("HS256", jwt.getHeader(JWTHeader.ALGORITHM));
		Assertions.assertNull(jwt.getHeader(JWTHeader.CONTENT_TYPE));

		//payload
		Assertions.assertEquals("1234567890", jwt.getPayload("sub"));
		Assertions.assertEquals("looly", jwt.getPayload("name"));
		Assertions.assertEquals(true, jwt.getPayload("admin"));
	}

	@Test
	public void createNoneTest(){
		final JWT jwt = JWT.of()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setSigner(JWTSignerUtil.none());

		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Imxvb2x5IiwiYWRtaW4iOnRydWV9.";

		final String token = jwt.sign();
		Assertions.assertEquals(rightToken, token);

		Assertions.assertTrue(JWT.of(rightToken).setSigner(JWTSignerUtil.none()).verify());
	}

	/**
	 * 必须定义签名器
	 */
	@Test
	public void needSignerTest(){
		Assertions.assertThrows(JWTException.class, ()->{
			final JWT jwt = JWT.of()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true);

			jwt.sign();
		});
	}

	@Test
	public void verifyTest(){
		final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2MjQwMDQ4MjIsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV_op5LoibLkuozlj7ciLCJzeXNfbWVudV8xIiwiUk9MRV_op5LoibLkuIDlj7ciLCJzeXNfbWVudV8yIl0sImp0aSI6ImQ0YzVlYjgwLTA5ZTctNGU0ZC1hZTg3LTVkNGI5M2FhNmFiNiIsImNsaWVudF9pZCI6ImhhbmR5LXNob3AifQ." +
				"aixF1eKlAKS_k3ynFnStE7-IRGiD5YaqznvK2xEjBew";

		final boolean verify = JWT.of(token).setKey(ByteUtil.toUtf8Bytes("123456")).verify();
		Assertions.assertTrue(verify);
	}

	@Data
	public static class UserTest {
		private String name;
		private Integer age;
	}
	@Test
	public void payloadTest() {

		final UserTest bean = new UserTest();
		bean.setAge(18);
		bean.setName("takaki");

		final Date date = new Date();
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final Integer num = 18;
		final String username = "takaki";
		final HashMap<String, String> map = new HashMap<>();
		map.put("test1", "1");
		map.put("test2", "2");
		final Map<String, Object> payload = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("username", username);
				put("bean", bean);
				put("number", num);
				put("list", list);
				put("date", date);
				put("map", map);
			}
		};

		final String token = JWTUtil.createToken(payload, "123".getBytes());
		final JWT jwt = JWT.of(token);
		final String strRes = jwt.getPayload("username", String.class);
		final UserTest beanRes = jwt.getPayload("bean", UserTest.class);
		final Date dateRes = jwt.getPayload("date", Date.class);
		final List<?> listRes = jwt.getPayload("list", List.class);
		final Integer numRes = jwt.getPayload("number", Integer.class);
		final HashMap<?, ?> mapRes = jwt.getPayload("map", HashMap.class);

		Assertions.assertEquals(bean, beanRes);
		Assertions.assertEquals(numRes, num);
		Assertions.assertEquals(username, strRes);
		Assertions.assertEquals(list, listRes);

		final String formattedDate = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
		final String formattedRes = DateUtil.format(dateRes, "yyyy-MM-dd HH:mm:ss");
		Assertions.assertEquals(formattedDate, formattedRes);
		Assertions.assertEquals(map, mapRes);
	}

	@Test()
	public void getDateTest(){
		final String token = JWT.of()
				.setIssuedAt(DateUtil.parse("2022-02-02"))
				.setKey("123456".getBytes())
				.sign();

		// 签发时间早于被检查的时间
		final Date date = JWT.of(token).getPayload().getClaimsJson().getDate(JWTPayload.ISSUED_AT);
		Assertions.assertEquals("2022-02-02", DateUtil.format(date, DatePattern.NORM_DATE_PATTERN));
	}

	@Test
	public void issue2581Test(){
		final Map<String, Object> map = new HashMap<>();
		map.put("test2", 22222222222222L);
		final JWTSigner jwtSigner = JWTSignerUtil.createSigner(AlgorithmUtil.getAlgorithm("HS256"), Base64.getDecoder().decode("abcdefghijklmn"));
		final String sign = JWT.of().addPayloads(map).sign(jwtSigner);
		final Object test2 = JWT.of(sign).getPayload().getClaim("test2");
		Assertions.assertEquals(Long.class, test2.getClass());
	}

	@Test
	public void getLongTest(){
		final String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"
				+ ".eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJhZG1pbiIsImRldmljZSI6ImRlZmF1bHQtZGV2aWNlIiwiZWZmIjoxNjc4Mjg1NzEzOTM1LCJyblN0ciI6IkVuMTczWFhvWUNaaVZUWFNGOTNsN1pabGtOalNTd0pmIn0"
				+ ".wRe2soTaWYPhwcjxdzesDi1BgEm9D61K-mMT3fPc4YM";

		final JWT jwt = JWTUtil.parseToken(rightToken);

		Assertions.assertEquals(
				"{\"loginType\":\"login\",\"loginId\":\"admin\",\"device\":\"default-device\"," +
						"\"eff\":1678285713935,\"rnStr\":\"En173XXoYCZiVTXSF93l7ZZlkNjSSwJf\"}",
				jwt.getPayloads().toString());
		Assertions.assertEquals(Long.valueOf(1678285713935L), jwt.getPayloads().getLong("eff"));
	}
}
