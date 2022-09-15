package cn.hutool.extra.qrcode;

import cn.hutool.core.lang.ansi.AnsiElement;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import cn.hutool.swing.img.color.ColorUtil;
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
	public QrAsciiArt(BitMatrix matrix, QrConfig qrConfig) {
		this.matrix = matrix;
		this.qrConfig = qrConfig;
	}

	@Override
	public String toString() {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();


		final AnsiElement foreground = qrConfig.foreColor == null ? null : ColorUtil.toAnsiColor(qrConfig.foreColor, true, false);
		final AnsiElement background = qrConfig.backColor == null ? null : ColorUtil.toAnsiColor(qrConfig.backColor, true, true);

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= height; i += 2) {
			StringBuilder rowBuilder = new StringBuilder();
			for (int j = 0; j < width; j++) {
				boolean tp = matrix.get(i, j);
				boolean bt = i + 1 >= height || matrix.get(i + 1, j);
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
