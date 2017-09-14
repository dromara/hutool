package com.xiaoleilu.hutool.db.test;

import org.junit.Test;

import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.lang.Console;

public class EntityTest {
	
	@Test
	public void parseTest() {
		User user = new User();
		user.setId(1);
		user.setName("test");
		
		Entity entity = Entity.create("testTable").parseBean(user);
		Console.log(entity);
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
