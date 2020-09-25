package cn.hutool.http.test;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.http.HtmlUtil;

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
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.removeHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//包含内容标签
		str = "pre<div class=\"test_div\">dfdsfdsfdsf</div>";
		result = HtmlUtil.removeHtmlTag(str, "div");
		Assert.assertEquals("pre", result);
		
		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tdfdsfdsfdsf\r\n</div>";
		result = HtmlUtil.removeHtmlTag(str, "div");
		Assert.assertEquals("pre", result);
	}
	
	@Test
	public void cleanHtmlTagTest() {
		//非闭合标签
		String str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
		String result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("pre", result);
		
		//包含内容标签
		str = "pre<div class=\"test_div\">dfdsfdsfdsf</div>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("predfdsfdsfdsf", result);
		
		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tdfdsfdsfdsf\r\n</div><div class=\"test_div\">BBBB</div>";
		result = HtmlUtil.cleanHtmlTag(str);
		Assert.assertEquals("pre\r\n\t\tdfdsfdsfdsf\r\nBBBB", result);
	}
	
	@Test
	public void unwrapHtmlTagTest() {
		//非闭合标签
		String str = "pre<img src=\"xxx/dfdsfds/test.jpg\">";
		String result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img>";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img src=\"xxx/dfdsfds/test.jpg\" />";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//闭合标签
		str = "pre<img />";
		result = HtmlUtil.unwrapHtmlTag(str, "img");
		Assert.assertEquals("pre", result);
		
		//包含内容标签
		str = "pre<div class=\"test_div\">abc</div>";
		result = HtmlUtil.unwrapHtmlTag(str, "div");
		Assert.assertEquals("preabc", result);
		
		//带换行
		str = "pre<div class=\"test_div\">\r\n\t\tabc\r\n</div>";
		result = HtmlUtil.unwrapHtmlTag(str, "div");
		Assert.assertEquals("pre\r\n\t\tabc\r\n", result);
	}
	
	@Test
	public void escapeTest() {
		String html = "<html><body>123'123'</body></html>";
		String escape = HtmlUtil.escape(html);
		Assert.assertEquals("&lt;html&gt;&lt;body&gt;123&#039;123&#039;&lt;/body&gt;&lt;/html&gt;", escape);
		String restoreEscaped = HtmlUtil.unescape(escape);
		Assert.assertEquals(html, restoreEscaped);
		Assert.assertEquals("'", HtmlUtil.unescape("&apos;"));
	}
	
	@Test
	public void filterTest() {
		String html = "<alert></alert>";
		String filter = HtmlUtil.filter(html);
		Assert.assertEquals("", filter);
	}
	
	@Test
	public void removeHtmlAttrTest() {

		// 去除的属性加双引号测试
		String html = "<div class=\"test_div\"></div><span class=\"test_div\"></span>";
		String result = HtmlUtil.removeHtmlAttr(html, "class");
		Assert.assertEquals("<div></div><span></span>", result);

		// 去除的属性后跟空格、加单引号、不加引号测试
		html = "<div class=test_div></div><span Class='test_div' ></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assert.assertEquals("<div></div><span></span>", result);

		// 去除的属性位于标签末尾、其它属性前测试
		html = "<div style=\"margin:100%\" class=test_div></div><span Class='test_div' width=100></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assert.assertEquals("<div style=\"margin:100%\"></div><span width=100></span>", result);

		// 去除的属性名和值之间存在空格
		html = "<div style = \"margin:100%\" class = test_div></div><span Class = 'test_div' width=100></span>";
		result = HtmlUtil.removeHtmlAttr(html, "class");
		Assert.assertEquals("<div style = \"margin:100%\"></div><span width=100></span>", result);
	}
	
	@Test
	public void removeAllHtmlAttrTest() {
		String html = "<div class=\"test_div\" width=\"120\"></div>";
		String result = HtmlUtil.removeAllHtmlAttr(html, "div");
		Assert.assertEquals("<div></div>", result);
	}
}
