数据源配置db.setting样例
===

DsFactory默认读取的配置文件是config/db.setting或db.setting，db.setting的配置包括两部分：基本连接信息和连接池配置信息。

基本连接信息所有连接池都支持，连接池配置信息根据不同的连接池，连接池配置是根据连接池相应的配置项移植而来。

## 基本配置样例

```
#------------------------------------------------------------------------------------------
## 基本配置信息
# JDBC URL，根据不同的数据库，使用相应的JDBC连接字符串
url = jdbc:mysql://<host>:<port>/<database_name>
# 用户名，此处也可以使用 user 代替
username = 用户名
# 密码，此处也可以使用 pass 代替
password = 密码
# JDBC驱动名，可选（Hutool会自动识别）
driver = com.mysql.jdbc.Driver

## 可选配置
# 是否在日志中显示执行的SQL
showSql = true
# 是否格式化显示的SQL
formatSql = false
# 是否显示SQL参数
showParams = true
#------------------------------------------------------------------------------------------
```

## HikariCP
```
## 连接池配置项
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

## Druid
```
# 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
initialSize = 0
# 最大连接池数量
maxActive = 8
# 最小连接池数量
minIdle = 0
# 获取连接时最大等待时间，单位毫秒。配置了maxWait之后， 缺省启用公平锁，并发效率会有所下降， 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
maxWait = 0
# 是否缓存preparedStatement，也就是PSCache。 PSCache对支持游标的数据库性能提升巨大，比如说oracle。 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。作者在5.5版本中使用PSCache，通过监控界面发现PSCache有缓存命中率记录， 该应该是支持PSCache。
poolPreparedStatements = false
# 要启用PSCache，必须配置大于0，当大于0时， poolPreparedStatements自动触发修改为true。 在Druid中，不会存在Oracle下PSCache占用内存过多的问题， 可以把这个数值配置大一些，比如说100
maxOpenPreparedStatements = -1
# 用来检测连接是否有效的sql，要求是一个查询语句。 如果validationQuery为null，testOnBorrow、testOnReturn、 testWhileIdle都不会其作用。
validationQuery = SELECT 1
# 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
testOnBorrow = true
# 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
testOnReturn = false
# 建议配置为true，不影响性能，并且保证安全性。 申请连接的时候检测，如果空闲时间大于 timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
testWhileIdle = false
# 有两个含义： 1) Destroy线程会检测连接的间隔时间 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
timeBetweenEvictionRunsMillis = 60000
# 物理连接初始化的时候执行的sql
connectionInitSqls = SELECT 1
# 属性类型是字符串，通过别名的方式配置扩展插件， 常用的插件有： 监控统计用的filter:stat  日志用的filter:log4j 防御sql注入的filter:wall
filters = stat
# 类型是List<com.alibaba.druid.filter.Filter>， 如果同时配置了filters和proxyFilters， 是组合关系，并非替换关系
proxyFilters = 
```

## Tomcat JDBC Pool
```
# (boolean) 连接池创建的连接的默认的auto-commit 状态
defaultAutoCommit = true
# (boolean) 连接池创建的连接的默认的read-only 状态。 如果没有设置则setReadOnly 方法将不会被调用。 ( 某些驱动不支持只读模式， 比如：Informix)
defaultReadOnly = false
# (String) 连接池创建的连接的默认的TransactionIsolation 状态。 下面列表当中的某一个： ( 参考javadoc) NONE READ_COMMITTED EAD_UNCOMMITTED REPEATABLE_READ SERIALIZABLE
defaultTransactionIsolation = NONE
# (int) 初始化连接： 连接池启动时创建的初始化连接数量，1。2 版本后支持
initialSize = 10
# (int) 最大活动连接： 连接池在同一时间能够分配的最大活动连接的数量， 如果设置为非正数则表示不限制
maxActive = 100
# (int) 最大空闲连接： 连接池中容许保持空闲状态的最大连接数量， 超过的空闲连接将被释放， 如果设置为负数表示不限制 如果启用，将定期检查限制连接，如果空闲时间超过minEvictableIdleTimeMillis 则释放连接 （ 参考testWhileIdle ）
maxIdle = 8
# (int) 最小空闲连接： 连接池中容许保持空闲状态的最小连接数量， 低于这个数量将创建新的连接， 如果设置为0 则不创建 如果连接验证失败将缩小这个值（ 参考testWhileIdle ）
minIdle = 0
# (int) 最大等待时间： 当没有可用连接时， 连接池等待连接被归还的最大时间( 以毫秒计数)， 超过时间则抛出异常， 如果设置为-1 表示无限等待
maxWait = 30000
# (String) SQL 查询， 用来验证从连接池取出的连接， 在将连接返回给调用者之前。 如果指定， 则查询必须是一个SQL SELECT 并且必须返回至少一行记录 查询不必返回记录，但这样将不能抛出SQL异常
validationQuery = SELECT 1
# (boolean) 指明是否在从池中取出连接前进行检验， 如果检验失败， 则从池中去除连接并尝试取出另一个。注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串 参考validationInterval以获得更有效的验证
testOnBorrow = false
# (boolean) 指明是否在归还到池中前进行检验 注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串
testOnReturn = false
# (boolean) 指明连接是否被空闲连接回收器( 如果有) 进行检验。 如果检测失败， 则连接将被从池中去除。注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串
testWhileIdle = false
```

## C3P0（不推荐）
```
# 连接池中保留的最大连接数。默认值: 15
maxPoolSize = 15
# 连接池中保留的最小连接数，默认为：3
minPoolSize = 3
# 初始化连接池中的连接数，取值应在minPoolSize与maxPoolSize之间，默认为3
initialPoolSize = 3
# 最大空闲时间，60秒内未使用则连接被丢弃。若为0则永不丢弃。默认值: 0
maxIdleTime = 0
# 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出SQLException，如设为0则无限期等待。单位毫秒。默认: 0
checkoutTimeout = 0
# 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。默认值: 3
acquireIncrement = 3
# 定义在从数据库获取新连接失败后重复尝试的次数。默认值: 30 ；小于等于0表示无限次
acquireRetryAttempts = 0
# 重新尝试的时间间隔，默认为：1000毫秒
acquireRetryDelay = 1000
# 关闭连接时，是否提交未提交的事务，默认为false，即关闭连接，回滚未提交的事务
autoCommitOnClose = false
# c3p0将建一张名为Test的空表，并使用其自带的查询语句进行测试。如果定义了这个参数那么属性preferredTestQuery将被忽略。你不能在这张Test表上进行任何操作，它将只供c3p0测试使用。默认值: null
automaticTestTable = null
# 如果为false，则获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常，但是数据源仍有效保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试获取连接失败后该数据源将申明已断开并永久关闭。默认: false
breakAfterAcquireFailure = false
# 检查所有连接池中的空闲连接的检查频率。默认值: 0，不检查
idleConnectionTestPeriod = 0
# c3p0全局的PreparedStatements缓存的大小。如果maxStatements与maxStatementsPerConnection均为0，则缓存不生效，只要有一个不为0，则语句的缓存就能生效。如果默认值: 0
maxStatements = 0
# maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。默认值: 0
maxStatementsPerConnection = 0
```

## DBCP（不推荐）
```
# (boolean) 连接池创建的连接的默认的auto-commit 状态
defaultAutoCommit = true
# (boolean) 连接池创建的连接的默认的read-only 状态。 如果没有设置则setReadOnly 方法将不会被调用。 ( 某些驱动不支持只读模式， 比如：Informix)
defaultReadOnly = false
# (String) 连接池创建的连接的默认的TransactionIsolation 状态。 下面列表当中的某一个： ( 参考javadoc) NONE READ_COMMITTED EAD_UNCOMMITTED REPEATABLE_READ SERIALIZABLE
defaultTransactionIsolation = NONE
# (int) 初始化连接： 连接池启动时创建的初始化连接数量，1。2 版本后支持
initialSize = 10
# (int) 最大活动连接： 连接池在同一时间能够分配的最大活动连接的数量， 如果设置为非正数则表示不限制
maxActive = 100
# (int) 最大空闲连接： 连接池中容许保持空闲状态的最大连接数量， 超过的空闲连接将被释放， 如果设置为负数表示不限制 如果启用，将定期检查限制连接，如果空闲时间超过minEvictableIdleTimeMillis 则释放连接 （ 参考testWhileIdle ）
maxIdle = 8
# (int) 最小空闲连接： 连接池中容许保持空闲状态的最小连接数量， 低于这个数量将创建新的连接， 如果设置为0 则不创建 如果连接验证失败将缩小这个值（ 参考testWhileIdle ）
minIdle = 0
# (int) 最大等待时间： 当没有可用连接时， 连接池等待连接被归还的最大时间( 以毫秒计数)， 超过时间则抛出异常， 如果设置为-1 表示无限等待
maxWait = 30000
# (String) SQL 查询， 用来验证从连接池取出的连接， 在将连接返回给调用者之前。 如果指定， 则查询必须是一个SQL SELECT 并且必须返回至少一行记录 查询不必返回记录，但这样将不能抛出SQL异常
validationQuery = SELECT 1
# (boolean) 指明是否在从池中取出连接前进行检验， 如果检验失败， 则从池中去除连接并尝试取出另一个。注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串 参考validationInterval以获得更有效的验证
testOnBorrow = false
# (boolean) 指明是否在归还到池中前进行检验 注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串
testOnReturn = false
# (boolean) 指明连接是否被空闲连接回收器( 如果有) 进行检验。 如果检测失败， 则连接将被从池中去除。注意： 设置为true 后如果要生效，validationQuery 参数必须设置为非空字符串
testWhileIdle = false
```