package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue1075Test {
	@Test
	public void test() {
		String s = "{\"f1\":\"f1\",\"F2\":\"f2\",\"fac\":\"fac\"}";

		ObjA o2 = JSONUtil.parseObj(s, JSONConfig.create().setIgnoreCase(true)).toBean(ObjA.class);
		Assert.assertEquals("fac", o2.getFAC());
	}

	@Data
	public static class ObjA {
		private String f1;
		private String F2;
		private String FAC;
	}
}
