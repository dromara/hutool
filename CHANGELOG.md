# Changelog

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