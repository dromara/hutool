package cn.hutool.db;

import cn.hutool.db.pojo.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
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
		db = Db.use("test");
	}

	@Test
	public void findAllBeanTest() throws SQLException {
		List<User> results = db.findAll(Entity.create("user"), User.class);
		
		Assert.assertEquals(4, results.size());
		Assert.assertEquals(Integer.valueOf(1), results.get(0).getId());
		Assert.assertEquals("张三", results.get(0).getName());
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void findAllListTest() throws SQLException {
		List<List> results = db.findAll(Entity.create("user"), List.class);
		
		Assert.assertEquals(4, results.size());
		Assert.assertEquals(1, results.get(0).get(0));
		Assert.assertEquals("张三", results.get(0).get(1));
	}
	
	@Test
	public void findAllArrayTest() throws SQLException {
		List<Object[]> results = db.findAll(Entity.create("user"), Object[].class);
		
		Assert.assertEquals(4, results.size());
		Assert.assertEquals(1, results.get(0)[0]);
		Assert.assertEquals("张三", results.get(0)[1]);
	}
	
	@Test
	public void findAllStringTest() throws SQLException {
		List<String> results = db.findAll(Entity.create("user"), String.class);
		Assert.assertEquals(4, results.size());
	}
	
	@Test
	public void findAllStringArrayTest() throws SQLException {
		List<String[]> results = db.findAll(Entity.create("user"), String[].class);
		
		Assert.assertEquals(4, results.size());
		Assert.assertEquals("1", results.get(0)[0]);
		Assert.assertEquals("张三", results.get(0)[1]);
	}
}
