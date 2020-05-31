
# Changelog

-------------------------------------------------------------------------------------------------------------

## 5.3.6 (2020-05-30)

### 新特性
* 【core   】     NumberConverter Long类型增加日期转换（pr#872@Github）
* 【all    】     StrUtil and SymmetricCrypto注释修正（pr#873@Github）
* 【core   】     CsvReader支持返回Bean（issue#869@Github）
* 【core   】     Snowflake循环等待下一个时间时避免长时间循环，加入对时钟倒退的判断（pr#874@Github）
* 【extra  】     新增 QRCode base64 编码形式返回（pr#878@Github）
* 【core   】     ImgUtil增加toBase64DateUri，URLUtil增加getDataUri方法
* 【core   】     IterUtil添加List转Map的工具方法（pr#123@Gitee）
* 【core   】     BeanValuePovider转换失败时，返回原数据，而非null
* 【core   】     支持BeanUtil.toBean(object, Map.class)转换（issue#I1I4HC@Gitee）
* 【core   】     MapUtil和CollUtil增加clear方法（issue#I1I4HC@Gitee）
* 【core   】     增加FontUtil，可定义pressText是否从中间（issue#I1HSWU@Gitee）
* 【http   】     SoapClient支持自定义请求头（issue#I1I0AO@Gitee）
* 【script 】     ScriptUtil增加evalInvocable和invoke方法（issue#I1HHCP@Gitee）
* 【core   】     ImgUtil增加去除背景色的方法（pr#124@Gitee）
* 【system 】     OshiUtil增加获取CPU使用率的方法（pr#124@Gitee）
* 【crypto 】     AsymmetricAlgorithm去除EC（issue#887@Github）
* 【cache  】     超时缓存使用的线程池大小默认为1（issue#890@Github）
* 【poi    】     ExcelSaxReader支持handleCell方法
* 【core   】     Snowflake容忍2秒内的时间回拨（issue#I1IGDX@Gitee）
* 【core   】     StrUtil增加isAllNotEmpty、isAllNotBlank方法（pr#895@Github）
* 【core   】     DateUtil增加dayOfYear方法（pr#895@Github）
* 【core   】     DateUtil增加dayOfYear方法（pr#895@Github）
* 【http   】     HttpUtil增加downloadBytes方法（pr#895@Github）
* 【core   】     isMactchRegex失效标记，增加isMatchRegex（issue#I1IPJG@Gitee）
* 【core   】     优化Validator.isChinese
* 【core   】     ArrayUtil.addAll增加原始类型支持（issue#898@Github）
* 【core   】     DateUtil.parse支持2020-1-1这类日期解析（issue#I1HGWW@Github）

### Bug修复
* 【core   】     修复SimpleCache死锁问题（issue#I1HOKB@Gitee）
* 【core   】     修复SemaphoreRunnable释放问题（issue#I1HLQQ@Gitee）
* 【poi    】     修复Sax方式读取Excel行号错误问题（issue#882@Github）
* 【poi    】     修复Sax方式读取Excel日期类型数据03和07不一致问题（issue#I1HL1C@Gitee）
* 【poi    】     修复CamelCaseLinkedMap构造错误（issue#I1IZ30@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.3.5 (2020-05-13)

### 新特性
* 【core   】     增加CollUtil.map方法
* 【extra  】     增加Sftp.lsEntries方法，Ftp和Sftp增加recursiveDownloadFolder（pr#121@Gitee）
* 【system 】     OshiUtil增加getNetworkIFs方法
* 【core   】     CollUtil增加unionDistinct、unionAll方法（pr#122@Gitee）
* 【core   】     增加IoUtil.readObj重载，通过ValidateObjectInputStream由用户自定义安全检查。
* 【http   】     改造HttpRequest中文件上传部分，增加MultipartBody类

### Bug修复
* 【core   】     修复IoUtil.readObj中反序列化安全检查导致的一些问题，去掉安全检查。
* 【http   】     修复SimpleServer文件访问404问题（issue#I1GZI3@Gitee）
* 【core   】     修复BeanCopier中循环引用逻辑问题（issue#I1H2VN@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.3.4 (2020-05-10)

