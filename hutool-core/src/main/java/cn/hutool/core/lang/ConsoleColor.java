package cn.hutool.core.lang;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.img.BackgroundRemoval;
import cn.hutool.core.util.RandomUtil;

import java.awt.*;

/**
 * <p>控制台打印颜色工具</p>
 * <p>更多详情参考</p>
 * <li><a href="https://docs.microsoft.com/en-us/windows/console/console-virtual-terminal-sequences#output-sequences">Console Virtual Terminal Sequences</a></li>
 * <li><a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape code</a></li>
 *
 * @author Norman
 * @since 5.7.13
 */
public class ConsoleColor {

	/*======================================================重置颜色======================================================*/

	/**
	 * 重置
	 */
	public static final String RESET = "\u001B[0m";

	/*======================================================基础8色======================================================*/

	/**
	 * 黑色
	 */
	public static final String BLACK_TEXT = "\u001B[30m";
	/**
	 * 红色
	 */
	public static final String RED_TEXT = "\u001B[31m";
	/**
	 * 绿色
	 */
	public static final String GREEN_TEXT = "\u001B[32m";
	/**
	 * 黄色
	 */
	public static final String YELLOW_TEXT = "\u001B[33m";
	/**
	 * 蓝色
	 */
	public static final String BLUE_TEXT = "\u001B[34m";
	/**
	 * 品红
	 */
	public static final String MAGENTA_TEXT = "\u001B[35m";
	/**
	 * 青色
	 */
	public static final String CYAN_TEXT = "\u001B[36m";
	/**
	 * 白色
	 */
	public static final String WHITE_TEXT = "\u001B[37m";
	/**
	 * 默认
	 */
	public static final String DEFAULT_TEXT = "\u001B[39m";

	/*======================================================高亮8色======================================================*/

	/**
	 * 亮黑色
	 */
	public static final String BRIGHT_BLACK_TEXT = "\u001B[90m";
	/**
	 * 亮红色
	 */
	public static final String BRIGHT_RED_TEXT = "\u001B[91m";
	/**
	 * 亮绿色
	 */
	public static final String BRIGHT_GREEN_TEXT = "\u001B[92m";
	/**
	 * 亮黄色
	 */
	public static final String BRIGHT_YELLOW_TEXT = "\u001B[93m";
	/**
	 * 亮蓝色
	 */
	public static final String BRIGHT_BLUE_TEXT = "\u001B[94m";
	/**
	 * 亮品红
	 */
	public static final String BRIGHT_MAGENTA_TEXT = "\u001B[95m";
	/**
	 * 亮青色
	 */
	public static final String BRIGHT_CYAN_TEXT = "\u001B[96m";
	/**
	 * 亮白色
	 */
	public static final String BRIGHT_WHITE_TEXT = "\u001B[97m";

	/*======================================================基础8色背景======================================================*/

	/**
	 * 黑色背景
	 */
	public static final String BLACK_BACKGROUND = "\u001B[40m";
	/**
	 * 红色背景
	 */
	public static final String RED_BACKGROUND = "\u001B[41m";
	/**
	 * 绿色背景
	 */
	public static final String GREEN_BACKGROUND = "\u001B[42m";
	/**
	 * 黄色背景
	 */
	public static final String YELLOW_BACKGROUND = "\u001B[43m";
	/**
	 * 蓝色背景
	 */
	public static final String BLUE_BACKGROUND = "\u001B[44m";
	/**
	 * 品红背景
	 */
	public static final String MAGENTA_BACKGROUND = "\u001B[45m";
	/**
	 * 青色背景
	 */
	public static final String CYAN_BACKGROUND = "\u001B[46m";
	/**
	 * 白色背景
	 */
	public static final String WHITE_BACKGROUND = "\u001B[47m";
	/**
	 * 默认背景
	 */
	public static final String DEFAULT_BACKGROUND = "\u001B[49m";

	/*======================================================高亮8色背景======================================================*/

