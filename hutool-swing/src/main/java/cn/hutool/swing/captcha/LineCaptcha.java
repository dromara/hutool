package cn.hutool.swing.captcha;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.swing.img.color.ColorUtil;
import cn.hutool.swing.img.GraphicsUtil;

import java.awt.Color;
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
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public LineCaptcha(final int width, final int height) {
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
	public LineCaptcha(final int width, final int height, final int codeCount, final int lineCount) {
		super(width, height, codeCount, lineCount);
	}
	// -------------------------------------------------------------------- Constructor end

	@Override
	public Image createImage(final String code) {
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
