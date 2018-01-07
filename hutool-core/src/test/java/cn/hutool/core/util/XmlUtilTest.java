package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import cn.hutool.core.util.XmlUtil;

/**
 * {@link XmlUtil} 工具类
 * @author Looly
 *
 */
public class XmlUtilTest {
	
	@Test
	public void parseTest() {
		String result="<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>ok</message><remainpoint>1490</remainpoint><taskID>885</taskID><successCounts>1</successCounts></returnsms>";
		Document docResult=XmlUtil.parseXml(result);
		String elementText = XmlUtil.elementText(docResult.getDocumentElement(), "returnstatus");
		Assert.assertEquals("Success", elementText);
	}
	
	@Test
	@Ignore
	public void writeTest() {
		String result="<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<returnsms>"
					+ "<returnstatus>Success（成功）</returnstatus>"
					+ "<message>ok</message>"
					+ "<remainpoint>1490</remainpoint>"
					+ "<taskID>885</taskID>"
					+ "<successCounts>1</successCounts>"
				+ "</returnsms>";
		Document docResult=XmlUtil.parseXml(result);
		XmlUtil.toFile(docResult, "d:/aaa.xml", "utf-8");
	}
}
