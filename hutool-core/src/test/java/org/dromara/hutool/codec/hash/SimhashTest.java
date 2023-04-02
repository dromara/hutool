package org.dromara.hutool.codec.hash;

import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.text.split.SplitUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimhashTest {

	@Test
	public void simTest() {
		final String text1 = "我是 一个 普通 字符串";
		final String text2 = "我是 一个 普通 字符串";

		final Simhash simhash = new Simhash();
		final long hash = simhash.hash64(SplitUtil.split(text1, StrUtil.SPACE));
		Assertions.assertTrue(hash != 0);

		simhash.store(hash);
		final boolean duplicate = simhash.equals(SplitUtil.split(text2, StrUtil.SPACE));
		Assertions.assertTrue(duplicate);
	}
}
