package cn.hutool.crypto.digest;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ConcurrencyTester;
import static org.junit.jupiter.api.Assertions.*;
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
		String hex16 = new MD5().digestHex16("中国");
		assertEquals(16, hex16.length());
		assertEquals("cb143acd6c929826", hex16);
	}

	@Test
	public void md5ThreadSafeTest() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = new MD5().digestHex(text);
			assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.close(tester);
	}
}
