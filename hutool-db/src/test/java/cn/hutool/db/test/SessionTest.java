package cn.hutool.db.test;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.VoidFunc;
import cn.hutool.db.Entity;
import cn.hutool.db.Session;

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
	public void transFuncTest() {
		final Session session = Session.create("test");
		session.trans(new VoidFunc() {
			@Override
			public void call() throws SQLException {
				session.update(Entity.create().set("age", 78), Entity.create("user").set("name", "unitTestUser"));
			}
		});
	}
}
