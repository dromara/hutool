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
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("orcl").page(Entity.create("T_USER").set("name", "张三"), new Page(1, 10));
		Console.log(result);
	}

}
