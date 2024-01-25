package cn.hutool.http;

import org.junit.Assert;
import org.junit.Test;

public class IssueI8YV0KTest {

	@Test
	public void removeHtmlAttrTest(){
		final String str = "<content styleCode=\"xmChange yes\">";
		Assert.assertEquals("<content>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest2(){
		final String str = "<content styleCode=\"xmChange\"/>";
		Assert.assertEquals("<content/>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}
}
