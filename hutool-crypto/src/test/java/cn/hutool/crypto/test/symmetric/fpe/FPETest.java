package cn.hutool.crypto.test.symmetric.fpe;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.fpe.FPE;
import org.bouncycastle.crypto.util.BasicAlphabetMapper;
import org.junit.Assert;
import org.junit.Test;

public class FPETest {

	@Test
	public void ff1Test(){
		// 映射字符表，规定了明文和密文的字符范围
		BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF1, keyBytes, numberMapper, null);

		// 原始数据
		String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assert.assertEquals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assert.assertEquals(phone, decrypt);
	}

	@Test
	public void ff3Test(){
		// 映射字符表，规定了明文和密文的字符范围
		BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF3_1, keyBytes, numberMapper, null);

		// 原始数据
		String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assert.assertEquals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assert.assertEquals(phone, decrypt);
	}
}
