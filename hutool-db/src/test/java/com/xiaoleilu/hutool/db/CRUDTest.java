package com.xiaoleilu.hutool.db;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.db.ds.DSFactory;

public class CRUDTest {
	
	/**
	 * 对增删改查做单元测试
	 * @throws SQLException
	 */
	@Test
	public void crudTest() throws SQLException{
		SqlRunner runner = SqlRunner.create(DSFactory.get());
		
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
}
