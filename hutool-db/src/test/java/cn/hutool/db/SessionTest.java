package cn.hutool.db;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 事务性数据库操作单元测试
 * @author looly
 *
 */
public class SessionTest {

	@Test
	@Disabled
	public void transTest() {
		final Session session = Session.of("test");
		session.beginTransaction();
		session.update(Entity.of().set("age", 76), Entity.of("user").set("name", "unitTestUser"));
		session.commit();
	}

	@Test
	@Disabled
	public void txTest() {
		Session.of("test").tx(session -> session.update(Entity.of().set("age", 78), Entity.of("user").set("name", "unitTestUser")));
	}
}
