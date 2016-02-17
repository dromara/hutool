package com.xiaoleilu.hutool.demo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.Page;
import com.xiaoleilu.hutool.db.PageResult;
import com.xiaoleilu.hutool.db.Session;
import com.xiaoleilu.hutool.db.SqlRunner;
import com.xiaoleilu.hutool.db.ds.SimpleDataSource;
import com.xiaoleilu.hutool.db.ds.druid.DruidDS;
import com.xiaoleilu.hutool.db.ds.pool.PooledDataSource;
import com.xiaoleilu.hutool.db.handler.EntityListHandler;
import com.xiaoleilu.hutool.db.meta.Table;
import com.xiaoleilu.hutool.db.sql.Order;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.Direction;
import com.xiaoleilu.hutool.db.sql.SqlExecutor;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * DB使用样例
 * 
 * @author loolly
 * 
 */
public class DbDemo {
	private final static Log log = StaticLog.get();

	private static String TABLE_NAME = "test_table";

	public static void main(String[] args) throws SQLException {
	}

	/**
	 * 样例
	 */
	public static void demo() {
		DataSource ds = getDataSource();

		sqlExecutorDemo(ds);
		sqlRunnerDemo(ds);
		sessionDemo(ds);
		getTableMetaInfo(ds);
	}

	/**
	 * @return 获得数据源样例方法
	 */
	private static DataSource getDataSource() {
		/*
		 * 获得数据源，可以使用Druid、DBCP或者C3P0数据源
		 * 我封装了Druid的数据源，在classpath下放置db.setting和druid.setting文件 
		 * 详细格式请参考doc/db-example.setting和doc/db/example.setting 
		 * 如果没有druid.setting文件，使用连接池默认的参数 可以配置多个数据源，用分组隔离
		 */
		DataSource ds = DruidDS.getDataSource("test");

		//当然，如果你不喜欢用DruidDS类，你也可以自己去实例化连接池的数据源 具体的配置参数请参阅Druid官方文档
		@SuppressWarnings("resource")
		DruidDataSource ds2 = new DruidDataSource();
		ds2.setUrl("jdbc:mysql://fedora.vmware:3306/extractor");
		ds2.setUsername("root");
		ds2.setPassword("123456");
		ds = ds2;
		
		//无连接池的数据源
		SimpleDataSource ds3 = SimpleDataSource.getDataSource();
		ds = ds3;
		
		//简易连接池
		DataSource ds4 = PooledDataSource.getDataSource();
		ds = ds4;

		return ds;
	}

	/**
	 * SqlExecutor样例方法<br>
	 * 如果你只是执行SQL语句，使用SqlExecutor类里的静态方法即可
	 * 
	 * @param ds 数据源
	 */
	private static void sqlExecutorDemo(DataSource ds) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			// 执行非查询语句，返回影响的行数
			int count = SqlExecutor.execute(conn, "UPDATE " + TABLE_NAME + " set field1 = ? where id = ?", 0, 0);
			log.info("影响行数：{}", count);
			// 执行非查询语句，返回自增的键，如果有多个自增键，只返回第一个
			Long generatedKey = SqlExecutor.executeForGeneratedKey(conn, "UPDATE " + TABLE_NAME + " set field1 = ? where id = ?", 0, 0);
			log.info("主键：{}", generatedKey);

