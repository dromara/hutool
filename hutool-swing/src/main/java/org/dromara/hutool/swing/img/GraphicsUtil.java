/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.swing.img.color.ColorUtil;

import java.awt.*;
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
	 * @param color {@link Color}背景颜色以及当前画笔颜色，{@code null}表示不设置背景色
	 * @return {@link Graphics2D}
	 * @since 4.5.2
	 */
	public static Graphics2D createGraphics(final BufferedImage image, final Color color) {
		final Graphics2D g = image.createGraphics();
		if (null != color) {
			// 填充背景
			g.setColor(color);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
		}

		return g;
	}

	/**
	 * 获取文字居中高度的Y坐标（距离上边距距离）<br>
	 * 此方法依赖FontMetrics，如果获取失败，默认为背景高度的1/3
	 *
	 * @param g                {@link Graphics2D}画笔
	 * @param backgroundHeight 背景高度
	 * @return 最小高度，-1表示无法获取
	 * @since 4.5.17
	 */
	public static int getCenterY(final Graphics g, final int backgroundHeight) {
		// 获取允许文字最小高度
		FontMetrics metrics = null;
		try {
			metrics = g.getFontMetrics();
		} catch (final Exception e) {
			// 此处报告bug某些情况下会抛出IndexOutOfBoundsException，在此做容错处理
		}
		final int y;
		if (null != metrics) {
			y = (backgroundHeight - metrics.getHeight()) / 2 + metrics.getAscent();
		} else {
			y = backgroundHeight / 3;
		}
		return y;
	}

	/**
	 * 绘制字符串，使用随机颜色，默认抗锯齿
	 *
	 * @param g      {@link Graphics}画笔
	 * @param str    字符串
	 * @param font   字体
	 * @param width  字符串总宽度
	 * @param height 字符串背景高度
	 * @return 画笔对象
	 * @since 4.5.10
	 */
	public static Graphics drawStringColourful(final Graphics g, final String str, final Font font, final int width, final int height) {
		return drawString(g, str, font, null, width, height);
	}

	/**
	 * 绘制字符串，默认抗锯齿
	 *
	 * @param g      {@link Graphics}画笔
	 * @param str    字符串
	 * @param font   字体
	 * @param color  字体颜色，{@code null} 表示使用随机颜色（每个字符单独随机）
	 * @param width  字符串背景的宽度
	 * @param height 字符串背景的高度
	 * @return 画笔对象
	 * @since 4.5.10
	 */
	public static Graphics drawString(final Graphics g, final String str, final Font font, final Color color, final int width, final int height) {
		// 抗锯齿
		enableAntialias(g);
		// 创建字体
		g.setFont(font);

		// 文字高度（必须在设置字体后调用）
		final int midY = getCenterY(g, height);
		if (null != color) {
			g.setColor(color);
		}

		final int len = str.length();
		final int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			if (null == color) {
				// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
				g.setColor(ColorUtil.randomColor());
			}
			g.drawString(String.valueOf(str.charAt(i)), i * charWidth, midY);
		}
		return g;
	}

	/**
	 * 绘制字符串，默认抗锯齿。<br>
	 * 此方法定义一个矩形区域和坐标，文字基于这个区域中间偏移x,y绘制。
	 *
	 * @param g         {@link Graphics}画笔
	 * @param str       字符串
	 * @param font      字体，字体大小决定了在背景中绘制的大小
	 * @param color     字体颜色，{@code null} 表示使用黑色
	 * @param rectangle 字符串绘制坐标和大小，此对象定义了绘制字符串的区域大小和偏移位置
	 * @return 画笔对象
	 * @since 4.5.10
	 */
	public static Graphics drawString(final Graphics g, final String str, final Font font, final Color color, final Rectangle rectangle) {
		// 背景长宽
		final int backgroundWidth = rectangle.width;
		final int backgroundHeight = rectangle.height;

		//获取字符串本身的长宽
		Dimension dimension;
		try {
			dimension = FontUtil.getDimension(g.getFontMetrics(font), str);
		} catch (final Exception e) {
			// 此处报告bug某些情况下会抛出IndexOutOfBoundsException，在此做容错处理
			dimension = new Dimension(backgroundWidth / 3, backgroundHeight / 3);
		}

		rectangle.setSize(dimension.width, dimension.height);
		final Point point = ImgUtil.getPointBaseCentre(rectangle, backgroundWidth, backgroundHeight);

		return drawString(g, str, font, color, point);
	}

	/**
	 * 绘制字符串，默认抗锯齿
	 *
	 * @param g     {@link Graphics}画笔
	 * @param str   字符串
	 * @param font  字体，字体大小决定了在背景中绘制的大小
	 * @param color 字体颜色，{@code null} 表示使用黑色
	 * @param point 绘制字符串的位置坐标
	 * @return 画笔对象
	 * @since 5.3.6
	 */
	public static Graphics drawString(final Graphics g, final String str, final Font font, final Color color, final Point point) {
		// 抗锯齿
		enableAntialias(g);

		g.setFont(font);
		g.setColor(ObjUtil.defaultIfNull(color, Color.BLACK));
		g.drawString(str, point.x, point.y);

		return g;
	}

	/**
	 * 绘制图片
	 *
	 * @param g     画笔
	 * @param img   要绘制的图片
	 * @param point 绘制的位置，基于左上角
	 * @return 画笔对象
	 */
	public static Graphics drawImg(final Graphics g, final Image img, final Point point) {
		return drawImg(g, img,
				new Rectangle(point.x, point.y, img.getWidth(null), img.getHeight(null)));
	}

	/**
	 * 绘制图片
	 *
	 * @param g         画笔
	 * @param img       要绘制的图片
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height,，基于左上角
	 * @return 画笔对象
	 */
	public static Graphics drawImg(final Graphics g, final Image img, final Rectangle rectangle) {
		g.drawImage(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null); // 绘制切割后的图
		return g;
	}

	/**
	 * 设置画笔透明度
	 *
	 * @param g     画笔
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 画笔
	 */
	public static Graphics2D setAlpha(final Graphics2D g, final float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		return g;
	}

	/**
	 * 打开抗锯齿和文本抗锯齿
	 *
	 * @param g {@link Graphics}
	 */
	private static void enableAntialias(final Graphics g) {
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHints(
					RenderingHintsBuilder.of()
							.setAntialiasing(RenderingHintsBuilder.Antialias.ON)
							.setTextAntialias(RenderingHintsBuilder.TextAntialias.ON).build()
			);
		}
	}
}
