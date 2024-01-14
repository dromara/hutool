<p align="center">
	<a href="https://hutool.cn/"><img src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-db 模块介绍

`Hutool-db`是一个在JDBC基础上封装的数据库操作工具类。通过包装，使用ActiveRecord思想操作数据库。
在Hutool-db中，使用Entity（本质上是个Map）代替Bean来使数据库操作更加灵活，同时提供Bean和Entity的转换提供传统ORM的兼容支持。

-------------------------------------------------------------------------------

## 🛠️包含内容

### 数据库方言（dialect）
通过抽象CRUD方法，针对不同数据库，封装对应的查询、分页等SQL转换功能，入口为`DialectFactory`。

### 数据源（ds）
提供常用数据库连接池的门面支持，支持顺序为：

- Hikari > Druid > Tomcat > BeeCP > Dbcp > C3p0 > Hutool Pooled

### SQL相关工具（sql）
提供SQL相关功能，包括SQL变量替换（NamedSql），通过对象完成SQL构建（SqlBuilder）等。

`SqlExecutor`提供SQL执行的静态方法。

### 数据库元信息（meta）
通过`MetaUtil`提供数据库表、字段等信息的读取操作。

### 增删改查（Db、Session）
提供增删改查的汇总方法类。

### 结果转换（handler）
通过封装`RsHandler`，提供将ResultSet转换为需要的对象的功能。