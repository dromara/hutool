package cn.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.text.Simhash;
import cn.hutool.core.text.StrUtil;

public class SimhashTest {

	@Test
	public void simTest() {
		final String text1 = "我是 一个 普通 字符串";
		final String text2 = "我是 一个 普通 字符串";

		final Simhash simhash = new Simhash();
		final long hash = simhash.hash(StrUtil.split(text1, ' '));
		Assert.assertTrue(hash != 0);

		simhash.store(hash);
		final boolean duplicate = simhash.equals(StrUtil.split(text2, ' '));
		Assert.assertTrue(duplicate);
	}
}
