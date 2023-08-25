package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class Issue2997Test {
	@Test
	public void toBeanTest() {
		// https://github.com/dromara/hutool/issues/2997
		final Object o = JSONUtil.toBean("{}", Object.class);
		Assert.assertEquals(JSONObject.class, o.getClass());
	}
}
