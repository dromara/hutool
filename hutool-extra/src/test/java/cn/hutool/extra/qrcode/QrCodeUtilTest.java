package cn.hutool.extra.qrcode;

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
		QrCodeUtil.generate("http://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode.jpg"));
	}

	@Test
	@Ignore
	public void decodeTest() {
		String decode = QrCodeUtil.decode(FileUtil.file("d:/qrcode.jpg"));
		Console.log(decode);
	}
}
