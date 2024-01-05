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

/**
 * 图形验证码工具
 *
 * @author looly
 * @since 3.1.2
 */
public class CaptchaUtil {

	/**
	 * 创建线干扰的验证码，默认5位验证码，150条干扰线
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 * @return {@link LineCaptcha}
	 */
	public static LineCaptcha ofLineCaptcha(final int width, final int height) {
		return new LineCaptcha(width, height);
	}

	/**
	 * 创建线干扰的验证码
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param lineCount 干扰线条数
	 * @return {@link LineCaptcha}
	 */
	public static LineCaptcha ofLineCaptcha(final int width, final int height, final int codeCount, final int lineCount) {
		return new LineCaptcha(width, height, codeCount, lineCount);
	}

	/**
	 * 创建圆圈干扰的验证码，默认5位验证码，15个干扰圈
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 * @return {@link CircleCaptcha}
	 * @since 3.2.3
	 */
	public static CircleCaptcha ofCircleCaptcha(final int width, final int height) {
		return new CircleCaptcha(width, height);
	}

	/**
	 * 创建圆圈干扰的验证码
	 *
	 * @param width       图片宽
	 * @param height      图片高
	 * @param codeCount   字符个数
	 * @param circleCount 干扰圆圈条数
	 * @return {@link CircleCaptcha}
	 * @since 3.2.3
	 */
	public static CircleCaptcha ofCircleCaptcha(final int width, final int height, final int codeCount, final int circleCount) {
		return new CircleCaptcha(width, height, codeCount, circleCount);
	}

	/**
	 * 创建扭曲干扰的验证码，默认5位验证码
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 * @return {@link ShearCaptcha}
	 * @since 3.2.3
	 */
	public static ShearCaptcha ofShearCaptcha(final int width, final int height) {
		return new ShearCaptcha(width, height);
	}

	/**
	 * 创建扭曲干扰的验证码，默认5位验证码
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param thickness 干扰线宽度
	 * @return {@link ShearCaptcha}
	 * @since 3.3.0
	 */
	public static ShearCaptcha ofShearCaptcha(final int width, final int height, final int codeCount, final int thickness) {
		return new ShearCaptcha(width, height, codeCount, thickness);
	}

	/**
	 * 创建GIF验证码
	 *
	 * @param width 宽
	 * @param height 高
	 * @return {@link GifCaptcha}
	 */
	public static GifCaptcha ofGifCaptcha(final int width, final int height) {
		return new GifCaptcha(width, height);
	}

	/**
	 * 创建GIF验证码
	 *
	 * @param width 宽
	 * @param height 高
	 * @param codeCount 字符个数
	 * @return {@link GifCaptcha}
	 */
	public static GifCaptcha ofGifCaptcha(final int width, final int height, final int codeCount) {
		return new GifCaptcha(width, height, codeCount);
	}
}
