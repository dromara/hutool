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

package org.dromara.hutool.extra.qrcode;

import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.swing.img.ImgUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

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
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64DataUri(final String content, final QrConfig qrConfig, final String imageType) {
		final BufferedImage img = generate(content, qrConfig);
		return ImgUtil.toBase64DataUri(img, imageType);
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
	 * @param width      宽度
	 * @param height     高度
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 */
	public static File generate(final String content, final int width, final int height, final File targetFile) {
		final BufferedImage image = generate(content, width, height);
		ImgUtil.write(image, targetFile);
		return targetFile;
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
		final BufferedImage image = generate(content, config);
		ImgUtil.write(image, targetFile);
		return targetFile;
	}

	/**
	 * 生成二维码到输出流
	 *
	 * @param content   文本内容
	 * @param width     宽度
	 * @param height    高度
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param out       目标流
	 */
	public static void generate(final String content, final int width, final int height, final String imageType, final OutputStream out) {
		final BufferedImage image = generate(content, width, height);
		ImgUtil.write(image, imageType, out);
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
		final BufferedImage image = generate(content, config);
		ImgUtil.write(image, imageType, out);
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
		return decode(ImgUtil.read(qrCodeInputstream));
	}

	/**
	 * 解码二维码或条形码图片为文本
	 *
	 * @param qrCodeFile 二维码文件
	 * @return 解码文本
	 */
	public static String decode(final File qrCodeFile) {
		return decode(ImgUtil.read(qrCodeFile));
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
}
