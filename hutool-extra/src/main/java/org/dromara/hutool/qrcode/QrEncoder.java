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

package org.dromara.hutool.qrcode;

import org.dromara.hutool.codec.Encoder;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.ObjUtil;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码（条形码等）编码器，用于将文本内容转换为二维码
 *
 * @author looly
 * @since 6.0.0
 */
public class QrEncoder implements Encoder<CharSequence, BitMatrix> {

	/**
	 * 创建QrEncoder
	 *
	 * @param config {@link QrConfig}
	 * @return QrEncoder
	 */
	public static QrEncoder of(final QrConfig config) {
		return new QrEncoder(config);
	}

	private final QrConfig config;

	/**
	 * 构造
	 *
	 * @param config {@link QrConfig}
	 */
	public QrEncoder(final QrConfig config) {
		this.config = ObjUtil.defaultIfNull(config, QrConfig::of);
	}

	@Override
	public BitMatrix encode(final CharSequence content) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		final BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(
					StrUtil.toString(content),
					config.format, config.width, config.height,
					config.toHints());
		} catch (final WriterException e) {
			throw new QrCodeException(e);
		}

		return bitMatrix;
	}
}
