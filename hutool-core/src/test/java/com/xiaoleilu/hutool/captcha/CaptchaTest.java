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
	@Ignore
	public void lineCaptchaTest() {
		
		//定义图形验证码的长和宽
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
//		LineCaptcha lineCaptcha = new LineCaptcha(200, 100, 4, 150);
		//图形验证码写出，可以写出到文件，也可以写出到流
		lineCaptcha.write("d:/line.png");
		//验证图形验证码的有效性，返回boolean值
		lineCaptcha.verify("1234");
	}
	
	@Test
	@Ignore
	public void circleCaptchaTest() {
		
		//定义图形验证码的长和宽
		CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
//		CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
		//图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("d:/circle.png");
		//验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}
	
	@Test
	@Ignore
	public void ShearCaptchaTest() {
		
		//定义图形验证码的长和宽
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
//		ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
		//图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("d:/shear.png");
		//验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}
}
