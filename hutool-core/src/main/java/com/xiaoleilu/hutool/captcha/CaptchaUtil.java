package com.xiaoleilu.hutool.captcha;

/**
 * 图形验证码工具
 * @author looly
 * @since 3.1.2
 */
public class CaptchaUtil {
	
	/**
	 * 创建线干扰的验证码，默认4位验证码，150条干扰线
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @return {@link LineCaptcha}
	 */
	public static LineCaptcha createLineCaptcha(int width, int height) {
		return new LineCaptcha(width, height);
	}
	
	/**
	 * 创建线干扰的验证码
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 * @param lineCount 干扰线条数
	 * @return {@link LineCaptcha}
	 */
	public static LineCaptcha createLineCaptcha(int width, int height, int codeCount, int lineCount) {
		return new LineCaptcha(width, height, codeCount, lineCount);
	}
}
