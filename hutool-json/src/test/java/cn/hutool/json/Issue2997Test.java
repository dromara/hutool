package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Issue2997Test {
	@Test
	public void toBeanTest() {
		// https://github.com/dromara/hutool/issues/2997
		final Object o = JSONUtil.toBean("{}", Object.class);
		assertEquals(JSONObject.class, o.getClass());
	}
}
