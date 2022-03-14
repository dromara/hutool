package cn.hutool.json;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I4XFMW
 */
public class IssueI4XFMWTest {

	@Test
	public void test() {
		List<TestEntity> entityList = new ArrayList<>();
		TestEntity entityA = new TestEntity();
		entityA.setId("123");
		entityA.setPassword("456");
		entityList.add(entityA);

		TestEntity entityB = new TestEntity();
		entityB.setId("789");
		entityB.setPassword("098");
		entityList.add(entityB);

		String jsonStr = JSONUtil.toJsonStr(entityList);
		Assert.assertEquals("[{\"uid\":\"123\",\"password\":\"456\"},{\"uid\":\"789\",\"password\":\"098\"}]", jsonStr);
		List<TestEntity> testEntities = JSONUtil.toList(jsonStr, TestEntity.class);
		Assert.assertEquals("123", testEntities.get(0).getId());
		Assert.assertEquals("789", testEntities.get(1).getId());
	}

	@Data
	static class TestEntity {
		@Alias("uid")
		private String id;
		private String password;
	}
}
