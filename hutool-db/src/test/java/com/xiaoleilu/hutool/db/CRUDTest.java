package com.xiaoleilu.hutool.db;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CollectionUtil;

public class CRUDTest {
	
	SqlRunner runner;
	
	@Before
	public void init() {
		runner = SqlRunner.create(DSFactory.get());
	}
	
	/**
	 * 对增删改查做单元测试
	 * @throws SQLException
	 */
	@Test
	@Ignore
	public void crudTest() throws SQLException{
		
		//增
		Long id = runner.insertForGeneratedKey(Entity.create("user").set("name", "unitTestUser").set("age", 66));
		Assert.assertTrue(id > 0);
		Entity result = runner.get("user", "name", "unitTestUser");
		Assert.assertSame(66, (int)result.getInt("age"));
		
		//改
		int update = runner.update(Entity.create().set("age", 88), Entity.create("user").set("name", "unitTestUser"));
		Assert.assertTrue(update > 0);
		Entity result2 = runner.get("user", "name", "unitTestUser");
		Assert.assertSame(88, (int)result2.getInt("age"));
		
		//删
		int del = runner.del("user", "name", "unitTestUser");
		Assert.assertTrue(del > 0);
		Entity result3 = runner.get("user", "name", "unitTestUser");
		Assert.assertNull(result3);
	}
	
	@Test
	@Ignore
	public void insertBatchTest() throws SQLException {
		User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);
		
		User user2 = new User();
		user2.setName("李四");
		user2.setAge(12);
		user2.setBirthday("19890512");
		user2.setGender(false);
		
		Entity data1 = Entity.parse(user1);
		Entity data2 = Entity.parse(user2);
		
		Console.log(data1);
		Console.log(data2);
		
		int[] result = runner.insert(CollectionUtil.newArrayList(data1, data2));
		Console.log(result);
	}
	
	public static class User{
		private Integer id;
		private String name;
		private int age;
		private String birthday;
		private boolean gender;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		public boolean isGender() {
			return gender;
		}
		public void setGender(boolean gender) {
			this.gender = gender;
		}
	}
}
