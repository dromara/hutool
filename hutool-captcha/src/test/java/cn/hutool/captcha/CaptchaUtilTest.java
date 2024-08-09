package cn.hutool.captcha;

import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
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
			CaptchaUtil.createShearCaptcha(320, 240);
		}
	}

	@Test
	@Disabled
	public void drawStringColourfulColorDistanceTest() {
		for(int i = 0; i < 10; i++) {
			AbstractCaptcha lineCaptcha = new TestLineCaptchaColorDistance(200, 100, 5, 10);
			lineCaptcha.write("d:/captcha/line1-"+i+".png");
		}
	}

	@Test
	@Disabled
	public void drawStringColourfulDefaultColorDistanceTest() {
		for(int i = 0; i < 10; i++) {
			AbstractCaptcha lineCaptcha = new TestLineCaptchaColorDistanceDefaultColorDistance(200, 100, 5, 10);
			lineCaptcha.write("d:/captcha/line2-"+i+".png");
		}
	}

	static class TestLineCaptchaColorDistance extends AbstractCaptcha{
		private static final long serialVersionUID = -558846929114465692L;

		public TestLineCaptchaColorDistance(int width, int height, int codeCount, int interfereCount) {
			super(width, height, codeCount, interfereCount);
		}

		@Override
		protected Image createImage(String code) {
			// 图像buffer
			final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = GraphicsUtil.createGraphics(image, ObjectUtil.defaultIfNull(this.background, Color.WHITE));

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
		protected void drawString(Graphics2D g, String code) {
			// 指定透明度
			if (null != this.textAlpha) {
				g.setComposite(this.textAlpha);
			}
			// 自定义与背景颜色的色差值，200是基于Color.WHITE较为居中的值
			GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height,Color.WHITE,200);
		}

		/**
		 * 绘制干扰线
		 *
		 * @param g {@link Graphics2D}画笔
		 */
		private void drawInterfere(Graphics2D g) {
			final ThreadLocalRandom random = RandomUtil.getRandom();
			// 干扰线
			for (int i = 0; i < this.interfereCount; i++) {
				int xs = random.nextInt(width);
				int ys = random.nextInt(height);
				int xe = xs + random.nextInt(width / 3);
				int ye = ys + random.nextInt(height / 3);
				g.setColor(ImgUtil.randomColor(random));
				g.drawLine(xs, ys, xe, ye);
			}
		}
		// ----------------------------------------------------------------------------------------------------- Private method start
	}

	static class TestLineCaptchaColorDistanceDefaultColorDistance extends TestLineCaptchaColorDistance {


		public TestLineCaptchaColorDistanceDefaultColorDistance(int width, int height, int codeCount, int interfereCount) {
			super(width, height, codeCount, interfereCount);
		}

		@Override
		protected void drawString(Graphics2D g, String code) {
			// 指定透明度
			if (null != this.textAlpha) {
				g.setComposite(this.textAlpha);
			}
			// 使用默认色差设置
			GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height,Color.WHITE);
		}
	}
}
