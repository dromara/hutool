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

package org.dromara.hutool.extra.qrcode;

import org.dromara.hutool.core.codec.Decoder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.swing.img.ImgUtil;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码（条形码等）解码器
 *
 * @author looly
 * @since 6.0.0
 */
public class QrDecoder implements Decoder<Image, String> {

	private final Map<DecodeHintType, Object> hints;

	/**
	 * 创建二维码（条形码等）解码器，用于将二维码（条形码等）解码为所代表的内容字符串
	 *
	 * @param isTryHarder   是否优化精度
	 * @param isPureBarcode 是否使用复杂模式，扫描带logo的二维码设为true
	 * @return QrDecoder
	 */
	public static QrDecoder of(final boolean isTryHarder, final boolean isPureBarcode) {
		return of(buildHints(isTryHarder, isPureBarcode));
	}

	/**
	 * 创建二维码（条形码等）解码器
	 *
	 * @param hints 自定义扫码配置，包括算法、编码、复杂模式等
	 * @return QrDecoder
	 */
	public static QrDecoder of(final Map<DecodeHintType, Object> hints) {
		return new QrDecoder(hints);
	}

	/**
	 * 构造
	 *
	 * @param hints 自定义扫码配置，包括算法、编码、复杂模式等
	 */
	public QrDecoder(final Map<DecodeHintType, Object> hints) {
		this.hints = hints;
	}

	@Override
	public String decode(final Image image) {
		final MultiFormatReader formatReader = new MultiFormatReader();
		formatReader.setHints(hints);

		final LuminanceSource source = new BufferedImageLuminanceSource(
			ImgUtil.castToBufferedImage(image, ImgUtil.IMAGE_TYPE_JPG));

		Result result = _decode(formatReader, new HybridBinarizer(source));
		if (null == result) {
			result = _decode(formatReader, new GlobalHistogramBinarizer(source));
		}

		return null != result ? result.getText() : null;
	}

	/**
	 * 解码多种类型的码，包括二维码和条形码
	 *
	 * @param formatReader {@link MultiFormatReader}
	 * @param binarizer    {@link Binarizer}
	 * @return {@link Result}
	 */
	private static Result _decode(final MultiFormatReader formatReader, final Binarizer binarizer) {
		try {
			return formatReader.decodeWithState(new BinaryBitmap(binarizer));
		} catch (final NotFoundException e) {
			return null;
		}
	}

	/**
	 * 创建解码选项
	 *
	 * @param isTryHarder   是否优化精度
	 * @param isPureBarcode 是否使用复杂模式，扫描带logo的二维码设为true
	 * @return 选项Map
	 */
	private static Map<DecodeHintType, Object> buildHints(final boolean isTryHarder, final boolean isPureBarcode) {
		final HashMap<DecodeHintType, Object> hints = new HashMap<>(3, 1);
		hints.put(DecodeHintType.CHARACTER_SET, CharsetUtil.NAME_UTF_8);

		// 优化精度
		if (isTryHarder) {
			hints.put(DecodeHintType.TRY_HARDER, true);
		}
		// 复杂模式，开启PURE_BARCODE模式
		if (isPureBarcode) {
			hints.put(DecodeHintType.PURE_BARCODE, true);
		}
		return hints;
	}
}
