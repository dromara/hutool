数据源工厂-DsFactory
===

## 释义

数据源（DataSource）的概念来自于JDBC规范中，一个数据源表示针对一个数据库（或者集群）的描述，从数据源中我们可以获得N个数据库连接，从而对数据库进行操作。

每一个开源JDBC连接池都有对DataSource的实现，比如Druid为DruidDataSource，Hikari为HikariDataSource。但是各大连接池配置各不相同，配置文件也不一样，Hutool的针对常用的连接池做了封装，最大限度简化和提供一致性配置。

Hutool的解决方案是：在ClassPath中使用`config/db.setting`一个配置文件，配置所有种类连接池的数据源，然后使用`DsFactory.get()`方法自动识别数据源以及自动注入配置文件中的连接池配置（包括数据库连接配置）。`DsFactory`通过`try`的方式按照顺序检测项目中引入的jar包来甄别用户使用的是哪种连接池，从而自动构建相应的数据源。

Hutool支持以下连接池，并按照其顺序检测存在与否：

1. HikariCP
2. Druid
3. Tomcat
4. Dbcp
5. C3p0

在没有引入任何连接池的情况下，Hutool会使用其内置的连接池：Hutool Pooled（简易连接池，不推荐在线上环境使用）。

## 基本使用

### 1. 引入连接池的jar
Hutool不会强依赖于任何第三方库，在Hutool支持的连接池范围内，用户需自行选择自己喜欢的连接池并引入。

### 2. 编写配置文件
Maven项目中，在`src/main/resources/config`下创建文件`db.setting`，编写配置文件即可。这个配置文件位置就是Hutool与用户间的一个约定（符合约定大于配置的原则）：

配置文件分为两部分

#### 1. 基本连接信息
```java
## 基本配置信息
# JDBC URL，根据不同的数据库，使用相应的JDBC连接字符串
url = jdbc:mysql://<host>:<port>/<database_name>
# 用户名，此处也可以使用 user 代替
username = 用户名
# 密码，此处也可以使用 pass 代替
password = 密码
# JDBC驱动名，可选（Hutool会自动识别）
driver = com.mysql.jdbc.Driver
```

> ** 小提示 **
> 其中driver是可选的，Hutool会根据url自动加载相应的Driver类。基本连接信息是所有连接池通用的，原则上，只有基本信息就可以成功连接并操作数据库。

#### 2. 连接池特有配置信息
针对不同的连接池，除了基本信息外的配置都各不相同，Hutool针对不同的连接池封装了其配置项，可以在项目的`src/test/resources/example`中看到针对不同连接池的配置文件样例。

我们以HikariCP为例：

```java
# 自动提交
autoCommit = true
# 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
connectionTimeout = 30000
# 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
idleTimeout = 600000
# 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';）
maxLifetime = 1800000
# 获取连接前的测试SQL
connectionTestQuery = SELECT 1
# 最小闲置连接数
minimumIdle = 10
# 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
maximumPoolSize = 10
# 连接只读数据库时配置为true， 保证安全
readOnly = false
```

### 3. 获取数据源
```java
//获取默认数据源
DataSource ds = DSFactory.get()
```

是滴，就是这么简单，一个简单的方法，可以识别数据源并读取默认路径(`config/db.setting`)下信息从而获取数据源。

### 4. 直接创建数据源
当然你依旧可以按照连接池本身的方式获取数据源对象。我们以Druid为例：
```java
//具体的配置参数请参阅Druid官方文档
DruidDataSource ds2 = new DruidDataSource();
ds2.setUrl("jdbc:mysql://localhost:3306/dbName");
ds2.setUsername("root");
ds2.setPassword("123456");
```
        
### 5. 创建简单数据源
有时候我们的操作非常简单，亦或者只是测试下远程数据库是否畅通，我们可以使用Hutool提供的`SimpleDataSource`:

```java
DataSource ds = new SimpleDataSource("jdbc:mysql://localhost:3306/dbName", "root", "123456");
```

SimpleDataSource只是`DriverManager.getConnection`的简单包装，本身并不支持池化功能，此类特别适合少量数据库连接的操作。

同样的，SimpleDataSource也支持默认配置文件：
```java
DataSource ds = new SimpleDataSource();
```

## 高级实用

### 1. 自定义连接池

有时候当项目引入多种数据源时，我们希望自定义需要的连接池，此时可以：
```java
//自定义连接池实现为Tomcat-pool
DSFactory.setCurrentDSFactory(new TomcatDSFactory());
DataSource ds = DSFactory.get();
```

需要注意的是，`DSFactory.setCurrentDSFactory`是一个全局方法，必须在所有获取数据源的时机之前调用，调用一次即可（例如项目启动）。

### 2. 自定义配置文件
有时候由于项目规划的问题，我们希望自定义数据库配置Setting的位置，甚至是动态加载Setting对象，此时我们可以使用以下方法从其它的Setting对象中获取数据库连接信息：

```java
//自定义数据库Setting，更多实用请参阅Hutool-Setting章节
Setting setting = new Setting("otherPath/other.setting");
//获取指定配置，第二个参数为分组，用于多数据源，无分组情况下传null
DataSource ds = DSFactory.get(setting, null);
```

### 3. 多数据源

有的时候我们需要操作不同的数据库，也有可能我们需要针对线上、开发和测试分别操作其数据库，无论哪种情况，Hutool都针对多数据源做了很棒的支持。

多数据源有两种方式可以实现：

#### 1. 多个配置文件分别获得数据源
就是按照自定义配置文件的方式读取多个配置文件即可。

#### 2. 在同一配置文件中使用分组隔离不同的数据源配置：

```java
[group_db1]
url = jdbc:mysql://<host>:<port>/<database_name>
username = 用户名
password = 密码

[group_db2]
url = jdbc:mysql://<host2>:<port>/<database_name>
username = 用户名
password = 密码
```

我们按照上面的方式编写`db.setting`文件，然后：

```java
DataSource ds1 = DSFactory.get("group_db1");
DataSource ds2 = DSFactory.get("group_db2");
```

这样我们就可以在一个配置文件中实现多数据源的配置。

## 结语

Hutool通过多种方式获取DataSource对象，获取后除了可以在Hutool自身应用外，还可以将此对象传入不同的框架以实现无缝结合。

Hutool对数据源的封装很好的诠释了以下几个原则：
1. 自动识别优于用户定义
2. 便捷性与灵活性并存
3. 适配与兼容

