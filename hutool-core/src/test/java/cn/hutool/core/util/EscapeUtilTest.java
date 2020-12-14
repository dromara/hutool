package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class EscapeUtilTest {
	
	@Test
	public void escapeHtml4Test() {
		String escapeHtml4 = EscapeUtil.escapeHtml4("<a>你好</a>");
		Assert.assertEquals("&lt;a&gt;你好&lt;/a&gt;", escapeHtml4);
		
		String result = EscapeUtil.unescapeHtml4("&#25391;&#33633;&#22120;&#31867;&#22411;");
		Assert.assertEquals("振荡器类型", result);

		String escape = EscapeUtil.escapeHtml4("*@-_+./(123你好)");
		Assert.assertEquals("*@-_+./(123你好)", escape);
	}

	@Test
	public void escapeTest(){
		String str = "*@-_+./(123你好)ABCabc";
		String escape = EscapeUtil.escape(str);
		Assert.assertEquals("*@-_+./%28123%u4f60%u597d%29ABCabc", escape);

		String unescape = EscapeUtil.unescape(escape);
		Assert.assertEquals(str, unescape);
	}

	@Test
	public void escapeAllTest(){
		String str = "*@-_+./(123你好)ABCabc";

		String escape = EscapeUtil.escapeAll(str);
		Assert.assertEquals("%2a%40%2d%5f%2b%2e%2f%28%31%32%33%u4f60%u597d%29%41%42%43%61%62%63", escape);

		String unescape = EscapeUtil.unescape(escape);
		Assert.assertEquals(str, unescape);
	}

	@Test
	public void escapeSinleQuotesTest(){
		String str = "'some text with single quotes'";
		final String s = EscapeUtil.escapeHtml4(str);
		Assert.assertEquals(str, s);
	}
}
