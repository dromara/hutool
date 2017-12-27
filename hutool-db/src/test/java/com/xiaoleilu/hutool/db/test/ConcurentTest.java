package com.xiaoleilu.hutool.db.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlRunner;
import com.xiaoleilu.hutool.db.handler.EntityListHandler;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * SqlRunner线程安全测试
 * 
 * @author looly
 *
 */
@Ignore
public class ConcurentTest {
	
	private SqlRunner runner;
	
	@Before
	public void init() {
		LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
		DbUtil.setShowSqlGlobal(true, false);
		runner = SqlRunner.create();
	}
	
	@Test
	public void findTest() {
		for(int i = 0; i < 10000; i++) {
			ThreadUtil.execute(new Runnable() {
				@Override
				public void run() {
					List<Entity> find = null;
					try {
						find = runner.find(CollectionUtil.newArrayList("name AS name2"), Entity.create("user"), new EntityListHandler());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Console.log(find);
				}
			});
		}
	}
}
