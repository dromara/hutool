package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class JWTTest {

	@Test
	public void createHs256Test(){
		byte[] key = "1234567890".getBytes();
		JWT jwt = JWT.create()
				.setPayload("sub", "1234567890")
				.setPayload("name", "looly")
				.setPayload("admin", true)
				.setExpiresAt(DateUtil.parse("2022-01-01"))
				.setKey(key);

		String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Imxvb2x5IiwiYWRtaW4iOnRydWUsImV4cCI6MTY0MDk2NjQwMH0." +
				"bXlSnqVeJXWqUIt7HyEhgKNVlIPjkumHlAwFY-5YCtk";

		String token = jwt.sign();
		Assert.assertEquals(rightToken, token);

		Assert.assertTrue(JWT.of(rightToken).setKey(key).verify());
	}

	@Test
	public void parseTest(){
		String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
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

	@Test
	public void verifyTest(){
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2MjQwMDQ4MjIsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV_op5LoibLkuozlj7ciLCJzeXNfbWVudV8xIiwiUk9MRV_op5LoibLkuIDlj7ciLCJzeXNfbWVudV8yIl0sImp0aSI6ImQ0YzVlYjgwLTA5ZTctNGU0ZC1hZTg3LTVkNGI5M2FhNmFiNiIsImNsaWVudF9pZCI6ImhhbmR5LXNob3AifQ." +
				"aixF1eKlAKS_k3ynFnStE7-IRGiD5YaqznvK2xEjBew";

		final boolean verify = JWT.of(token).setKey(StrUtil.utf8Bytes("123456")).verify();
		Assert.assertTrue(verify);
	}

	@Data
	public static class UserTest {
		private String name;
		private Integer age;
	}
	@Test
	public void payloadTest() {

		UserTest bean = new UserTest();
		bean.setAge(18);
		bean.setName("takaki");

		Date date = new Date();
		List<Integer> list = Arrays.asList(1, 2, 3);
		Integer num = 18;
		String username = "takaki";
		HashMap<String, String> map = new HashMap<>();
		map.put("test1", "1");
		map.put("test2", "2");
		Map<String, Object> payload = new HashMap<String, Object>() {
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

		String token = JWTUtil.createToken(payload, "123".getBytes());
		JWT jwt = JWT.of(token);
		String strRes = jwt.getPayload("username", String.class);
		UserTest beanRes = jwt.getPayload("bean", UserTest.class);
		Date dateRes = jwt.getPayload("date", Date.class);
		List listRes = jwt.getPayload("list", List.class);
		Integer numRes = jwt.getPayload("number", Integer.class);
		HashMap mapRes = jwt.getPayload("map", HashMap.class);

		Assert.assertEquals(bean, beanRes);
		Assert.assertEquals(numRes, num);
		Assert.assertEquals(username, strRes);
		Assert.assertEquals(list, listRes);

		String formattedDate = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
		String formattedRes = DateUtil.format(dateRes, "yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(formattedDate, formattedRes);
		Assert.assertEquals(map, mapRes);

	}
}
