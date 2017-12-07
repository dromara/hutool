package com.xiaoleilu.hutool.db.test;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.Session;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {
	
	@Test
	@Ignore
	public void transTest() {
		Session session = Session.create();
		try {
			session.beginTransaction();
			session.update(Entity.create().set("age", 76), Entity.create("user").set("name", "unitTestUser"));
			session.commit();
		} catch (SQLException e) {
			session.quietRollback();
		}
	}
}
