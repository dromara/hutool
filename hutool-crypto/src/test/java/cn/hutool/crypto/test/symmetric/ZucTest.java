package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.ZUC;
import org.junit.Assert;
import org.junit.Test;

public class ZucTest {

	@Test
	public void zuc128Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_128);
		byte[] iv = RandomUtil.randomBytes(16);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_128, secretKey, iv);

		String msg = RandomUtil.randomString(500);
		byte[] crypt2 = zuc.encrypt(msg);
		String msg2 = zuc.decryptStr(crypt2, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(msg, msg2);
	}

	@Test
	public void zuc256Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_256);
		byte[] iv = RandomUtil.randomBytes(25);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_256, secretKey, iv);

		String msg = RandomUtil.randomString(500);
		byte[] crypt2 = zuc.encrypt(msg);
		String msg2 = zuc.decryptStr(crypt2, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(msg, msg2);
	}
}
