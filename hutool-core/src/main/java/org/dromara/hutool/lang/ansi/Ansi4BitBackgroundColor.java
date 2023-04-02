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

package org.dromara.hutool.lang.ansi;

import org.dromara.hutool.text.StrUtil;

/**
 * ANSI背景颜色枚举
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb, Geoffrey Chandler
 * @since 5.8.0
 */
public enum Ansi4BitBackgroundColor implements AnsiElement {

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

	Ansi4BitBackgroundColor(int code) {
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

	/**
	 * 获取ANSI颜色代码
	 *
	 * @param isBackground 是否背景色
	 * @return 颜色代码
	 */
	public int getCode(boolean isBackground) {
		return isBackground ? this.code : this.code - 10;
	}

	/**
	 * 获取背景色对应的前景色
	 *
	 * @return 前景色
	 */
	public Ansi4BitColor asForeground() {
		return Ansi4BitColor.of(getCode(false));
	}

	@Override
	public String toString() {
		return StrUtil.toString(this.code);
	}

	/**
	 * 根据code查找对应的Ansi4BitBackgroundColor
	 *
	 * @param code Ansi 4bit 颜色代码
	 * @return Ansi4BitBackgroundColor
	 */
	public static Ansi4BitBackgroundColor of(int code) {
		for (Ansi4BitBackgroundColor item : Ansi4BitBackgroundColor.values()) {
			if (item.getCode() == code) {
				return item;
			}
		}
		throw new IllegalArgumentException(StrUtil.format("No matched Ansi4BitBackgroundColor instance,code={}", code));
	}
}
