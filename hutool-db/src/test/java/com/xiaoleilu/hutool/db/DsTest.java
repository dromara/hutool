package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.ds.druid.DruidDSFactory;
import com.xiaoleilu.hutool.lang.Console;

public class DsTest {
	public static void main(String[] args) throws SQLException {
		DruidDSFactory factory = new DruidDSFactory();
		DataSource ds = factory.getDataSource();
		SqlRunner runner = SqlRunner.create(ds);
		List<Entity> all = runner.findAll("user");
		Console.log(all);
	}
}
