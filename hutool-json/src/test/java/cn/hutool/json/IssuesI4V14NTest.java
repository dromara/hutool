package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssuesI4V14NTest {

	@Test
	public void parseTest(){
		String str = "{\"A\" : \"A\\nb\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(str);
		assertEquals("A\nb", jsonObject.getStr("A"));

		final Map<String, String> map = jsonObject.toBean(new TypeReference<Map<String, String>>() {});
		assertEquals("A\nb", map.get("A"));
	}
}
