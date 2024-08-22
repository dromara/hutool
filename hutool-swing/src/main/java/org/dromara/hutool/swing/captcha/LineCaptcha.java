/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.swing.captcha.generator.CodeGenerator;
import org.dromara.hutool.swing.captcha.generator.RandomGenerator;
import org.dromara.hutool.swing.img.GraphicsUtil;
import org.dromara.hutool.swing.img.color.ColorUtil;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 使用干扰线方式生成的图形验证码
 *
 * @author looly
 * @since 3.1.2
 */
public class LineCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = 8691294460763091089L;

	// -------------------------------------------------------------------- Constructor start

	/**
	 * 构造，默认5位验证码，150条干扰线
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 */
	public LineCaptcha(final int width, final int height) {
		this(width, height, 5, 150);
	}

	/**
	 * 构造
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 * @param lineCount 干扰线条数
	 */
	public LineCaptcha(final int width, final int height, final int codeCount, final int lineCount) {
		this(width, height, new RandomGenerator(codeCount), lineCount);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param generator      验证码生成器
	 * @param interfereCount 验证码干扰元素个数
	 */
	public LineCaptcha(final int width, final int height, final CodeGenerator generator, final int interfereCount) {
		super(width, height, generator, interfereCount);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param codeCount      字符个数
	 * @param interfereCount 验证码干扰元素个数
	 * @param sizeBaseHeight 字体的大小 高度的倍数
	 */
	public LineCaptcha(final int width, final int height, final int codeCount, final int interfereCount, final float sizeBaseHeight) {
		super(width, height, new RandomGenerator(codeCount), interfereCount, sizeBaseHeight);
	}
	// -------------------------------------------------------------------- Constructor end

	@Override
	public Image createImage(final String code) {
		// 图像buffer
		final BufferedImage image = new BufferedImage(width, height, (null == this.background) ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = GraphicsUtil.createGraphics(image, this.background);

		try {
			// 干扰线
			drawInterfere(g);

			// 字符串
			drawString(g, code);
		} finally {
			g.dispose();
		}

		return image;
	}

	// ----------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 绘制字符串
	 *
	 * @param g    {@link Graphics}画笔
	 * @param code 验证码
	 */
	private void drawString(final Graphics2D g, final String code) {
		// 指定透明度
		if (null != this.textAlpha) {
			g.setComposite(this.textAlpha);
		}
		GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height);
	}

	/**
	 * 绘制干扰线
	 *
	 * @param g {@link Graphics2D}画笔
	 */
	private void drawInterfere(final Graphics2D g) {
		final ThreadLocalRandom random = RandomUtil.getRandom();
		// 干扰线
		for (int i = 0; i < this.interfereCount; i++) {
			final int xs = random.nextInt(width);
			final int ys = random.nextInt(height);
			final int xe = xs + random.nextInt(width / 8);
			final int ye = ys + random.nextInt(height / 8);
			g.setColor(ColorUtil.randomColor(random));
			g.drawLine(xs, ys, xe, ye);
		}
	}
	// ----------------------------------------------------------------------------------------------------- Private method start
}
