/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.swing.img.GraphicsUtil;
import org.dromara.hutool.swing.img.color.ColorUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaUtilTest {

	@Test
	@Disabled
	public void createTest() {
		for(int i = 0; i < 1; i++) {
			CaptchaUtil.ofShearCaptcha(320, 240);
		}
	}

	@Test
	@Disabled
	public void drawStringColourfulTest() {
		for(int i = 0; i < 10; i++) {
			final AbstractCaptcha lineCaptcha = new TestLineCaptcha(200, 100, 5, 10);
			lineCaptcha.write("d:/captcha/line"+i+".png");
		}
	}

	static class TestLineCaptcha extends AbstractCaptcha{
		private static final long serialVersionUID = -558846929114465692L;

		public TestLineCaptcha(final int width, final int height, final int codeCount, final int interfereCount) {
			super(width, height, codeCount, interfereCount);
		}

		@Override
		protected Image createImage(final String code) {
			// 图像buffer
			final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = GraphicsUtil.createGraphics(image, ObjUtil.defaultIfNull(this.background, Color.WHITE));

			// 干扰线
			drawInterfere(g);

			// 字符串
			drawString(g, code);

			return image;
		}

		// ----------------------------------------------------------------------------------------------------- Private method start
		/**
		 * 绘制字符串
		 *
		 * @param g {@link Graphics}画笔
		 * @param code 验证码
		 */
		private void drawString(final Graphics2D g, final String code) {
			// 指定透明度
			if (null != this.textAlpha) {
				g.setComposite(this.textAlpha);
			}
			GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height,Color.WHITE,200);
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
				final int xe = xs + random.nextInt(width / 3);
				final int ye = ys + random.nextInt(height / 3);
				g.setColor(ColorUtil.randomColor(random));
				g.drawLine(xs, ys, xe, ye);
			}
		}
		// ----------------------------------------------------------------------------------------------------- Private method start
	}
}
