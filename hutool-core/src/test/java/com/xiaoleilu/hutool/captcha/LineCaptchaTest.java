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
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
		lineCaptcha.write("d:/aaa.png");
	}
}
