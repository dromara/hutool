package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.Test;

public class JWTValidatorTest {

	@Test(expected = ValidateException.class)
	public void expiredAtTest(){
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0Nzc1OTJ9.isvT0Pqx0yjnZk53mUFSeYFJLDs-Ls9IsNAm86gIdZo";
		JWTValidator.of(token).validateDate(DateUtil.date());
	}

	@Test(expected = ValidateException.class)
	public void issueAtTest(){
		final String token = JWT.create()
				.setIssuedAt(DateUtil.date())
				.setKey("123456".getBytes())
				.sign();

		// 签发时间早于被检查的时间
		JWTValidator.of(token).validateDate(DateUtil.yesterday());
	}

	@Test
	public void issueAtPassTest(){
		final String token = JWT.create()
				.setIssuedAt(DateUtil.date())
				.setKey("123456".getBytes())
				.sign();

		// 签发时间早于被检查的时间
		JWTValidator.of(token).validateDate(DateUtil.date());
	}

	@Test(expected = ValidateException.class)
	public void notBeforeTest(){
		final JWT jwt = JWT.create()
				.setNotBefore(DateUtil.date());

		JWTValidator.of(jwt).validateDate(DateUtil.yesterday());
	}

	@Test
	public void notBeforePassTest(){
		final JWT jwt = JWT.create()
				.setNotBefore(DateUtil.date());
		JWTValidator.of(jwt).validateDate(DateUtil.date());
	}

	@Test
	public void validateAlgorithmTest(){
		final String token = JWT.create()
				.setNotBefore(DateUtil.date())
				.setKey("123456".getBytes())
				.sign();

		// 验证算法
		JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256("123456".getBytes()));
	}
}
