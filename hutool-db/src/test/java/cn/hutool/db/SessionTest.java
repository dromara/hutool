package cn.hutool.db;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {

	@Test
	@Ignore
	public void transTest() {
		final Session session = Session.create("test");
		try {
			session.beginTransaction();
			session.update(Entity.create().set("age", 76), Entity.create("user").set("name", "unitTestUser"));
			session.commit();
		} catch (final SQLException e) {
			session.quietRollback();
		}
	}

	@Test
	@Ignore
	public void txTest() throws SQLException {
		Session.create("test").tx(session -> session.update(Entity.create().set("age", 78), Entity.create("user").set("name", "unitTestUser")));
	}
}
