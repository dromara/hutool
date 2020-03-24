
# Changelog

-------------------------------------------------------------------------------------------------------------

## 5.2.5

### 新特性
* 【core   】     增加逻辑，对于原始类型注入，（issue#797@Github）

### Bug修复

-------------------------------------------------------------------------------------------------------------
## 5.2.4

### 新特性
* 【setting】     Setting中增加addSetting和autoLoad重载（pr#104@Gitee）
* 【core   】     增加copyProperties，根据Class创建对象并进行属性拷贝（pr#105@Gitee）
* 【core   】     添加获取class当前文件夹名称方法（pr#106@Gitee）
* 【core   】     BooleanUtil中重载歧义修正，修改了包装参数的方法名（issue#I1BSK8@Gitee）
* 【core   】     XmlUtil增加xmlToBean和beanToXml方法
* 【db     】     设置全局忽略大小写DbUtil.setCaseInsensitiveGlobal(true)（issue#784@Github）
* 【core   】     增加CallerUtil.getCallerMethodName方法
* 【core   】     Tree增加getParent方法，可以获取父节点，抽象Node接口
* 【core   】     增加社会信用代码工具CreditCodeUtil（pr#112@Gitee）
* 【core   】     ChineseDate增加构造重载，增加toStringNormal（issue#792@Github）
* 【core   】     BeanUtil.toBean增加重载（issue#797@Github）

### Bug修复
* 【core   】     修复TypeUtil无法获取泛型接口的泛型参数问题（issue#I1BRFI@Gitee）
* 【core   】     修复MySQL中0000报错问题
* 【core   】     修复BeanPath从Map取值为空的问题（issue#790@Github）
* 【poi    】     修复添加图片尺寸的单位问题（issue#I1C2ER@Gitee）
* 【setting】     修复getStr中逻辑问题（pr#113@Gitee）
* 【json   】     修复JSONUtil.toXml汉字被编码的问题（pr#795@Gitee）
* 【poi    】     修复导出的Map列表中每个map长度不同导致的对应不一致的问题（issue#793@Gitee）

-------------------------------------------------------------------------------------------------------------
## 5.2.3

### 新特性
* 【http   】     UserAgentUtil增加识别ios和android等（issue#781@Github）
* 【core   】     支持新领车牌（issue#I1BJHE@Gitee）

### Bug修复
* 【core   】     修复PageUtil第一页语义不明确的问题（issue#782@Github）
* 【extra  】     修复TemplateFactory引入包导致的问题
* 【core   】     修复ServiceLoaderUtil.loadFirstAvailable问题

-------------------------------------------------------------------------------------------------------------
## 5.2.2

### 新特性

### Bug修复
* 【http   】     修复body方法添加多余头的问题（issue#769@Github）
* 【bloomFilter 】修复默认为int类型,左移超过32位后,高位丢失问题（pr#770@Github）
* 【core   】     修复beginOfWeek和endOfWeek一周开始计算错误问题（issue#I1BDPW@Gitee）
* 【db     】     修复Db.query使用命名方式查询产生的歧义（issue#776@Github）

-------------------------------------------------------------------------------------------------------------

## 5.2.1

### 新特性
* 【core   】     修改FastDateParser策略，与JDK保持一致（issue#I1AXIN@Gitee）
* 【core   】     增加tree（树状结构）（pr#100@Gitee）
* 【core   】     增加randomEleList（pr#764@Github）
### Bug修复
* 【setting】     修复Props.toBean方法null的问题
* 【core   】     修复DataUtil.parseLocalDateTime无时间部分报错问题（issue#I1B18H@Gitee）
* 【core   】     修复NetUtil.isUsableLocalPort()判断问题（issue#765@Github）
* 【poi    】     修复ExcelWriter写出多个sheet错误的问题（issue#766@Github）
* 【extra  】     修复模板引擎自定义配置失效问题（issue#767@Github）

-------------------------------------------------------------------------------------------------------------

## 5.2.0

