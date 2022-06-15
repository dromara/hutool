package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class Issue2380Test {

	@Test
	public void parseObjTest(){
		// 多个都好，做容错处理
		final String s = "{\"k\":\"v\",}";
		final JSONObject jsonObject = JSONUtil.parseObj(s);
		Assert.assertEquals("{\"k\":\"v\"}", jsonObject.toString());
	}
}
