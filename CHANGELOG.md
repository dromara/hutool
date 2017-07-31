# Changelog

## 3.1.0

### 新特性

### Bug修复
* 修复CollectionUtil中并集、差集问题（issue#IE9VH@osc）

-------------------------------------------------------------------------------------------------------------

## 3.0.9

### 新特性
* CollectionUtil新增针对Map的排序（issue#30@osc）
* 增加ArrayUtil.reverse方法
* 增加StrUtil.reverse方法
* 增加NumberWordFormater和Convert.numberToWord方法（感谢@【福建】极速蜗牛）
* IoUtil和FileUtil增加readLines(LineHandler)方法用于按行处理（感谢@汪汪）
* 扩充NumberUtil，提供对BigDecimal参数支持（感谢@【杭州】KIWI）
* 新增ReflectUtil，将原有ClassUtil部分方法迁入，同时提供针对父类私有字段、方法的访问支持
* ArrayUtil增加min和max方法（感谢@【贵阳】shadow）
* 增加Caller类，用于获取调用者类（感谢@【北京】宁静 提供需求）
* JSONUtil.parse方法支持是否忽略空值的参数
* JSONObject支持初始大小和按照KEY有序（感谢【深圳】Vmo）
* 对ImageUtil重构以支持更对类型参数
* DateTime增加offsetNew方法
* 增加Range类，抽象递增递减列表（感谢@【悉尼】C - ActFramework）
* 扩充XmlUtil，提供更多重载方法（感谢@【北京】仓山有井名为空）
* NumberUtil增加decimalFormatMoney方法（感谢@【北京】宁静 ）
* FileUtil增加rename方法
* 增加Copier接口抽象拷贝
* 增加FileCopier强化文件拷贝
* ZipUtil增加多个流的方法（issue#IE5ZC@osc）
* HttpResponse支持body异步，既执行executeAsync()方法后不再直接读取body，而是持有http流对象
* 新增ClassLoaderUtil

### Bug修复
* 修复Entity.parseEntity方法中获取表名逻辑的问题（感谢@【北京】游弋苍茫）
* 修复批量插入值顺序错乱问题（感谢@【北京】游弋苍茫）
* 修复ComparatorChain的equals方法问题
* 修复ArrayUtil.isEmpty(Object)方法问题（pull request #28@github）
* 修复JSONUtil#toBean 和FileUtil#equals中的问题（pull request #31@github）
* 正文获取编码问题修复（@talent-aio）
* 修复Http部分定义编码不能应用于Response的问题（issue#31@osc）
* 修复FileUtil.equals方法在两个文件都不存在的情况下判定问题
* 修复Http请求结果多出一个换行的问题（感谢@【北京】仓山有井名为空）
* 修复StrUtil.cleanBlank方法中length方法调用两次问题（感谢@【天津】〓下页）
* 修复IoUtil.readHex28Lower方法参数传入错误（issue#IE81V@osc）

## 3.0.8

### 新特性
* ArrayUtil增加remove和removeEle方法（此方法来自commons-lang，按照用户留言要求添加）
* 增加ArrayIterator
* Tuple对象实现Iterable接口，支持forEach循环
* 增加ClassUtil.getResources方法
* 增加StrSpliter（支持字符、字符串、正则分隔符），同时StrUtil.split方法实现也指向此类
* 增加EnumerationIterator、IteratorEnumeration
* DateUtil增加betweenMonth和betweenYear
* 增加SimpleCache使用WeakHashMap简易缓存实现
* 正则Pattern和FastDateFormat添加缓存支持
* DateTime和DateUtil添加isIn方法（感谢@【合肥】天涯）
* HexUtil增加方法encodeColor和decodeColor（感谢@【帝都】宁静）
* 新增AES快捷支持（SymmetricCrypto包装）
* SymmetricCrypto支持自定义加盐（偏移向量）（感谢【鲁】full）
* NumberUtil运算使用String参数（感谢：@【北京】仓山有井名为空）
* XmlUtil增加getRootElement方法
* JSONUtil增加parseArray(Object)方法

