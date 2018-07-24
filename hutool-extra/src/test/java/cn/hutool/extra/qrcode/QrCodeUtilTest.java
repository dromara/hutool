package cn.hutool.extra.qrcode;

import java.awt.Color;

import org.junit.Ignore;
import org.junit.Test;

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
		QrCodeUtil.generate("http://hutool.cn/", 300, 300, FileUtil.file("e:/qrcode.jpg"));
	}
	
	@Test
	@Ignore
	public void generateTest2() {
		QrConfig config = new QrConfig();
		config.setMargin(3);
		config.setForeColor(Color.CYAN.getRGB());
		config.setBackColor(Color.GRAY.getRGB());
		QrCodeUtil.generate("http://hutool.cn/", config, FileUtil.file("e:/qrcode.jpg"));
	}

	@Test
	@Ignore
	public void decodeTest() {
		String decode = QrCodeUtil.decode(FileUtil.file("e:/test.png"));
		Console.log(decode);
	}
}
