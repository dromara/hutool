package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class IssueI3EGJP {

	@Test
	public void hutoolMapToBean() {
		JSONObject paramJson = new JSONObject();
		paramJson.set("is_booleana", "1");
		paramJson.set("is_booleanb", true);
		ConvertDO convertDO = BeanUtil.toBean(paramJson, ConvertDO.class);

		Assert.assertTrue(convertDO.isBooleana());
		Assert.assertTrue(convertDO.getIsBooleanb());
	}

	@Data
	public static class ConvertDO {
		private boolean isBooleana;
		private Boolean isBooleanb;
	}
}
