package cn.hutool.extra.qrcode;

import cn.hutool.core.codec.BaseN.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.StrUtil;
import cn.hutool.swing.img.ImgUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 二维码工具类单元测试
 *
 * @author looly
 */
public class QrCodeUtilTest {

	@Test
	public void generateTest() {
		final BufferedImage image = QrCodeUtil.generate("https://hutool.cn/", 300, 300);
		Assert.assertNotNull(image);
	}

	@Test
	@Ignore
	public void generateCustomTest() {
		final QrConfig config = QrConfig.of();
		config.setMargin(0);
		config.setForeColor(Color.CYAN);
		// 背景色透明
		config.setBackColor(null);
		config.setErrorCorrection(ErrorCorrectionLevel.H);
		final String path = FileUtil.isWindows() ? "d:/test/qrcodeCustom.png" : "~/Desktop/hutool/qrcodeCustom.png";
		QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.touch(path));
	}

	@Test
	@Ignore
	public void generateWithLogoTest() {
		final String icon = FileUtil.isWindows() ? "d:/test/pic/logo.jpg" : "~/Desktop/hutool/pic/logo.jpg";
		final String targetPath = FileUtil.isWindows() ? "d:/test/qrcodeWithLogo.jpg" : "~/Desktop/hutool/qrcodeWithLogo.jpg";
		QrCodeUtil.generate(//
				"https://hutool.cn/", //
				QrConfig.of().setImg(icon), //
				FileUtil.touch(targetPath));
	}

	@Test
	@Ignore
	public void decodeTest() {
		final String decode = QrCodeUtil.decode(FileUtil.file("d:/test/pic/qr.png"));
		Console.log(decode);
	}

	@Test
	@Ignore
	public void decodeTest2() {
		// 条形码
		final String decode = QrCodeUtil.decode(FileUtil.file("d:/test/90.png"));
		Console.log(decode);
	}

	@Test
	public void generateAsBase64AndDecodeTest() {
		final String url = "https://hutool.cn/";
		String base64 = QrCodeUtil.generateAsBase64DataUri(url, new QrConfig(400, 400), "png");
		Assert.assertNotNull(base64);

		base64 = StrUtil.removePrefix(base64, "data:image/png;base64,");
		final String decode = QrCodeUtil.decode(IoUtil.toStream(Base64.decode(base64)));
		Assert.assertEquals(url, decode);
	}

	@Test
	@Ignore
	public void generateAsBase64Test2() {
		final byte[] bytes = FileUtil.readBytes(new File("d:/test/qr.png"));
		final String base641 = QrCodeUtil.generateAsBase64DataUri("https://hutool.cn/",
				new QrConfig(400, 400), "png", bytes);
		Assert.assertNotNull(base641);
	}

	@Test
	@Ignore
	public void decodeTest3() {
		final String decode = QrCodeUtil.decode(ImgUtil.read("d:/test/qr_a.png"), false, true);
		Console.log(decode);
	}

	@Test
	public void pdf417Test() {
		final BufferedImage image = QrCodeUtil.generate("content111", QrConfig.of().setFormat(BarcodeFormat.PDF_417));
		Assert.assertNotNull(image);
	}

	@Test
	public void generateDataMatrixTest() {
		final QrConfig qrConfig = QrConfig.of();
		qrConfig.setShapeHint(SymbolShapeHint.FORCE_RECTANGLE);
		final BufferedImage image = QrCodeUtil.generate("content111", qrConfig.setFormat(BarcodeFormat.DATA_MATRIX));
		Assert.assertNotNull(image);
		final QrConfig config = QrConfig.of();
		config.setShapeHint(SymbolShapeHint.FORCE_SQUARE);
		final BufferedImage imageSquare = QrCodeUtil.generate("content111", qrConfig.setFormat(BarcodeFormat.DATA_MATRIX));
		Assert.assertNotNull(imageSquare);
	}

	@Test
	@Ignore
	public void generateSvgTest() {
		final QrConfig qrConfig = QrConfig.of()
				.setImg("d:/test/pic/logo.jpg")
				.setForeColor(Color.blue)
				.setBackColor(Color.pink)
				.setRatio(8)
				.setErrorCorrection(ErrorCorrectionLevel.M)
				.setMargin(1);
		final String svg = QrCodeUtil.generateAsSvg("https://hutool.cn/", qrConfig);
		Assert.assertNotNull(svg);
		FileUtil.writeString(svg, FileUtil.touch("d:/test/hutool_qr.svg"), StandardCharsets.UTF_8);
	}

	@Test
	public void generateAsciiArtTest() {
		final QrConfig qrConfig = QrConfig.of()
				.setForeColor(Color.BLUE)
				.setBackColor(Color.MAGENTA)
				.setWidth(0)
				.setHeight(0).setMargin(1);
		final String asciiArt = QrCodeUtil.generateAsAsciiArt("https://hutool.cn/",qrConfig);
		Assert.assertNotNull(asciiArt);
		//Console.log(asciiArt);
	}

	@Test
	public void generateAsciiArtNoCustomColorTest() {
		final QrConfig qrConfig = QrConfig.of()
				.setForeColor(null)
				.setBackColor(null)
				.setWidth(0)
				.setHeight(0).setMargin(1);
		final String asciiArt = QrCodeUtil.generateAsAsciiArt("https://hutool.cn/",qrConfig);
		Assert.assertNotNull(asciiArt);
		//Console.log(asciiArt);
	}
}
