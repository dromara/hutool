package org.dromara.hutool.swing.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;

/**
 * 显示文本，用于保存在图片上绘图的文本信息，包括内容、字体、大小、位置和透明度等
 *
 * @author looly
 * @since 6.0.0
 */
public class DisplayText implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 构建DisplayText
	 *
	 * @param text  文本
	 * @param color 文本颜色
	 * @param font  文本显示字体
	 * @param point 起始左边位置
	 * @param alpha 透明度
	 * @return DisplayText
	 */
	public static DisplayText of(final String text, final Color color, final Font font, final Point point, final float alpha) {
		return new DisplayText(text, color, font, point, alpha);
	}

	private String pressText;
	private Color color;
	private Font font;
	private Point point;
	private float alpha;

	/**
	 * 构造
	 *
	 * @param text  文本
	 * @param color 文本颜色
	 * @param font  文本显示字体
	 * @param point 起始左边位置
	 * @param alpha 透明度
	 */
	public DisplayText(final String text, final Color color, final Font font, final Point point, final float alpha) {
		this.pressText = text;
		this.color = color;
		this.font = font;
		this.point = point;
		this.alpha = alpha;
	}

	/**
	 * 获取文本
	 *
	 * @return 获取文本
	 */
	public String getPressText() {
		return pressText;
	}

	/**
	 * 设置文本
	 *
	 * @param pressText 文本
	 */
	public void setPressText(final String pressText) {
		this.pressText = pressText;
	}

	/**
	 * 获取文本颜色
	 *
	 * @return 文本颜色
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 设置文本颜色
	 *
	 * @param color 文本颜色
	 */
	public void setColor(final Color color) {
		this.color = color;
	}

	/**
	 * 获取字体
	 *
	 * @return 字体
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * 设置字体
	 *
	 * @param font 字体
	 */
	public void setFont(final Font font) {
		this.font = font;
	}

	/**
	 * 获取二维坐标点
	 *
	 * @return 二维坐标点
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * 设置二维坐标点
	 *
	 * @param point 二维坐标点
	 */
	public void setPoint(final Point point) {
		this.point = point;
	}

	/**
	 * 获取透明度
	 *
	 * @return 透明度
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * 设置透明度
	 *
	 * @param alpha 透明度
	 */
	public void setAlpha(final float alpha) {
		this.alpha = alpha;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final DisplayText that = (DisplayText) o;
		return Float.compare(alpha, that.alpha) == 0
			&& Objects.equals(pressText, that.pressText)
			&& Objects.equals(color, that.color)
			&& Objects.equals(font, that.font)
			&& Objects.equals(point, that.point);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pressText, color, font, point, alpha);
	}

	@Override
	public String toString() {
		return "DisplayText{" +
			"pressText='" + pressText + '\'' +
			", color=" + color +
			", font=" + font +
			", point=" + point +
			", alpha=" + alpha +
			'}';
	}
}