	/**
	 * 亮黑色背景
	 */
	public static final String BRIGHT_BLACK_BACKGROUND = "\u001B[100m";
	/**
	 * 亮红色背景
	 */
	public static final String BRIGHT_RED_BACKGROUND = "\u001B[101m";
	/**
	 * 亮绿色背景
	 */
	public static final String BRIGHT_GREEN_BACKGROUND = "\u001B[102m";
	/**
	 * 亮黄色背景
	 */
	public static final String BRIGHT_YELLOW_BACKGROUND = "\u001B[103m";
	/**
	 * 亮蓝色背景
	 */
	public static final String BRIGHT_BLUE_BACKGROUND = "\u001B[104m";
	/**
	 * 亮品红背景
	 */
	public static final String BRIGHT_MAGENTA_BACKGROUND = "\u001B[105m";
	/**
	 * 亮青色背景
	 */
	public static final String BRIGHT_CYAN_BACKGROUND = "\u001B[106m";
	/**
	 * 亮白色背景
	 */
	public static final String BRIGHT_WHITE_BACKGROUND = "\u001B[107m";

	/*======================================================私有属性======================================================*/

	/**
	 * 8位颜色文字前缀
	 */
	private static final String _8_BIT_TEXT_PREFIX = "\u001B[38;5;";
	/**
	 * 8位颜色背景前缀
	 */
	private static final String _8_BIT_BACKGROUND_PREFIX = "\u001B[48;5;";
	/**
	 * RGB颜色文字前缀
	 */
	private static final String RGB_TEXT_PREFIX = "\u001B[38;2;";
	/**
	 * RGB颜色背景前缀
	 */
	private static final String RGB_BACKGROUND_PREFIX = "\u001B[48;2;";

	/*======================================================方法======================================================*/

	/**
	 * 设置文字颜色
	 *
	 * @param text      文字内容
	 * @param colorEnum 颜色
	 * @return 上色的文字
	 */
	public static String colorText(String text, ColorEnum colorEnum) {
		return colorText(text, colorEnum, false);
	}

	/**
	 * 设置文字颜色
	 *
	 * @param text      文字内容
	 * @param colorEnum 颜色
	 * @param isBright  是否为亮色
	 * @return 上色的文字
	 */
	public static String colorText(String text, ColorEnum colorEnum, boolean isBright) {
		StringBuilder coloredText = new StringBuilder();
		if (isBright) {
			switch (colorEnum) {
				case BLACK:
					coloredText.append(BRIGHT_BLACK_TEXT);
					break;
				case RED:
					coloredText.append(BRIGHT_RED_TEXT);
					break;
				case GREEN:
					coloredText.append(BRIGHT_GREEN_TEXT);
					break;
				case YELLOW:
					coloredText.append(BRIGHT_YELLOW_TEXT);
					break;
				case BLUE:
					coloredText.append(BRIGHT_BLUE_TEXT);
					break;
				case MAGENTA:
					coloredText.append(BRIGHT_MAGENTA_TEXT);
					break;
				case CYAN:
					coloredText.append(BRIGHT_CYAN_TEXT);
					break;
				case WHITE:
				default:
					coloredText.append(BRIGHT_WHITE_TEXT);
					break;
			}
		} else {
			switch (colorEnum) {
				case BLACK:
					coloredText.append(BLACK_TEXT);
					break;
				case RED:
					coloredText.append(RED_TEXT);
					break;
				case GREEN:
					coloredText.append(GREEN_TEXT);
					break;
				case YELLOW:
					coloredText.append(YELLOW_TEXT);
					break;
				case BLUE:
					coloredText.append(BLUE_TEXT);
					break;
				case MAGENTA:
					coloredText.append(MAGENTA_TEXT);
					break;
				case CYAN:
					coloredText.append(CYAN_TEXT);
					break;
				case WHITE:
				default:
					coloredText.append(WHITE_TEXT);
					break;
			}
		}
		coloredText.append(text).append(DEFAULT_TEXT);
		return coloredText.toString();
	}

	/**
	 * 设置文字背景
	 *
	 * @param text      文字内容
	 * @param colorEnum 颜色
	 * @return 背景上色的文字
	 */
	public static String colorBackground(String text, ColorEnum colorEnum) {
		return colorBackground(text, colorEnum, false);
	}

