package cn.hutool.db;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.db.sql.Condition;
import cn.hutool.log.StaticLog;

/**
 * Db对象单元测试
 * @author looly
 *
 */
public class DbTest {

	@Test
	public void queryTest() throws SQLException {
		List<Entity> find = Db.use().query("select * from user where age = ?", 18);
		Assert.assertEquals("王五", find.get(0).get("name"));
	}
	
	@Test
	public void findTest() throws SQLException {
		List<Entity> find = Db.use().find(Entity.create("user").set("age", 18));
		Assert.assertEquals("王五", find.get(0).get("name"));
	}
	
	@Test
	public void findByTest() throws SQLException {
		List<Entity> find = Db.use().findBy("user",
				Condition.parse("age", "> 18"), 
				Condition.parse("age", "< 100")
		);
		for (Entity entity : find) {
			StaticLog.debug("{}", entity);
		}
		Assert.assertEquals("unitTestUser", find.get(0).get("name"));
	}
	
	@Test
	@Ignore
	public void txTest() throws SQLException {
		Db.use().tx(new VoidFunc1<Db>() {
			
			@Override
			public void call(Db db) throws SQLException {
				db.insert(Entity.create("user").set("name", "unitTestUser2"));
				db.update(Entity.create().set("age", 79), Entity.create("user").set("name", "unitTestUser2"));
				db.del("user", "name", "unitTestUser2");
			}
		});
	}
}
