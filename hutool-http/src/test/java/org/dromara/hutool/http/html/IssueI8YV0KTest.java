package org.dromara.hutool.http.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI8YV0KTest {
	@Test
	public void removeHtmlAttrTest(){
		final String str = "<content styleCode=\"xmChange yes\">";
		Assertions.assertEquals("<content>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest2(){
		final String str = "<content styleCode=\"xmChange\"/>";
		Assertions.assertEquals("<content/>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest3(){
		final String str = "<content styleCode=\"dada ada\" data=\"dsad\" >";
		Assertions.assertEquals("<content data=\"dsad\">", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}
}
