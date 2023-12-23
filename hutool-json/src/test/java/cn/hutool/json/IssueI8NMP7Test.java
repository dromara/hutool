package cn.hutool.json;

import lombok.Data;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class IssueI8NMP7Test {
	@Test
	public void toBeanTest() {
		final String jsonString = "{\"enableTime\":\"1702262524444\"}";
		final DemoModel bean = JSONUtil.toBean(jsonString, JSONConfig.create(), DemoModel.class);
		Assert.assertNotNull(bean.getEnableTime());
	}

	@Data
	@ToString
	static class DemoModel{
		private Date enableTime;
	}
}