### 新特性
* 【core  】     NumberUtil.decimalFormat增加Object对象参数支持
* 【core  】     增加ReflectUtil.getFieldValue支持Alias注解
* 【core  】     Bean字段支持Alias注解（包括转map,转bean等）
* 【core  】     增加ValueListHandler，优化结果集获取方式
* 【http  】     支持patch方法（issue#666@Github）
* 【crypto】     BCUtil支持更加灵活的密钥类型，增加writePemObject方法
* 【core  】     增加ServiceLoaderUtil
* 【core  】     增加EnumUtil.getEnumAt方法
* 【core  】     增强EnumConvert判断能力（issue#I17082@Gitee）
* 【all   】     log、template、tokenizer使用SPI机制代替硬编码
* 【poi   】     Word07Writer增加addPicture
* 【crypto】     RSA算法中，BlockSize长度策略调整（issue#721@Github）
* 【crypto】     删除SM2Engine，使用BC库中的对象替代
* 【crypto】     增加PemUtil工具类
* 【dfa   】     WordTree增加Filter，支持自定义特殊字符过滤器
* 【poi   】     对于POI依赖升级到4.1.2
* 【crypto】     增加国密SM2验签密钥格式支持（issue#686@Github）

### Bug修复

-------------------------------------------------------------------------------------------------------------

## 5.1.5

### 新特性
* 【poi  】     Excel合并单元格读取同一个值，不再为空
* 【core 】     增加EscapeUtil.escapeAll（issue#758@Github）
* 【core 】     增加formatLocalDateTime和parseLocalDateTime方法（pr#97@Gitee）

### Bug修复
* 【core 】     修复EscapeUtil.escape转义错误（issue#758@Github）
* 【core 】     修复Convert.toLocalDateTime(Object value, Date defaultValue)返回结果不是LocalDateTime类型的问题（pr#97@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.1.4

### 新特性
* 【poi  】     增加单元格位置引用（例如A11等方式获取单元格）
* 【extra】     ServletUtil.fillBean支持数据和集合字段（issue#I19ZMK@Gitee）
* 【core 】     修改ThreadUtil.newSingleExecutor默认队列大小（issue#754@Github）
* 【core 】     修改ExecutorBuilder默认队列大小（issue#753@Github）
* 【core 】     FileTypeUtil增加mp4的magic（issue#756@Github）

### Bug修复
* 【core 】     修复CombinationAnnotationElement数组判断问题（issue#752@Github）
* 【core 】     修复log4j2使用debug行号打印问题（issue#I19NFJ@Github）
* 【poi  】     修复sax读取excel03数组越界问题（issue#750@Github）

-------------------------------------------------------------------------------------------------------------

## 5.1.3

### 新特性
* 【core 】     废弃isMactchRegex，改为isMatchRegex（方法错别字）
* 【core 】     修正hasNull()方法上注释错误（issue#I18TAG@Gitee）
* 【core 】     Snowflake的起始时间可以被指定（pr#95@Gitee）
* 【core 】     增加PropsUtil及getFirstFound方法（issue#I1960O@Gitee）
### Bug修复
* 【core 】     CharsetUtil在不支持GBK的系统中运行报错问题（issue#731@Github）
* 【core 】     RandomUtil的randomEleSet方法顺序不随机的问题（pr#741@Github）
* 【core 】     修复StopWatch的toString判断问题（issue#I18VIK@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.1.2

### 新特性
* 【core 】     XmlUtil支持可选是否输出omit xml declaration（pr#732@Github）
* 【core 】     车牌号校验兼容新能源车牌（pr#92@Gitee）
* 【core 】     在NetUtil中新增ping功能（pr#91@Gitee）
* 【core 】     DateUtil.offset不支持ERA，增加异常提示（issue#I18KD5@Gitee）
* 【http 】     改进HttpUtil访问HTTPS接口性能问题，SSL证书使用单例（issue#I18AL1@Gitee）

### Bug修复
* 【core 】     修复isExpired的bug（issue#733@Gtihub）

-------------------------------------------------------------------------------------------------------------

## 5.1.1

