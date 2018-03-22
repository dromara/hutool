# Changelog

-------------------------------------------------------------------------------------------------------------

## 3.3.2

### 新特性
* 改进HttpUtil.download使之支持更多方法和参数

### Bug修复
* FileTypeUtil.getType未关闭流问题（issue#79@Github）
* 修复EntityHandler和EntityListHandler转换问题
* 修复BeanDesc类中boolean类型字段名为isXXX的情况无法注入问题

-------------------------------------------------------------------------------------------------------------

## 3.3.1

### 新特性
* StrUtil增加方法equalsCharAt
* 增加方法FileUtil.cleanInvalid
* db模块BeanHandler支持忽略大小写（针对Oracle返回大写字段无法注入Bean的问题）
* Setting增加set方法，支持分组
* JSONObject支持键忽略大小写模式
* db模块针对java.sql.Time增加单独判断
* DbUtil.getTables支持Oracle用户参数传入（感谢@【广西】Succy）
* 优化BeanHandler，去除转为Entity环节

### Bug修复
* 修复HttpUtil.post方法超时失效的问题（感谢@【常州】Gavin）
* 修复Http的get方法传入String带有空格时，报空指针错误（感谢@【北京】宁静）
* 修复DateUtil.endOfWeek问题（感谢@【北京】谢栋）
* 修复Convert.numberToChinese大数字出现的问题（issue#IEYLP@Gitee）
* 修复HtmlUtil.restoreEscaped缺失分号问题，以及单引号处理（issue#IH5ZH@Gitee）
* 修复签名问题（issue#IH61M@Gitee）
* 修复Excel空行下报错问题（issue#78@Github）
* 修复NumberUtil.isInteger无法判断负数问题（感谢@【杭州】fineliving）
* 修复BeanUtil.fillBean等方法忽略大小写失败的问题
* 修复JSON中getStr方法返回字符串中含有\b等特殊字符时显示错误问题（感谢@【广西】Succy）

-------------------------------------------------------------------------------------------------------------

## 3.3.0

### 新特性
* 删除Deprecated方法和类
* 增加StrUtil.isEmptyIfStr和isBlankIfStr方法
* 改进ExcelReader，对于空行默认不读取（包括元素全部为null或“”的行）（感谢@【北京】新任女人国王）
* Validator增加isLetter、isUpperCase、isLowerCase方法（感谢@【深圳】objectboy(刚转java) ）
* 增加权重随机数算法WeightRandom，RandomUtil增加Long、Double随机数生成
* XmlUtil.transElements增加节点类型判断（issue#IGTGO@Gitee）
* JSONUtil增加isJson、isJsonObj、isJsonArray方法（issue#IGT7A@Gitee）
* Week增加方法toChinese（感谢@【北京】宁静）
* AbstractCaptcha增加方法generateCode，用于抽象验证码字符串生成，提供可定制验证码字符串生成（感谢@【杭州】KIWI @【杭州】t-io ）
* NetUtil增加netCat方法
* 新增ExceptionUtil.wrap方法，包装异常
* 改进AES个DES类，在构造中加入自定义偏移
* 实现ActiveEntity
* HttpRequest增加fileForm方法
* 增加DESede快捷实现类
* 签名算法从AsymmetricCrypto中剥离，形成Sign对象
* 增加EnumUtil工具类
* Convert.convert方法支持Map转换
* HttpRequest.body方法增加自动判断Content-Type类型（感谢@【北京】酱油君）
* 增加自定义重定向次数
* SecureUtil针对hmac方法增加String参数（感谢@【北京】宁静）
* db模块去除获取null值类型的错误日志
* 手机号验证(Validator)支持199、198、166号段（issue#IH0TD@Gitee）
* Base64、Base32、BCD全部移入codec包
* Map相关类移入map包中，与collection区分
* HttpUtil.get增加超时重载（感谢@【山东】UP ）

