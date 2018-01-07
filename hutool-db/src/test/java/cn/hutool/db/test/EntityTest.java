package cn.hutool.db.test;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.db.Entity;

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
	public void entityToBeanIgnoreCaseTest() {
		Entity entity = Entity.create().set("ID", 2).set("NAME", "testName");
		User user = entity.toBeanIgnoreCase(User.class);
		
		Assert.assertEquals(2, user.getId());
		Assert.assertEquals("testName", user.getName());
	}
	
	public static class User{
		private int id;
		private String name;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
