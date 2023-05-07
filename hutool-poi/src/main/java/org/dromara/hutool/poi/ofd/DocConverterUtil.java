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

package org.dromara.hutool.poi.ofd;

import org.dromara.hutool.core.io.IORuntimeException;
import org.ofdrw.converter.ofdconverter.DocConverter;
import org.ofdrw.converter.ofdconverter.ImageConverter;
import org.ofdrw.converter.ofdconverter.PDFConverter;
import org.ofdrw.converter.ofdconverter.TextConverter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 基于{@code ofdrw-converter}文档转换，提供：
 * <ul>
 *     <li>OFD PDF  相互转换</li>
 *     <li>OFD TEXT 相互转换</li>
 *     <li>OFD 图片 相互转换</li>
 * </ul>
 *
 * 具体见:https://toscode.gitee.com/ofdrw/ofdrw/blob/master/ofdrw-converter/doc/CONVERTER.md
 *
 * @author looly
 * @since 6.0.0
 */
public class DocConverterUtil {

	/**
	 * PDF转为ODF
	 *
	 * @param src    PDF文件路径
	 * @param target OFD文件路径
	 * @param pages  页码,（从0起）
	 */
	public static void pdfToOfd(final Path src, final Path target, final int... pages) {
		try (final DocConverter converter = new PDFConverter(target)) {
			converter.convert(src, pages);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 纯文本转为ODF
	 *
	 * @param src      纯文件路径
	 * @param target   OFD文件路径
	 * @param fontSize 字体大小
	 */
	public static void textToOfd(final Path src, final Path target, final double fontSize) {
		try (final TextConverter converter = new TextConverter(target)) {
			converter.setFontSize(fontSize);
			converter.convert(src);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 多个图片转为ODF
	 *
	 * @param target OFD文件路径
	 * @param images 图片列表
	 */
	public static void imgToOfd(final Path target, final Path... images) {
		try (final DocConverter converter = new ImageConverter(target)) {
			for (final Path image : images) {
				converter.convert(image);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