	/**
	 * 设置文字背景
	 *
	 * @param text      文字内容
	 * @param colorEnum 颜色
	 * @param isBright  是否为亮色
	 * @return 背景上色的文字
	 */
	public static String colorBackground(String text, ColorEnum colorEnum, boolean isBright) {
		StringBuilder coloredBackground = new StringBuilder();
		if (isBright) {
			switch (colorEnum) {
				case RED:
					coloredBackground.append(BRIGHT_RED_BACKGROUND);
					break;
				case GREEN:
					coloredBackground.append(BRIGHT_GREEN_BACKGROUND);
					break;
				case YELLOW:
					coloredBackground.append(BRIGHT_YELLOW_BACKGROUND);
					break;
				case BLUE:
					coloredBackground.append(BRIGHT_BLUE_BACKGROUND);
					break;
				case MAGENTA:
					coloredBackground.append(BRIGHT_MAGENTA_BACKGROUND);
					break;
				case CYAN:
					coloredBackground.append(BRIGHT_CYAN_BACKGROUND);
					break;
				case WHITE:
					coloredBackground.append(BRIGHT_WHITE_BACKGROUND);
					break;
				case BLACK:
				default:
					coloredBackground.append(BRIGHT_BLACK_BACKGROUND);
					break;
			}
		} else {
			switch (colorEnum) {
				case RED:
					coloredBackground.append(RED_BACKGROUND);
					break;
				case GREEN:
					coloredBackground.append(GREEN_BACKGROUND);
					break;
				case YELLOW:
					coloredBackground.append(YELLOW_BACKGROUND);
					break;
				case BLUE:
					coloredBackground.append(BLUE_BACKGROUND);
					break;
				case MAGENTA:
					coloredBackground.append(MAGENTA_BACKGROUND);
					break;
				case CYAN:
					coloredBackground.append(CYAN_BACKGROUND);
					break;
				case WHITE:
					coloredBackground.append(WHITE_BACKGROUND);
					break;
				case BLACK:
				default:
					coloredBackground.append(BLACK_BACKGROUND);
					break;
			}
		}
		coloredBackground.append(text).append(DEFAULT_BACKGROUND);
		return coloredBackground.toString();
	}

	/**
	 * <p>设置文字8位颜色</p>
	 * <p>颜色编号(0~255)详见</p>
	 * <li>{@link ConsoleColor#printStandardColors()}</li>
	 * <li>{@link ConsoleColor#printBrightColors()}</li>
	 * <li>{@link ConsoleColor#printDarkColors()}</li>
	 * <li>{@link ConsoleColor#printLightColors()}</li>
	 * <li>{@link ConsoleColor#printGrayscaleColors()}</li>
	 *
	 * @param text 文字
	 * @param code 颜色编号
	 * @return 上色的文字
	 */
	public static String color8BitText(String text, int code) {
		if (!Validator.isBetween(code, 0, 255)) {
			throw new ValidateException("颜色编号必须在0~255之间");
		}
		return _8_BIT_TEXT_PREFIX + code + "m" + text + DEFAULT_TEXT;
	}

	/**
	 * <p>设置背景8位颜色</p>
	 * <p>颜色编号(0~255)详见</p>
	 * <li>{@link ConsoleColor#printStandardColors()}</li>
	 * <li>{@link ConsoleColor#printBrightColors()}</li>
	 * <li>{@link ConsoleColor#printDarkColors()}</li>
	 * <li>{@link ConsoleColor#printLightColors()}</li>
	 * <li>{@link ConsoleColor#printGrayscaleColors()}</li>
	 *
	 * @param text 文字
	 * @param code 颜色编号
	 * @return 背景上色的文字
	 */
	public static String color8BitBackground(String text, int code) {
		if (!Validator.isBetween(code, 0, 255)) {
			throw new ValidateException("颜色编号必须在0~255之间");
		}
		return _8_BIT_BACKGROUND_PREFIX + code + "m" + text + DEFAULT_BACKGROUND;
	}

	/**
	 * 设置文字RGB颜色
	 *
	 * @param text  文字
	 * @param color RGB颜色
	 * @return 上色的文字
	 */
	public static String colorRGBText(String text, Color color) {
		return RGB_TEXT_PREFIX + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + "m" + text + DEFAULT_TEXT;
	}

	/**
	 * 设置文字RGB颜色
	 *
	 * @param text 文字
	 * @param hex  十六进制颜色码
	 * @return 上色的文字
	 */
	public static String colorRGBText(String text, String hex) {
		return colorRGBText(text, BackgroundRemoval.hexToRgb(hex));
	}

	/**
	 * 设置文字RGB颜色
	 *
	 * @param text 文字
	 * @param r    红色值
	 * @param g    绿色值
	 * @param b    蓝色值
	 * @return 上色的文字
	 */
	public static String colorRGBText(String text, int r, int g, int b) {
		return colorRGBText(text, new Color(r, g, b));
	}

