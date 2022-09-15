package cn.hutool.extra.qrcode;

import cn.hutool.core.text.StrUtil;
import cn.hutool.swing.img.ImgUtil;
import com.google.zxing.common.BitMatrix;

import java.awt.Color;
import java.awt.Image;

/**
 * 二维码的SVG表示
 *
 * @author Tom Xin
 */
public class QrSVG {

	private final BitMatrix matrix;
	private final QrConfig qrConfig;

	public QrSVG(BitMatrix matrix, QrConfig qrConfig) {
		this.matrix = matrix;
		this.qrConfig = qrConfig;
	}

	@Override
	public String toString() {
		final Image logoImg = qrConfig.img;
		final Integer foreColor = qrConfig.foreColor;
		final Integer backColor = qrConfig.backColor;
		final int ratio = qrConfig.ratio;

		final StringBuilder sb = new StringBuilder();
		int qrWidth = matrix.getWidth();
		int qrHeight = matrix.getHeight();
		int moduleHeight = (qrHeight == 1) ? qrWidth / 2 : 1;
		for (int y = 0; y < qrHeight; y++) {
			for (int x = 0; x < qrWidth; x++) {
				if (matrix.get(x, y)) {
					sb.append(" M").append(x).append(",").append(y).append("h1v").append(moduleHeight).append("h-1z");
				}
			}
		}
		qrHeight *= moduleHeight;
		String logoBase64 = "";
		int logoWidth = 0;
		int logoHeight = 0;
		int logoX = 0;
		int logoY = 0;
		if (logoImg != null) {
			logoBase64 = ImgUtil.toBase64DataUri(logoImg, "png");
			// 按照最短的边做比例缩放
			if (qrWidth < qrHeight) {
				logoWidth = qrWidth / ratio;
				logoHeight = logoImg.getHeight(null) * logoWidth / logoImg.getWidth(null);
			} else {
				logoHeight = qrHeight / ratio;
				logoWidth = logoImg.getWidth(null) * logoHeight / logoImg.getHeight(null);
			}
			logoX = (qrWidth - logoWidth) / 2;
			logoY = (qrHeight - logoHeight) / 2;

		}

		StringBuilder result = StrUtil.builder();
		result.append("<svg width=\"").append(qrWidth).append("\" height=\"").append(qrHeight).append("\" \n");
		if (backColor != null) {
			Color back = new Color(backColor, true);
			result.append("style=\"background-color:rgba(").append(back.getRed()).append(",").append(back.getGreen()).append(",").append(back.getBlue()).append(",").append(back.getAlpha()).append(")\"\n");
		}
		result.append("viewBox=\"0 0 ").append(qrWidth).append(" ").append(qrHeight).append("\" \n");
		result.append("xmlns=\"http://www.w3.org/2000/svg\" \n");
		result.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" >\n");
		result.append("<path d=\"").append(sb).append("\" ");
		if (foreColor != null) {
			Color fore = new Color(foreColor, true);
			result.append("stroke=\"rgba(").append(fore.getRed()).append(",").append(fore.getGreen()).append(",").append(fore.getBlue()).append(",").append(fore.getAlpha()).append(")\"");
		}
		result.append(" /> \n");
		if (StrUtil.isNotBlank(logoBase64)) {
			result.append("<image xlink:href=\"").append(logoBase64).append("\" height=\"").append(logoHeight).append("\" width=\"").append(logoWidth).append("\" y=\"").append(logoY).append("\" x=\"").append(logoX).append("\" />\n");
		}
		result.append("</svg>");
		return result.toString();
	}
}
