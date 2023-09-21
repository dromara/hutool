/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.handler.EntityListHandler;
import org.dromara.hutool.db.pojo.User;
import org.dromara.hutool.db.sql.Condition;
import org.dromara.hutool.db.sql.Condition.LikeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 增删改查测试
 *
 * @author looly
 */
public class CRUDTest {

	private static final Db db = Db.of("test");

	@Test
	public void findIsNullTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", "is null"));
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void findIsNullTest2() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", "= null"));
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void findIsNullTest3() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", null));
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void findBetweenTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", "between '18' and '40'"));
		Assertions.assertEquals(1, results.size());
	}

	@Test
	public void findByBigIntegerTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", new BigInteger("12")));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findByBigDecimalTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("age", new BigDecimal("12")));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("name", "like \"%三%\""));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest2() {
		final List<Entity> results = db.findAll(Entity.of("user").set("name", new Condition("name", "三", LikeType.Contains)));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findLikeTest3() {
		final List<Entity> results = db.findAll(Entity.of("user").set("name", new Condition("name", null, LikeType.Contains)));
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void findInTest() {
		final List<Entity> results = db.findAll(Entity.of("user").set("id", "in 1,2,3"));
		Console.log(results);
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findInTest2() {
		final List<Entity> results = db.findAll(Entity.of("user")
				.set("id", new Condition("id", new long[]{1, 2, 3})));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findInTest3() {
		final List<Entity> results = db.findAll(Entity.of("user")
				.set("id", new long[]{1, 2, 3}));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findInTest4() {
		final List<Entity> results = db.findAll(Entity.of("user")
				.set("id", new String[]{"1", "2", "3"}));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	public void findAllTest() {
		final List<Entity> results = db.findAll("user");
		Assertions.assertEquals(4, results.size());
	}

	@Test
	public void findTest() {
		final List<Entity> find = db.find(ListUtil.of("name AS name2"), Entity.of("user"), EntityListHandler.of());
		Assertions.assertFalse(find.isEmpty());
	}

	@Test
	public void findActiveTest() {
		final ActiveEntity entity = new ActiveEntity(db, "user");
		entity.setFieldNames("name AS name2").load();
		Assertions.assertEquals("user", entity.getTableName());
		Assertions.assertFalse(entity.isEmpty());
	}

	/**
	 * 对增删改查做单元测试
	 *
	 */
	@Test
	@Disabled
	public void crudTest() {

		// 增
		final Long id = db.insertForGeneratedKey(Entity.of("user").set("name", "unitTestUser").set("age", 66));
		Assertions.assertTrue(id > 0);
		final Entity result = db.get("user", "name", "unitTestUser");
		Assertions.assertSame(66, result.getInt("age"));

		// 改
		final int update = db.update(Entity.of().set("age", 88), Entity.of("user").set("name", "unitTestUser"));
		Assertions.assertTrue(update > 0);
		final Entity result2 = db.get("user", "name", "unitTestUser");
		Assertions.assertSame(88, result2.getInt("age"));

		// 删
		final int del = db.del("user", "name", "unitTestUser");
		Assertions.assertTrue(del > 0);
		final Entity result3 = db.get("user", "name", "unitTestUser");
		Assertions.assertNull(result3);
	}

	@Test
	@Disabled
	public void insertBatchTest() {
		final User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);

		final User user2 = new User();
		user2.setName("李四");
		user2.setAge(12);
		user2.setBirthday("19890512");
		user2.setGender(false);

		final Entity data1 = Entity.parse(user1);
		data1.put("name", null);
		final Entity data2 = Entity.parse(user2);

		Console.log(data1);
		Console.log(data2);

		final int[] result = db.insert(ListUtil.of(data1, data2));
		Console.log(result);
	}

	@Test
	@Disabled
	public void insertBatchOneTest() {
		final User user1 = new User();
		user1.setName("张三");
		user1.setAge(12);
		user1.setBirthday("19900112");
		user1.setGender(true);

		final Entity data1 = Entity.parse(user1);

		Console.log(data1);

		final int[] result = db.insert(ListUtil.of(new Entity[]{data1}));
		Console.log(result);
	}

	@Test
	public void selectInTest() {
		final List<Entity> results = db.query("select * from user where id in (:ids)",
				MapUtil.of("ids", new int[]{1, 2, 3}));
		Assertions.assertEquals(2, results.size());
	}

	@Test
	@Disabled
	public void findWithDotTest(){
		db.find(Entity.of("user").set("WTUR.Other.Rg.S.WTName", "value"));
	}
}
