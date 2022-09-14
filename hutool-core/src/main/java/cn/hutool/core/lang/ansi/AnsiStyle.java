package cn.hutool.core.lang.ansi;

import cn.hutool.core.util.StrUtil;

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
	NORMAL(0),

	/**
	 * 粗体或增加强度
	 */
	BOLD(1),

	/**
	 * 弱化（降低强度）
	 */
	FAINT(2),

	/**
	 * 斜体
	 */
	ITALIC(3),

	/**
	 * 下划线
	 */
	UNDERLINE(4);

	private final int code;

	AnsiStyle(int code) {
		this.code = code;
	}

	/**
	 * 获取ANSI文本样式风格代码
	 *
	 * @return 文本样式风格代码
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
