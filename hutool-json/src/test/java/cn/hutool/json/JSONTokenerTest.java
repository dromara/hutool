package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Assert;
import org.junit.Test;

public class JSONTokenerTest {
	@Test
	public void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.getUtf8Reader("issue1200.json"));
		Assert.assertNotNull(jsonObject);
	}
}
