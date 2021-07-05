package cn.hutool.crypto.test.digest;

import cn.hutool.core.codec.Base32;
import cn.hutool.crypto.digest.otp.TOTP;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

/**
 * @author: xlgogo@outlook.com
 * @date: 2021-07-01 18:14
 * @description:
 */
public class OTPTest {
	@Test
	public void genKey(){
		String key = TOTP.generateSecretKey(8);
		System.out.println(key);
	}

	@Test
	public void valid(){
		String key = "VYCFSW2QZ3WZO";
		// 2021/7/1下午6:29:54 显示code为 106659
		//Assert.assertEquals(new TOTP(Base32.decode(key)).generate(Instant.ofEpochSecond(1625135394L)),106659);
		TOTP totp = new TOTP(Base32.decode(key));
		Instant instant = Instant.ofEpochSecond(1625135394L);
		Assert.assertTrue(totp.validate(instant,0,106659));
		Assert.assertTrue(totp.validate(instant.plusSeconds(30),1,106659));
		Assert.assertTrue(totp.validate(instant.plusSeconds(60),2,106659));

		Assert.assertFalse(totp.validate(instant.plusSeconds(60),1,106659));
		Assert.assertFalse(totp.validate(instant.plusSeconds(90),2,106659));
	}

	@Test
	public void googleAuthTest(){
		String str = TOTP.generateGoogleSecretKey("xl7@qq.com", 10);
		System.out.println(str);
	}

}
