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

			// 原图片上直接绘制水印
			Img.from(this).pressImage(//
					Img.from(logoImg).round(config.imgRound).getImg(), // 圆角
					new Rectangle(imgWidth, imgHeight), // 位置
					1//不透明
			);
		}
	}
}