### 新特性
* 【core   】     增加URLUtil.getContentLength方法（issue#I1GB1Z@Gitee）
* 【extra  】     增加PinyinUtil（issue#I1GMIV@Gitee）

### Bug修复
* 【extra  】     修复Ftp设置超时问题（issue#I1GMTQ@Gitee）
* 【core   】     修复TreeUtil根据id查找子节点时的NPE问题（pr#120@Gitee）
* 【core   】     修复BeanUtil.copyProperties中Alias注解无效问题（issue#I1GK3M@Gitee）
* 【core   】     修复CollUtil.containsAll空集合判断问题（issue#I1G9DE@Gitee）
* 【core   】     修复XmlUtil.xmlToBean失败问题（issue#865@Github）

-------------------------------------------------------------------------------------------------------------

## 5.3.3 (2020-05-05)

### 新特性
* 【core   】     ImgUtil.createImage支持背景透明（issue#851@Github）
* 【json   】     更改JSON转字符串时"</"被转义的规则为不转义（issue#852@Github）
* 【cron   】     表达式的所有段支持L关键字（issue#849@Github）
* 【extra  】     增加PinyinUtil，封装TinyPinyin
* 【extra  】     Ftp和Sftp增加FtpConfig，提供超时等更多可选参数
* 【extra  】     SpringUtil增加getActiveProfiles、getBeansOfType、getBeanNamesForType方法（issue#I1FXF3@Gitee）
* 【bloomFilter】 避免布隆过滤器数字溢出（pr#119@Gitee）
* 【core   】     增加IoUtil.writeObj（issue#I1FZIE）
* 【core   】     增加FastStringWriter
* 【core   】     增加NumberUtil.ceilDiv方法（pr#858@Github）
* 【core   】     IdcardUtil增加省份校验（issue#859@Github）
* 【extra  】     TemplateFactory和TokenizerFactory增加单例的get方法

### Bug修复
* 【core   】     修复URLBuilder中请求参数有`&amp;`导致的问题（issue#850@Github）
* 【core   】     修复URLBuilder中路径以`/`结尾导致的问题（issue#I1G44J@Gitee）
* 【db     】     修复SqlBuilder中orderBy无效问题（issue#856@Github）
* 【core   】     修复StrUtil.subBetweenAll错误问题（issue#861@Github）

-------------------------------------------------------------------------------------------------------------

## 5.3.2 (2020-04-23)

### 新特性
* 【core   】     增加NetUtil.isOpen方法
* 【core   】     增加ThreadUtil.sleep和safeSleep的重载
* 【core   】     Sftp类增加toString方法（issue#I1F2T4@Gitee）
* 【core   】     修改FileUtil.size逻辑，不存在的文件返回0
* 【extra  】     Sftp.ls遇到文件不存在返回空集合，而非抛异常（issue#844@Github）
* 【http   】     改进HttpRequest.toString()格式，添加url

### Bug修复
* 【db     】     修复PageResult.isLast计算问题
* 【cron   】     修复更改系统时间后CronTimer被阻塞的问题（issue#838@Github）
* 【db     】     修复Page.addOrder无效问题（issue#I1F9MZ@Gitee）
* 【json   】     修复JSONConvert转换日期空指针问题（issue#I1F8M2@Gitee）
* 【core   】     修复XML中带注释Xpath解析导致空指针问题（issue#I1F2WI@Gitee）
* 【core   】     修复FileUtil.rename原文件无扩展名多点的问题（issue#839@Github）
* 【db     】     修复DbUtil.close可能存在的空指针问题（issue#847@Github）

-------------------------------------------------------------------------------------------------------------
## 5.3.1 (2020-04-17)

### 新特性
* 【core   】     ListUtil、MapUtil、CollUtil增加empty方法
* 【poi    】     调整别名策略，clearHeaderAlias和addHeaderAlias同时清除aliasComparator（issue#828@Github）
* 【core   】     修改StrUtil.equals逻辑，改为contentEquals
* 【core   】     增加URLUtil.UrlDecoder
* 【core   】     增加XmlUtil.setNamespaceAware，getByPath支持UniversalNamespaceCache
* 【aop    】     增加Spring-cglib支持，改为SPI实现
* 【json   】     增加JSONUtil.parseXXX增加JSONConfig参数
* 【core   】     RandomUtil.randomNumber改为返回char
* 【crypto 】     SM2支持设置Digest和DSAEncoding（issue#829@Github）

