package cn.hutool.json;

import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueI8PC9FTest {

	@Test
	public void toBeanIgnoreErrorTest() {
		final String testJson = "{\"testMap\":\"\"}";
		final TestBean test = JSONUtil.parseObj(testJson, JSONConfig.create().setIgnoreError(true))
			.toBean(TestBean.class);
		assertNotNull(test);
		assertNull(test.getTestMap());
	}

	@Data
	static class TestBean{
		private Map<?, ?> testMap;
	}
}
