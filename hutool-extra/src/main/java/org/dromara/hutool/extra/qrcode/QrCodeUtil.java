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

package org.dromara.hutool.extra.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.swing.img.ImgUtil;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * 基于Zxing的二维码工具类，支持：
 * <ul>
 *     <li>二维码生成和识别，见{@link BarcodeFormat#QR_CODE}</li>
 *     <li>条形码生成和识别，见{@link BarcodeFormat#CODE_39}等很多标准格式</li>
 * </ul>
 *
 * @author looly
 * @since 4.0.2
 */
public class QrCodeUtil {

	/**
	 * SVG矢量图格式
	 */
	public static final String QR_TYPE_SVG = "svg";
	/**
	 * Ascii Art字符画文本
	 */
	public static final String QR_TYPE_TXT = "txt";

	/**
	 * 生成 Base64 编码格式的二维码，以 String 形式表示
	 *
	 * <p>
	 * 输出格式为: data:image/[type];base64,[data]
	 * </p>
	 *
	 * @param content   内容
	 * @param qrConfig  二维码配置，包括宽度、高度、边距、颜色等
	 * @param imageType 类型（图片扩展名），见{@link #QR_TYPE_SVG}、 {@link #QR_TYPE_TXT}、{@link ImgUtil}
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64DataUri(final String content, final QrConfig qrConfig, final String imageType) {
		switch (imageType) {
			case QR_TYPE_SVG:
				return svgToBase64DataUri(generateAsSvg(content, qrConfig));
			case QR_TYPE_TXT:
				return txtToBase64DataUri(generateAsAsciiArt(content, qrConfig));
			default:
				BufferedImage img = null;
				try {
					img = generate(content, qrConfig);
					return ImgUtil.toBase64DataUri(img, imageType);
				} finally {
					ImgUtil.flush(img);
				}
		}
	}

	/**
	 * 生成PNG格式的二维码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param width   宽度
	 * @param height  高度
	 * @return 图片的byte[]
	 * @since 4.0.10
	 */
	public static byte[] generatePng(final String content, final int width, final int height) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, width, height, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成PNG格式的二维码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param config  二维码配置，包括宽度、高度、边距、颜色等
	 * @return 图片的byte[]
	 * @since 4.1.2
	 */
	public static byte[] generatePng(final String content, final QrConfig config) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, config, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成二维码到文件，二维码图片格式取决于文件的扩展名
	 *
	 * @param content    文本内容
	 * @param width      宽度（单位：类型为一般图片或SVG时，单位是像素，类型为 Ascii Art 字符画时，单位是字符▄或▀的大小）
	 * @param height     高度（单位：类型为一般图片或SVG时，单位是像素，类型为 Ascii Art 字符画时，单位是字符▄或▀的大小）
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 */
	public static File generate(final String content, final int width, final int height, final File targetFile) {
		return generate(content, QrConfig.of(width, height), targetFile);
	}

	/**
	 * 生成二维码到文件，二维码图片格式取决于文件的扩展名
	 *
	 * @param content    文本内容
	 * @param config     二维码配置，包括宽度、高度、边距、颜色等
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 * @since 4.1.2
	 */
	public static File generate(final String content, final QrConfig config, final File targetFile) {
		final String extName = FileNameUtil.extName(targetFile);
		switch (extName) {
			case QR_TYPE_SVG:
				FileUtil.writeUtf8String(generateAsSvg(content, config), targetFile);
				break;
			case QR_TYPE_TXT:
				FileUtil.writeUtf8String(generateAsAsciiArt(content, config), targetFile);
				break;
			default:
				BufferedImage image = null;
				try {
					image = generate(content, config);
					ImgUtil.write(image, targetFile);
				} finally {
					ImgUtil.flush(image);
				}
		}

		return targetFile;
	}

	/**
	 * 生成二维码到输出流
	 *
	 * @param content   文本内容
	 * @param width     宽度（单位：类型为一般图片或SVG时，单位是像素，类型为 Ascii Art 字符画时，单位是字符▄或▀的大小）
	 * @param height    高度（单位：类型为一般图片或SVG时，单位是像素，类型为 Ascii Art 字符画时，单位是字符▄或▀的大小）
	 * @param imageType 类型（图片扩展名），见{@link #QR_TYPE_SVG}、 {@link #QR_TYPE_TXT}、{@link ImgUtil}
	 * @param out       目标流
	 */
	public static void generate(final String content, final int width, final int height, final String imageType, final OutputStream out) {
		generate(content, QrConfig.of(width, height), imageType, out);
	}

	/**
	 * 生成二维码到输出流
	 *
	 * @param content   文本内容
	 * @param config    二维码配置，包括宽度、高度、边距、颜色等
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param out       目标流
	 * @since 4.1.2
	 */
	public static void generate(final String content, final QrConfig config, final String imageType, final OutputStream out) {
		switch (imageType) {
			case QR_TYPE_SVG:
				IoUtil.writeUtf8(out, false, generateAsSvg(content, config));
				break;
			case QR_TYPE_TXT:
				IoUtil.writeUtf8(out, false, generateAsAsciiArt(content, config));
				break;
			default:
				BufferedImage img = null;
				try {
					img = generate(content, config);
					ImgUtil.write(img, imageType, out);
				} finally {
					ImgUtil.flush(img);
				}
		}
	}

	/**
	 * 生成二维码图片
	 *
	 * @param content 文本内容
	 * @param width   宽度
	 * @param height  高度
	 * @return 二维码图片（黑白）
	 */
	public static BufferedImage generate(final String content, final int width, final int height) {
		return generate(content, QrConfig.of(width, height));
	}

	/**
	 * 生成二维码或条形码图片<br>
	 * 只有二维码时QrConfig中的图片才有效
	 *
	 * @param content 文本内容
	 * @param config  二维码配置，包括宽度、高度、边距、颜色等
	 * @return 二维码图片（黑白）
	 * @since 4.1.14
	 */
	public static BufferedImage generate(final String content, final QrConfig config) {
		return new QrImage(content, ObjUtil.defaultIfNull(config, QrConfig::new));
	}

	// ------------------------------------------------------------------------------------------------------------------- encode

	/**
	 * 将文本内容编码为条形码或二维码
	 *
	 * @param content 文本内容
	 * @param config  二维码配置，包括宽度、高度、边距、颜色、格式等
	 * @return {@link BitMatrix}
	 * @since 4.1.2
	 */
	public static BitMatrix encode(final CharSequence content, final QrConfig config) {
		return QrEncoder.of(config).encode(content);
	}

	// ------------------------------------------------------------------------------------------------------------------- decode

	/**
	 * 解码二维码或条形码图片为文本
	 *
	 * @param qrCodeInputstream 二维码输入流
	 * @return 解码文本
	 */
	public static String decode(final InputStream qrCodeInputstream) {
		BufferedImage image = null;
		try {
			image = ImgUtil.read(qrCodeInputstream);
			return decode(image);
		} finally {
			ImgUtil.flush(image);
		}
	}

	/**
	 * 解码二维码或条形码图片为文本
	 *
	 * @param qrCodeFile 二维码文件
	 * @return 解码文本
	 */
	public static String decode(final File qrCodeFile) {
		BufferedImage image = null;
		try {
			image = ImgUtil.read(qrCodeFile);
			return decode(image);
		} finally {
			ImgUtil.flush(image);
		}
	}

	/**
	 * 将二维码或条形码图片解码为文本
	 *
	 * @param image {@link Image} 二维码图片
	 * @return 解码后的文本
	 */
	public static String decode(final Image image) {
		return decode(image, true, false);
	}

	/**
	 * 将二维码或条形码图片解码为文本<br>
	 * 此方法会尝试使用{@link HybridBinarizer}和{@link GlobalHistogramBinarizer}两种模式解析<br>
	 * 需要注意部分二维码如果不带logo，使用PureBarcode模式会解析失败，此时须设置此选项为false。
	 *
	 * @param image         {@link Image} 二维码图片
	 * @param isTryHarder   是否优化精度
	 * @param isPureBarcode 是否使用复杂模式，扫描带logo的二维码设为true
	 * @return 解码后的文本
	 * @since 4.3.1
	 */
	public static String decode(final Image image, final boolean isTryHarder, final boolean isPureBarcode) {
		return QrDecoder.of(isTryHarder, isPureBarcode).decode(image);
	}

	/**
	 * 将二维码或条形码图片解码为文本<br>
	 * 此方法会尝试使用{@link HybridBinarizer}和{@link GlobalHistogramBinarizer}两种模式解析<br>
	 * 需要注意部分二维码如果不带logo，使用PureBarcode模式会解析失败，此时须设置此选项为false。
	 *
	 * @param image {@link Image} 二维码图片
	 * @param hints 自定义扫码配置，包括算法、编码、复杂模式等
	 * @return 解码后的文本
	 * @since 5.7.12
	 */
	public static String decode(final Image image, final Map<DecodeHintType, Object> hints) {
		return QrDecoder.of(hints).decode(image);
	}

	/**
	 * BitMatrix转BufferedImage
	 *
	 * @param matrix    BitMatrix
	 * @param foreColor 前景色
	 * @param backColor 背景色(null表示透明背景)
	 * @return BufferedImage
	 * @since 4.1.2
	 */
	public static BufferedImage toImage(final BitMatrix matrix, final int foreColor, final Integer backColor) {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();
		final BufferedImage image = new BufferedImage(width, height, null == backColor ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					image.setRGB(x, y, foreColor);
				} else if (null != backColor) {
					image.setRGB(x, y, backColor);
				}
			}
		}
		return image;
	}

	/**
	 * @param content  内容
	 * @param qrConfig 二维码配置，包括宽度、高度、边距、颜色等
	 * @return SVG矢量图（字符串）
	 * @since 5.8.6
	 */
	public static String generateAsSvg(final String content, final QrConfig qrConfig) {
		return toSVG(encode(content, qrConfig), qrConfig);
	}

	/**
	 * BitMatrix转SVG(字符串)
	 *
	 * @param matrix BitMatrix
	 * @param config {@link QrConfig}
	 * @return SVG矢量图（字符串）
	 */
	public static String toSVG(final BitMatrix matrix, final QrConfig config) {
		return new QrSVG(matrix, config).toString();
	}

	/**
	 * 生成ASCII Art字符画形式的二维码
	 *
	 * @param content  内容
	 * @param qrConfig 二维码配置，仅宽度、高度、边距配置有效
	 * @return ASCII Art字符画形式的二维码
	 * @since 5.8.6
	 */
	public static String generateAsAsciiArt(final String content, final QrConfig qrConfig) {
		return toAsciiArt(encode(content, qrConfig), qrConfig);
	}

	/**
	 * BitMatrix转ASCII Art字符画形式的二维码
	 *
	 * @param bitMatrix BitMatrix
	 * @param qrConfig  QR设置
	 * @return ASCII Art字符画形式的二维码
	 * @since 5.8.6
	 */
	public static String toAsciiArt(final BitMatrix bitMatrix, final QrConfig qrConfig) {
		return new QrAsciiArt(bitMatrix, qrConfig).toString();
	}

	// region ----- Private Methods

	/**
	 * 将文本转换为Base64编码的Data URI。
	 *
	 * @param txt 需要转换为Base64编码Data URI的文本。
	 * @return 转换后的Base64编码Data URI字符串。
	 */
	private static String txtToBase64DataUri(final String txt) {
		return UrlUtil.getDataUriBase64("text/plain", Base64.encode(txt));
	}

	/**
	 * 将SVG字符串转换为Base64数据URI格式。
	 * <p>此方法通过将SVG内容编码为Base64，并将其封装在数据URI中，以便于在HTML或CSS中直接嵌入SVG图像。</p>
	 *
	 * @param svg SVG图像的内容，为字符串形式。
	 * @return 转换后的Base64数据URI字符串，可用于直接在HTML或CSS中显示SVG图像。
	 */
	private static String svgToBase64DataUri(final String svg) {
		return UrlUtil.getDataUriBase64("image/svg+xml", Base64.encode(svg));
	}

	// endregion
}
