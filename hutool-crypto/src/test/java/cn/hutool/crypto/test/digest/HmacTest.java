package cn.hutool.crypto.test.digest;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.ZUC;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.spec.IvParameterSpec;

/**
 * Hmac单元测试
 * @author Looly
 *
 */
public class HmacTest {

	@Test
	public void hmacTest(){
		String testStr = "test中文";

		byte[] key = "password".getBytes();
		HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);

		String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
	}

	@Test
	public void hmacMd5Test(){
		String testStr = "test中文";

		HMac mac = SecureUtil.hmacMd5("password");

		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);

		String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
	}

	@Test
	public void hmacSha1Test(){
		HMac mac = SecureUtil.hmacSha1("password");

		String testStr = "test中文";
		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("1dd68d2f119d5640f0d416e99d3f42408b88d511", macHex1);

		String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("1dd68d2f119d5640f0d416e99d3f42408b88d511", macHex2);
	}

	@Test
	public void zuc128MacTest(){
		byte[] iv = new byte[16];
		final byte[] key = new byte[16];
		HMac mac = new HMac("ZUC-128",
				KeyUtil.generateKey(ZUC.ZUCAlgorithm.ZUC_128.getValue(), key),
				new IvParameterSpec(iv));

		String testStr = "test中文";
		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("1e0b9455", macHex1);
	}

	@Test
	public void zuc256MacTest(){
		byte[] iv = new byte[25];
		final byte[] key = new byte[32];
		HMac mac = new HMac("ZUC-256",
				KeyUtil.generateKey(ZUC.ZUCAlgorithm.ZUC_128.getValue(), key),
				new IvParameterSpec(iv));

		String testStr = "test中文";
		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("d9ad618357c1bfb1d9d1200a763d5eaa", macHex1);
	}
}
