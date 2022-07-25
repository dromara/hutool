
# 🚀Changelog

-------------------------------------------------------------------------------------------------------------

# 5.8.5.M1 (2022-07-22)

### ❌不兼容特性
* 【core   】     合成注解相关功能重构，增加@Link及其子注解（pr#702@Gitee）

### 🐣新特性
* 【core   】     NumberUtil新增isIn方法（pr#669@Gitee）
* 【core   】     修复注解工具类getAnnotations的NPE问题，注解扫描器添新功能（pr#671@Gitee）
* 【core   】     合成注解SyntheticAnnotation提取为接口，并为实现类添加注解选择器和属性处理器（pr#678@Gitee）
* 【core   】     增加BeanValueProvider（issue#I5FBHV@Gitee）
* 【core   】     Convert工具类中，新增中文大写数字金额转换为数字工具方法（pr#674@Gitee）
* 【core   】     新增CollectorUtil.reduceListMap()（pr#676@Gitee）
* 【core   】     CollStreamUtil为空返回空的集合变为可编辑（pr#681@Gitee）
* 【core   】     增加StrUtil.containsAll（pr#2437@Github）
* 【core   】     ForestMap添加getNodeValue方法（pr#699@Gitee）
* 【http   】     优化HttpUtil.isHttp判断，避免NPE（pr#698@Gitee）
* 【core   】     修复Dict#containsKey方法没区分大小写问题（pr#697@Gitee）
* 【core   】     增加比较两个LocalDateTime是否为同一天（pr#693@Gitee）
* 【core   】     增加TemporalAccessorUtil.isIn、LocalDateTimeUtil.isIn（issue#I5HBL0@Gitee）
* 【core   】     ReUtil增加getAllGroups重载（pr#2455@Github）
* 【core   】     PageUtil#totalPage增加totalCount为long类型的重载方法（pr#2442@Github）
* 【crypto 】     PemUtil.readPemPrivateKey支持pkcs#1格式，增加OpensslKeyUtil（pr#2456@Github）
* 【core   】     添加了通用的注解扫描器 `GenericAnnotationScanner`，并在 `AnnotationScanner` 接口中统一提供了提前配置好的扫描器静态实例（pr#715@Github）
* 【json   】     JSONConfig增加允许重复key配置，解决不规整json序列化的问题（pr#720@Github）
* 【core   】     完善了codec包下一些方法的入参空校验（pr#719@Gitee）
* 【extra  】     完善QrCodeUtil对于DATA_MATRIX生成的形状随机不可指定的功能（pr#722@Gitee）
* 
### 🐞Bug修复
* 【core   】     修复CollUtil里面关于可变参数传null造成的crash问题（pr#2428@Github）
* 【socket 】     修复异常socket没有关闭问题（pr#690@Gitee）
* 【core   】     修复当时间戳为Integer时时间转换问题（pr#2449@Github）
* 【core   】     修复bmp文件判断问题（issue#I5H93G@Gitee）
* 【core   】     修复CombinationAnnotationElement造成递归循环（issue#I5FQGW@Gitee）
* 【core   】     修复Dict缺少putIfAbsent、computeIfAbsent问题（issue#I5FQGW@Gitee）
* 【core   】     修复Console.log应该把异常信息输出位置错误问题（pr#716@Gitee）
* 【core   】     修复UrlBuilder无法配置末尾追加“/”问题（issue#2459@Github）
* 【core   】     修复SystemPropsUtil.getBoolean方法应该只有值为true时才返回true，其他情况都应该返回false（pr#717@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.8.4 (2022-06-27)

