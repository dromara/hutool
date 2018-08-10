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
 * Oracle操作单元测试
 * 
 * @author looly
 *
 */
public class OracleTest {

	@Test
	@Ignore
	public void insertTest() throws SQLException {
		for (int id = 100; id < 200; id++) {
			Db.use("orcl").insert(Entity.create("T_USER")//
					.set("ID", id)//
					.set("name", "测试用户" + id)//
					.set("TEXT", "描述" + id)//
					.set("TEST1", "t" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("orcl").page(Entity.create("T_USER"), new Page(2, 10));
		for (Entity entity : result) {
			Console.log(entity.get("ID"));
		}
	}

}
