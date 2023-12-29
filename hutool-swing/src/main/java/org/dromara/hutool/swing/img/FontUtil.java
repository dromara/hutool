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

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.io.IORuntimeException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * AWT中字体相关工具类
 *
 * @author looly
 * @since 5.3.6
 */
public class FontUtil {

	/**
	 * 创建默认字体
	 *
	 * @return 默认字体
	 */
	public static Font createFont() {
		return new Font(null);
	}

	/**
	 * 创建SansSerif字体
	 *
	 * @param size 字体大小
	 * @return 字体
	 */
	public static Font createSansSerifFont(final int size) {
		return createFont(Font.SANS_SERIF, size);
	}

	/**
	 * 创建指定名称的字体
	 *
	 * @param name 字体名称
	 * @param size 字体大小
	 * @return 字体
	 */
	public static Font createFont(final String name, final int size) {
		return new Font(name, Font.PLAIN, size);
	}

	/**
	 * 根据文件创建字体<br>
	 * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
	 *
	 * @param fontFile 字体文件
	 * @return {@link Font}
	 */
	public static Font createFont(final File fontFile) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (final FontFormatException e) {
			// True Type字体无效时使用Type1字体
			try {
				return Font.createFont(Font.TYPE1_FONT, fontFile);
			} catch (final Exception e1) {
				throw ExceptionUtil.wrapRuntime(e);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 根据文件创建字体<br>
	 * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
	 *
	 * @param fontStream 字体流
	 * @return {@link Font}
	 */
	public static Font createFont(final InputStream fontStream) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (final FontFormatException e) {
			// True Type字体无效时使用Type1字体
			try {
				return Font.createFont(Font.TYPE1_FONT, fontStream);
			} catch (final Exception e1) {
				throw ExceptionUtil.wrapRuntime(e1);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得字体对应字符串的长宽信息
	 *
	 * @param metrics {@link FontMetrics}
	 * @param str  字符串
	 * @return 长宽信息
	 */
	public static Dimension getDimension(final FontMetrics metrics, final String str) {
		final int width = metrics.stringWidth(str);
		final int height = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();

		return new Dimension(width, height);
	}

}
