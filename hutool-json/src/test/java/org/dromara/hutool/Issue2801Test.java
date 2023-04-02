package org.dromara.hutool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2801Test {
	@Test
	public void toStringTest() {
		final JSONObject recordObj = new JSONObject(JSONConfig.of().setIgnoreNullValue(false));
		recordObj.put("m_reportId", null);
		Assertions.assertEquals("{\"m_reportId\":null}", recordObj.toString());
	}
}
