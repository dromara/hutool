package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue1075Test {

	final String jsonStr = "{\"f1\":\"f1\",\"f2\":\"f2\",\"fac\":\"fac\"}";

	@Test
	public void testToBean() {
		// 在不忽略大小写的情况下，f2、fac都不匹配
		ObjA o2 = JSONUtil.toBean(jsonStr, ObjA.class);
		Assert.assertNull(o2.getFAC());
		Assert.assertNull(o2.getF2());
	}

	@Test
	public void testToBeanIgnoreCase() {
		// 在忽略大小写的情况下，f2、fac都匹配
		ObjA o2 = JSONUtil.parseObj(jsonStr, JSONConfig.create().setIgnoreCase(true)).toBean(ObjA.class);
		Assert.assertEquals("fac", o2.getFAC());
		Assert.assertEquals("f2", o2.getF2());
	}

	@Data
	public static class ObjA {
		private String f1;
		private String F2;
		private String FAC;
	}
}
