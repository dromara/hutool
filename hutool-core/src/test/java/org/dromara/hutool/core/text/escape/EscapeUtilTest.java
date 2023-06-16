package org.dromara.hutool.core.text.escape;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EscapeUtilTest {

	@Test
	public void escapeHtml4Test() {
		final String escapeHtml4 = EscapeUtil.escapeHtml4("<a>你好</a>");
		Assertions.assertEquals("&lt;a&gt;你好&lt;/a&gt;", escapeHtml4);

		final String result = EscapeUtil.unescapeHtml4("&#25391;&#33633;&#22120;&#31867;&#22411;");
		Assertions.assertEquals("振荡器类型", result);

		final String escape = EscapeUtil.escapeHtml4("*@-_+./(123你好)");
		Assertions.assertEquals("*@-_+./(123你好)", escape);
	}

	@Test
	public void escapeTest(){
		final String str = "*@-_+./(123你好)ABCabc";
		final String escape = EscapeUtil.escape(str);
		Assertions.assertEquals("*@-_+./%28123%u4f60%u597d%29ABCabc", escape);

		final String unescape = EscapeUtil.unescape(escape);
		Assertions.assertEquals(str, unescape);
	}

	@Test
	public void escapeAllTest(){
		final String str = "*@-_+./(123你好)ABCabc";

		final String escape = EscapeUtil.escapeAll(str);
		Assertions.assertEquals("%2a%40%2d%5f%2b%2e%2f%28%31%32%33%u4f60%u597d%29%41%42%43%61%62%63", escape);

		final String unescape = EscapeUtil.unescape(escape);
		Assertions.assertEquals(str, unescape);
	}

	/**
	 * https://gitee.com/dromara/hutool/issues/I49JU8
	 */
	@Test
	public void escapeAllTest2(){
		final String str = "٩";

		final String escape = EscapeUtil.escapeAll(str);
		Assertions.assertEquals("%u0669", escape);

		final String unescape = EscapeUtil.unescape(escape);
		Assertions.assertEquals(str, unescape);
	}

	@Test
	public void escapeSingleQuotesTest(){
		// 单引号不做转义
		final String str = "'some text with single quotes'";
		final String s = EscapeUtil.escapeHtml4(str);
		Assertions.assertEquals("'some text with single quotes'", s);
	}

	@Test
	public void unescapeSingleQuotesTest(){
		final String str = "&apos;some text with single quotes&apos;";
		final String s = EscapeUtil.unescapeHtml4(str);
		Assertions.assertEquals("'some text with single quotes'", s);
	}

	@Test
	public void escapeXmlTest(){
		final String a = "<>";
		final String escape = EscapeUtil.escape(a);
		Console.log(escape);
	}
}
