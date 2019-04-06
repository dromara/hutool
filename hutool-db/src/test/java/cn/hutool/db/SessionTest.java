package cn.hutool.db;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.func.VoidFunc1;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {
	
	@Test
	@Ignore
	public void transTest() {
		Session session = Session.create("test");
		try {
			session.beginTransaction();
			session.update(Entity.create().set("age", 76), Entity.create("user").set("name", "unitTestUser"));
			session.commit();
		} catch (SQLException e) {
			session.quietRollback();
		}
	}
	
	@Test
	@Ignore
	public void transFuncTest() throws SQLException {
		Session.create("test").tx(new VoidFunc1<Session>() {
			@Override
			public void call(Session session) throws SQLException {
				session.update(Entity.create().set("age", 78), Entity.create("user").set("name", "unitTestUser"));
			}
		});
	}
}