### Bug修复
* 修复CollectionUtil.newHashSet方法参数失效的bug
* 修复DbUtil.getTables方法获取为空问题（感谢@【天津】〓下页）
* 修复HttpUtil.toParams方法key或value为null的情况下拼接为null字符串的问题（感谢@talent-aio）
* 修复HttpUtil.decodeParams方法全局解码导致的不一致问题（感谢@talent-aio）
* 修复Entity.parseEntity方法中获取表名逻辑的问题（感谢@【北京】游弋苍茫）
* 修复批量插入值顺序错乱问题（感谢@【北京】游弋苍茫）
* 修复JSONObject中toBean参数歧义导致的toBean异常
* 修复中文路径下获取ClassPath路径导致的问题（感谢@【重庆】周路、@质量过关）

-------------------------------------------------------------------------------------------------------------

## 3.0.7

### 新特性
* 增加NetUtil.getLocalhostStr（感谢【帝都】-宁静）
* ArrayUtil和CollectionUtil增加hasNull方法
* 添加PatternPool用于存放常用编译好的正则表达式
* 完善Assert类，可变消息参数以及规范注释
* 添加NetUtil.getMacAddress和NetUtil.getLocalMacAddress用于获取MAC地址
* StrUtil.repeat方法优化
* 增加comparator包，补充ComparableComparator，ComparatorChain，ReverseComparator
* 增加BeanResolver，同时增加BeanUtil.getProperty方法
* 增加WeakCache
* HttpRequest中增加cookie方法用于覆盖默认的Cookie行为，自定义Cookie值（感谢@质量过关）
* getPropertyDescriptor和getPropertyDescriptorMap增加缓存支持
* 添加DynaBean，反射方式对Bean做get和set操作
* ArrayUtil中数组参数变为可变参数。提供跟多灵活性
* StrUtil增加更多实用方法
* DateUtil.date方法支持java.util.Date参数；DateTime增加toTimestamp和toSqlDate两个方法
* FileUtil增加getResourceUrl方法

### Bug修复
* Validator.isPlateNmber 拼写修复为isPlateNumber（感谢：飞天奔月）
* 修复DbUtil.getTableMeta获取主键出错问题
* 修复ConverterRegistry中默认Date对象错误问题（issue#22@github）
* 修复NumberUtil中方法非static的问题（感谢[霾都] QQ小冰）
* 修复FileUtil.equals方法，实现改为JDK7实现。
* 修复FileUtil.copy 方法判断错误问题（issue#24@github）
* 修复Bean中Date对象转JSON格式问题（会导致toBean异常）（感谢@【武汉】徐元程）
* 修复Cache模块并发修改Map会导致的异常（感谢@【北京】liyong）

-------------------------------------------------------------------------------------------------------------

## 3.0.6

### 新特性
* ThreadUtil增加newExecutorByBlockingCoefficient方法（感谢：@【北京】仓山有井名为空）
* 解决LogFactory中频繁创建Log对象造成的性能问题（issue#19@osc）
* 解决LFUCache中负载因子导致的扩容rehash问题（issue#18@osc）
* FileUtil中IOException全部包装为IORuntimeException
* 针对JDK8文档注释修复
* 添加SystemClock
* 增加ArrayUtil.copy方法
* 增加FtpUtil
* 增加FileUtil.clean方法，用于清空目录
* 增加SqlRunner.create()方法，使用默认连接
* 扩充Entity类型
* Setting添加Profile功能支持（感谢@长沙-渔泯小镇）
* ZipUtil增加对输入流和字符串压缩的支持
* Validator增加中国车牌号码
* 新增StrUtil.cut方法，等分切割
* 增加HttpUtil.download方法对https的支持

