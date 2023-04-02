package org.dromara.hutool.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PunyCodeTest {

	@Test
	public void encodeDecodeTest() {
		final String text = "Hutool编码器";
		final String strPunyCode = PunyCode.encode(text);
		Assertions.assertEquals("Hutool-ux9js33tgln", strPunyCode);
		String decode = PunyCode.decode("Hutool-ux9js33tgln");
		Assertions.assertEquals(text, decode);
		decode = PunyCode.decode("xn--Hutool-ux9js33tgln");
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeTest2(){
		// 无需编码和解码
		String text = "Hutool";
		String strPunyCode = PunyCode.encode(text);
		Assertions.assertEquals("Hutool", strPunyCode);
	}

	@Test
	public void encodeDecodeDomainTest() {
		// 全中文
		final String text = "百度.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("xn--wxtr44c.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest2() {
		// 中英文分段
		final String text = "hutool.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("hutool.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeDomainTest3() {
		// 中英文混合
		final String text = "hutool工具.中国";
		final String strPunyCode = PunyCode.encodeDomain(text);
		Assertions.assertEquals("xn--hutool-up2j943f.xn--fiqs8s", strPunyCode);

		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(text, decode);
	}

	@Test
	public void encodeEncodeDomainTest2(){
		String domain = "赵新虎.com";
		String strPunyCode = PunyCode.encodeDomain(domain);
		Assertions.assertEquals("xn--efvz93e52e.com", strPunyCode);
		String decode = PunyCode.decodeDomain(strPunyCode);
		Assertions.assertEquals(domain, decode);
	}
}
