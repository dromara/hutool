package cn.hutool.core.text.replacer;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class SearchReplacerTest {

	@Test
	public void replaceOnlyTest() {
		final String result = CharSequenceUtil.replace(",", ",", "|");
		Assert.assertEquals("|", result);
	}

	@Test
	public void replaceTestAtBeginAndEnd() {
		final String result = CharSequenceUtil.replace(",abcdef,", ",", "|");
		Assert.assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest() {
		final String str = "AAABBCCCBBDDDBB";
		String replace = StrUtil.replace(str, 0, "BB", "22", false);
		Assert.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 3, "BB", "22", false);
		Assert.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "BB", "22", false);
		Assert.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "22", true);
		Assert.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "", true);
		Assert.assertEquals("AAABBCCCDDD", replace);

		replace = StrUtil.replace(str, 4, "bb", null, true);
		Assert.assertEquals("AAABBCCCDDD", replace);
	}
}
