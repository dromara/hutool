package org.dromara.hutool.symmetric;

import org.dromara.hutool.util.RandomUtil;
import org.dromara.hutool.SecureUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PBKDF2Test {

	@Test
	public void encryptTest(){
		final String s = SecureUtil.pbkdf2("123456".toCharArray(), RandomUtil.randomBytes(16));
		Assertions.assertEquals(128, s.length());
	}
}
