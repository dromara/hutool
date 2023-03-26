package cn.hutool.swing.img.color;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.ansi.Ansi4BitColor;
import cn.hutool.core.lang.ansi.Ansi8BitColor;
import cn.hutool.core.lang.ansi.AnsiElement;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.text.split.SplitUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 颜色工具类
 *
 * @since 5.8.7
 */
public class ColorUtil {

	private static final Map<String, Color> COLOR_MAPPING;

	static {
		final Map<String, Color> colorMap = MapUtil
				.builder("BLACK", Color.BLACK)
				.put("WHITE", Color.WHITE)
				.put("LIGHTGRAY", Color.LIGHT_GRAY)
				.put("LIGHT_GRAY", Color.LIGHT_GRAY)
				.put("GRAY", Color.GRAY)
				.put("DARKGRAY", Color.DARK_GRAY)
				.put("DARK_GRAY", Color.DARK_GRAY)
				.put("RED", Color.RED)
				.put("PINK", Color.PINK)
				.put("ORANGE", Color.ORANGE)
				.put("YELLOW", Color.YELLOW)
				.put("GREEN", Color.GREEN)
				.put("MAGENTA", Color.MAGENTA)
				.put("CYAN", Color.CYAN)
				.put("BLUE", Color.BLUE)
				// 暗金色
				.put("DARKGOLD", hexToColor("#9e7e67"))
				.put("DARK_GOLD", hexToColor("#9e7e67"))
				// 亮金色
				.put("LIGHTGOLD", hexToColor("#ac9c85"))
				.put("LIGHT_GOLD", hexToColor("#ac9c85"))
				.build();
		COLOR_MAPPING = MapUtil.view(colorMap);
	}

	/**
	 * RGB颜色范围上限
	 */
	private static final int RGB_COLOR_BOUND = 256;

	/**
	 * 将颜色转换为CSS的rgba表示形式，输出结果格式为：rgba(red, green, blue)
	 *
	 * @param color AWT颜色
	 * @return rgb(red, green, blue)
	 */
	public static String toCssRgb(final Color color) {
		return StrUtil.builder()
				.append("rgb(")
				.append(color.getRed()).append(",")
				.append(color.getGreen()).append(",")
				.append(color.getBlue()).append(")")
				.toString();
	}

	/**
	 * 将颜色转换为CSS的rgba表示形式，输出结果格式为：rgba(red, green, blue, alpha)
	 *
	 * @param color AWT颜色
	 * @return rgba(red, green, blue, alpha)
	 */
	public static String toCssRgba(final Color color) {
		return StrUtil.builder()
				.append("rgba(")
				.append(color.getRed()).append(",")
				.append(color.getGreen()).append(",")
				.append(color.getBlue()).append(",")
				.append(color.getAlpha() / 255D).append(")")
				.toString();
	}

