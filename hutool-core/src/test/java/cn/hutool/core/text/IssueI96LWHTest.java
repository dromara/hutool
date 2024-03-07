package cn.hutool.core.text;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

public class IssueI96LWHTest {
	@Test
	public void replaceTest() {
		String str = "\uD83D\uDC46最上方点击蓝字";
		Console.log(str.codePoints().toArray());
		Console.log(StrUtil.replaceByCodePoint(str, 3, 4, "下"));
		Console.log(new StringBuilder(str).replace(3, 4, "下"));
	}
}
