/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.poi.ofd;

import org.dromara.hutool.core.io.IORuntimeException;
import org.ofdrw.converter.export.*;
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
 * <p>
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

	/**
	 * OFD转图片
	 *
	 * @param src       ODF路径
	 * @param targetDir 生成图片存放目录
	 * @param imgType   生成图片的格式，如 JPG、PNG、GIF、BMP、SVG
	 * @param ppm       转换图片质量，每毫米像素数量(Pixels per millimeter)
	 */
	public static void odfToImage(final Path src, final Path targetDir, final String imgType, final double ppm) {
		if ("svg".equalsIgnoreCase(imgType)) {
			odfToSvg(src, targetDir, ppm);
		}
		try (final ImageExporter exporter = new ImageExporter(src, targetDir, imgType, ppm)) {
			exporter.export();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * OFD转HTML
	 *
	 * @param src        ODF路径
	 * @param targetPath 生成HTML路径
	 */
	public static void odfToHtml(final Path src, final Path targetPath) {
		try (final HTMLExporter exporter = new HTMLExporter(src, targetPath)) {
			exporter.export();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * OFD转文本
	 *
	 * @param src        ODF路径
	 * @param targetPath 生成文本路径
	 */
	public static void odfToText(final Path src, final Path targetPath) {
		try (final TextExporter exporter = new TextExporter(src, targetPath)) {
			exporter.export();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * OFD转PDF
	 *
	 * @param src        ODF路径
	 * @param targetPath 生成PDF路径
	 */
	public static void odfToPdf(final Path src, final Path targetPath) {
		try (final OFDExporter exporter = new PDFExporterPDFBox(src, targetPath)) {
			exporter.export();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final Exception e) {
			// 当用户未引入PDF-BOX时,尝试iText
			try (final OFDExporter exporter = new PDFExporterIText(src, targetPath)) {
				exporter.export();
			} catch (final IOException e2) {
				throw new IORuntimeException(e);
			}
		}
	}

	/**
	 * OFD转SVG
	 *
	 * @param src       ODF路径
	 * @param targetDir 生成SVG存放目录
	 * @param ppm       转换图片质量，每毫米像素数量(Pixels per millimeter)
	 */
	private static void odfToSvg(final Path src, final Path targetDir, final double ppm) {
		try (final SVGExporter exporter = new SVGExporter(src, targetDir, ppm)) {
			exporter.export();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
