package cn.hutool.crypto.test;

import java.security.KeyPair;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.KeyUtil;

public class KeyUtilTest {
	
	/**
	 * 测试关闭BouncyCastle支持时是否会正常抛出异常，即关闭是否有效
	 */
	@Test(expected=CryptoException.class)
	@Ignore
	public void generateKeyPairTest() {
		GlobalBouncyCastleProvider.setUseBouncyCastle(false);
		KeyPair pair = KeyUtil.generateKeyPair("SM2");
		Assert.assertNotNull(pair);
	}
}
