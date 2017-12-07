package com.xiaoleilu.hutool.captcha;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 直线干扰验证码单元测试
 * @author looly
 *
 */
public class CaptchaTest {
	
	@Test
//	@Ignore
	public void lineCaptchaTest() {
		
		//定义图形验证码的长和宽
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
		//图形验证码写出，可以写出到文件，也可以写出到流
		lineCaptcha.write("d:/aaa.png");
		//验证图形验证码的有效性，返回boolean值
		lineCaptcha.verify("1234");
	}
	
	@Test
	@Ignore
	public void circleCaptchaTest() {
		
		//定义图形验证码的长和宽
		CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
		//图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("d:/aaa.png");
		//验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}
	
	@Test
	@Ignore
	public void ShareCaptchaTest() {
		
		//定义图形验证码的长和宽
		ShareCaptcha captcha = new ShareCaptcha(200, 100, 4, 20);
		//图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("d:/aaa.png");
		//验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}
}
