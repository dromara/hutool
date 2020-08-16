package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.bee.BeeDSFactory;
import cn.hutool.db.ds.c3p0.C3p0DSFactory;
import cn.hutool.db.ds.dbcp.DbcpDSFactory;
import cn.hutool.db.ds.druid.DruidDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.db.ds.tomcat.TomcatDSFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据源单元测试
 * 
 * @author Looly
 *
 */
public class DsTest {

	@Test
	public void defaultDsTest() throws SQLException {
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void hikariDsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new HikariDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void druidDsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new DruidDSFactory());
		DataSource ds = DSFactory.get("test");

		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void tomcatDsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new TomcatDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void beeCPDsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new BeeDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void dbcpDsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new DbcpDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new C3p0DSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void hutoolPoolTest() throws SQLException {
		DSFactory.setCurrentDSFactory(new PooledDSFactory());
		DataSource ds = DSFactory.get("test");
		Db db = Db.use(ds);
		List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}
}