### 新特性
* 【core 】     ClassUtil.isSimpleValueType增加TemporalAccessor支持（issue#I170HK@Gitee）
* 【core 】     增加Convert.toPrimitiveByteArray方法，Convert支持对象序列化和反序列化
* 【core 】     DateUtil增加isExpired(Date startDate, Date endDate, Date checkDate)（issue#687@Github）
* 【core 】     增加Alias注解
* 【core 】     修正NumberChineseFormatter和NumberWordFormatter（类名拼写错误）
* 【all  】     修正equals，避免可能存在的空指针问题（pr#692@Github）
* 【core  】    提供一个自带默认值的Map（pr#87@Gitee）
* 【core  】    修改Dict在非大小写敏感状态下get也不区分大小写（issue#722@Github）
* 【core  】    StrUtil增加contains方法（issue#716@Github）
* 【core  】    QrCodeUtil增加背景透明支持（pr#89@Gitee）
* 【core  】    增加农历ChineseDate（pr#90@Gitee）
* 【core  】    ZipUtil增加zip方法写出到流（issue#I17SCT@Gitee）
* 【db    】    Db.use().query的方法中增加Map参数接口（issue#709@Github）
* 【db    】    getDialect使用数据源作为锁（issue#720@Github）

### Bug修复
* 【core 】     修复NumberUtil.mul中null的结果错误问题（issue#I17Y4J@Gitee）
* 【core 】     修复当金额大于等于1亿时，转换会多出一个万字的bug（pr#715@Github）
* 【core 】     修复FileUtil.listFileNames位于jar内导致的文件找不到问题
* 【core 】     修复TextSimilarity.similar去除字符导致的问题（issue#I17K2A@Gitee）
* 【core 】     修复unzip文件路径问题（issue#I17VU7@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.1.0

### 新特性
* 【core 】     新增WatchServer（issue#440@Github）
* 【core 】     ReflectUtil.getFieldValue支持static（issue#662@Github）
* 【core 】     改进Bean判断和注入逻辑：支持public字段注入（issue#I1689L@Gitee）
* 【extra】     新增SpringUtil
* 【http 】     Get请求支持body，移除body（JSON）方法（issue#671@Github）
* 【core 】     ReflectUtil修正getFieldValue逻辑，防止歧义


### Bug修复
* 【db  】      修复SqlExecutor.callQuery关闭Statement导致的问题（issue#I16981@Gitee）
* 【db  】      修复XmlUtil.xmlToMap中List节点的问题（pr#82@Gitee）
* 【core】      修复ZipUtil中对于/结尾路径处理的问题（issue#I16PKP@Gitee）
* 【core】      修复DateConvert对int不支持导致的问题（issue#677@Github）

-------------------------------------------------------------------------------------------------------------

## 5.0.7

### 新特性
* 【core 】      解决NumberUtil导致的ambiguous问题（issue#630@Github）
* 【core 】      BeanUtil.isEmpty()忽略字段支持，增加isNotEmpty（issue#629@Github）
* 【extra】      邮件发送后获取message-id（issue#I15FKR@Gitee）
* 【core 】      CaseInsensitiveMap/CamelCaseMap增加toString（issue#636@Github）
* 【core 】      XmlUtil多节点改进（issue#I15I0R@Gitee）
* 【core 】      Thread.excAsync修正为execAsync（issue#642@Github）
* 【core 】      FileUtil.getAbsolutePath修正正则（issue#648@Github）
* 【core 】      NetUtil增加getNetworkInterface方法（issue#I15WEL@Gitee）
* 【core 】      增加ReflectUtil.getFieldMap方法（issue#I15WJ7@Gitee）

### Bug修复
* 【extra】      修复SFTP.upload上传失败的问题（issue#I15O40@Gitee）
* 【db】         修复findLike匹配错误问题
* 【core 】      修复scale方法透明无效问题（issue#I15L5S@Gitee）
* 【extra】      修复exec返回无效（issue#I15L5S@Gitee）
* 【cron】       修复CronPattern注释（pr#646@Github）
* 【json】       修复LocalDateTime等JDK8时间对象不被支持的问题（issue#644@Github）

-------------------------------------------------------------------------------------------------------------

## 5.0.6

