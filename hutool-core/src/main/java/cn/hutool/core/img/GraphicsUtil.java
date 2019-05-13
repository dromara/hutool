package cn.hutool.core.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * {@link Graphics}相关工具类
 * 
 * @author looly
 * @since 4.5.2
 */
public class GraphicsUtil {

	/**
	 * 创建{@link Graphics2D}
	 * 
	 * @param image {@link BufferedImage}
	 * @param color {@link Color}背景颜色以及当前画笔颜色
	 * @return {@link Graphics2D}
	 * @since 4.5.2
	 */
	public static Graphics2D createGraphics(BufferedImage image, Color color) {
		final Graphics2D g = image.createGraphics();
		// 填充背景
		g.setColor(color);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		return g;
	}

	/**
	 * 获取最小高度
	 * 
	 * @param g {@link Graphics2D}画笔
	 * @return 最小高度，-1表示无法获取
	 */
	public static int getMinY(Graphics g) {
		// 获取允许文字最小高度
		FontMetrics metrics = null;
		try {
			metrics = g.getFontMetrics();
		} catch (Exception e) {
			// 此处报告bug某些情况下会抛出IndexOutOfBoundsException，在此做容错处理
		}
		int minY;
		if (null != metrics) {
			minY = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();
		} else {
			minY = -1;
		}
		return minY;
	}

	/**
	 * 绘制字符串，使用随机颜色，默认抗锯齿
	 * 
	 * @param g {@link Graphics}画笔
	 * @param str 字符串
	 * @param font 字体
	 * @param width 字符串总宽度
	 * @param height 字符串高度，填充请使用{@link #getMinY(Graphics)}获取
	 * @return 画笔对象
	 * @since 4.5.10
	 */
	public static Graphics drawStringColourful(Graphics g, String str, Font font, int width, int height) {
		return drawString(g, str, font, null, width, height);
	}

	/**
	 * 绘制字符串，默认抗锯齿
	 * 
	 * @param g {@link Graphics}画笔
	 * @param str 字符串
	 * @param font 字体
	 * @param color 字体颜色，{@code null} 表示使用随机颜色（每个字符单独随机）
	 * @param width 字符串背景的宽度
	 * @param height 字符串背景的高度
	 * @return 画笔对象
	 * @since 4.5.10
	 */
	public static Graphics drawString(Graphics g, String str, Font font, Color color, int width, int height) {
		// 抗锯齿
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// 创建字体
		g.setFont(font);

		// 文字高度（必须在设置字体后调用）
		int minY = GraphicsUtil.getMinY(g);
		if (minY < 0) {
			minY = height - 4;
		}
		
		if (null != color) {
			g.setColor(color);
		}

		final int len = str.length();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			if (null == color) {
				// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
				g.setColor(ImgUtil.randomColor());
			}
			g.drawString(String.valueOf(str.charAt(i)), i * charWidth, minY);
		}
		return g;
	}

}
