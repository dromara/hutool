package cn.hutool.captcha;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;

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
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public LineCaptcha(int width, int height) {
		this(width, height, 5, 150);
	}

	/**
	 * 构造
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 * @param lineCount 干扰线条数
	 */
	public LineCaptcha(int width, int height, int codeCount, int lineCount) {
		super(width, height, codeCount, lineCount);
	}
	// -------------------------------------------------------------------- Constructor end

	@Override
	public Image createImage(String code) {
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
	 * @param g {@link Graphics2D}画笔
	 * @param random 随机对象
	 * @param code 验证码
	 */
	private void drawString(Graphics2D g, String code) {
		// 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 创建字体
		g.setFont(this.font);

		// 文字
		int minY = GraphicsUtil.getMinY(g);
		if(minY < 0) {
			minY = this.height -1;
		}
		
		final int len = this.generator.getLength();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
			g.setColor(ImgUtil.randomColor());
			g.drawString(String.valueOf(code.charAt(i)), i * charWidth, RandomUtil.randomInt(minY, this.height));
		}
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
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			g.setColor(ImgUtil.randomColor(random));
			g.drawLine(xs, ys, xe, ye);
		}
	}
	// ----------------------------------------------------------------------------------------------------- Private method start
}