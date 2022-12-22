package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class IssuesI4V14NTest {

	@Test
	public void parseTest(){
		String str = "{\"A\" : \"A\\nb\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(str);
		Assert.assertEquals("A\nb", jsonObject.getStr("A"));

		final Map<String, String> map = jsonObject.toBean(new TypeReference<Map<String, String>>() {});
		Assert.assertEquals("A\nb", map.get("A"));
	}
}
