/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.ansi;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.ObjUtil;

/**
 * ANSI 8-bit前景或背景色（即8位编码，共256种颜色（2^8） ）<br>
 * <ul>
 *     <li>0-7：                        标准颜色（同ESC [ 30–37 m）</li>
 *     <li>8-15：                       高强度颜色（同ESC [ 90–97 m）</li>
 *     <li>16-231（6 × 6 × 6 共 216色）： 16 + 36 × r + 6 × g + b (0 ≤ r, g, b ≤ 5)</li>
 *     <li>232-255：                    从黑到白的24阶灰度色</li>
 * </ul>
 *
 * <p>来自Spring Boot</p>
 *
 * @author Toshiaki Maki, Phillip Webb
 * @see #foreground(int)
 * @see #background(int)
 * @since 5.8.0
 */
public class Ansi8BitColor implements AnsiElement {

	private static final String PREFIX_FORE = "38;5;";
	private static final String PREFIX_BACK = "48;5;";

	/**
	 * 前景色ANSI颜色实例
	 *
	 * @param code 颜色代码(0-255)
	 * @return 前景色ANSI颜色实例
	 */
	public static Ansi8BitColor foreground(final int code) {
		return new Ansi8BitColor(PREFIX_FORE, code);
	}

	/**
	 * 背景色ANSI颜色实例
	 *
	 * @param code 颜色代码(0-255)
	 * @return 背景色ANSI颜色实例
	 */
	public static Ansi8BitColor background(final int code) {
		return new Ansi8BitColor(PREFIX_BACK, code);
	}

	private final String prefix;
	private final int code;

	/**
	 * 构造
	 *
	 * @param prefix 前缀
	 * @param code   颜色代码(0-255)
	 * @throws IllegalArgumentException 颜色代码不在 0~255 范围内
	 */
	private Ansi8BitColor(final String prefix, final int code) {
		Assert.isTrue(code >= 0 && code <= 255, "Code must be between 0 and 255");
		this.prefix = prefix;
		this.code = code;
	}

	/**
	 * 获取颜色代码(0-255)
	 *
	 * @return 颜色代码(0 - 255)
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	/**
	 * 转换为前景色
	 *
	 * @return 前景色
	 */
	public Ansi8BitColor asForeground() {
		if (PREFIX_FORE.equals(this.prefix)) {
			return this;
		}
		return Ansi8BitColor.foreground(this.code);
	}

	/**
	 * 转换为背景色
	 *
	 * @return 背景色
	 */
	public Ansi8BitColor asBackground() {
		if (PREFIX_BACK.equals(this.prefix)) {
			return this;
		}
		return Ansi8BitColor.background(this.code);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Ansi8BitColor other = (Ansi8BitColor) obj;
		return ObjUtil.equals(this.prefix, other.prefix) && this.code == other.code;
	}

	@Override
	public int hashCode() {
		return this.prefix.hashCode() * 31 + this.code;
	}

	@Override
	public String toString() {
		return this.prefix + this.code;
	}
}
