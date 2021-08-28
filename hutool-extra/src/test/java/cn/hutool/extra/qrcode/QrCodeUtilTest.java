package cn.hutool.extra.qrcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

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

}
