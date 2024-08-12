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
