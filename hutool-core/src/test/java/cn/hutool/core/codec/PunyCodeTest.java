package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class PunyCodeTest {

	@Test
	public void encodeDecodeTest() {
		final String text = "Hutool编码器";
		final String strPunyCode = PunyCode.encode(text);
		Assert.assertEquals("Hutool-ux9js33tgln", strPunyCode);
		String decode = PunyCode.decode("Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
		decode = PunyCode.decode("xn--Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest() {
		// 全中文
		final String text = "百度.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assert.assertEquals("xn--wxtr44c.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest2() {
		// 中英文分段
		final String text = "hutool.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assert.assertEquals("xn--hutool-.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest3() {
		// 中英文混合
		final String text = "hutool工具.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assert.assertEquals("xn--hutool-up2j943f.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(text, decode);
	}
}
