package com.xiaoleilu.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.exceptions.ValidateException;
import com.xiaoleilu.hutool.lang.Validator;

/**
 * 验证器单元测试
 * @author Looly
 *
 */
public class ValidatorTest {
	@Test
	public void isBirthdayTest(){
		boolean b = Validator.isBirthday("20150101");
		Assert.assertTrue(b);
		boolean b2 = Validator.isBirthday("2015-01-01");
		Assert.assertTrue(b2);
		boolean b3 = Validator.isBirthday("2015.01.01");
		Assert.assertTrue(b3);
		boolean b4 = Validator.isBirthday("2015年01月01日");
		Assert.assertTrue(b4);
		boolean b5 = Validator.isBirthday("2015.01.01");
		Assert.assertTrue(b5);
		
		//验证年非法
		Assert.assertFalse(Validator.isBirthday("2095.05.01"));
		//验证月非法
		Assert.assertFalse(Validator.isBirthday("2015.13.01"));
		//验证日非法
		Assert.assertFalse(Validator.isBirthday("2015.02.29"));
	}
	
	@Test
	public void isCitizenIdTest(){
		boolean b = Validator.isCitizenId("150218199012123389");
		Assert.assertTrue(b);
	}
	
	@Test(expected=ValidateException.class)
	public void validateTest() throws ValidateException{
		Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
	}
	
	@Test
	public void isEmailTest() {
		boolean email = Validator.isEmail("abc_cde@163.com");
		Assert.assertTrue(email);
		boolean email1 = Validator.isEmail("abc_%cde@163.com");
		Assert.assertTrue(email1);
		boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		Assert.assertTrue(email2);
		boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		Assert.assertTrue(email3);
	}
	
	@Test
	public void isMobileTest() {
		boolean m1 = Validator.isMobile("13900221432");
		Assert.assertTrue(m1);
		boolean m2 = Validator.isMobile("015100221432");
		Assert.assertTrue(m2);
		boolean m3 = Validator.isMobile("+8618600221432");
		Assert.assertTrue(m3);
	}
}