			/* 执行查询语句，返回实体列表，一个Entity对象表示一行的数据，Entity对象是一个继承自HashMap的对象，存储的key为字段名，value为字段值 */
			List<Entity> entityList = SqlExecutor.query(conn, "select * from " + TABLE_NAME + " where param1 = ?", new EntityListHandler(), "值");
			log.info("{}", entityList);
		} catch (SQLException e) {
			log.error(e, "SQL error!");
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * SqlRunner是继承自SqlConnRunner的（SqlConnRunner继承自SqlExecutor），所以相应的方法也继承了下来，可以像SqlExecutor一样使用静态方法<br>
	 * 当然，SqlRunner更强大的功能在于对Entity对象做CRUD，避免写SQL语句。 SqlRunner需要实例化
	 * 
	 * SqlRunner同时提供了带Connection参数的CRUD方法，方便外部提供Connection对象而由使用者提供事务的操作
	 * 
	 * @param ds 数据源
	 */
	private static void sqlRunnerDemo(DataSource ds) {
		Entity entity = Entity.create(TABLE_NAME).set("字段1", "值").set("字段2", 2);
		Entity where = Entity.create(TABLE_NAME).set("条件1", "条件值");

		try {
			SqlRunner runner = SqlRunner.create(ds);
			// 根据DataSource会自动识别数据库方言
			runner = SqlRunner.create(ds);

			// 增，生成SQL为 INSERT INTO `table_name` SET(`字段1`, `字段2`) VALUES(?,?)
			runner.insert(entity);

			// 删，生成SQL为 DELETE FROM `table_name` WHERE `条件1` = ?
			runner.del(where);

			// 改，生成SQL为 UPDATE `table_name` SET `字段1` = ?, `字段2` = ? WHERE `条件1` = ?
			runner.update(entity, where);

			// 查，生成SQL为 SELECT * FROM `table_name` WHERE WHERE `条件1` = ? 第一个参数为返回的字段列表，如果null则返回所有字段
			List<Entity> entityList = runner.find(null, where, new EntityListHandler());
			log.info("{}", entityList);

			// 分页
			List<Entity> pagedEntityList = runner.page(null, where, 0, 20, new EntityListHandler());
			log.info("{}", pagedEntityList);
			
			//分页，提供了Page对象满足更多的排序条件要求
			PageResult<Entity> pageResult = runner.page(where, new Page(0, 20, new Order("字段名", Direction.DESC)));
			log.info("{}", pageResult);

			// 满足条件的结果数，生成SQL为 SELECT count(1) FROM `table_name` WHERE WHERE `条件1` = ?
			int count = runner.count(where);
			log.info("count: {}", count);
		} catch (SQLException e) {
			log.error(e, "SQL error!");
		} finally {
		}
	}

	private static void sessionDemo(DataSource ds) {
		Entity entity = Entity.create(TABLE_NAME).set("字段1", "值").set("字段2", 2);
		Entity where = Entity.create(TABLE_NAME).set("条件1", "条件值");

		Session session = Session.create(ds);
		try {
			session.beginTransaction();

			// 增，生成SQL为 INSERT INTO `table_name` SET(`字段1`, `字段2`) VALUES(?,?)
			session.insert(entity);

			// 删，生成SQL为 DELETE FROM `table_name` WHERE `条件1` = ?
			session.del(where);

			// 改，生成SQL为 UPDATE `table_name` SET `字段1` = ?, `字段2` = ? WHERE `条件1` = ?
			session.update(entity, where);

			// 查，生成SQL为 SELECT * FROM `table_name` WHERE WHERE `条件1` = ? 第一个参数为返回的字段列表，如果null则返回所有字段
			List<Entity> entityList = session.find(null, where, new EntityListHandler());
			log.info("{}", entityList);

			// 分页
			List<Entity> pagedEntityList = session.page(null, where, 0, 20, new EntityListHandler());
			log.info("{}", pagedEntityList);

			session.commit();
		} catch (Exception e) {
			session.quietRollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 获得表的元数据
	 * 
	 * @param ds 数据源
	 */
	private static void getTableMetaInfo(DataSource ds) {
		// 获得当前库的所有表的表名
		List<String> tableNames = DbUtil.getTables(ds);
		log.info("{}", tableNames);

		/*
		 * 获得表结构 表结构封装为一个表对象，里面有Column对象表示一列，列中有列名、类型、大小、是否允许为空等信息
		 */
		Table table = DbUtil.getTableMeta(ds, TABLE_NAME);
		log.info("{}", table);
	}
}