### 🐣新特性
* 【extra  】     Sftp增加构造重载，支持超时（pr#653@Gitee）
* 【core   】     BeanUtil增加isCommonFieldsEqual（pr#653@Gitee）
* 【json   】     修改byte[]统一转换为数组形式（issue#2377@Github）
* 【http   】     HttpResponse增加body方法，支持自定义返回内容（pr#655@Gitee）
* 【core   】     修改ObjectUtil.isNull逻辑（issue#I5COJF@Gitee）
* 【core   】     BlockPolicy增加线程池关闭后的逻辑（pr#660@Gitee）
* 【core   】     Ipv4Util增加ipv4ToLong重载（pr#661@Gitee）
* 【core   】     LocalDateTimeUtil.parse改为blank检查（issue#I5CZJ9@Gitee）
* 【core   】     BeanPath在空元素时默认加入map，修改根据下标类型赋值List or map（issue#2362@Github）
* 【core   】     localAddressList 添加重构方法（pr#665@Gitee）
* 【cron   】     从配置文件加载任务时，自定义ID避免重复从配置文件加载（issue#I5E7BM@Gitee）
* 【core   】     新增注解扫描器和合成注解（pr#654@Gitee）
* 
### 🐞Bug修复
* 【extra  】     修复createExtractor中抛出异常后流未关闭问题（pr#2384@Github）
* 【core   】     修复CsvData.getHeader没有判空导致空指针问题（issue#I5CK7Q@Gitee）
* 【core   】     修复单字母转换为数字的问题（issue#I5C4K1@Gitee）
* 【core   】     修复IterUtil.filter无效问题
* 【core   】     修复NumberUtil传入null，返回了true(issue#I5DTSL@Gitee)
* 【core   】     修复NumberUtil.isDouble问题(pr#2400@Github)
* 【core   】     修复ZipUtil使用append替换文件时，父目录存在报错问题(issue#I5DRU0@Gitee)

-------------------------------------------------------------------------------------------------------------

# 5.8.3 (2022-06-10)

### 🐣新特性
* 【extra  】     mail增加writeTimeout参数支持（issue#2355@Github）
* 【core   】     FileTypeUtil增加pptx扩展名支持（issue#I5A0GO@Gitee）
* 【core   】     IterUtil.get增加判空（issue#I5B12A@Gitee）
* 【core   】     FileTypeUtil增加webp类型判断（issue#I5BGTF@Gitee）
### 🐞Bug修复
* 【core   】     修复NumberUtil.isXXX空判断错误（issue#2356@Github）
* 【core   】     修复Convert.toSBC空指针问题（issue#I5APKK@Gitee）
* 【json   】     修复Bean中存在bytes，无法转换问题（issue#2365@Github）
* 【core   】     ArrayUtil.setOrAppend()传入空数组时，抛出异常（issue#I5APJE@Gitee）
* 【extra  】     JschSessionPool修复空指针检查问题（issue#I5BK4D@Gitee）
* 【core   】     修复使用ValueProvider中setFieldMapping无效问题（issue#I5B4R7@Gitee）
* 【json   】     修复byte[]作为JSONArray构造问题（issue#2369@Github）

-------------------------------------------------------------------------------------------------------------

# 5.8.2 (2022-05-27)

### 🐣新特性
* 【core   】     BeanUtil拷贝对象增加空检查（issue#I58CJ3@Gitee）
* 【db     】     Column#size改为long
* 【core   】     ClassUtil增加isInterface等方法（pr#623@Gitee）
* 【socket 】     增加ChannelUtil

### 🐞Bug修复
* 【extra  】     修复SshjSftp初始化未能代入端口配置问题（issue#2333@Github）
* 【core   】     修复Convert.numberToSimple转换问题（issue#2334@Github）
* 【core   】     修复TemporalAccessorConverter导致的转换问题（issue#2341@Github）
* 【core   】     修复NumberUtil除法空指针问题（issue#I58XKE@Gitee）
* 【core   】     修复CAR_VIN正则（pr#624@Gitee）
* 【db     】     修复count查询别名问题（issue#I590YB@Gitee）
* 【json   】     修复json中byte[]无法转换问题（issue#I59LW4@Gitee）
* 【core   】     修复NumberUtil.isXXX未判空问题（issue#2350@Github）
* 【core   】     修复Singleton中ConcurrentHashMap在JDK8下的bug引起的可能的死循环问题（issue#2349@Github）

-------------------------------------------------------------------------------------------------------------

# 5.8.1 (2022-05-16)

### 🐣新特性
* 【core   】     BooleanUtil增加toBooleanObject方法（issue#I56AG3@Gitee）
* 【core   】     CharSequenceUtil增加startWithAnyIgnoreCase方法（issue#2312@Github）
* 【system 】     JavaInfo增加版本（issue#2310@Github）
* 【core   】     新增CastUtil（pr#2313@Github）
* 【core   】     ByteUtil新增bytesToShort重载（issue#I57FA7@Gitee）
* 【core   】     ReflectUtil.invoke方法抛出运行时异常增加InvocationTargetRuntimeException（issue#I57GI2@Gitee）
* 【core   】     NumberUtil.parseNumber支持16进制（issue#2328@Github）

### 🐞Bug修复
* 【core   】     MapUtil.map对null友好，且修复了测试用例中分组问题（pr#614@Gitee）
* 【core   】     修复BeanUtil.beanToMap中properties为null的空指针问题（issue#2303@Github）
* 【db     】     DialectName中修正为POSTGRESQL（issue#2308@Github）
* 【core   】     修复BeanPath无法识别引号内的内容问题（issue#I56DE0@Gitee）
* 【core   】     修复Map.entry方法返回可变不可变相反问题
* 【jwt    】     修复jwt的过期容忍时间问题（issue#2329@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.8.0 (2022-05-06)

### ❌不兼容特性
* 【extra  】     升级jakarta.validation-api到3.x，包名变更导致不能向下兼容
* 【core   】     BeanUtil删除了beanToMap(Object)方法，因为有可变参数的方法，这个删除可能导致直接升级找不到方法，重新编译项目即可。

### 🐣新特性
* 【core   】     Singleton增加部分方法（pr#609@Gitee）
* 【core   】     BeanUtil增加beanToMap重载（pr#2292@Github）
* 【core   】     Assert增加对应的equals及notEquals方法（pr#612@Gitee）
* 【core   】     Assert增加对应的equals及notEquals方法（pr#612@Gitee）
* 【core   】     DigestUtil增加sha512方法（issue#2298@Github）

### 🐞Bug修复
* 【db     】     修复RedisDS无法设置maxWaitMillis问题（issue#I54TZ9@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.8.0.M4 (2022-04-27)

### ❌不兼容特性
* 【json   】     【可能兼容问题】JSONArray删除部分构造
* 【json   】     【可能兼容问题】JSONTokener使用InputStream作为源时，由系统编码变更为UTF-8

### 🐣新特性
* 【core   】     BeanUtil增加toBean重载（pr#598@Gitee）
* 【json   】     新增JSONParser
* 【json   】     JSON新增在解析时的过滤方法（issue#I52O85@Gitee）
* 【core   】     添加ArrayUtil.distinct、CollUtil.distinct重载（issue#2256@Github）
* 【core   】     添加TransMap、FuncMap、ReferenceConcurrentMap、WeakConcurrentMap
* 【json   】     添加ObjectMapper
* 【core   】     CHINESE_NAME正则条件放宽（pr#599@Gitee）
* 【extra  】     增加JakartaServletUtil（issue#2271@Github）
* 【poi    】     ExcelWriter支持重复别名的数据写出（issue#I53APY@Gitee）
* 【core   】     增加Hashids（issue#I53APY@Gitee）
* 【core   】     ReflectUtil.newInstanceIfPossible添加枚举、数组等类型的默认实现
* 【core   】     CombinationAnnotationElement增加过滤（pr#605@Gitee）
* 【all    】     精简CHANGELOG
* 【core   】     新增AnsiEncoder
* 【log    】     新增彩色日式输出风格ConsoleColorLog（pr#607@Gitee）

### 🐞Bug修复
* 【core   】     修复StrUtil.firstNonX非static问题（issue#2257@Github）
* 【core   】     修复SimpleCache线程安全问题
* 【core   】     修复ClassLoaderUtil中可能的关联ClassLoader错位问题
* 【extra  】     修复Sftp错误内容解析大小写问题（issue#I53GPI@Gitee）
* 【core   】     修复Tailer当文件内容为空时，会报异常问题（pr#602@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.8.0.M3 (2022-04-14)

### ❌不兼容特性
* 【core   】     StreamProgress#progress方法参数变更为2个（pr#594@Gitee）
* 【core   】     SimpleCache的raw key使用Mutable
* 【core   】     ArrayUtil.join删除已经弃用的无用原始类型重载
* 【core   】     删除Holder类，ReUtil.extractMultiAndDelPre方法参数改为Mutable

### 🐣新特性
* 【core   】     CopyOptions支持以Lambda方式设置忽略属性列表（pr#590@Gitee）
* 【core   】     增加中文姓名正则及其校验（pr#592@Gitee）
* 【core   】     Snowflake支持sequence使用随机数（issue#I51EJY@Gitee）
* 【core   】     JarClassLoader增加构造（pr#593@Gitee）
* 【core   】     增加Pid，以便获取单例pid
* 【core   】     Img增加全覆盖水印pressTextFull（pr#595@Gitee）
* 【core   】     ByteUtil.numberToBytes增加Byte判断（issue#2252@Github）
* 【core   】     CopyOptions添加converter，可以自定义非全局类型转换
* 【core   】     添加了设置从绝对路径加载数据库配置文件的功能（pr#2253@Github）

### 🐞Bug修复
* 【core   】     修复UserAgentUtil识别Linux出错（issue#I50YGY@Gitee）
* 【poi    】     修复ExcelWriter.getDisposition方法生成错误（issue#2239@Github）
* 【core   】     修复UrlBuilder重复编码的问题（issue#2243@Github）
* 【http   】     修复HttpRequest中urlQuery，处理get请求参数的时候会导致空指针异常（pr#2248@Github）
* 【core   】     修复SimpleCache在get时未使用读锁可能导致的问题
* 【aop    】     修复JdkInterceptor before 方法拦截 return false 仍然执行了 after 的拦截问题（issue#I5237G@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.8.0.M2 (2022-04-02)

### ❌不兼容特性
* 【extra  】     【可能兼容问题】BeanCopierCache的key结构变更
* 【http   】     【可能兼容问题】HttpInterceptor增加泛型标识，HttpRequest中配置汇总于HttpConfig
* 【core   】     【可能兼容问题】UrlQuery.addQuery参数2从String变更为Object
* 【core   】     【可能兼容问题】WorkbookUtil.createBook实现改为WorkbookFactory.create

### 🐣新特性
* 【core   】     MapUtil增加entry、ofEntries方法
* 【core   】     ZipWriter增加add方法重载
* 【core   】     IterUtil增加filtered，增加FilterIter（issue#2228）
* 【core   】     增加NodeListIter、ResettableIter
* 【crypto 】     HmacAlgorithm增加SM4CMAC（issue#2206@Github）
* 【http   】     增加HttpConfig，响应支持拦截（issue#2217@Github）
* 【core   】     增加BlockPolicy，ThreadUtil增加newFixedExecutor方法（pr#2231@Github）
* 【crypto 】     BCMacEngine、Mac、CBCBlockCipherMacEngine、SM4MacEngine（issue#2206@Github）

### 🐞Bug修复
* 【core   】     IdcardUtil#getCityCodeByIdCard位数问题（issue#2224@Github）
* 【core   】     修复urlWithParamIfGet函数逻辑问题（issue#I50IUD@Gitee）
* 【core   】     修复IoUtil.readBytes限制长度读取问题（issue#2230@Github）
* 【http   】     修复HttpRequest中编码对URL无效的问题（issue#I50NHQ@Gitee）
* 【poi    】     修复读取excel抛NPE错误（pr#2234@Github）

-------------------------------------------------------------------------------------------------------------

# 5.8.0.M1 (2022-03-28)

### ❌不兼容特性
* 【db     】     【不向下兼容  】增加MongoDB4.x支持返回MongoClient变更（pr#568@Gitee）
* 【json   】     【可能兼容问题】修改JSONObject结构，继承自MapWrapper
* 【core   】     【可能兼容问题】BeanCopier重构，新建XXXCopier，删除XXXValueProvider
* 【core   】     【可能兼容问题】URLEncoder废弃，URLEncoderUtil使用RFC3986
* 【core   】     【可能兼容问题】Base32分离编码和解码，以便减少数据加载，支持Hex模式
* 【core   】     【可能兼容问题】Base58分离编码和解码
* 【core   】     【可能兼容问题】Base62分离编码和解码，增加inverted模式支持
* 【core   】     【兼容问题   】PunyCode参数由String改为Charsequence
* 【cron   】     【可能兼容问题】SimpleValueParser改名为AbsValueParser，改为abstract
* 【poi    】     【可能兼容问题】ExcelUtil.getBigWriter返回值改为BigExcelWriter
* 【core   】     【可能兼容问题】Opt.ofEmptyAble参数由List改为Collection子类（pr#580@Gitee）
* 【json   】     【可能兼容问题】JSON转Bean时，使用JSON本身的相关设置，而非默认（issue#2212@Github）
* 【json   】     【可能兼容问题】JSONConfig中isOrder废弃，默认全部有序

### 🐣新特性
* 【http   】     HttpRequest.form采用TableMap方式（issue#I4W427@Gitee）
* 【core   】     AnnotationUtil增加getAnnotationAlias方法（pr#554@Gitee）
* 【core   】     FileUtil.extName增加对tar.gz特殊处理（issue#I4W5FS@Gitee）
* 【crypto 】     增加XXTEA实现（issue#I4WH2X@Gitee）
* 【core   】     增加Table实现（issue#2179@Github）
* 【core   】     增加UniqueKeySet（issue#I4WUWR@Gitee）
* 【core   】     阿拉伯数字转换成中文对发票票面金额转换的扩展（pr#570@Gitee）
* 【core   】     ArrayUtil增加replace方法（pr#570@Gitee）
* 【core   】     CsvReadConfig增加自定义标题行行号（issue#2180@Github）
* 【core   】     FileAppender优化初始List大小（pr#2197@Github）
* 【core   】     Base32增加pad支持（pr#2195@Github）
* 【core   】     Dict增加setFields方法（pr#578@Gitee）
* 【db     】     新加db.meta的索引相关接口（pr#563@Gitee）
* 【db     】     Oracle中Column#typeName后的长度去掉（pr#563@Gitee）
* 【poi    】     优化ExcelReader，采用只读模式（pr#2204@Gitee）
* 【poi    】     优化ExcelBase，将alias放入
* 【poi    】     优化ExcelBase，将alias放入
* 【core   】     改进StrUtil#startWith、endWith性能
* 【cron   】     增加CronPatternParser、MatcherTable
* 【http   】     GlobalHeaders增加系统属性allowUnsafeServerCertChange、allowUnsafeRenegotiation
* 【http   】     UserAgentUtil 解析，增加MiUI/XiaoMi浏览器判断逻辑（pr#581@Gitee）
* 【core   】     FileAppender添加锁构造（pr#2211@Github）
* 【poi    】     ExcelReader增加构造（pr#2213@Github）
* 【core   】     MapUtil提供change函数，EnumUtil提供getBy函数，通过lambda进行枚举字段映射（pr#583@Gitee）
* 【core   】     CompareUtil增加comparingIndexed（pr#585@Gitee）
* 【db     】     DruidDataSource构建时支持自定义参数（issue#I4ZKCW@Gitee）
* 【poi    】     ExcelWriter增加addImg重载（issue#2218@Github）
* 【bloomFilter】 增加FuncFilter
* 【http   】     增加GlobalInterceptor（issue#2217）

### 🐞Bug修复
* 【core   】     修复ObjectUtil.hasNull传入null返回true的问题（pr#555@Gitee）
* 【core   】     修复NumberConverter对数字转换的问题（issue#I4WPF4@Gitee）
* 【core   】     修复ReflectUtil.getMethods获取接口方法问题（issue#I4WUWR@Gitee）
* 【core   】     修复NamingCase中大写转换问题（pr#572@Gitee）
* 【http   】     修复GET重定向时，携带参数问题（issue#2189@Github）
* 【core   】     修复FileUtil、FileCopier相对路径获取父路径错误问题（pr#2188@Github）
* 【core   】     修复CopyOptions中fieldNameEditor无效问题（issue#2202@Github）
* 【json   】     修复JSON对Map.Entry的解析问题
* 【core   】     修复MapConverter中map与map转换兼容问题
* 【poi    】     解决sax读取时，POI-5.2.x兼容性问题
* 【core   】     修复判断两段时间区间交集问题（pr#2210@Github）
* 【http   】     修复标签误删问题（issue#I4Z7BV@Gitee）
* 【core   】     修复Win下文件名带*问题（pr#584@Gitee）
* 【core   】     FileUtil.getMimeType增加rar、7z支持（issue#I4ZBN0@Gitee）
* 【json   】     JSON修复transient设置无效问题（issue#2212@Github）
* 【core   】     修复IterUtil.getElementType获取结果为null的问题（issue#2222@Github）
* 【core   】     修复农历转公历在闰月时错误（issue#I4ZSGJ@Gitee）

# 5.7.x 或更早版本
* [https://gitee.com/dromara/hutool/blob/v5-master/CHANGELOG_5.0-5.7.md](https://gitee.com/dromara/hutool/blob/v5-master/CHANGELOG_5.0-5.7.md)