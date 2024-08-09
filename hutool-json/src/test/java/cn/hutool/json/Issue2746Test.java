package cn.hutool.json;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Issue2746Test {
	@Test
	public void parseObjTest() {
		final String str = StrUtil.repeat("{", 1500) + StrUtil.repeat("}", 1500);
		try{
			JSONUtil.parseObj(str);
		} catch (final JSONException e){
			assertTrue(e.getMessage().startsWith("A JSONObject can not directly nest another JSONObject or JSONArray"));
		}
	}

	@Test
	public void parseTest() {
		assertThrows(JSONException.class, () -> {
			final String str = StrUtil.repeat("[", 1500) + StrUtil.repeat("]", 1500);
			JSONUtil.parseArray(str);
		});
	}
}
