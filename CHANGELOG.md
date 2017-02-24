# Changelog

## 3.0.1

### 新特性
* 拆分项目
* 增加 ThreadUtil.safeSleep方法
* 增加CollectionUtil.addAllIfNotContains方法
* 增加NumberUtil
* 增加hutool-cron模块
* 增加 ThreadUtil.waitForDie
* 增加DateUtil.betweenDay方法（感谢 @宁静之湖）
* 增加Snowflake算法
* 对Util类加final修饰符，并添加private构造方法
* 增加 ThreadUtil.interupt方法
* Setting中对RuntimeException变为SettingRuntimeException
* [log] LogFactory.setCurrentLogFactory支持传入LogFactory类
* [core] 增加CollectionUtil.getFirst
* [core] NumberUtil中增加binaryToInt、binaryToLong、getBinaryStr
* 增加Mutable类型
* HttpUtil.downFile增加String参数方法
* 修复NumberUtil.round方法传入double某些数字无效问题

### Bug修复
* DateUtil中offsite修正为offset
* ClassUtil.invoke方法在执行无法实例化的静态方法时报错问题
* 修复克隆方法对数组支持不足问题

-------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------

## 2.16.2

### 新特性
* SecureUtil对常用算法静态函数化封装
* 丰富Sql查询种类
* 对db中的Session和SqlRunner中的方法抽象到AbstractRunner中
* 增加CollectionUtil.split方法
* 增加 BOMInputStream.java用于读取带BOM头的文件，同时FileUtil提供getBOMInputStream方法
* 扩充CollectionUtil
* 扩充FileUtil，增加针对JDK7新特性的文件拷贝方法
* 对File的读写抽象为FileReader和FileWriter
* 添加Season，对季度的Enum支持
* HexUtil增加重载方法
* 引入Apache Commons Lang中的FastDateFormat，增强日期format和parse性能

### Bug修复
* 修复季度计算错误
* 修复DateTime.month()注释错误

-------------------------------------------------------------------------------------------------------------

## 2.16.1

### 新特性

* BeanUtil.copyProperties方法中CopyOptions增加可选是否忽略注入错误
* BeanUtil.fillBean方法的ValueProvider增加方法，用于判断是否忽略注入错误
* HttpUtil.getClientIP方法增加默认header
* crypto包中添加非对称加密算法、HMAC算法，优化对称加密算法类
* 增加StrUtil.removeAll
* 增加RandomUtil.randomEleSet方法
* 增加 CollectionUtil.distinct方法
* 增加BASE32实现

### Bug修复
* 修正Http模块无法301和302重定向问题
* 修复SqlBuilder中Insert值为null时SQL语句错误问题

-------------------------------------------------------------------------------------------------------------

## 2.16.0

### 新特性

*  全面更新db模块中的ds部分，使用工厂类自动选定连接池实现
* 扩充CollectionUtil，增加交集（intersection）、并集（union）、差集（disjunction）方法
* ds部分增加Tomcat-jdbc-pool的实现
* ds增加JndiDSFactory，支持JNDI数据源
* Setting中删除大量重复的方法，统一getXXX
* 扩充HexUtil，增加isHexNumber方法
* CollectionUtil增加forEach方法，用于支持Map等类型遍历
* 将DateUtil包变更为date，同时拆分格式化模式为单独的类，优化性能。DateUtil中增加方法
* SecureUtil中decodeBase64增加重载方法
* CharsetUtil增加defaultCharset方法
* 增加Base64类，支持url safe的encode和decode
* StrUtil.str方法增加支持Byte[]参数支持
* ClassUtil中增加原始类和包装类的转换
* clone包单独成包
* 全面优化Convert，单独成包convert，使用Convert以及其实现类实现可扩展的类型转换
* JSONObject实现Map接口，JSONArray实现List接口
* 增加代理包(com.xiaoleilu.hutool.proxy)，新增代理和切面功能
* 包扫描独立出来为ClassScaner，ClassUtil中对应方法引用此类方法
* ClassUil增加getConstructor方法，可匹配继承参数
* 优化ClassPath路径转为绝对路径
* Direction增加方法从字符串转换（大小写不敏感）
* 添加DigestUtil方法，用于md* sha1等摘要算法，同时简化SecureUtil，其中md* sha1方法调用DigestUtil
* 增加Editor接口，用于规范对象编辑器，主要用于集合元素的统一修改
* 修改Filter接口，同时去除ClassUtil中的ClassFilter，使过滤器抽象度更高
* HttpConnection支持Proxy
* StrUtil中某些方法使用CharSequence接口做为参数
* Setting 拆分配置文件解析为SettingLoader
* ClassUtil增加getTypeArgument方法，用于获取泛型属性类型
* 大量扩充Converter，支持转换为数组
* 增加PathConverter，转换支持Path对象
* Setting和Props支持文件变更自动加载
* StrUtil增加startWithIgnoreCase和endWithIgnoreCase
* script包引擎封装，封装JavaScriptEngine
* 增加RuntimeInfo
* 增加DateUtil.formatDiff方法
* 增加Matcher接口，用于抽象接口逻辑
* 新建crypto包，提供摘要算法和对称加密算法
* CollectionUtil增加count方法
* CollectionUtil中关于数组方法全部迁移至ArrayUtil
* 扩充日期工具，添加月份和周的枚举
* 强化DateTime类
* 增加IoUtil.copyByNIO方法

### Bug修复

* 修复各个包中error日志使用问题导致的无法打印堆栈
* 修复StrUtil.join时传入数组或集合类无法转为字符串问题
* 修复FileUtil.writeString方法未检查文件的问题
* 修复StrUtil.count修复空指针问题，给定null返回0
* 修复BeanUtil中beanToMap方法，空值可选注入
* 修复SqlBuilder中插入和更新对空值得判断
* 修复Boolean转换的一个错误
* 修复CharsetUtil中默认的destCharset错误赋值问题
* 修复StrUtil.count方法错误计数问题
* 修复DbSetting传值Setting不为空空指针问题
* 修复HttpConnection中判断Http Method 错误
* 修复PooledDataSource和SimpleDataSource配置文件读取错误问题