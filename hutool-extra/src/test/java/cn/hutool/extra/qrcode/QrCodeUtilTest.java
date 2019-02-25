package cn.hutool.extra.qrcode;

import java.awt.Color;

import org.junit.Ignore;
import org.junit.Test;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;

/**
 * 二维码工具类单元测试
 * 
 * @author looly
 *
 */
public class QrCodeUtilTest {

	@Test
	@Ignore
	public void generateTest() {
		QrCodeUtil.generate("https://hutool.cn/", 300, 300, FileUtil.file("e:/qrcode.jpg"));
	}

	@Test
	@Ignore
	public void generateCustomTest() {
		QrConfig config = new QrConfig();
		config.setMargin(3);
		config.setForeColor(Color.CYAN.getRGB());
		config.setBackColor(Color.GRAY.getRGB());
		config.setErrorCorrection(ErrorCorrectionLevel.H);
		QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.file("e:/qrcodeCustom.jpg"));
	}

	@Test
	@Ignore
	public void generateWithLogoTest() {
		QrCodeUtil.generate(//
				"http://hutool.cn/", //
				QrConfig.create().setImg("e:/logo_small.jpg"), //
				FileUtil.file("e:/qrcodeWithLogo.jpg"));
	}

	@Test
//	@Ignore
	public void decodeTest() {
		String decode = QrCodeUtil.decode(FileUtil.file("e:/pic/qr.png"));
		Console.log(decode);
	}
}