### Bug修复
* 修复ExcelUtil.readBySax方法判断03或07格式导致的问题（issue#IGT7X@Gitee）
* 修复CronUtil中一些方法非static问题（issue#74@Github）
* 修复ShearCaptcha的thickness无效问题
* 修复AbstractCaptcha写出文件未关闭流的问题
* 修复AES中自定义Mode和Padding导致的自动生成密钥问题（issue#55@Github）
* 修复MapConvert类中类型判断导致的null问题（issue#IGXNI@Gitee）
* 修复FileUtil.copy方法复制文件到目录失败问题（感谢@【广州】Sinderlar）
* 修复邮件默认配置问题
* 按照RFC2616规范，读取响应头信息时忽略大小写（感谢@【武汉】赛车手）
* 修复POI模块read07BySax导致的空白单元格丢失和日期粘连问题（issue#76@Github）
* 修复ExcelWriter在写出4000行以上时样式超出范围问题（issue#IH05B@Gitee）
* 修复StrUtil.replaceChars方法无效问题
* 修复在JSONArray中toList有null元素返回null的问题（感谢@【银川】野马）
* 修复Base64解码时在有换行符的情况下解码失败的问题（感谢@【霾都】QQ小冰）

-------------------------------------------------------------------------------------------------------------

## 3.2.3

### 新特性
* 增加NumberChineseFormater数字转汉字（感谢@【杭州】fineliving）
* StrUtil增加ordinalIndexOf方法（感谢@【四川】开心一笑）
* 增加H2和SQLServer2012方言支持
* 邮件支持发件人姓名（issue#IGMW2@Gitee）
* db部分中Session增加默认数据源的Session
* 增加RedisDs，Jedis的薄封装
* 增加字符串相似度计算工具TextSimilarity（感谢@【杭州】fineliving）
* 增加几个验证码类型
* 增加HtmlUtil.escape方法（issue#IGOKB@Gitee）
* ExcelWriter排序支持
* BeanUtil中CopyOptions增加mapping选项，可以不同字段名之间值复制
* 使用BeanCopier抽象Bean属性拷贝

