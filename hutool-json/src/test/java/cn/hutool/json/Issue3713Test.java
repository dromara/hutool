package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

public class Issue3713Test {
	@Test
	void toBeanTest() throws ParseException {
		String jsonStr = "{\"operDate\":\"Aug 22, 2024, 4:21:21 PM\"}";
		final CustomerCreateLog bean = JSONUtil.toBean(jsonStr, JSONConfig.create().setDateFormat("MMM dd, yyyy, h:mm:ss a"), CustomerCreateLog.class);
		Assertions.assertEquals("Thu Aug 22 16:21:21 CST 2024", bean.getOperDate().toString());
	}

	@Data
	private static class CustomerCreateLog{
		private Date operDate;
	}
}
