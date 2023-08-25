package cn.hutool.crypto.digest;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ConcurrencyTester;
import org.junit.Assert;
import org.junit.Test;

/**
 * MD5 单元测试
 *
 * @author Looly
 *
 */
public class Md5Test {

	@Test
	public void md5To16Test() {
		String hex16 = new MD5().digestHex16("中国");
		Assert.assertEquals(16, hex16.length());
		Assert.assertEquals("cb143acd6c929826", hex16);
	}

	@Test
	public void md5ThreadSafeTest() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = new MD5().digestHex(text);
			Assert.assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.close(tester);
	}
}
