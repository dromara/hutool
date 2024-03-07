package org.dromara.hutool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI96LWHTest {

	@Test
	public void replaceByCodePointTest() {
		final String str = "\uD83D\uDC46最上方点击蓝字";

		// 这个方法里\uD83D\uDC46表示一个emoji表情，使用codePoint之后，一个表情表示一个字符，因此按照一个字符对
		Assertions.assertEquals("\uD83D\uDC46最上下点击蓝字", StrUtil.replaceByCodePoint(str, 3, 4, "下"));
		Assertions.assertEquals("\uD83D\uDC46最下方点击蓝字", new StringBuilder(str).replace(3, 4, "下").toString());
	}
}