### 新特性
* 【setting】    toBean改为泛型，增加class参数重载（pr#80@Gitee）
* 【core】       XmlUtil使用JDK默认的实现，避免第三方实现导致的问题（issue#I14ZS1@Gitee）
* 【poi】        写入单元格数据类型支持jdk8日期格式（pr#628@Github）

### Bug修复
* 【core】       修复DateUtil.format使用DateTime时区失效问题（issue#I150I7@Gitee）
* 【core】       修复ZipUtil解压目录遗留问题（issue#I14NO3@Gitee）
* 【core】       修复等比缩放给定背景色无效问题（pr#625@Github）
* 【poi 】       修复sax方式读取excel中无样式表导致的空指针问题
* 【core】       修复标准化URL时domain被转义的问题（pr#654@Github）

-------------------------------------------------------------------------------------------------------------

## 5.0.5

### 新特性
* 【core】       增加MapUtil.removeAny（issue#612@Github）
* 【core】       Convert.toList支持[1,2]字符串（issue#I149XN@Gitee）
* 【core】       修正DateUtil.thisWeekOfMonth注释错误（issue#614@Github）
* 【core】       DateUtil增加toLocalDate等方法，DateTime更好的支持时区
* 【core】       BeanUtil.getProperty返回泛型对象（issue#I14PIW@Gitee）
* 【core】       FileTypeUtil使用扩展名辅助判断类型（issue#I14JBH@Gitee）

### Bug修复
* 【db】         修复MetaUtil.getTableMeta()方法未释放ResultSet的bug（issue#I148GH@Gitee）
* 【core】       修复DateUtil.age闰年导致的问题（issue#I14BVN@Gitee）
* 【extra】      修复ServletUtil.getCookie大小写问题（pr#79@Gitee）
* 【core】       修复IdcardUtil.isValidCard18报错问题（issue#I14LTJ@Gitee）
* 【poi】        修复double值可能存在的精度问题（issue#I14FG1@Gitee）
* 【core】       修复Linux下解压目录不正确的问题（issue#I14NO3@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.0.4

### 新特性
* 【setting】    增加System.getenv变量替换支持
* 【core】       XmlUtil中mapToStr支持namespace（pr#599@Github）
* 【core】       ZipUtil修改策略:默认关闭输入流（issue#604@Github）
* 【core】       改进CsvReader，支持RowHandler按行处理（issue#608@Github）
* 【core】       增加MapUtil.sortJoin，改进SecureUtil.signParams支持补充字符串（issue#606@Github）
* 【core】       增加Money类（issue#605@Github）

### Bug修复
* 【core】       解决ConcurrentHashSet不能序列化的问题（issue#600@Github）
* 【core】       解决CsvReader.setErrorOnDifferentFieldCount循环调用问题

-------------------------------------------------------------------------------------------------------------

## 5.0.3

### 新特性
### Bug修复
* 【extra】      修复遗留的getSession端口判断错误（issue#594@Github）

-------------------------------------------------------------------------------------------------------------

## 5.0.2

### 新特性
* 【core】       强化java.time包的对象转换支持

### Bug修复
* 【db】         修正字段中含有as导致触发关键字不能包装字段的问题（issue#I13ML7@Gitee）
* 【extra】      修复QrCode中utf-8不支持大写的问题。（issue#I13MT6@Gitee）
* 【http】       修复请求defalte数据解析错误问题。（pr#593@Github）

-------------------------------------------------------------------------------------------------------------

## 5.0.1

### 新特性
* 【json】       JSONUtil.toBean支持JSONArray
### Bug修复
* 【extra】      修复getSession端口判断错误

-------------------------------------------------------------------------------------------------------------

## 5.0.0

### 新特性
* 【all】        升级JDK最低 支持到8
* 【log】        Log接口添加get的static方法
* 【all】        部分接口添加FunctionalInterface修饰
* 【crypto】     KeyUtil增加readKeyStore重载
* 【extra】      JschUtil增加私钥传入支持（issue#INKDR@Gitee）
* 【core】       DateUtil、DateTime、Convert全面支持jdk8的time包

### Bug修复
* 【http】       修复Cookie中host失效导致的问题（issue#583@Github）
