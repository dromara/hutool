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

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.swing.captcha.generator.CodeGenerator;
import org.dromara.hutool.swing.captcha.generator.RandomGenerator;
import org.dromara.hutool.swing.img.GraphicsUtil;
import org.dromara.hutool.swing.img.color.ColorUtil;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 圆圈干扰验证码
 *
 * @author looly
 * @since 3.2.3
 */
public class CircleCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = -7096627300356535494L;

	/**
	 * 构造
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 */
	public CircleCaptcha(final int width, final int height) {
		this(width, height, 5);
	}

	/**
	 * 构造
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 */
	public CircleCaptcha(final int width, final int height, final int codeCount) {
		this(width, height, codeCount, 15);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param codeCount      字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public CircleCaptcha(final int width, final int height, final int codeCount, final int interfereCount) {
		this(width, height, new RandomGenerator(codeCount), interfereCount);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param generator      验证码生成器
	 * @param interfereCount 验证码干扰元素个数
	 */
	public CircleCaptcha(final int width, final int height, final CodeGenerator generator, final int interfereCount) {
		super(width, height, generator, interfereCount);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param codeCount      字符个数
	 * @param interfereCount 验证码干扰元素个数
	 * @param sizeBaseHeight           字体的大小 高度的倍数
	 */
	public CircleCaptcha(final int width, final int height, final int codeCount, final int interfereCount, final float sizeBaseHeight) {
		super(width, height, new RandomGenerator(codeCount), interfereCount, sizeBaseHeight);
	}

	@Override
	public Image createImage(final String code) {
		final BufferedImage image = new BufferedImage(width, height, (null == this.background) ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = GraphicsUtil.createGraphics(image, this.background);

		try {
			// 随机画干扰圈圈
			drawInterfere(g);

			// 画字符串
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
	 * @param g    {@link Graphics2D}画笔
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
	 * 画随机干扰
	 *
	 * @param g {@link Graphics2D}
	 */
	private void drawInterfere(final Graphics2D g) {
		final ThreadLocalRandom random = RandomUtil.getRandom();

		for (int i = 0; i < this.interfereCount; i++) {
			g.setColor(ColorUtil.randomColor(random));
			g.drawOval(random.nextInt(width), random.nextInt(height), random.nextInt(height >> 1), random.nextInt(height >> 1));
		}
	}
	// ----------------------------------------------------------------------------------------------------- Private method end
}
