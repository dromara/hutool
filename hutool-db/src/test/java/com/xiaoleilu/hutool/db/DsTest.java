package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;

import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.db.ds.c3p0.C3p0DSFactory;
import com.xiaoleilu.hutool.db.ds.dbcp.DbcpDSFactory;
import com.xiaoleilu.hutool.db.ds.druid.DruidDSFactory;
import com.xiaoleilu.hutool.db.ds.hikari.HikariDSFactory;
import com.xiaoleilu.hutool.db.ds.pooled.PooledDSFactory;
import com.xiaoleilu.hutool.db.ds.tomcat.TomcatDSFactory;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 数据源单元测试
 * @author Looly
 *
 */
public class DsTest {
	
	@Test
	public void DefaultDsTest() throws SQLException{
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void HikariDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new HikariDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void DruidDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new DruidDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void TomcatDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new TomcatDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void DbcpDsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new DbcpDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void C3p0DsTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new C3p0DSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
	
	@Test
	public void HutoolPoolTest() throws SQLException{
		DSFactory.setCurrentDSFactory(new PooledDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
}
