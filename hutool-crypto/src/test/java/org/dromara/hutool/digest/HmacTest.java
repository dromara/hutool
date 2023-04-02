package org.dromara.hutool.digest;

import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.KeyUtil;
import org.dromara.hutool.SecureUtil;
import org.dromara.hutool.symmetric.ZUC;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.IvParameterSpec;

/**
 * Hmac单元测试
 * @author Looly
 *
 */
public class HmacTest {

	@Test
	public void hmacTest(){
		final String testStr = "test中文";

		final byte[] key = "password".getBytes();
		final HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);

		final String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.UTF_8));
		Assertions.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
	}

	@Test
	public void hmacMd5Test(){
		final String testStr = "test中文";

		final HMac mac = SecureUtil.hmacMd5("password");

		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);

		final String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.UTF_8));
		Assertions.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
	}

	@Test
	public void hmacSha1Test(){
		final HMac mac = SecureUtil.hmacSha1("password");

		final String testStr = "test中文";
		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("1dd68d2f119d5640f0d416e99d3f42408b88d511", macHex1);

		final String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.UTF_8));
		Assertions.assertEquals("1dd68d2f119d5640f0d416e99d3f42408b88d511", macHex2);
	}

	@Test
	public void zuc128MacTest(){
		final byte[] iv = new byte[16];
		final byte[] key = new byte[16];
		final HMac mac = new HMac("ZUC-128",
				KeyUtil.generateKey(ZUC.ZUCAlgorithm.ZUC_128.getValue(), key),
				new IvParameterSpec(iv));

		final String testStr = "test中文";
		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("1e0b9455", macHex1);
	}

	@Test
	public void zuc256MacTest(){
		final byte[] key = new byte[32];
		final byte[] iv = new byte[25];
		final HMac mac = new HMac("ZUC-256",
				KeyUtil.generateKey(ZUC.ZUCAlgorithm.ZUC_128.getValue(), key),
				new IvParameterSpec(iv));

		final String testStr = "test中文";
		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("d9ad618357c1bfb1d9d1200a763d5eaa", macHex1);
	}

	@Test
	public void sm4CMACTest(){
		// https://github.com/dromara/hutool/issues/2206
		final byte[] key = new byte[16];
		final HMac mac = new HMac(HmacAlgorithm.SM4CMAC,
				KeyUtil.generateKey("SM4", key));

		// 原文
		final String testStr = "test中文";

		final String macHex1 = mac.digestHex(testStr);
		Assertions.assertEquals("58a0d231315664af51b858a174eabc21", macHex1);
	}
}