### Bug修复
* 【json   】     修复解析JSON字符串时配置无法传递问题（issue#I1EIDN@Gitee）
* 【core   】     修复ServletUtil.readCookieMap空指针问题（issue#827@Github）
* 【crypto 】     修复SM2中检查密钥导致的问题（issue#I1EC47@Gitee）
* 【core   】     修复TableMap.isEmpty判断问题
* 【http   】     修复编码后的URL传入导致二次编码的问题（issue#I1EIMN@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.3.0 (2020-04-07)

### 新特性
* 【extra  】     JschUtil增加execByShell方法(issue#I1CYES@Gitee)
* 【core   】     StrUtil增加subBetweenAll方法，Console增加where和lineNumber方法(issue#812@Github)
* 【core   】     TableMap增加getKeys和getValues方法
* 【json   】     JSONObject和JSONArray增加set方法，标识put弃用
* 【http   】     增加SimpleHttpServer
* 【script 】     增加createXXXScript，区别单例
* 【core   】     修改FileUtil.writeFileToStream等方法返回值为long
* 【core   】     CollUtil.split增加空集合判定（issue#814@Github）
* 【core   】     NetUtil增加parseCookies方法
* 【core   】     CollUtil增加toMap方法
* 【core   】     CollUtil和IterUtil废弃一些方法
* 【core   】     添加ValidateObjectInputStream避免对象反序列化漏洞风险
* 【core   】     添加BiMap
* 【all    】     cn.hutool.extra.servlet.multipart包迁移到cn.hutool.core.net下
* 【core   】     XmlUtil.mapToXml方法支持集合解析（issue#820@Github）
* 【json   】     解析Object中对是否为bean单独判断，而不是直接解析
* 【core   】     SimHash锁改为StampedLock
* 【core   】     Singleton改为SimpleCache实现
* 【core   】     增加CalendarUtil，DateUtil相关方法全部迁移到此

### Bug修复
* 【extra  】     修复SpringUtil使用devtools重启报错问题
* 【http   】     修复HttpUtil.encodeParams针对无参数URL问题（issue#817@Github）
* 【extra  】     修复模板中无效引用的问题
* 【extra  】     修复读取JSON文本配置未应用到子对象的问题（issue#818@Github）
* 【extra  】     修复XmlUtil.createXml中namespace反向问题
* 【core   】     修复WatchMonitor默认无event问题

-------------------------------------------------------------------------------------------------------------

## 5.2.5 (2020-03-26)

### 新特性
* 【core   】     增加逻辑，对于原始类型注入，使用默认值（issue#797@Github）
* 【core   】     增加CityHash算法
* 【core   】     PageUtil支持setFirstPageNo自定义第一页的页码（issue#I1CGNZ@Gitee）
* 【http   】     UserAgentUtil增加Chromium内核的Edge浏览器支持（issue#800@Github）
* 【cache  】     修改FIFOCache中linkedHashMap的初始容量策略（pr#801@Github）
* 【core   】     修改XmlUtil中setNamespaceAware默认为true
* 【core   】     TreeNode增加extra
* 【core   】     CollUtil.newHashSet重载歧义，更换为set方法
* 【core   】     增加ListUtil，增加Hash32、Hash64、Hash128接口
* 【crypto 】     BCUtil增加readPemPrivateKey和readPemPublicKey方法
* 【cache  】     替换读写锁为StampedLock，增加LockUtil

### Bug修复
* 【core   】     修复NumberWordFormatter拼写错误（issue#799@Github）
* 【poi    】     修复xls文件下拉列表无效问题（issue#I1C79P@Gitee）
* 【poi    】     修复使用Cglib代理问题（issue#I1C79P@Gitee）
* 【core   】     修复DateUtil.weekCount跨年计算问题

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
