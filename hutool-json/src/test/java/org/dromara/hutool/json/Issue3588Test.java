package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3588Test {
	@Test
	public void toBeanIgnoreCaseTest() {
		final String json = "{id: 1, code: 1122, tsemaphores: [{type: 1, status: 12}]}";
		final AttrData attrData = JSONUtil.toBean(json, JSONConfig.of().setIgnoreCase(true), AttrData.class);
		Assertions.assertEquals("1", attrData.getId());
		Assertions.assertEquals("1122", attrData.getCode());
		Assertions.assertEquals("1", attrData.getTSemaphores().get(0).getType());
		Assertions.assertEquals("12", attrData.getTSemaphores().get(0).getStatus());
	}

	@Data
	static class AttrData {
		private String id;
		private String code;
		private List<TSemaphore> tSemaphores = new ArrayList<>();
	}

	@Data
	static class TSemaphore{
		private String type;
		private String status;
	}
}
