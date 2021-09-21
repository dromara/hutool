package cn.hutool.jwt;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JWTUtilTest {

	@Test
	public void createTest(){
		byte[] key = "1234".getBytes();
		Map<String, Object> map = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("uid", Integer.parseInt("123"));
				put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);

			}
		};

		JWTUtil.createToken(map, key);
	}
}
