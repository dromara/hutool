package cn.hutool.db;

import cn.hutool.db.pojo.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * Entity测试
 *
 * @author looly
 *
 */
public class EntityTest {

	@Test
	public void parseTest() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of("testTable").parseBean(user);
		Assert.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assert.assertEquals("test", entity.getStr("name"));
	}

	@Test
	public void parseTest2() {
		final User user = new User();
		user.setId(1);
		user.setName("test");

		final Entity entity = Entity.of().parseBean(user);
		Assert.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assert.assertEquals("test", entity.getStr("name"));
		Assert.assertEquals("user", entity.getTableName());
	}

	@Test
	public void parseTest3() {
		final User user = new User();
		user.setName("test");

		final Entity entity = Entity.of().parseBean(user, false, true);

		Assert.assertFalse(entity.containsKey("id"));
		Assert.assertEquals("test", entity.getStr("name"));
		Assert.assertEquals("user", entity.getTableName());
	}

	@Test
	public void entityToBeanIgnoreCaseTest() {
		final Entity entity = Entity.of().set("ID", 2).set("NAME", "testName");
		final User user = entity.toBeanIgnoreCase(User.class);

		Assert.assertEquals(Integer.valueOf(2), user.getId());
		Assert.assertEquals("testName", user.getName());
	}
}
