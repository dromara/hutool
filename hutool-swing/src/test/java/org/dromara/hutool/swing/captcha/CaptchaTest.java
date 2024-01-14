/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.captcha;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.swing.captcha.generator.MathGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.Color;

/**
 * 直线干扰验证码单元测试
 *
 * @author looly
 */
public class CaptchaTest {

	@Test
	public void lineCaptchaTest1() {
		// 定义图形验证码的长和宽
		final LineCaptcha lineCaptcha = CaptchaUtil.ofLineCaptcha(200, 100);
		Assertions.assertNotNull(lineCaptcha.getCode());
		Assertions.assertTrue(lineCaptcha.verify(lineCaptcha.getCode()));
	}

	@Test
	@Disabled
	public void lineCaptchaTest3() {
		// 定义图形验证码的长和宽
		final LineCaptcha lineCaptcha = CaptchaUtil.ofLineCaptcha(200, 70, 4, 15);
		lineCaptcha.setBackground(Color.yellow);
		lineCaptcha.write("f:/test/captcha/tellow.png");
	}

	@Test
	@Disabled
	public void lineCaptchaWithMathTest() {
		// 定义图形验证码的长和宽
		final LineCaptcha lineCaptcha = CaptchaUtil.ofLineCaptcha(200, 80);
		lineCaptcha.setGenerator(new MathGenerator());
		lineCaptcha.setTextAlpha(0.8f);
		lineCaptcha.write("f:/captcha/math.png");
	}

	@Test
	@Disabled
	public void lineCaptchaTest2() {

		// 定义图形验证码的长和宽
		final LineCaptcha lineCaptcha = CaptchaUtil.ofLineCaptcha(200, 100);
		// LineCaptcha lineCaptcha = new LineCaptcha(200, 100, 4, 150);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		lineCaptcha.write("f:/captcha/line.png");
		Console.log(lineCaptcha.getCode());
		// 验证图形验证码的有效性，返回boolean值
		lineCaptcha.verify("1234");

		lineCaptcha.createCode();
		lineCaptcha.write("f:/captcha/line2.png");
		Console.log(lineCaptcha.getCode());
		// 验证图形验证码的有效性，返回boolean值
		lineCaptcha.verify("1234");
	}

	@Test
	@Disabled
	public void circleCaptchaTest() {

		// 定义图形验证码的长和宽
		final CircleCaptcha captcha = CaptchaUtil.ofCircleCaptcha(200, 100, 4, 20);
		// CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("f:/captcha/circle.png");
		// 验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}

	@Test
	@Disabled
	public void shearCaptchaTest() {

		// 定义图形验证码的长和宽
		final ShearCaptcha captcha = CaptchaUtil.ofShearCaptcha(203, 100, 4, 4);
		// ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("f:/captcha/shear.png");
		// 验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}

	@Test
	@Disabled
	public void shearCaptchaTest2() {

		// 定义图形验证码的长和宽
		final ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("d:/test/shear.png");
		// 验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}

	@Test
	@Disabled
	public void ShearCaptchaWithMathTest() {
		// 定义图形验证码的长和宽
		final ShearCaptcha captcha = CaptchaUtil.ofShearCaptcha(200, 45, 4, 4);
		captcha.setGenerator(new MathGenerator());
		// ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		captcha.write("f:/captcha/shear_math.png");
		// 验证图形验证码的有效性，返回boolean值
		captcha.verify("1234");
	}

	@Test
	@Disabled
	public void GifCaptchaTest() {
		final GifCaptcha captcha = CaptchaUtil.ofGifCaptcha(200, 100, 4);
		captcha.write("d:/test/gif_captcha.gif");
		assert captcha.verify(captcha.getCode());
	}

	@Test
	@Disabled
	public void bgTest(){
		final LineCaptcha captcha = CaptchaUtil.ofLineCaptcha(200, 100, 4, 1);
		captcha.setBackground(Color.WHITE);
		captcha.write("d:/test/test.jpg");
	}
}
