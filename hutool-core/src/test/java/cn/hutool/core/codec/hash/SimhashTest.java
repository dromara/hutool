package cn.hutool.core.codec.hash;

import cn.hutool.core.text.StrUtil;
import cn.hutool.core.text.split.SplitUtil;
import org.junit.Assert;
import org.junit.Test;

public class SimhashTest {

	@Test
	public void simTest() {
		final String text1 = "我是 一个 普通 字符串";
		final String text2 = "我是 一个 普通 字符串";

		final Simhash simhash = new Simhash();
		final long hash = simhash.hash64(SplitUtil.split(text1, StrUtil.SPACE));
		Assert.assertTrue(hash != 0);

		simhash.store(hash);
		final boolean duplicate = simhash.equals(SplitUtil.split(text2, StrUtil.SPACE));
		Assert.assertTrue(duplicate);
	}
}