	/**
	 * Color对象转16进制表示，例如#fcf6d6
	 *
	 * @param color {@link Color}
	 * @return 16进制的颜色值，例如#fcf6d6
	 * @since 4.1.14
	 */
	public static String toHex(final Color color) {
		return toHex(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * RGB颜色值转换成十六进制颜色码
	 *
	 * @param r 红(R)
	 * @param g 绿(G)
	 * @param b 蓝(B)
	 * @return 返回字符串形式的 十六进制颜色码 如
	 */
	public static String toHex(final int r, final int g, final int b) {
		// rgb 小于 255
		if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
			throw new IllegalArgumentException("RGB must be 0~255!");
		}
		return String.format("#%02X%02X%02X", r, g, b);
	}

	/**
	 * 将颜色值转换成具体的颜色类型 汇集了常用的颜色集，支持以下几种形式：
	 *
	 * <pre>
	 * 1. 颜色的英文名（大小写皆可）
	 * 2. 16进制表示，例如：#fcf6d6或者$fcf6d6
	 * 3. RGB形式，例如：13,148,252
	 * 4. RGBA形式，例如：13,148,252,1
	 * </pre>
	 * <p>
	 * 方法来自：com.lnwazg.kit
	 *
	 * @param colorName 颜色的英文名，16进制表示或RGB表示
	 * @return {@link Color}
	 * @since 4.1.14
	 */
	public static Color getColor(String colorName) {
		if (StrUtil.isBlank(colorName)) {
			return null;
		}
		colorName = colorName.toUpperCase();

		// 预定义颜色别名
		final Color color = COLOR_MAPPING.get(colorName);
		if (null != color) {
			return color;
		}

		// 16进制
		if (StrUtil.startWith(colorName, '#')) {
			return hexToColor(colorName);
		} else if (StrUtil.startWith(colorName, '$')) {
			// 由于#在URL传输中无法传输，因此用$代替#
			return hexToColor("#" + colorName.substring(1));
		}

		// RGB值和RGBA
		final List<String> rgb = SplitUtil.split(colorName, StrUtil.COMMA);
		final int size = rgb.size();

		if (3 == size) {
			// RGB
			final Integer[] rgbIntegers = Convert.toIntArray(rgb);
			return new Color(rgbIntegers[0], rgbIntegers[1], rgbIntegers[2]);
		}
		if (4 == size) {
			// RGBA
			final Float[] rgbFloats = Convert.toFloatArray(rgb);
			Float a = rgbFloats[3];
			if (a < 1) {
				// 识别CSS形式
				a *= 255;
			}
			return new Color(rgbFloats[0], rgbFloats[1], rgbFloats[2], a);
		}

		return null;
	}

	/**
	 * 获取一个RGB值对应的颜色
	 *
	 * @param rgb RGB值
	 * @return {@link Color}
	 * @since 4.1.14
	 */
	public static Color getColor(final int rgb) {
		return new Color(rgb);
	}

	/**
	 * 16进制的颜色值转换为Color对象，例如#fcf6d6
	 *
	 * @param hex 16进制的颜色值，例如#fcf6d6
	 * @return {@link Color}
	 * @since 4.1.14
	 */
	public static Color hexToColor(final String hex) {
		return getColor(Integer.parseInt(StrUtil.removePrefix(hex, "#"), 16));
	}

	/**
	 * 叠加颜色
	 *
	 * @param color1 颜色1
	 * @param color2 颜色2
	 * @return 叠加后的颜色
	 */
	public static Color add(final Color color1, final Color color2) {
		final double r1 = color1.getRed();
		final double g1 = color1.getGreen();
		final double b1 = color1.getBlue();
		final double a1 = color1.getAlpha();

		final double r2 = color2.getRed();
		final double g2 = color2.getGreen();
		final double b2 = color2.getBlue();
		final double a2 = color2.getAlpha();

		final int r = (int) ((r1 * a1 / 255 + r2 * a2 / 255) / (a1 / 255 + a2 / 255));
		final int g = (int) ((g1 * a1 / 255 + g2 * a2 / 255) / (a1 / 255 + a2 / 255));
		final int b = (int) ((b1 * a1 / 255 + b2 * a2 / 255) / (a1 / 255 + a2 / 255));
		return new Color(r, g, b);
	}

	/**
	 * 生成随机颜色
	 *
	 * @return 随机颜色
	 * @since 3.1.2
	 */
	public static Color randomColor() {
		return randomColor(null);
	}

	/**
	 * 生成随机颜色
	 *
	 * @param random 随机对象 {@link Random}
	 * @return 随机颜色
	 * @since 3.1.2
	 */
	public static Color randomColor(Random random) {
		if (null == random) {
			random = RandomUtil.getRandom();
		}
		return new Color(random.nextInt(RGB_COLOR_BOUND), random.nextInt(RGB_COLOR_BOUND), random.nextInt(RGB_COLOR_BOUND));
	}

	/**
	 * AWT的{@link Color}颜色转换为ANSI颜色，由于取最接近颜色，故可能有色差
	 *
	 * @param rgb          RGB颜色
	 * @param is8Bit       是否8bit的ANSI颜色
	 * @param isBackground 是否背景色
	 * @return ANSI颜色
	 */
	public static AnsiElement toAnsiColor(final int rgb, final boolean is8Bit, final boolean isBackground) {
		return toAnsiColor(getColor(rgb), is8Bit, isBackground);
	}

	/**
	 * AWT的{@link Color}颜色转换为ANSI颜色，由于取最接近颜色，故可能有色差
	 *
	 * @param color        {@link Color}
	 * @param is8Bit       是否8bit的ANSI颜色
	 * @param isBackground 是否背景色
	 * @return ANSI颜色
	 */
	public static AnsiElement toAnsiColor(final Color color, final boolean is8Bit, final boolean isBackground) {
		if (is8Bit) {
			final Ansi8BitColor ansiElement = (Ansi8BitColor) Ansi8bitMapping.INSTANCE.lookupClosest(color);
			if (isBackground) {
				return ansiElement.asBackground();
			}
			return ansiElement;
		} else {
			final Ansi4BitColor ansiElement = (Ansi4BitColor) Ansi4bitMapping.INSTANCE.lookupClosest(color);
			if (isBackground) {
				return ansiElement.asBackground();
			}
			return ansiElement;
		}
	}

	/**
	 * 获取给定图片的主色调，背景填充用
	 *
	 * @param image      {@link BufferedImage}
	 * @param rgbFilters 过滤多种颜色
	 * @return {@link String} #ffffff
	 * @since 5.6.7
	 */
	public static String getMainColor(final BufferedImage image, final int[]... rgbFilters) {
		int r, g, b;
		final Map<String, Long> countMap = new HashMap<>();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int minx = image.getMinX();
		final int miny = image.getMinY();
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				final int pixel = image.getRGB(i, j);
				r = (pixel & 0xff0000) >> 16;
				g = (pixel & 0xff00) >> 8;
				b = (pixel & 0xff);
				if (matchFilters(r, g, b, rgbFilters)) {
					continue;
				}
				countMap.merge(r + "-" + g + "-" + b, 1L, Long::sum);
			}
		}
		String maxColor = null;
		long maxCount = 0;
		for (final Map.Entry<String, Long> entry : countMap.entrySet()) {
			final String key = entry.getKey();
			final Long count = entry.getValue();
			if (count > maxCount) {
				maxColor = key;
				maxCount = count;
			}
		}
		final String[] splitRgbStr = SplitUtil.splitToArray(maxColor, StrUtil.DASHED);
		String rHex = Integer.toHexString(Integer.parseInt(splitRgbStr[0]));
		String gHex = Integer.toHexString(Integer.parseInt(splitRgbStr[1]));
		String bHex = Integer.toHexString(Integer.parseInt(splitRgbStr[2]));
		rHex = rHex.length() == 1 ? "0" + rHex : rHex;
		gHex = gHex.length() == 1 ? "0" + gHex : gHex;
		bHex = bHex.length() == 1 ? "0" + bHex : bHex;
		return "#" + rHex + gHex + bHex;
	}

	/**
	 * 给定RGB是否匹配过滤器中任何一个RGB颜色
	 *
	 * @param r          R
	 * @param g          G
	 * @param b          B
	 * @param rgbFilters 颜色过滤器
	 * @return 是否匹配
	 */
	private static boolean matchFilters(final int r, final int g, final int b, final int[]... rgbFilters) {
		if (ArrayUtil.isNotEmpty(rgbFilters)) {
			for (final int[] rgbFilter : rgbFilters) {
				if (r == rgbFilter[0] && g == rgbFilter[1] && b == rgbFilter[2]) {
					return true;
				}
			}
		}
		return false;
	}
}
