package cn.hutool.db.test;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;

/**
 * MySQL操作单元测试
 * 
 * @author looly
 *
 */
public class MySQLTest {

	@Test
	@Ignore
	public void insertTest() throws SQLException {
		for (int id = 100; id < 200; id++) {
			Db.use("mysql").insert(Entity.create("user")//
					.set("id", id)//
					.set("name", "测试用户" + id)//
					.set("text", "描述" + id)//
					.set("test1", "t" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("mysql").page(Entity.create("user"), new Page(2, 10));
		for (Entity entity : result) {
			Console.log(entity.get("id"));
		}
	}

}
