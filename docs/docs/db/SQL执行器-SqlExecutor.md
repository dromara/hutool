SQL执行器-SqlExecutor
===

## 介绍
这是一个静态类，对JDBC的薄封装，里面的静态方法只有两种：执行非查询的SQL语句和查询的SQL语句

## 使用

```java
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
	Log.error(log, e, "SQL error!");
} finally {
	DbUtil.close(conn);
}
```

