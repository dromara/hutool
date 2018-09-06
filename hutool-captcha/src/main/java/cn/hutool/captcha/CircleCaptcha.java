package cn.hutool.captcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 圆圈干扰验证码
 * 
 * @author looly
 * @since 3.2.3
 *
 */
public class CircleCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = -7096627300356535494L;

	/**
	 * 构造
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public CircleCaptcha(int width, int height) {
		this(width, height, 5);
	}

	/**
	 * 构造
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 */
	public CircleCaptcha(int width, int height, int codeCount) {
		this(width, height, codeCount, 15);
	}

	/**
	 * 构造
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public CircleCaptcha(int width, int height, int codeCount, int interfereCount) {
		super(width, height, codeCount, interfereCount);
	}

	@Override
	public Image createImage(String code) {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = ImageUtil.createGraphics(image, Color.WHITE);
		
		// 随机画干扰圈圈
		drawInterfere(g);
		
		// 画字符串
		// 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		final FontMetrics metrics = g.getFontMetrics();
		final int minY = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();
		int len = code.length();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			// 指定透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
			g.setColor(ImageUtil.randomColor());
			g.drawString(String.valueOf(code.charAt(i)), i * charWidth, RandomUtil.randomInt(minY, this.height));
		}
		
		return image;
	}

	// ----------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 画随机干扰
	 * 
	 * @param g {@link Graphics2D}
	 */
	private void drawInterfere(Graphics2D g) {
		final ThreadLocalRandom random = RandomUtil.getRandom();

		for (int i = 0; i < this.interfereCount; i++) {
			g.setColor(ImageUtil.randomColor(random));
			g.drawOval(random.nextInt(width), random.nextInt(height), random.nextInt(height >> 1), random.nextInt(height >> 1));
		}
	}
	// ----------------------------------------------------------------------------------------------------- Private method end
}