### Bug修复
* 修复AbstractCache.onRemove回调方法在get和pruneCache时不被触发问题（issue#18@osc）
* 技术债务修复：空指针等问题修复
* 修复RandomUtil.randomEleSet方法获取随机个数出错问题。（感谢@【北京】Sych）
* NumberUtil的isInteger和isDouble方法非静态问题修复（感谢@【广州】流行の云）
* Base64的URL Safe模式等号没有替换问题。（issue#20@osc）
* 修复Convert.strToUnicode转换后位数不足问题
* 修复针对StrUtil.cleanBlank和Convert.toDBC中不间断空白符无法去除问题
* 修复db模块对表名的包装策略
* 修复BeanToMap和JSON中toMap导致的循环引用
* 修复在自动侦测数据源的时候重复加载配置文件问题
* 修复针对JSON转Bean中数组参数转换失败的问题，同时添加ArrayUtil.cast方法用于强制转换数组类型
* 修复NumberUtil.isInteger方法中对空串的判断(#22@osc)
* 修复dfa模块匹配时正文中存在停顿词（特殊字符）时导致匹配失效问题（感谢@talent-aio）

-------------------------------------------------------------------------------------------------------------

## 3.0.5

### 新特性
* ReUtil.replaceAll扩充支持Pattern对象
* 优化log模块中自动选择日志实现机制
* 针对不同连接池添加样例配置文件
* AsymmetricCriptor类增加构造方法，可以传入Base64编码的私钥和公钥
* SecureUtil增加RSA和DSA快速创建工具方法
* Hutool-log支持tinylog
* 添加GlobalThreadPool，将公共线程池从ThreadUtil中剥离出来
* 扩充Base32中的方法

### Bug修复
* db模块报错问题修复（感谢@尘风了了）
* DateUtil.month方法注释修复 (感谢@〓下页)
* 对db模块的数据源部分的配置做修复
* Base64传入null导致空指针异常修复（解决方法为返回null）
* crypto模块类名存在拼写错误：修正SymmetricCriptor -> SymmetricCrypto，AsymmetricCriptor -> AsymmetricCrypto
* FileCache中初始化capacity为0问题。（issue#17@osc）

-------------------------------------------------------------------------------------------------------------

## 3.0.4

### 新特性
* 新增CollectionUtil.newCopyOnWriteArrayList方法
* 新增IdcardUtil，身份证验证和相关信息提取
* Convert内部修改，避免循环引用
* ArrayUtil.zip方法添加参数isOrder，可选是否有序
* 增加HexUtil.decodeHexStr(String) 感谢（@MaxSherry）pull request #15
* 增加SymmetricCriptor.encryptHex 感谢（@MaxSherry）pull request #15
* JavaInfo增加对JDK1.8的判断 感谢（@MaxSherry）pull request #19
* BeanUtil.copyProperties方法增加参数，可选是否忽略大小写 感谢（@talent-aio）

### Bug修复
* 修复Convert.toBigDecimal和toBigInteger值传为默认值的问题（感谢@〆得不到的永远在骚动）
* 修复AsymmetricCriptor.getKeyByType中判断出错问题，感谢（@姚洪涛）
* 修复Http模块中Proxy设置无效问题，现在Proxy对象在HttpRequest中设置
* Convert中数组转换修复方法，消除歧义
* 修复BeanUtil.fillBean注入时空指针问题
* 修复Aspect中afterException对象传错问题 #17@github 感谢（@970655147）
* 修复CaseInsensitiveMap导致的忽略大小写注入问题
* 解决LongBitMap中强转位丢失问题

-------------------------------------------------------------------------------------------------------------

## 3.0.3

### 新特性
* DigestUtil中增加hmac方法，HMac对象快速构建通道
* HMac类中增加getSecretKey方法，获取密钥

### Bug修复
* 修复Digester.digest方法返回null的问题（issue#16@osc）
* 修复HMac.digest方法返回null的问题
* HttpBase中的header等添加this返回（感谢github@MaxSherry）

-------------------------------------------------------------------------------------------------------------

## 3.0.2

### 新特性
* 增加CaseInsensitiveMap

### Bug修复
* 解决JSONObject.toBean方法无法多层转换的问题
* 解决FileUtil.append非追加而是覆盖问题（感谢 @Andy）

-------------------------------------------------------------------------------------------------------------

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
* 增加FileUtil.getType方法
* 优化整理JSON部分
* 增加ThreadUtil.getThreads、getMainThread方法
* 增强JarClassLoader
* WatchMonitor增加createAll方法
* 增加NetUtil.getLocalhost
* 优化ClassPathResource错误提示
* 增加 ArrayUtil.toArray方法
* MathUtil中的方法迁移到NumberUtil，去除MathUtil
* 针对Cache模块做包结构调整，扩展FileCache

### Bug修复
* DateUtil中offsite修正为offset
* ClassUtil.invoke方法在执行无法实例化的静态方法时报错问题
* 修复克隆方法对数组支持不足问题
* Convert修复众多bug
* 去掉IoUtil.getReader无用的IO异常抛出
* ImageUtil.pressImage修正值无效问题（感谢@Rainplus）
* 修复cache模块中CacheValuesIterator对象next()方法没有元素时无抛出异常问题

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