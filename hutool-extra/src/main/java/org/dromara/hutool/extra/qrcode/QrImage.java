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

import org.dromara.hutool.swing.img.Img;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * 二维码图片封装
 *
 * @author looly
 * @since 6.0.0
 */
public class QrImage extends BufferedImage {

	/**
	 * 构造
	 *
	 * @param content 文本内容
	 * @param config {@link QrConfig} 二维码配置，包括宽度、高度、边距、颜色、格式等
	 */
	public QrImage(final String content, final QrConfig config) {
		this(QrCodeUtil.encode(content, config), config);
	}

	/**
	 * 构造
	 *
	 * @param matrix {@link BitMatrix}
	 * @param config {@link QrConfig}，非空
	 */
	public QrImage(final BitMatrix matrix, final QrConfig config) {
		super(matrix.getWidth(), matrix.getHeight(), null == config.backColor ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		init(matrix, config);
	}

	/**
	 * 初始化
	 *
	 * @param matrix {@link BitMatrix}
	 * @param config {@link QrConfig}
	 */
	private void init(final BitMatrix matrix, final QrConfig config) {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();
		final Integer foreColor = config.foreColor;
		final Integer backColor = config.backColor;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					setRGB(x, y, foreColor);
				} else if (null != backColor) {
					setRGB(x, y, backColor);
				}
			}
		}

		final Image logoImg = config.img;
		if (null != logoImg && BarcodeFormat.QR_CODE == config.format) {
			// 只有二维码可以贴图
			final int qrWidth = getWidth();
			final int qrHeight = getHeight();
			final int imgWidth;
			final int imgHeight;
			// 按照最短的边做比例缩放
			if (qrWidth < qrHeight) {
				imgWidth = qrWidth / config.ratio;
				imgHeight = logoImg.getHeight(null) * imgWidth / logoImg.getWidth(null);
			} else {
				imgHeight = qrHeight / config.ratio;
				imgWidth = logoImg.getWidth(null) * imgHeight / logoImg.getHeight(null);
			}

			Img.from(this).pressImage(//
					Img.from(logoImg).round(0.3).getImg(), // 圆角
					new Rectangle(imgWidth, imgHeight), // 位置
					1//不透明
			);
		}
	}
}