### Bug修复
* 修复Oracle下Date类型字段带时分秒取出丢失问题（感谢@烟雨江南）
* 修复Convert中金额转为大写格式问题（感谢@【杭州】fineliving）
* 邮件修复附件名乱码问题（issue#IGMW2@Gitee）
* 修复 DateBetween.betweenYear中月份一致导致判断有误问题（issue#IGN0N@Gitee）
* 修复JSONStrFormater在字符串中出现“[”、“{”等导致的换行错误问题（感谢@【北京】宁静）
* 修复Props中两次创建Writer问题
* 修复ZipUtil.unzip丢失编码参数问题（issue#71@Github）
* 修复JSON中设置不忽略null值后子对象null值丢失问题（感谢@【北京】xkcoding）
* 修复HtmlUtil.removeAllHtmlAttr方法无效问题
* 修复DateUtil.parse方法对时间中"."误替换
* 修复Season.AUTUMN拼写错误（issue#73@Github）
* 修复插入返回主键错误问题（感谢@【北京】Pom）

-------------------------------------------------------------------------------------------------------------

## 3.2.2

### 新特性
* 增加MapUtil.reverse方法（感谢@【北京】宁静）
* Bean转JSON的时候key与字段名保持一致（感谢@【北京】宁静）
* 允许设置查询字段（pr#56@Github）
* 增加SQL执行查询单条记录的方法（pr#57@Github）
* 增加查询结果为单个字符串时的处理器（pr#58@Github）
* DateTime提供toJdkDate方法以适应需要原生Date对象的环境
* IdCardUtil提供hide方法，用于将固定位置的数字隐藏为星号
* 增加parseBean重载（针对Dict和Entity）（pr#63@Github）
* StrUtil增加replaceChars用于替换某些字符（感谢@【北京】宁静）
* DateUtil.parse增加日期支持的格式，DatePattern增加纯数字形式日期（感谢@【北京】宁静）
* 改进Convert部分，使之支持数组类型转换
* ArrayUtil增加getArrayType方法，可以从数组元素类型获取其数组类型
* CollUtil和ArrayUtil增加removeNull、removeEmpty、removeBlank方法（感谢@【北京】宁静）
* PageUtil增加rainbow方法（来自@【北京】宁静的iceroot项目）
* CollUtil增加group和groupByField方法，用于集合分组（感谢@【北京】宁静）
* StrUtil增加subWithLength方法（感谢@weibaohui）
* ZipUtil添加编码参数支持（感谢@【天津】〓下页 ）
* SqlRunner增加分页重载方法
* imageUtil增加旋转和翻转
* SqlRunner增加返回Bean列表的方法
* ImageUtil增加方法，并支持根据文件扩展名输出其格式的图片

### Bug修复
* 修复MapUtil.filter方法修改值无效问题
* fix 调用BeanUtil.fillBeanWithMap方法错误（pr#59@Github）
* fix 线程不断创建OOM（pr#61@Github）
* 修复ReflectUtil.invoke方法执行时参数为原始类型导致的类型不匹配问题（感谢@【上海】简简单单）
* 修复JSON转Bean时多层嵌套集合时导致的转换失败问题（issue#60@Github）
* 修复db模块传入参数为null时无法获取类型的错误提示
* 修复Number.round导致的精度丢失问题

-------------------------------------------------------------------------------------------------------------

## 3.2.1

### 新特性
* HttpUtil中增加一些空判断
* 增强Props和Setting，使传入路径支持绝对路径
* 增加Resource接口和FileResource对象
* DB模块新增between and支持（pr#53@Github）
* poi模块中增加写出Excel针对null值得支持（默认输出空字符串）
* DB模块find方法支持从where的Entity中获取返回字段列表（感谢@【北京】大大宝）
* StrUtil增加indexOfIgnoreCase、isSubEquals、indexOf（String参数和IgnoreCase参数）方法
* StrSpliter类中的方法支持大小写敏感可选参数
* StrUtil增加replace方法，可以替换字符串某个部分为重复的字符（感谢@【北京】宁静）
* 改进ExcelWriter增加标题别名（感谢@【帝都】汪汪）
* ExcelWriter增加构造方法，ExcelUtil.getWriter增加相应的重载方法（感谢@【帝都】汪汪）
* ArrayUtil增加nullToEmpty方法（感谢@【北京】宁静）

### Bug修复
* 在JSON中，空字符串转为bean中的非字符串对象时，传入null，而非报错
* 修复在获取jar包中绝对路径时file:前缀导致获取路径错误（issue#IG7Q6@Gitee）
* 修复ZipUtil.zip方法压缩目录时空的子目录被忽略问题（issue#IG94M@Gitee）
* 修复ClassPathResource.getAbsolutePath方法获取路径非标准的问题（感谢@【北京】宁静）
* 修复ClassUtil.getClassPath方法获取到的中文路径为编码后的，同时添加可选是否获取解码后的路径（感谢@【北京】宁静）
* Props的store方法异常抛出，而非打印日志。修复Writer未关闭问题（感谢@【西安】小雷）
* 修复ArrayUtil.filter方法过滤无效错误

-------------------------------------------------------------------------------------------------------------

## 3.2.0

### 新特性
* MailUtil邮件工具类支持附件
* Convert增加int、long、short与bytes之间的转换
* BeetlUtil增加更多简化方法
* extra模块中模板相关工具类移入template包中
* ScriptUtil增加eval方法，执行脚本快捷方法
* 增加Excel03SaxReader用于03格式的Excel通过Sax方式读取
* HttpUtil增加超时重载，post方法支持Rest模式
* core包中去除servlet-api可选依赖，extra模块中增加ServletUtil（core包中的部分方法移入此工具类）
* MailUtil支持SSL方式连接
* 增加MapProxy，用于代理Map对象，提供各种getXXX方法（感谢@【珠海】hzhhui）
* Convert增加toXXXArray方法
* 增加剪贴板工具类ClipboardUtil（感谢@【北京】宁静）
* ObjectUtil增加toString方法（感谢@【南京】toling）
* XmlUtil增加readObjectFromXml重载（感谢@【北京】酱油君）
* FileUtil和IoUtil去除final修饰（issue#49@Github）
* 为了更好的兼容性，Getter和Setter方法获取忽略大小写
* StrUtil增加split和splitTrim重载方法（感谢@【南京】toling @【北京】宁静）
* 增加FileUtil.writeLines重载方法和writeUtf8Lines方法（感谢@【北京】宁静）

### Bug修复
* 修复FileUtil.normalize导致的路径修复问题
* db模块中字段使用别名时去掉包装符
* CollUtil.filter方法对于不可变集合参数报错问题改进（issue#IFW3Y@Gitee）
* 修复Convert.convert方法目标为数组对象时导致的问题
* 修复poi模块中ExcelReader读取带小数的标准单元格时小数部分丢失问题修复
* 修复SecureUtil.rsa和SecureUtil.dsa方法中publicKey传入问题（感谢@【上海】毛毛虎）
* 修复Cache模块传入Integer.MAX_VALUE错误问题（感谢@【南京】雲栖鬆）
* 修复BeanDesc无法识别isXXX方法的问题

-------------------------------------------------------------------------------------------------------------

## 3.1.2

### 新特性
* ArrayUtil增加containsIgnoreCase和indexOfIgnoreCase方法
* DbUtil增加toSqlDate和toSqlTimestamp和setShowSqlGlobal方法
* RuntimeUtil增加可选编码的方法
* CharsetUtil.systemCharset修改逻辑，Windows使用GBK编码
* db模块加强异常提示信息
* 升级可选依赖版本
* Setting增加getandRemove和getAndRemoveStr方法，并去除db模块中配置键别名获取相关冗余方法
* FileUtil增加subPath、getPathEle方法（针对JDK7的Path对象封装）
* 改进WatchMonitor，文件不存在时根据path判断是文件还是目录
* 参阅iceroot库，增加DateUtil增加timeToSecond和secondToTime方法（感谢@【北京】宁静）
* DateUtil.beginOfWeek加入方法可选一周的第一天，且默认周一为第一天（感谢@【北京】flyinke）
* Hutool-db支持value为"= null"转换为"is null"
* 工具类继承支持（去除private构造）
* 增加getGroup和findAllGroup两类方法
* UrlUtil增加utf-8重载（感谢@【北京】宁静）
* 按照Ali规范整理代码。Cache模块中定时清理改为定时任务池方式
* Http模块全局Header支持
* JSON模块增加JSONStrFromater，用于格式化非标准的JSON字符串
* 增加BeanDesc代替JDK的BeanInfo，提供更灵活的Getter和Setter
* BeanUtil.fillBean方法使用BeanDesc替换
* 增加FileAppender类用于积累追加文件内容
* 增加ReferenceUtil用于工具化创建软引用和弱引用
* ReflectUtil中将异常包装为UtilExcception，并增加invoke重载支持字符串
* ClassUtil中部分方法被标记过时，大部分与ReflectUtil中方法重叠，通过注释指引到ReflectUtil
* core包中加入验证码生成
* 弃用Random类，改为ThreadLocalRandom
* extra模块中增加MailUtil发送邮件工具（依赖javax.mail）
* StrUtil增加strip和stripIgnoreCase方法
* poi模块新增ExcelSaxReader，对大数据量的Excel读取增加支持

### Bug修复
* db模块修复Oracle中传入java.util.Date对象无法识别类型问题（默认按照Timestamp处理）（感谢@【杭州】wiley）
* 修复RuntimeUtil中乱码和单条带参数命令执行失败问题（@【北京】宁静）
* 修复ThreadUtil newExecutorByBlockingCoefficient中不能传入0的bug（issue#IF7UN@Gitee）
* 修复CollUtil.split的bug（issue#IF7UT@Gitee）
* 修复Page.getEndPosition()错误（感谢@【深圳】尘风了了）
* 修复Http中head方法读取body失败问题，略过读取body（issue#IFA3C@Gitee）
* 修复从正文中获取编码类型的错误（issue#IFBYO@Gitee）
* 修复IOUtil.readBytes中读取为0导致的越界问题（issue#46@Github）
* 修复Crypto模块中DESede算法密钥生成bug
* 修复JSON转Bean时在ignoreError模式下类型不匹配时无法忽略问题
* 修复RSA分组加密中中文导致的问题（pr#47@Github）
* 修复NumberUtil.equals方法的一个坑（精度不同导致不同）（感谢@【北京】Dull）
* 修复StrUtil中部分方法判空后返回原值导致的空指针问题

-------------------------------------------------------------------------------------------------------------

## 3.1.1

### 新特性
* ExcelReader中根据单元格格式判断Double还是Long类型（感谢@act家的excel-reader）
* Map相关方法剥离为MapUtil
* 新增CollUtil做为CollectionUtil别名
* 非对称加密加入PublicKey对象和PrivateKey对象构造，RSA加入N,e,d参数支持（感谢@【帝都】小帅帅）
* Props支持其它编码格式（PR#37@Github）
* DateBetween增加可选是否取绝对值选项构造（issue#IETE0@gitee）
* 加入Rythm模板引擎工具类
* cron模块中增加方法支持获取Task和CronPattern（感谢@Γ平淡ㄎ）
* HttpResponse中增加个体Cookie方法
* Hive驱动识别支持。（@【北京】宁静）
* IoUtil中IOException替换为IORuntimeException
* IoUtil和FileUtil增加UTF-8编码重载
* Http增加headerList方法
* Http设置Cookie支持HttpCookie对象列表
* 新增RuntimeUtil，用于执行系统命令的快捷工具类（感谢@【北京】宁静）
* 新增DateUtil.isExpired方法（issue#41@Github）
* 新增MapUtil.join和builder方法（pr#40@Github）

### Bug修复
* NumberUtil中针对Double重载方法，避免传入包装类型引起的歧义
* 修复Bean转JSONObject时字段无getter方法导致的字段值丢失问题（感谢@猎隼丶止戈，issue#IEIJG@osc）
* 修复StrUtil.addPrefixIfNot方法问题（感谢@【苏州】咖啡）
* 修复db部分Session中beginTransaction()逻辑问题（感谢@taoguan）
* 修复POI模块ExcelReader空单元格被忽略问题。
* 修复cron模块中移除Task导致的index错误问题（感谢@Γ平淡ㄎ）
* 修复POI模块中自定义单元格含有中文时无法识别为日期的问题（感谢@【昆明】Tang）
* 修复RSA算法编码问题（感谢@【长沙】笑小生）
* Http模块对参数key做编码（issue#IEYLP@gitee）
* 修复ImageUtil写出文件没有关闭流导致的文件被占用问题（issue#44@Github）

-------------------------------------------------------------------------------------------------------------

## 3.1.0

### 新特性
* CollectionUtil增加findOne、findOneByField、getFieldValues等方法
* cron模块支持Quartz的"?"表达式
* ReUtil增加getAllGroups方法用于获取所有分组匹配
* CollectionUtil增加toMapList和toListMap方法，提供行列转换（感谢@【北京】宁静）
* WatchMonitor增加文件递归（子目录）监听支持（感谢@t-io）
* cron模块中改进InvokeTask，在初始化时验证并加载类和方法（感谢@【南京】toling）
* 增加ConcurrentHashSet
* HttpRequestsetXXX补充返回this（感谢【南京】peckey）
* Hutool-db增加 BeanHandler、BeanListHandler，find方法增加可变参数（返回字段）
* 增强手机号码验证正则（感谢@【北京】宁静 @【北京】iisimpler）
* 创建Chain接口，用于责任链模式的实现
* JSON.getByExp方法增加重载方法，可以指定返回值类型（感谢【深圳】富）
* FileUtil增加转换文件编码和换行符的方法（感谢@【北京】宁静）
* 增加IterUtil，将CollectionUtil中部分方法迁入

### Bug修复
* 修复CollectionUtil中并集、差集问题（issue#IE9VH@osc）
* 修复批量插入只有一个对象无法插入问题（感谢@【北京】游弋苍茫）
* 修复NumberUtil.div错误（感谢@【北京】宁静）
* 修复DateUtil.beginOfYear问题（感谢@【北京】iisimpler）
* 修正Email正则，符合RFC 5322规范（感谢@【北京】iisimpler）
* 修正ArrayUtil.isEmpty逻辑（感谢@【北京】仓山有井名为空）
* 修复计算第几周时没有考虑每周第一天的情况（DateTime增加setFirstDayOfWeek方法），并设置默认值为周一（@【北京】仓山有井名为空）
* 修复CollUtil.findOneByField方法传递参数错误的bug（感谢@【珠海】hzhhui）

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