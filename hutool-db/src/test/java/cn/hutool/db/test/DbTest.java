package cn.hutool.db.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.transaction.TxFunc;

/**
 * Db对象单元测试
 * @author looly
 *
 */
public class DbTest {
	
	@Test
	@Ignore
	public void txTest() throws SQLException {
		List<Entity> find = Db.use().find(Entity.create("user").set("age", 18));
		Console.log(find);
		
		Db.use().tx(new TxFunc() {
			
			@Override
			public void call(Db db) throws SQLException {
				db.insert(Entity.create("user").set("name", "unitTestUser"));
				db.update(Entity.create().set("age", 79), Entity.create("user").set("name", "unitTestUser"));
			}
		});
	}
}
