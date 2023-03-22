package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class PunyCodeTest {

	@Test
	public void encodeDecodeTest(){
		final String text = "Hutool编码器";
		final String strPunyCode = PunyCode.encode(text);
		Assert.assertEquals("Hutool-ux9js33tgln", strPunyCode);
		String decode = PunyCode.decode("Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
		decode = PunyCode.decode("xn--Hutool-ux9js33tgln");
		Assert.assertEquals(text, decode);
	}

	@Test
	public void encodeDecodeTest2(){
		// 无需编码和解码
		final String text = "Hutool";
		final String strPunyCode = PunyCode.encode(text);
		Assert.assertEquals("Hutool", strPunyCode);
	}

	@Test
	public void encodeEncodeDomainTest(){
		final String domain = "赵新虎.中国";
		final String strPunyCode = PunyCode.encodeDomain(domain);
		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(decode, domain);
	}

	@Test
	public void encodeEncodeDomainTest2(){
		final String domain = "赵新虎.com";
		final String strPunyCode = PunyCode.encodeDomain(domain);
		Assert.assertEquals("xn--efvz93e52e.com", strPunyCode);
		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(domain, decode);
	}

	@Test
	public void encodeEncodeDomainTest3(){
		final String domain = "赵新虎.COM";
		final String strPunyCode = PunyCode.encodeDomain(domain);
		Assert.assertEquals("xn--efvz93e52e.COM", strPunyCode);
		final String decode = PunyCode.decodeDomain(strPunyCode);
		Assert.assertEquals(domain, decode);
	}
}
