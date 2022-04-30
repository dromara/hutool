package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.ds.DataSourceWrapper;
import cn.hutool.db.ds.bee.BeeDSFactory;
import cn.hutool.db.ds.c3p0.C3p0DSFactory;
import cn.hutool.db.ds.dbcp.DbcpDSFactory;
import cn.hutool.db.ds.druid.DruidDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.db.ds.tomcat.TomcatDSFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源单元测试
 *
 * @author Looly
 *
 */
public class DsTest {

	@Test
	public void defaultDsTest() {
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void hikariDsTest() {
		DSFactory.setCurrentDSFactory(new HikariDSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void druidDsTest() {
		DSFactory.setCurrentDSFactory(new DruidDSFactory());
		final DataSource ds = DSFactory.get("test");

		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void tomcatDsTest() {
		DSFactory.setCurrentDSFactory(new TomcatDSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void beeCPDsTest() {
		DSFactory.setCurrentDSFactory(new BeeDSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void dbcpDsTest() {
		DSFactory.setCurrentDSFactory(new DbcpDSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsTest() {
		DSFactory.setCurrentDSFactory(new C3p0DSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsuserAndPassTest() {
		// https://gitee.com/dromara/hutool/issues/I4T7XZ
		DSFactory.setCurrentDSFactory(new C3p0DSFactory());
		final ComboPooledDataSource ds = (ComboPooledDataSource) ((DataSourceWrapper) DSFactory.get("mysql")).getRaw();
		Assert.assertEquals("root", ds.getUser());
		Assert.assertEquals("123456", ds.getPassword());
	}

	@Test
	public void hutoolPoolTest() {
		DSFactory.setCurrentDSFactory(new PooledDSFactory());
		final DataSource ds = DSFactory.get("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assert.assertTrue(CollUtil.isNotEmpty(all));
	}
}
