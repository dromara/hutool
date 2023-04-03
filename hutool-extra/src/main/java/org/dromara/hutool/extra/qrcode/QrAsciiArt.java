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

import org.dromara.hutool.core.lang.ansi.AnsiElement;
import org.dromara.hutool.core.lang.ansi.AnsiEncoder;
import org.dromara.hutool.swing.img.color.ColorUtil;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码的AsciiArt表示
 *
 * @author Tom Xin
 */
public class QrAsciiArt {

	private final BitMatrix matrix;
	private final QrConfig qrConfig;

	/**
	 * 构造
	 * @param matrix {@link BitMatrix}
	 * @param qrConfig {@link QrConfig}
	 */
	public QrAsciiArt(final BitMatrix matrix, final QrConfig qrConfig) {
		this.matrix = matrix;
		this.qrConfig = qrConfig;
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Override
	public String toString() {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();


		final AnsiElement foreground = qrConfig.foreColor == null ? null : ColorUtil.toAnsiColor(qrConfig.foreColor, true, false);
		final AnsiElement background = qrConfig.backColor == null ? null : ColorUtil.toAnsiColor(qrConfig.backColor, true, true);

		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= height; i += 2) {
			final StringBuilder rowBuilder = new StringBuilder();
			for (int j = 0; j < width; j++) {
				final boolean tp = matrix.get(i, j);
				final boolean bt = i + 1 >= height || matrix.get(i + 1, j);
				if (tp && bt) {
					rowBuilder.append(' ');//'\u0020'
				} else if (tp) {
					rowBuilder.append('▄');//'\u2584'
				} else if (bt) {
					rowBuilder.append('▀');//'\u2580'
				} else {
					rowBuilder.append('█');//'\u2588'
				}
			}
			builder.append(AnsiEncoder.encode(foreground, background, rowBuilder)).append('\n');
		}
		return builder.toString();
	}
}
