package cn.hutool.core.lang.ansi;

/**
 * ANSI文本样式风格枚举
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb
 * @since 5.8.0
 */
public enum AnsiStyle implements AnsiElement {

	/**
	 * 重置/正常
	 */
	NORMAL("0"),

	/**
	 * 粗体或增加强度
	 */
	BOLD("1"),

	/**
	 * 弱化（降低强度）
	 */
	FAINT("2"),

	/**
	 * 斜体
	 */
	ITALIC("3"),

	/**
	 * 下划线
	 */
	UNDERLINE("4");

	private final String code;

	AnsiStyle(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return this.code;
	}

}
