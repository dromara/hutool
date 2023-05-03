package org.dromara.hutool.crypto.digest;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.thread.ConcurrencyTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * MD5 单元测试
 *
 * @author Looly
 *
 */
public class Md5Test {

	@Test
	public void md5To16Test() {
		final String hex16 = MD5.of().digestHex16("中国");
		Assertions.assertEquals(16, hex16.length());
		Assertions.assertEquals("cb143acd6c929826", hex16);
	}

	@Test
	void md5ThreadSafeTest() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = MD5.of().digestHex(text);
			Assertions.assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.closeQuietly(tester);
	}

	@Test
	void md5ThreadSafeTest2() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = new Digester("MD5").digestHex(text);
			Assertions.assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.closeQuietly(tester);
	}
}
