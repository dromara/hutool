package cn.hutool.http;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueI8YV0KTest {

	@Test
	public void removeHtmlAttrTest(){
		final String str = "<content styleCode=\"xmChange yes\">";
		assertEquals("<content>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest2(){
		final String str = "<content styleCode=\"xmChange\"/>";
		assertEquals("<content/>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest3(){
		String str = "<content styleCode=\"dada ada\" data=\"dsad\" >";
		assertEquals("<content data=\"dsad\">", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}
}
