package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3588Test {

	@Test
	public void toBeanIgnoreCaseTest() {
		String json = "{id: 1, code: 1122, tsemaphores: [{type: 1, status: 12}]}";
		AttrData attrData = JSONUtil.toBean(json, JSONConfig.create().setIgnoreCase(true), AttrData.class);
		Assert.assertEquals("1", attrData.getId());
		Assert.assertEquals("1122", attrData.getCode());
		Assert.assertEquals("1", attrData.getTSemaphores().get(0).getType());
		Assert.assertEquals("12", attrData.getTSemaphores().get(0).getStatus());
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
