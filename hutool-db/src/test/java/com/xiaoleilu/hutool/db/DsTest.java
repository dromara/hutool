package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.db.ds.druid.DruidDSFactory;
import com.xiaoleilu.hutool.lang.Console;

public class DsTest {
	public static void main(String[] args) throws SQLException {
		DSFactory.setCurrentDSFactory(new DruidDSFactory());
		DataSource ds = DSFactory.get();
		SqlRunner runner = SqlRunner.create(ds);
		runner = SqlRunner.create(ds);
		
//		int i = runner.insert(Entity.create("user").set("name", "王五").set("age", 18));
//		Console.log(i);
		
		List<Entity> all = runner.findAll("user");
		for (Entity entity : all) {
			Console.log(entity);
		}
	}
}
