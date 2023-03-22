package cn.hutool.crypto.digest;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.digest.mac.Mac;
import cn.hutool.crypto.digest.mac.SM4MacEngine;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.junit.Assert;
import org.junit.Test;

public class CBCBlockCipherMacEngineTest {

	@Test
	public void SM4CMACTest(){
		// https://github.com/dromara/hutool/issues/2206
		final byte[] key = new byte[16];
		final CipherParameters parameter = new KeyParameter(KeyUtil.generateKey("SM4", key).getEncoded());
		Mac mac = new Mac(new SM4MacEngine(parameter));

		// 原文
		String testStr = "test中文";

		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("3212e848db7f816a4bd591ad9948debf", macHex1);
	}

	@Test
	public void SM4CMACWithIVTest(){
		// https://github.com/dromara/hutool/issues/2206
		final byte[] key = new byte[16];
		final byte[] iv = new byte[16];
		CipherParameters parameter = new KeyParameter(KeyUtil.generateKey("SM4", key).getEncoded());
		parameter = new ParametersWithIV(parameter, iv);
		Mac mac = new Mac(new SM4MacEngine(parameter));

		// 原文
		String testStr = "test中文";

		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("3212e848db7f816a4bd591ad9948debf", macHex1);
	}
}
