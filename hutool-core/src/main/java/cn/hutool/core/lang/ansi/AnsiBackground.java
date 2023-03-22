package cn.hutool.core.lang.ansi;

import cn.hutool.core.util.StrUtil;

/**
 * ANSI背景颜色枚举
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb, Geoffrey Chandler
 * @since 5.8.0
 */
public enum AnsiBackground implements AnsiElement {

	/**
	 * 默认背景色
	 */
	DEFAULT(49),

	/**
	 * 黑色
	 */
	BLACK(40),

	/**
	 * 红
	 */
	RED(41),

	/**
	 * 绿
	 */
	GREEN(42),

	/**
	 * 黄
	 */
	YELLOW(43),

	/**
	 * 蓝
	 */
	BLUE(44),

	/**
	 * 品红
	 */
	MAGENTA(45),

	/**
	 * 青
	 */
	CYAN(46),

	/**
	 * 白
	 */
	WHITE(47),

	/**
	 * 亮黑
	 */
	BRIGHT_BLACK(100),

	/**
	 * 亮红
	 */
	BRIGHT_RED(101),

	/**
	 * 亮绿
	 */
	BRIGHT_GREEN(102),

	/**
	 * 亮黄
	 */
	BRIGHT_YELLOW(103),

	/**
	 * 亮蓝
	 */
	BRIGHT_BLUE(104),

	/**
	 * 亮品红
	 */
	BRIGHT_MAGENTA(105),

	/**
	 * 亮青
	 */
	BRIGHT_CYAN(106),

	/**
	 * 亮白
	 */
	BRIGHT_WHITE(107);

	private final int code;

	AnsiBackground(int code) {
		this.code = code;
	}

	/**
	 * 获取ANSI颜色代码
	 *
	 * @return 颜色代码
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return StrUtil.toString(this.code);
	}

}
