package com.xiaoleilu.hutool.db.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlRunner;
import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.db.handler.EntityListHandler;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;

/**
 * 增删改查测试
 * 
 * @author looly
 *
 */
public class CRUDTest {
	
	SqlRunner runner;
	
	@Before
	public void init() {
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		DbUtil.setShowSqlGlobal(true, false);
		runner = SqlRunner.create(DSFactory.get());
	}
	
	@Test
	public void findBetweenTest() throws SQLException {
		List<Entity> results = runner.findAll(Entity.create("user").set("age", "between '18' and '40'"));
		Assert.assertEquals(1, results.size());
	}
	
	@Test
	public void findLikeTest() throws SQLException {
		List<Entity> results = runner.findAll(Entity.create("user").set("name", "like \"%三%\""));
		Assert.assertEquals(2, results.size());
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
	public void findTest() throws SQLException {
		List<Entity> find = runner.find(CollectionUtil.newArrayList("name AS name2"), Entity.create("user"), new EntityListHandler());
		Assert.assertFalse(find.isEmpty());
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
	
	@Test
	@Ignore
	public void insertBatchOneTest() throws SQLException {
		User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);
		
		Entity data1 = Entity.parse(user1);
		
		Console.log(data1);
		
		int[] result = runner.insert(CollectionUtil.newArrayList(data1));
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
