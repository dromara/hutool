package cn.hutool.db;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SQL Server操作单元测试
 *
 * @author looly
 *
 */
public class SqlServerTest {

	@Test
	@Ignore
	public void createTableTest() {
		Db.of("sqlserver").execute("create table T_USER(ID bigint, name varchar(255))");
	}

	@Test
	@Ignore
	public void insertTest() {
		for (int id = 100; id < 200; id++) {
			Db.of("sqlserver").insert(Entity.create("T_USER")//
					.set("ID", id)//
					.set("name", "测试用户" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() {
		final PageResult<Entity> result = Db.of("sqlserver").page(Entity.create("T_USER"), new Page(2, 10));
		for (final Entity entity : result) {
			Console.log(entity.get("ID"));
		}
	}

}
