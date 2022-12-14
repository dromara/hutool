package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class Issue2801Test {
	@Test
	public void toStringTest() {
		final JSONObject recordObj = new JSONObject(JSONConfig.of().setIgnoreNullValue(false));
		recordObj.put("m_reportId", null);
		Assert.assertEquals("{\"m_reportId\":null}", recordObj.toString());
	}
}
