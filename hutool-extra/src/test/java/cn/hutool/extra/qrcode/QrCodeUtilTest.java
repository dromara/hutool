package cn.hutool.extra.qrcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
//	@Ignore
	public void generateCustomTest() {
		QrConfig config = new QrConfig();
		config.setMargin(0);
		config.setForeColor(Color.CYAN);
		// 背景色透明
		config.setBackColor(null);
		config.setErrorCorrection(ErrorCorrectionLevel.H);
		String path = FileUtil.isWindows() ? "d:/test/qrcodeCustom.png" : "~/Desktop/hutool/qrcodeCustom.png";
		QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.touch(path));
	}
	@Test
//	@Ignore
	public void generateNoCustomColorTest() {
		QrConfig config = new QrConfig();
		config.setMargin(0);
		config.setForeColor(null);
		// 背景色透明
		config.setBackColor(null);
		config.setErrorCorrection(ErrorCorrectionLevel.H);
		String path = FileUtil.isWindows() ? "d:/test/qrcodeCustom.png" : "~/Desktop/hutool/qrcodeCustom.png";
		QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.touch(path));
	}

	@Test
	@Ignore
	public void generateWithLogoTest() {
		String icon = FileUtil.isWindows() ? "d:/test/pic/face.jpg" : "~/Desktop/hutool/pic/face.jpg";
		String targetPath = FileUtil.isWindows() ? "d:/test/qrcodeWithLogo.jpg" : "~/Desktop/hutool/qrcodeWithLogo.jpg";
		QrCodeUtil.generate(//
				"https://hutool.cn/", //
				QrConfig.create().setImg(icon), //
				FileUtil.touch(targetPath));
	}

	@Test
	@Ignore
	public void decodeTest() {
		String decode = QrCodeUtil.decode(FileUtil.file("d:/test/pic/qr.png"));
		Console.log(decode);
	}

	@Test
	@Ignore
	public void decodeTest2() {
		// 条形码
		String decode = QrCodeUtil.decode(FileUtil.file("d:/test/90.png"));
		Console.log(decode);
	}

	@Test
	public void generateAsBase64Test() {
		String base64 = QrCodeUtil.generateAsBase64("https://hutool.cn/", new QrConfig(400, 400), "png");
		Assert.assertNotNull(base64);
	}

	@Test
	@Ignore
	public void generateAsBase64Test2() {
		byte[] bytes = FileUtil.readBytes(
				new File("d:/test/qr.png"));
		String encode = Base64.encode(bytes);
		String base641 = QrCodeUtil.generateAsBase64("https://hutool.cn/", new QrConfig(400, 400), "png", encode);
		Assert.assertNotNull(base641);
	}

	@Test
	public void generateAsBase64Test3() {
		String base64 = QrCodeUtil.generateAsBase64("https://hutool.cn/", new QrConfig(400, 400), "svg");
		Assert.assertNotNull(base64);
		System.out.println(base64);
	}

	@Test
	@Ignore
	public void decodeTest3() {
		final String decode = QrCodeUtil.decode(ImgUtil.read("d:/test/qr_a.png"), false, true);
		Console.log(decode);
	}

	@Test
	public void pdf417Test() {
		final BufferedImage image = QrCodeUtil.generate("content111", BarcodeFormat.PDF_417, QrConfig.create());
		Assert.assertNotNull(image);
	}

	@Test
	public void generateDataMatrixTest() {
		QrConfig qrConfig = QrConfig.create();
		qrConfig.setShapeHint(SymbolShapeHint.FORCE_RECTANGLE);
		final BufferedImage image = QrCodeUtil.generate("content111", BarcodeFormat.DATA_MATRIX, qrConfig);
		Assert.assertNotNull(image);
		QrConfig config = QrConfig.create();
		config.setShapeHint(SymbolShapeHint.FORCE_SQUARE);
		final BufferedImage imageSquare = QrCodeUtil.generate("content111", BarcodeFormat.DATA_MATRIX, qrConfig);
		Assert.assertNotNull(imageSquare);
	}

	@Test
	public void generateSvgTest() {
		QrConfig qrConfig = QrConfig.create().setImg("d:/test/logo.png")
				.setForeColor(Color.blue)
				.setBackColor(Color.pink)
				.setRatio(8)
				.setErrorCorrection(ErrorCorrectionLevel.M)
				.setMargin(1);
		String svg = QrCodeUtil.generateAsSvg("https://hutool.cn/", qrConfig);
		Assert.assertNotNull(svg);
		FileUtil.writeString(svg, FileUtil.touch("d:/test/hutool_qr.svg"),StandardCharsets.UTF_8);
	}

	@Test
	public void generateAsciiArtTest() {
		QrConfig qrConfig = QrConfig.create()
				.setForeColor(Color.BLUE)
				.setBackColor(Color.MAGENTA)
				.setWidth(0)
				.setHeight(0).setMargin(1);
		String asciiArt = QrCodeUtil.generateAsAsciiArt("https://hutool.cn/",qrConfig);
		Assert.assertNotNull(asciiArt);
		System.out.println(asciiArt);
	}
	@Test
	public void generateAsciiArtNoCustomColorTest() {
		QrConfig qrConfig = QrConfig.create()
				.setForeColor(null)
				.setBackColor(null)
				.setWidth(0)
				.setHeight(0).setMargin(1);
		String asciiArt = QrCodeUtil.generateAsAsciiArt("https://hutool.cn/",qrConfig);
		Assert.assertNotNull(asciiArt);
		System.out.println(asciiArt);
	}


	@Test
	public void generateToFileTest() {
		QrConfig qrConfig = QrConfig.create()
				.setForeColor(Color.BLUE)
				.setBackColor(new Color(0,200,255))
				.setWidth(0)
				.setHeight(0).setMargin(1);
		File qrFile = QrCodeUtil.generate("https://hutool.cn/", qrConfig, FileUtil.touch("d:/test/ascii_art_qr_code.txt"));
		BufferedReader reader = FileUtil.getReader(qrFile, StandardCharsets.UTF_8);
		reader.lines().forEach(System.out::println);
	}

	@Test
	public void generateToStreamTest() {
		QrConfig qrConfig = QrConfig.create()
				.setForeColor(Color.BLUE)
				.setBackColor(new Color(0,200,255))
				.setWidth(0)
				.setHeight(0).setMargin(1);
		String filepath = "d:/test/qr_stream_to_txt.txt";
		try (BufferedOutputStream outputStream = FileUtil.getOutputStream(filepath)) {
			QrCodeUtil.generate("https://hutool.cn/", qrConfig,"txt", outputStream);
		}catch (IOException e){
			e.printStackTrace();
		}
		BufferedReader reader = FileUtil.getReader(filepath, StandardCharsets.UTF_8);
		reader.lines().forEach(System.out::println);
	}

}
