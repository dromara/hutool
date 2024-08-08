package cn.hutool.captcha;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaUtilTest {

	@Test
	@Ignore
	public void createTest() {
		for(int i = 0; i < 1; i++) {
			CaptchaUtil.createShearCaptcha(320, 240);
		}
	}

	@Test
	@Ignore
	public void drawStringColourfulTest() {
		for(int i = 0; i < 10; i++) {
			AbstractCaptcha lineCaptcha = new TestLineCaptcha(200, 100, 5, 10);
			lineCaptcha.write("d:/captcha/line"+i+".png");
		}
	}

	static class TestLineCaptcha extends AbstractCaptcha{

		public TestLineCaptcha(int width, int height, int codeCount, int interfereCount) {
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
		private void drawString(Graphics2D g, String code) {
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
}
