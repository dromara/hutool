package cn.hutool.json;

import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class Issue2746Test {
	@Test
	public void parseObjTest() {
		final String str = StrUtil.repeat("{", 1500) + StrUtil.repeat("}", 1500);
		try{
			JSONUtil.parseObj(str);
		} catch (final JSONException e){
			Assert.assertTrue(e.getMessage().startsWith("A JSONObject can not directly nest another JSONObject or JSONArray"));
		}
	}

	@Test(expected = JSONException.class)
	public void parseTest() {
		final String str = StrUtil.repeat("[", 1500) + StrUtil.repeat("]", 1500);
		JSONUtil.parseArray(str);
	}
}
