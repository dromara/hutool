package cn.hutool.db;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {

	@Test
	@Ignore
	public void transTest() {
		final Session session = Session.of("test");
		session.beginTransaction();
		session.update(Entity.create().set("age", 76), Entity.create("user").set("name", "unitTestUser"));
		session.commit();
	}

	@Test
	@Ignore
	public void txTest() {
		Session.of("test").tx(session -> session.update(Entity.create().set("age", 78), Entity.create("user").set("name", "unitTestUser")));
	}
}
