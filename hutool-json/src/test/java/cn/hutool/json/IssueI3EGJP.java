package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI3EGJP {

	@Test
	public void hutoolMapToBean() {
		JSONObject paramJson = new JSONObject();
		paramJson.set("is_booleana", "1");
		paramJson.set("is_booleanb", true);
		ConvertDO convertDO = BeanUtil.toBean(paramJson, ConvertDO.class);

		Assertions.assertTrue(convertDO.isBooleana());
		Assertions.assertTrue(convertDO.getIsBooleanb());
	}

	@Data
	public static class ConvertDO {
		private boolean isBooleana;
		private Boolean isBooleanb;
	}
}
