package org.dromara.hutool;

import org.dromara.hutool.bean.BeanUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI3EGJPTest {

	@Test
	public void hutoolMapToBean() {
		final JSONObject paramJson = new JSONObject();
		paramJson.set("is_booleana", "1");
		paramJson.set("is_booleanb", true);
		final ConvertDO convertDO = BeanUtil.toBean(paramJson, ConvertDO.class);

		Assertions.assertTrue(convertDO.isBooleana());
		Assertions.assertTrue(convertDO.getIsBooleanb());
	}

	@Data
	public static class ConvertDO {
		private boolean isBooleana;
		private Boolean isBooleanb;
	}
}
