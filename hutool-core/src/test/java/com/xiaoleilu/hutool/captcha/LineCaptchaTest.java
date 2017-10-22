package com.xiaoleilu.hutool.captcha;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 直线干扰验证码单元测试
 * @author looly
 *
 */
public class LineCaptchaTest {
	
	@Test
	@Ignore
	public void lineCaptchaTest() {
		
		//定义图形验证码的长和宽
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
		//图形验证码写出，可以写出到文件，也可以写出到流
		lineCaptcha.write("d:/aaa.png");
		//验证图形验证码的有效性，返回boolean值
		lineCaptcha.verify("1234");
	}
}
