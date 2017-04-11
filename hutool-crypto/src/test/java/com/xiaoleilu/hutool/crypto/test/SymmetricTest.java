package com.xiaoleilu.hutool.crypto.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricCriptor;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 对称加密算法单元测试
 * @author Looly
 *
 */
public class SymmetricTest {
	
	@Test
	public void aesTest(){
		String content = "test中文";
		
		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
		
		SymmetricCriptor aes = new SymmetricCriptor(SymmetricAlgorithm.AES, key);
		byte[] encrypt = aes.encrypt(content);
		byte[] decrypt = aes.decrypt(encrypt);
		
		Assert.assertEquals(content, StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
		
		String encryptHex = aes.encryptHex(content);
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
		
		Assert.assertEquals(content, decryptStr);
	}
}
