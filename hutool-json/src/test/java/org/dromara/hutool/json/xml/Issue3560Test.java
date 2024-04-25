package org.dromara.hutool.json.xml;

import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3560Test {
	@Test
	public void toJSONObjectTest() {
		final String inPara= "<ROOT><ID>002317479934367853</ID><CONTENT><![CDATA[asdfadf&amp;21sdgzxv&amp;aasfasf]]></CONTENT></ROOT>";
		final JSONObject json = JSONXMLUtil.toJSONObject(inPara, ParseConfig.of().setKeepStrings(true));
		Assertions.assertEquals("{\"ROOT\":{\"ID\":\"002317479934367853\",\"CONTENT\":\"asdfadf&amp;21sdgzxv&amp;aasfasf\"}}", json.toString());
	}
}
