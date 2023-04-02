package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.db.ds.DSUtil;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.db.ds.bee.BeeDSFactory;
import org.dromara.hutool.db.ds.c3p0.C3p0DSFactory;
import org.dromara.hutool.db.ds.dbcp.DbcpDSFactory;
import org.dromara.hutool.db.ds.druid.DruidDSFactory;
import org.dromara.hutool.db.ds.hikari.HikariDSFactory;
import org.dromara.hutool.db.ds.pooled.PooledDSFactory;
import org.dromara.hutool.db.ds.tomcat.TomcatDSFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void hikariDsTest() {
		DSUtil.setGlobalDSFactory(new HikariDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void druidDsTest() {
		DSUtil.setGlobalDSFactory(new DruidDSFactory());
		final DataSource ds = DSUtil.getDS("test");

		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void tomcatDsTest() {
		DSUtil.setGlobalDSFactory(new TomcatDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void beeCPDsTest() {
		DSUtil.setGlobalDSFactory(new BeeDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void dbcpDsTest() {
		DSUtil.setGlobalDSFactory(new DbcpDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsTest() {
		DSUtil.setGlobalDSFactory(new C3p0DSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}

	@Test
	public void c3p0DsuserAndPassTest() {
		// https://gitee.com/dromara/hutool/issues/I4T7XZ
		DSUtil.setGlobalDSFactory(new C3p0DSFactory());
		final ComboPooledDataSource ds = (ComboPooledDataSource) ((DSWrapper) DSUtil.getDS("mysql")).getRaw();
		Assertions.assertEquals("root", ds.getUser());
		Assertions.assertEquals("123456", ds.getPassword());
	}

	@Test
	public void hutoolPoolTest() {
		DSUtil.setGlobalDSFactory(new PooledDSFactory());
		final DataSource ds = DSUtil.getDS("test");
		final Db db = Db.of(ds);
		final List<Entity> all = db.findAll("user");
		Assertions.assertTrue(CollUtil.isNotEmpty(all));
	}
}
