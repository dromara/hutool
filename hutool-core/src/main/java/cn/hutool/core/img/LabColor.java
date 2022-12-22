package cn.hutool.core.img;

import cn.hutool.core.lang.Assert;

import java.awt.Color;
import java.awt.color.ColorSpace;

/**
 * 表示以 LAB 形式存储的颜色。<br>
 * <ul>
 *     <li>L: 亮度</li>
 *     <li>a: 正数代表红色，负端代表绿色</li>
 *     <li>b: 正数代表黄色，负端代表蓝色</li>
 * </ul>
 *
 * @author Tom Xin
 * @since 5.8.7
 */
public class LabColor {

	private static final ColorSpace XYZ_COLOR_SPACE = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);

	/**
	 * L: 亮度
	 */
	private final double l;
	/**
	 * A: 正数代表红色，负端代表绿色
	 */
	private final double a;
	/**
	 * B: 正数代表黄色，负端代表蓝色
	 */
	private final double b;

	public LabColor(Integer rgb) {
		this((rgb != null) ? new Color(rgb) : null);
	}

	public LabColor(Color color) {
		Assert.notNull(color, "Color must not be null");
		final float[] lab = fromXyz(color.getColorComponents(XYZ_COLOR_SPACE, null));
		this.l = lab[0];
		this.a = lab[1];
		this.b = lab[2];
	}

	/**
	 * 获取颜色差
	 * @param other 其他Lab颜色
	 * @return 颜色差
	 */
	// See https://en.wikipedia.org/wiki/Color_difference#CIE94
	public double getDistance(LabColor other) {
		double c1 = Math.sqrt(this.a * this.a + this.b * this.b);
		double deltaC = c1 - Math.sqrt(other.a * other.a + other.b * other.b);
		double deltaA = this.a - other.a;
		double deltaB = this.b - other.b;
		double deltaH = Math.sqrt(Math.max(0.0, deltaA * deltaA + deltaB * deltaB - deltaC * deltaC));
		return Math.sqrt(Math.max(0.0, Math.pow((this.l - other.l), 2)
				+ Math.pow(deltaC / (1 + 0.045 * c1), 2) + Math.pow(deltaH / (1 + 0.015 * c1), 2.0)));
	}

	private float[] fromXyz(float[] xyz) {
		return fromXyz(xyz[0], xyz[1], xyz[2]);
	}

	/**
	 * 从xyz换算<br>
	 * L=116f(y)-16<br>
	 * a=500[f(x/0.982)-f(y)]<br>
	 * b=200[f(y)-f(z/1.183 )]<br>
	 * 其中： f(x)=7.787x+0.138, x<0.008856; f(x)=(x)1/3,x>0.008856
	 *
	 * @param x X
	 * @param y Y
	 * @param z Z
	 * @return Lab
	 */
	private static float[] fromXyz(float x, float y, float z) {
		final double l = (f(y) - 16.0) * 116.0;
		final double a = (f(x) - f(y)) * 500.0;
		final double b = (f(y) - f(z)) * 200.0;
		return new float[]{(float) l, (float) a, (float) b};
	}

	private static double f(double t) {
		return (t > (216.0 / 24389.0)) ? Math.cbrt(t) : (1.0 / 3.0) * Math.pow(29.0 / 6.0, 2) * t + (4.0 / 29.0);
	}
}
