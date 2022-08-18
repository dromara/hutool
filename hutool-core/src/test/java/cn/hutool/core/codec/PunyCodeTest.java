package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class PunyCodeTest {

	@Test
	public void encodeDecodeTest(){
		String text = "Hutool编码器";
		String strPunyCode = PunyCode.encode(text);
		Assert.assertEquals("Hutool-ux9js33tgln", strPunyCode);
		String decode = PunyCode.decode("Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
		decode = PunyCode.decode("xn--Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
	}

	@Test
	public void encodeEncodeDomainTest(){
		String domain = "赵新虎.中国";
		String strPunyCode = PunyCode.encodeDomain(domain);
		String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(decode, domain);
	}
}
