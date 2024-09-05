/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db.ds;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.db.Db;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.ds.bee.BeeDSFactory;
import org.dromara.hutool.db.ds.c3p0.C3p0DSFactory;
import org.dromara.hutool.db.ds.dbcp.DbcpDSFactory;
import org.dromara.hutool.db.ds.druid.DruidDSFactory;
import org.dromara.hutool.db.ds.hikari.HikariDSFactory;
import org.dromara.hutool.db.ds.pooled.PooledDSFactory;
import org.dromara.hutool.db.ds.tomcat.TomcatDSFactory;
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
		final ComboPooledDataSource ds = (ComboPooledDataSource) DSUtil.getDS("mysql").getRaw();
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
