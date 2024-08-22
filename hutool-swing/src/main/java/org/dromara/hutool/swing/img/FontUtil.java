/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.io.IORuntimeException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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

	/**
	 * 获取font的样式应用在str上的整个矩形
	 *
	 * @param str  字符串，必须非空
	 * @param font 字体，必须非空
	 * @return {@link Rectangle2D}
	 * @since 5.3.3
	 */
	public static Rectangle2D getRectangle(final String str, final Font font) {
		return font.getStringBounds(str,
			new FontRenderContext(AffineTransform.getScaleInstance(1, 1),
				false,
				false));
	}

}
