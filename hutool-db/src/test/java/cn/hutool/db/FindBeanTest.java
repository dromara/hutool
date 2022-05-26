package cn.hutool.db;

import cn.hutool.db.pojo.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Entity测试
 *
 * @author looly
 *
 */
public class FindBeanTest {

	Db db;

	@Before
	public void init() {
		db = Db.of("test");
	}

	@Test
	public void findAllBeanTest() {
		final List<User> results = db.findAll(Entity.of("user"), User.class);

		Assert.assertEquals(4, results.size());
		Assert.assertEquals(Integer.valueOf(1), results.get(0).getId());
		Assert.assertEquals("张三", results.get(0).getName());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void findAllListTest() {
		final List<List> results = db.findAll(Entity.of("user"), List.class);

		Assert.assertEquals(4, results.size());
		Assert.assertEquals(1, results.get(0).get(0));
		Assert.assertEquals("张三", results.get(0).get(1));
	}

	@Test
	public void findAllArrayTest() {
		final List<Object[]> results = db.findAll(Entity.of("user"), Object[].class);

		Assert.assertEquals(4, results.size());
		Assert.assertEquals(1, results.get(0)[0]);
		Assert.assertEquals("张三", results.get(0)[1]);
	}

	@Test
	public void findAllStringTest() {
		final List<String> results = db.findAll(Entity.of("user"), String.class);
		Assert.assertEquals(4, results.size());
	}

	@Test
	public void findAllStringArrayTest() {
		final List<String[]> results = db.findAll(Entity.of("user"), String[].class);

		Assert.assertEquals(4, results.size());
		Assert.assertEquals("1", results.get(0)[0]);
		Assert.assertEquals("张三", results.get(0)[1]);
	}
}
