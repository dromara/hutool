package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

public class StrRepeaterTest {

	@Test
	public void repeatByLengthTest() {
		// 如果指定长度非指定字符串的整数倍，截断到固定长度
		final String ab = StrRepeater.of(5).repeatByLength("ab");
		Assert.assertEquals("ababa", ab);
	}

	@Test
	public void repeatByLengthTest2() {
		// 如果指定长度小于字符串本身的长度，截断之
		final String ab = StrRepeater.of(2).repeatByLength("abcde");
		Assert.assertEquals("ab", ab);
	}
}
