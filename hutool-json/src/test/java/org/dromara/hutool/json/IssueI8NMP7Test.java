package org.dromara.hutool.json;

import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class IssueI8NMP7Test {

	@Test
	public void toBeanTest() {
		final String jsonString = "{\"enableTime\":\"1702262524444\"}";
		final DemoModel bean = JSONUtil.toBean(jsonString, JSONConfig.of().setDateFormat("#SSS"), DemoModel.class);
		Assertions.assertNotNull(bean.getEnableTime());
	}

	@Data
	@ToString
	static class DemoModel{
		private Date enableTime;
	}

}
