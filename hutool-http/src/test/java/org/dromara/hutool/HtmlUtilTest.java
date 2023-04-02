package org.dromara.hutool;

import org.dromara.hutool.meta.ContentTypeUtil;
import org.dromara.hutool.regex.ReUtil;
import org.dromara.hutool.html.HtmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Html单元测试
 *
 * @author looly
 *
 */
public class HtmlUtilTest {

	@Test
	public void removeHtmlTagTest() {
		//非闭合标签
		String str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
		String result = HtmlUtil.removeHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//包含内容标签
		str = "pre<div class=\"test_div\">dfdsfdsfdsf</div>";
		result = HtmlUtil.removeHtmlTag(str, "div");
		Assertions.assertEquals("pre", result);

		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tdfdsfdsfdsf\r\n</div>";
		result = HtmlUtil.removeHtmlTag(str, "div");
		Assertions.assertEquals("pre", result);
	}

	@Test
	public void cleanHtmlTagTest() {
		//非闭合标签
		String str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
		String result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("pre", result);

		//包含内容标签
		str = "pre<div class=\"test_div\">dfdsfdsfdsf</div>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("predfdsfdsfdsf", result);

		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tdfdsfdsfdsf\r\n</div><div class=\"test_div\">BBBB</div>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assertions.assertEquals("pre\r\n\t\tdfdsfdsfdsf\r\nBBBB", result);
	}

	@Test
	public void unwrapHtmlTagTest() {
		//非闭合标签
		String str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
		String result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//闭合标签
		str = "pre<img/>";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assertions.assertEquals("pre", result);

		//包含内容标签
		str = "pre<div class=\"test_div\">abc</div>";
		result = HtmlUtil.unwrapHtmlTag(str, "div");
		Assertions.assertEquals("preabc", result);

		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tabc\r\n</div>";
		result = HtmlUtil.unwrapHtmlTag(str, "div");
		Assertions.assertEquals("pre\r\n\t\tabc\r\n", result);
	}

	@Test
	public void unwrapTest2() {
		// 避免移除i却误删img标签的情况
		final String htmlString = "<html><img src='aaa'><i>测试文本</i></html>";
		final String tagString = "i,br";
		final String cleanTxt = HtmlUtil.removeHtmlTag(htmlString, false, tagString.split(","));
		Assertions.assertEquals("<html><img src='aaa'>测试文本</html>", cleanTxt);
	}

	@Test
	public void escapeTest() {
		final String html = "<html><body>123'123'</body></html>";
		final String escape = HtmlUtil.escape(html);
		Assertions.assertEquals("&lt;html&gt;&lt;body&gt;123&#039;123&#039;&lt;/body&gt;&lt;/html&gt;", escape);
		final String restoreEscaped = HtmlUtil.unescape(escape);
		Assertions.assertEquals(html, restoreEscaped);
		Assertions.assertEquals("'", HtmlUtil.unescape("&apos;"));
	}

	@Test
	public void escapeTest2() {
		final char c = ' '; // 不断开空格（non-breaking space，缩写nbsp。)
		Assertions.assertEquals(c, 160);
		final String html = "<html><body> </body></html>";
		final String escape = HtmlUtil.escape(html);
		Assertions.assertEquals("&lt;html&gt;&lt;body&gt;&nbsp;&lt;/body&gt;&lt;/html&gt;", escape);
		Assertions.assertEquals(" ", HtmlUtil.unescape("&nbsp;"));
	}

	@Test
	public void filterTest() {
		final String html = "<alert></alert>";
		final String filter = HtmlUtil.filter(html);
		Assertions.assertEquals("", filter);
	}

	@Test
	public void removeHtmlAttrTest() {

		// 去除的属性加双引号测试
		String html = "<div class=\"test_div\"></div><span class=\"test_div\"></span>";
		String result = HtmlUtil.removeHtmlAttr(html, "class");
		Assertions.assertEquals("<div></div><span></span>", result);

		// 去除的属性后跟空格、加单引号、不加引号测试
		html = "<div class=test_div></div><span Class='test_div' ></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assertions.assertEquals("<div></div><span></span>", result);

		// 去除的属性位于标签末尾、其它属性前测试
		html = "<div style=\"margin:100%\" class=test_div></div><span Class='test_div' width=100></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assertions.assertEquals("<div style=\"margin:100%\"></div><span width=100></span>", result);

		// 去除的属性名和值之间存在空格
		html = "<div style = \"margin:100%\" class = test_div></div><span Class = 'test_div' width=100></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assertions.assertEquals("<div style = \"margin:100%\"></div><span width=100></span>", result);
	}

	@Test
	public void removeAllHtmlAttrTest() {
		final String html = "<div class=\"test_div\" width=\"120\"></div>";
		final String result = HtmlUtil.removeAllHtmlAttr(html, "div");
		Assertions.assertEquals("<div></div>", result);
	}

	@Test
	public void getCharsetTest() {
		String charsetName = ReUtil.get(ContentTypeUtil.CHARSET_PATTERN, "Charset=UTF-8;fq=0.9", 1);
		Assertions.assertEquals("UTF-8", charsetName);

		charsetName = ReUtil.get(HtmlUtil.META_CHARSET_PATTERN, "<meta charset=utf-8", 1);
		Assertions.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HtmlUtil.META_CHARSET_PATTERN, "<meta charset='utf-8'", 1);
		Assertions.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HtmlUtil.META_CHARSET_PATTERN, "<meta charset=\"utf-8\"", 1);
		Assertions.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HtmlUtil.META_CHARSET_PATTERN, "<meta charset = \"utf-8\"", 1);
		Assertions.assertEquals("utf-8", charsetName);
	}
}