	/**
	 * 设置背景RGB颜色
	 *
	 * @param text  文字
	 * @param color RGB颜色
	 * @return 背景上色的文字
	 */
	public static String colorRGBBackground(String text, Color color) {
		return RGB_BACKGROUND_PREFIX + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + "m" + text + DEFAULT_BACKGROUND;
	}

	/**
	 * 设置背景RGB颜色
	 *
	 * @param text 文字
	 * @param hex  十六进制颜色码
	 * @return 背景上色的文字
	 */
	public static String colorRGBBackground(String text, String hex) {
		return colorRGBBackground(text, BackgroundRemoval.hexToRgb(hex));
	}

	/**
	 * 设置背景RGB颜色
	 *
	 * @param text 文字
	 * @param r    红色值
	 * @param g    绿色值
	 * @param b    蓝色值
	 * @return 背景上色的文字
	 */
	public static String colorRGBBackground(String text, int r, int g, int b) {
		return colorRGBBackground(text, new Color(r, g, b));
	}

	/*======================================================打印色卡======================================================*/

	/**
	 * 标准8色卡
	 */
	public static void printStandardColors() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t\t\t\tStandard colors\t\t\t\t\t\t\n");
		for (int i = 0; i < 8; i++) {
			int code = 0 + i;
			sb.append(BRIGHT_WHITE_TEXT).append(_8_BIT_BACKGROUND_PREFIX).append(code).append("m").append("\t").append(code).append("\t");
		}
		sb.append(RESET);
		System.out.println(sb);
	}

	/**
	 * 亮色8色卡
	 */
	public static void printBrightColors() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t\t\tHigh-intensity (Bright) colors\t\t\t\t\t\n");
		for (int i = 0; i < 8; i++) {
			int code = 8 + i;
			sb.append(BLACK_TEXT).append(_8_BIT_BACKGROUND_PREFIX).append(code).append("m").append("\t").append(code).append("\t");
		}
		sb.append(RESET);
		System.out.println(sb);
	}

	/**
	 * 深系108色卡
	 */
	public static void printDarkColors() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\tDark colors\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 16; j++) {
				int code = 16 + 36 * i + j;
				sb.append(BRIGHT_WHITE_TEXT).append(_8_BIT_BACKGROUND_PREFIX).append(code).append("m").append("\t").append(code).append("\t");
			}
			sb.append(RESET).append('\n');
		}
		sb.deleteCharAt(sb.length() - 1).append(RESET);
		System.out.println(sb);
	}

	/**
	 * 浅系108色卡
	 */
	public static void printLightColors() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\tLight colors\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 16; j++) {
				int code = 34 + 36 * i + j;
				sb.append(BLACK_TEXT).append(_8_BIT_BACKGROUND_PREFIX).append(code).append("m").append("\t").append(code).append("\t");
			}
			sb.append(RESET).append('\n');
		}
		sb.deleteCharAt(sb.length() - 1).append(RESET);
		System.out.println(sb);
	}

	/**
	 * 灰度16色卡
	 */
	public static void printGrayscaleColors() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t\t\t\t\t\t\t\tGrayscale colors\t\t\t\t\t\t\t\t\t\t\n");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 12; j++) {
				int code = 232 + i * 12 + j;
				sb.append(BRIGHT_WHITE_TEXT).append(_8_BIT_BACKGROUND_PREFIX).append(code).append("m").append("\t").append(code).append("\t");
			}
			sb.append(RESET).append('\n');
		}
		sb.deleteCharAt(sb.length() - 1).append(RESET);
		System.out.println(sb);
	}

	/*======================================================枚举======================================================*/

	/**
	 * 基础8色枚举（附带”默认色“）
	 */
	public enum ColorEnum {
		BLACK(30),
		RED(31),
		GREEN(32),
		YELLOW(33),
		BLUE(34),
		MAGENTA(35),
		CYAN(36),
		WHITE(37),
		DEFAULT(39);
		/**
		 * 颜色编号
		 */
		private final int code;

		ColorEnum(int code) {
			this.code = code;
		}

		/**
		 * 随机获取颜色，不包括默认色
		 *
		 * @return 颜色
		 */
		public static ColorEnum random() {
			ColorEnum[] values = values();
			int random = RandomUtil.randomInt(values.length - 1);
			return values[random];
		}

		/**
		 * 颜色编号
		 *
		 * @return 颜色编号
		 */
		public int getCode() {
			return code;
		}
	}
}
