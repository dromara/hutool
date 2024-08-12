/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 * 创建线干扰的验证码
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param lineCount 干扰线条数
	 * @param sizeBaseHeight      字体的大小 高度的倍数
	 * @return {@link LineCaptcha}
	 */
	public static LineCaptcha ofLineCaptcha(final int width, final int height, final int codeCount,
											final int lineCount, final float sizeBaseHeight) {
		return new LineCaptcha(width, height, codeCount, lineCount, sizeBaseHeight);
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
	 * 创建圆圈干扰的验证码
	 *
	 * @param width       图片宽
	 * @param height      图片高
	 * @param codeCount   字符个数
	 * @param circleCount 干扰圆圈条数
	 * @param size        字体的大小 高度的倍数
	 * @return {@link CircleCaptcha}
	 */
	public static CircleCaptcha ofCircleCaptcha(final int width, final int height, final int codeCount,
												final int circleCount, final float size) {
		return new CircleCaptcha(width, height, codeCount, circleCount, size);
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
	 * 创建扭曲干扰的验证码，默认5位验证码
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param thickness 干扰线宽度
	 * @param sizeBaseHeight      字体的大小 高度的倍数
	 * @return {@link ShearCaptcha}
	 */
	public static ShearCaptcha ofShearCaptcha(final int width, final int height, final int codeCount, final int thickness, final float sizeBaseHeight) {
		return new ShearCaptcha(width, height, codeCount, thickness, sizeBaseHeight);
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

	/**
	 * 创建圆圈干扰的验证码
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param thickness 验证码干扰元素个数
	 * @param sizeBaseHeight      字体的大小 高度的倍数
	 * @return {@link GifCaptcha}
	 */
	public static GifCaptcha ofGifCaptcha(final int width, final int height, final int codeCount,
										  final int thickness, final float sizeBaseHeight) {
		return new GifCaptcha(width, height, codeCount, thickness, sizeBaseHeight);
	}
}
