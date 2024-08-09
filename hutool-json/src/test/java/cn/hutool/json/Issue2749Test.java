package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue2749Test {

	@Test
	@Disabled
	public void jsonObjectTest() {
		final Map<String, Object> map = new HashMap<>(1, 1f);
		Map<String, Object> node = map;
		for (int i = 0; i < 1000; i++) {
			//noinspection unchecked
			node = (Map<String, Object>) node.computeIfAbsent("a", k -> new HashMap<String, Object>(1, 1f));
		}
		node.put("a", 1);
		final String jsonStr = JSONUtil.toJsonStr(map);

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		final JSONObject jsonObject = new JSONObject(jsonStr);
		assertNotNull(jsonObject);

		// 栈溢出
		//noinspection ResultOfMethodCallIgnored
		jsonObject.toString();
	}
}
