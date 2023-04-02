package org.dromara.hutool.text.replacer;

import org.dromara.hutool.text.CharSequenceUtil;
import org.dromara.hutool.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchReplacerTest {

	@Test
	public void replaceOnlyTest() {
		final String result = CharSequenceUtil.replace(",", ",", "|");
		Assertions.assertEquals("|", result);
	}

	@Test
	public void replaceTestAtBeginAndEnd() {
		final String result = CharSequenceUtil.replace(",abcdef,", ",", "|");
		Assertions.assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest() {
		final String str = "AAABBCCCBBDDDBB";
		String replace = StrUtil.replace(str, 0, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 3, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "BB", "22", false);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "22", true);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "", true);
		Assertions.assertEquals("AAABBCCCDDD", replace);

		replace = StrUtil.replace(str, 4, "bb", null, true);
		Assertions.assertEquals("AAABBCCCDDD", replace);
	}
}
