package org.dromara.hutool.swing.captcha;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CaptchaUtilTest {

	@Test
	@Disabled
	public void createTest() {
		for(int i = 0; i < 1; i++) {
			CaptchaUtil.createShearCaptcha(320, 240);
		}
	}
}
