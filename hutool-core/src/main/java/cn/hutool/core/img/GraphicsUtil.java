package cn.hutool.core.img;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
}
