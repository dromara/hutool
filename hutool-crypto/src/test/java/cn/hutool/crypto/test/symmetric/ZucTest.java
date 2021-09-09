package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ZucTest {

	@Test
	public void zuc128Test(){
		final SecretKey secretKey = KeyUtil.generateKey("zuc-128");
		byte[] iv = RandomUtil.randomBytes(16);
		final SymmetricCrypto zuc = new SymmetricCrypto("zuc-128", secretKey, new IvParameterSpec(iv));

		String msg = RandomUtil.randomString(500);
		byte[] crypt2 = zuc.encrypt(msg);
		String msg2 = zuc.decryptStr(crypt2, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(msg, msg2);
	}

	@Test
	public void zuc256Test(){
		final SecretKey secretKey = KeyUtil.generateKey("zuc-256");
		byte[] iv = RandomUtil.randomBytes(25);
		final SymmetricCrypto zuc = new SymmetricCrypto("zuc-256", secretKey, new IvParameterSpec(iv));

		String msg = RandomUtil.randomString(500);
		byte[] crypt2 = zuc.encrypt(msg);
		String msg2 = zuc.decryptStr(crypt2, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(msg, msg2);
	}
}
