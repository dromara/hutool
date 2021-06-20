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
		User user = new User();
		user.setId(1);
		user.setName("test");

		Entity entity = Entity.create("testTable").parseBean(user);
		Assert.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assert.assertEquals("test", entity.getStr("name"));
	}

	@Test
	public void parseTest2() {
		User user = new User();
		user.setId(1);
		user.setName("test");

		Entity entity = Entity.create().parseBean(user);
		Assert.assertEquals(Integer.valueOf(1), entity.getInt("id"));
		Assert.assertEquals("test", entity.getStr("name"));
		Assert.assertEquals("user", entity.getTableName());
	}

	@Test
	public void parseTest3() {
		User user = new User();
		user.setName("test");

		Entity entity = Entity.create().parseBean(user, false, true);

		Assert.assertFalse(entity.containsKey("id"));
		Assert.assertEquals("test", entity.getStr("name"));
		Assert.assertEquals("user", entity.getTableName());
	}

	@Test
	public void entityToBeanIgnoreCaseTest() {
		Entity entity = Entity.create().set("ID", 2).set("NAME", "testName");
		User user = entity.toBeanIgnoreCase(User.class);

		Assert.assertEquals(Integer.valueOf(2), user.getId());
		Assert.assertEquals("testName", user.getName());
	}
}
