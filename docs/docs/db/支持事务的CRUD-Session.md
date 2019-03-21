支持事务的CRUD-Session
===

## 介绍
`Session`非常类似于`SqlRunner`，差别是`Session`对象中只有一个Connection，所有操作也是用这个Connection，便于事务操作，而`SqlRunner`每执行一个方法都要从`DataSource`中去要Connection。样例如下：

### Session创建
与`SqlRunner`类似，`Session`也可以通过调用create

```java
//默认数据源
Session session = Session.create();

//自定义数据源（此处取test分组的数据源）
Session session = Session.create(DSFactory.get("test"));
```

### 事务CRUD

`session.beginTransaction()`表示事务开始，调用后每次执行语句将不被提交，只有调用`commit`方法后才会合并提交，提交或者回滚后会恢复默认的自动提交模式。

1. 新增

```java
Entity entity = Entity.create(TABLE_NAME).set("字段1", "值").set("字段2", 2);
try {
	session.beginTransaction();
	// 增，生成SQL为 INSERT INTO `table_name` SET(`字段1`, `字段2`) VALUES(?,?)
	session.insert(entity);
	session.commit();
} catch (SQLException e) {
	session.quietRollback();
}
```

2. 更新

```java
Entity entity = Entity.create(TABLE_NAME).set("字段1", "值").set("字段2", 2);
Entity where = Entity.create(TABLE_NAME).set("条件1", "条件值");
try {
	session.beginTransaction();
	// 改，生成SQL为 UPDATE `table_name` SET `字段1` = ?, `字段2` = ? WHERE `条件1` = ?
	session.update(entity, where);
	session.commit();
} catch (SQLException e) {
	session.quietRollback();
}
```

3. 删除

```java
Entity where = Entity.create(TABLE_NAME).set("条件1", "条件值");
try {
	session.beginTransaction();
	// 删，生成SQL为 DELETE FROM `table_name` WHERE `条件1` = ?
	session.del(where);
	session.commit();
} catch (SQLException e) {
	session.quietRollback();
}
```


