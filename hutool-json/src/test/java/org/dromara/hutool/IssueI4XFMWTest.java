package org.dromara.hutool;

import org.dromara.hutool.annotation.Alias;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I4XFMW
 */
public class IssueI4XFMWTest {

	@Test
	public void test() {
		final List<TestEntity> entityList = new ArrayList<>();
		final TestEntity entityA = new TestEntity();
		entityA.setId("123");
		entityA.setPassword("456");
		entityList.add(entityA);

		final TestEntity entityB = new TestEntity();
		entityB.setId("789");
		entityB.setPassword("098");
		entityList.add(entityB);

		final String jsonStr = JSONUtil.toJsonStr(entityList);
		Assertions.assertEquals("[{\"uid\":\"123\",\"password\":\"456\"},{\"uid\":\"789\",\"password\":\"098\"}]", jsonStr);
		final List<TestEntity> testEntities = JSONUtil.toList(jsonStr, TestEntity.class);
		Assertions.assertEquals("123", testEntities.get(0).getId());
		Assertions.assertEquals("789", testEntities.get(1).getId());
	}

	@Data
	static class TestEntity {
		@Alias("uid")
		private String id;
		private String password;
	}
}
