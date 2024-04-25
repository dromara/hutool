package cn.hutool.json.xml;

import cn.hutool.json.JSONObject;
import cn.hutool.json.XML;
import org.junit.Assert;
import org.junit.Test;

public class Issue3560Test {
	@Test
	public void toJSONObjectTest() {
		String inPara= "<ROOT><ID>002317479934367853</ID><CONTENT><![CDATA[asdfadf&amp;21sdgzxv&amp;aasfasf]]></CONTENT></ROOT>";
		JSONObject json = XML.toJSONObject(inPara, true);
		Assert.assertEquals("{\"ROOT\":{\"ID\":\"002317479934367853\",\"CONTENT\":\"asdfadf&amp;21sdgzxv&amp;aasfasf\"}}", json.toString());
	}
}
