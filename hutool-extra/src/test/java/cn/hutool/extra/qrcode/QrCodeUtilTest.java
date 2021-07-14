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
 *
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
		QrConfig config = new QrConfig();
		config.setMargin(0);
		config.setForeColor(Color.CYAN);
		// 背景色透明
		config.setBackColor(null);
		config.setErrorCorrection(ErrorCorrectionLevel.H);
		String path = FileUtil.isWindows() ? "d:/hutool/qrcodeCustom.png" : "~/Desktop/hutool/qrcodeCustom.png";
		if (!FileUtil.file(path).getParentFile().exists()) {
			FileUtil.file(path).getParentFile().mkdirs();
		}
		QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.file(path));
	}

	@Test
	@Ignore
	public void generateWithLogoTest() {
		String icon = FileUtil.isWindows() ? "d:/hutool/pic/face.jpg" : "~/Desktop/hutool/pic/face.jpg";
		String targetPath = FileUtil.isWindows() ? "d:/hutool/qrcodeWithLogo.jpg" : "~/Desktop/hutool/qrcodeWithLogo.jpg";
		QrCodeUtil.generate(//
				"http://hutool.cn/", //
				QrConfig.create().setImg(icon), //
				FileUtil.file(targetPath));
	}

	@Test
	@Ignore
	public void decodeTest() {
		String decode = QrCodeUtil.decode(FileUtil.file("e:/pic/qr.png"));
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
	@Ignore
	public void generateAsBase64Test(){
		String base64 = QrCodeUtil.generateAsBase64("http://hutool.cn/", new QrConfig(400, 400), "png");
		System.out.println(base64);

		byte[] bytes = FileUtil.readBytes(
			new File("d:/test/qr.png"));
		String encode = Base64.encode(bytes);
		String base641 = QrCodeUtil.generateAsBase64("http://hutool.cn/", new QrConfig(400, 400), "png", encode);
		System.out.println(base641);

	}

}
