package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZucTest {

	@Test
	public void zuc128Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_128);
		final byte[] iv = RandomUtil.randomBytes(16);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_128, secretKey, iv);

		final String msg = RandomUtil.randomString(500);
		final byte[] crypt2 = zuc.encrypt(msg);
		final String msg2 = zuc.decryptStr(crypt2, CharsetUtil.UTF_8);
		Assertions.assertEquals(msg, msg2);
	}

	@Test
	public void zuc256Test(){
		final byte[] secretKey = ZUC.generateKey(ZUC.ZUCAlgorithm.ZUC_256);
		final byte[] iv = RandomUtil.randomBytes(25);
		final ZUC zuc = new ZUC(ZUC.ZUCAlgorithm.ZUC_256, secretKey, iv);

		final String msg = RandomUtil.randomString(500);
		final byte[] crypt2 = zuc.encrypt(msg);
		final String msg2 = zuc.decryptStr(crypt2, CharsetUtil.UTF_8);
		Assertions.assertEquals(msg, msg2);
	}
}
