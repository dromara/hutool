package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class IssueI8PC9FTest {

	@Test
	public void toBeanIgnoreErrorTest() {
		final String testJson = "{\"testMap\":\"\"}";
		final TestBean test = JSONUtil.parseObj(testJson, JSONConfig.create().setIgnoreError(true))
			.toBean(TestBean.class);
		Assert.assertNotNull(test);
		Assert.assertNull(test.getTestMap());
	}

	@Data
	static class TestBean{
		private Map<?, ?> testMap;
	}
}
