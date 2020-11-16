
# Changelog

-------------------------------------------------------------------------------------------------------------

# 5.5.1 (2020-11-16)

### 新特性
* 【core   】     增加CopyVisitor和DelVisitor

### Bug修复
* 【core   】     修复在Linux下FileUtil.move失败问题（issue#I254Y3@Gitee）
* 【http   】     修复UrlUtil和UrlBuilder中多个/被替换问题（issue#I25MZL@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.5.0 (2020-11-14)

### 大版本特性
* 【extra  】     增加jakarta.validation-api封装：ValidationUtil（pr#207@Gitee）
* 【extra  】     增加表达式引擎封装：ExpressionUtil（pr#1203@Github）
* 【extra  】     新增基于Apache-FtpServer封装：SimpleFtpServer
* 【extra  】     新增基于Commons-Compress封装：CompressUtil

### 新特性
* 【core   】     NumberUtil.parseInt等支持123,2.00这类数字（issue#I23ORQ@Gitee）
* 【core   】     增加ArrayUtil.isSub、indexOfSub、lastIndexOfSub方法（issue#I23O1K@Gitee）
* 【core   】     反射调用支持传递参数的值为null（pr#1205@Github）
* 【core   】     HexUtil增加format方法（issue#I245NF@Gitee）
* 【poi    】     ExcelWriter增加setCurrentRowToEnd方法（issue#I24A2R@Gitee）
* 【core   】     ExcelWriter增加setCurrentRowToEnd方法（issue#I24A2R@Gitee）
* 【core   】     增加enum转数字支持（issue#I24QZY@Gitee）
* 【core   】     NumberUtil.toBigDecimal空白符转换为0（issue#I24MRP@Gitee）
* 【core   】     CollUtil和IterUtil增加size方法（pr#208@Gitee）
* 【poi    】     ExcelReader的read方法读取空单元格增加CellEditor处理（issue#1213@Github）

### Bug修复
* 【core   】     修复DateUtil.current使用System.nanoTime的问题（issue#1198@Github）
* 【core   】     修复Excel03SaxReader判断日期出错问题（issue#I23M9H@Gitee）
* 【core   】     修复ClassUtil.getTypeArgument方法在判断泛型时导致的问题（issue#1207@Github）
* 【core   】     修复Ipv4Util分隔符问题（issue#I24A9I@Gitee）
* 【core   】     修复Ipv4Util.longToIp的问题
* 【poi    】     修复Excel07SaxReader读取公式的错误的问题（issue#I23VFL@Gitee）
* 【http   】     修复HttpUtil.isHttp判断问题（pr#1208@Github）
* 【http   】     修复Snowflake时间回拨导致ID重复的bug（issue#1206@Github）
* 【core   】     修复StrUtil.lastIndexOf查找位于首位的字符串找不到的bug（issue#I24RSV@Gitee）
* 【poi    】     修复BigExcelWriter的autoSizeColumnAll问题（pr#1221@Github）
* 【core   】     修复StrUtil.subBetweenAll不支持相同字符的问题（pr#1217@Github）

-------------------------------------------------------------------------------------------------------------

# 5.4.7 (2020-10-31)

### 新特性
* 【core   】     增加OptionalBean（pr#1182@Github）
* 【core   】     Ganzhi增加方法（issue#1186@Github）
* 【core   】     CollUtil增加forEach重载（issue#I22NA4@Gitee）
* 【core   】     CollUtil.map忽略空值改规则为原数组中的元素和处理后的元素都会忽略空值（issue#I22N08@Gitee）
* 【http   】     增加SoapClient增加addSOAPHeader重载
* 【http   】     ArrayUtil增加containsAll方法
* 【core   】     增加CharsetDetector
* 【cron   】     增加CronTask，监听支持获取id（issue#I23315@Gitee）

### Bug修复
* 【core   】     修复BeanUtil.beanToMap方法中editor返回null没有去掉的问题
* 【core   】     修复ImgUtil.toBufferedImage颜色模式的问题（issue#1194@Github）
* 【cron   】     修复TimeZone设置无效的问题（issue#I23315@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.4.6 (2020-10-23)

### 新特性
* 【http   】     HttpRequest增加basicProxyAuth方法（issue#I1YQGM@Gitee）
* 【core   】     NumberUtil.toStr修改逻辑，去掉BigDecimal的科学计数表示（pr#196@Gitee）
* 【core   】     ListUtil.page第一页页码使用PageUtil（pr#198@Gitee）
* 【http   】     增加微信、企业微信ua识别（pr#1179@Github）
* 【core   】     ObjectUtil增加defaultIfXXX（pr#199@Gitee）
* 【json   】     JSONObject构建时不支持的对象类型抛出异常

### Bug修复
* 【core   】     修复ChineseDate没有忽略时分秒导致计算错误问题（issue#I1YW12@Gitee）
* 【core   】     修复FileUtil中，copyFile方法断言判断参数传递错误（issue#I1Z2NY@Gitee）
* 【core   】     修复BeanDesc读取父类属性覆盖子类属性导致的问题（pr#1175@Github）
* 【aop    】     修复SimpleAspect一个重载导致的问题，去掉重载的after方法（issue#I1YUG9@Gitee）
* 【poi    】     修复03 sax读取日期问题（issue#I1Z83N@Gitee）
* 【core   】     修复FileUtil.size软链导致的问题（pr#200@Gitee）
* 【core   】     修复JSONObject构造时传入JSONArray结果出错问题（issue#I22FDS@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.4.5 (2020-10-18)

### 新特性
* 【core   】     ConsoleTable代码优化（pr#190@Gitee）
* 【http   】     HttpRequest增加setProxy重载（pr#190@Gitee）
* 【core   】     XmlUtil.cleanComment（pr#191@Gitee）
* 【core   】     ArrayUtil.unWrap增加默认值（pr#1149@Github）
* 【core   】     ArrayUtil.indexOf修改double的equals判断（pr#1147@Github）
* 【core   】     优化StrUtil中部分参数校验以及逻辑处理（pr#1144@Github）
* 【core   】     简化CreditCode逻辑去除无用Character.toUpperCase（pr#1145@Github）
* 【core   】     NumberUtil增加generateRandomNumber重载，可自定义seed（issue#I1XTUT@Gitee）
* 【core   】     DataSizeUtil支持小数（pr#1158@Github）
* 【core   】     完善注释（pr#193@Gitee）
* 【core   】     优化Combination.countAll（pr#1159@Github）
* 【core   】     优化针对list的split方法（pr#194@Gitee）
* 【poi    】     ExcelWriter增加setRowStyle方法
* 【core   】     Assert增加函数接口（pr#1166@Github）
* 【core   】     新增AtomicIntegerArray、AtomicLongArray转换
* 【extra  】     PinyinUtil新增Bopomofo4j支持
* 【core   】     新增TemporalUtil工具类，新增时间相关方法

### Bug修复
* 【core   】     解决农历判断节日未判断大小月导致的问题（issue#I1XHSF@Gitee）
* 【core   】     解决ListUtil计算总量可能的int溢出问题（pr#1150@Github）
* 【json   】     解决JSON中转换为double小数精度丢失问题（pr#192@Gitee）
* 【core   】     修复CaseInsensitiveMap的remove等方法并没有忽略大小写的问题（pr#1163@Gitee）
* 【poi    】     修复合并单元格值读取错误的问题
* 【poi    】     修复NamedSql解析形如col::numeric出错问题（issue#I1YHBX@Gitee）
* 【core   】     修复计算相差天数导致的问题

-------------------------------------------------------------------------------------------------------------

# 5.4.4 (2020-09-28)

### 新特性
* 【core   】     ServiceLoaderUtil改为使用contextClassLoader（pr#183@Gitee）
* 【core   】     NetUtil增加getLocalHostName（pr#1103@Github）
* 【extra  】     FTP增加stat方法（issue#I1W346@Gitee）
* 【core   】     Convert.toNumber支持类似12.2F这种形式字符串转换（issue#I1VYLJ@Gitee）
* 【core   】     使用静态变量替换999等（issue#I1W8IB@Gitee）
* 【core   】     URLUtil自动trim（issue#I1W803@Gitee）
* 【crypto 】     RC4增加ecrypt（pr#1108@Github）
* 【core   】     CharUtil and StrUtil增加@（pr#1106@Github）
* 【extra  】     优化EMOJ查询逻辑（pr#1112@Github）
* 【extra  】     优化CollUtil交并集结果集合设置初始化大小，避免扩容成本（pr#1110@Github）
* 【core   】     优化PageUtil彩虹算法（issue#1110@Github）
* 【core   】     IoUtil增加readUtf8方法
* 【core   】     优化全局邮箱账户初始化逻辑（pr#1114@Github）
* 【http   】     SoapClient增加addSOAPHeader方法
* 【http   】     完善StrUtil的注释（pr#186@Gitee）
* 【aop    】     去除调试日志（issue#1116@Github）
* 【core   】     增加&apos;反转义（pr#1121@Github）
* 【poi    】     增加SheetReader和XXXRowHandler（issue#I1WHJP@Gitee）
* 【dfa    】     增加过滤符号（pr#1122@Github）
* 【dfa    】     SensitiveUtil增加setCharFilter方法（pr#1123@Github）
* 【all    】     优化常量大小写规范（pr#188@Gitee）
* 【core   】     优化NumberUtil中针对BigDecimal的一些处理逻辑（pr#1127@Github）
* 【core   】     NumberUtil.factorial注释明确（pr#1126@Github）
* 【core   】     NumberUtil增加isPowerOfTwo方法（pr#1132@Github）
* 【core   】     优化BooleanUtil的校验逻辑（pr#1137@Github）
* 【poi    】     改进sax方式读取逻辑，支持sheetId（issue#1141@Github）
* 【core   】     XmlUtil增加readBySax方法

### Bug修复
* 【crypto 】     修复SM2验签后无法解密问题（issue#I1W0VP@Gitee）
* 【core   】     修复新建默认TreeSet没有默认比较器导致的问题（issue#1101@Github）
* 【core   】     修复Linux下使用Windows路径分隔符导致的解压错误（issue#I1MW0E@Gitee）
* 【core   】     修复Word07Writer写出map问题（issue#I1W49R@Gitee）
* 【script 】     修复函数库脚本执行问题
* 【core   】     修复RGB随机颜色的上限值不对且API重复（pr#1136@Gihub）

-------------------------------------------------------------------------------------------------------------

# 5.4.3 (2020-09-16)

### 新特性
* 【core   】     使用静态的of方法来new对象（pr#177@Gitee）
* 【setting】     Setting增加store无参方法（issue#1072@Github）
* 【setting】     StatementUtil增加null缓存（pr#1076@Github）
* 【core   】     扩充Console功能，支持可变参数（issue#1077@Github）
* 【crypto 】     增加ECKeyUtil（issue#I1UOF5@Gitee）
* 【core   】     增加TransXXX（issue#I1TU1Y@Gitee）
* 【core   】     增加Generator
* 【db     】     Column增加是否主键、保留位数等字段
* 【cache  】     Cache接口增加get重载（issue#1080@Github）
* 【core   】     增加Interner和InternUtil（issue#I1TU1Y@Gitee）
* 【core   】     增加Calculator（issue#1090@Github）
* 【core   】     IdcardUtil增加getIdcardInfo方法（issue#1092@Github）
* 【core   】     改进ObjectUtil.equal，支持BigDecimal判断
* 【core   】     ArrayConverter增加可选是否忽略错误（issue#I1VNYQ@Gitee）
* 【db     】     增加ConditionBuilder
* 【setting】     Setting和Props增加create方法
* 【log    】     增加TinyLog2支持（issue#1094@Github）

### Bug修复
* 【core   】     修复Dict.of错误（issue#I1UUO5@Gitee）
* 【core   】     修复UrlBuilder地址参数问题（issue#I1UWCA@Gitee）
* 【core   】     修复StrUtil.toSymbolCase转换问题（issue#1075@Github）
* 【log    】     修复打印null对象显示{msg}异常问题（issue#1084@Github）
* 【extra  】     修复ServletUtil.getReader中未关闭的问题
* 【extra  】     修复QrCodeUtil在新版本zxing报错问题（issue#1088@Github）
* 【core   】     修复LocalDateTimeUtil.parse无法解析yyyyMMddHHmmssSSS的bug（issue#1082@Github）
* 【core   】     修复VersionComparator.equals递归调用问题（issue#1093@Github）

-------------------------------------------------------------------------------------------------------------

# 5.4.2 (2020-09-09)

### 新特性
* 【core  】     lock放在try外边（pr#1050@Github）
* 【core  】     MailUtil增加错误信息（issue#I1TAKJ@Gitee）
* 【core  】     JschUtil添加远程转发功能（pr#171@Gitee）
* 【db    】     AbstractDb增加executeBatch重载（issue#1053@Github）
* 【extra 】     新增方便引入SpringUtil的注解@EnableSpringUtil（pr#172@Gitee）
* 【poi   】     RowUtil增加插入和删除行（pr#1060@Github）
* 【extra 】     SpringUtil增加注册bean（pr#174@Gitee）
* 【core  】     修改NetUtil.getMacAddress避免空指针（issue#1057@Github）
* 【core  】     增加EnumItem接口，枚举扩展转换，增加SPI自定义转换（pr#173@Github）
* 【core  】     TypeUtil增加getActualType，增加ActualTypeMapperPool类（issue#I1TBWH@Gitee）
* 【extra 】     QRConfig中添加qrVersion属性（pr#1068@Github）
* 【core  】     ArrayUtil增加equals方法
* 【core  】     BeanDesc增加方法
* 【core  】     增加@PropIgnore注解（issue#I1U846@Gitee）

### Bug修复
* 【core  】     重新整理农历节假日，解决一个pr过来的玩笑导致的问题
* 【poi   】     修复ExcelFileUtil.isXls判断问题（pr#1055@Github）
* 【poi   】     修复CglibUtil.copyList参数错误导致的问题
* 【http  】     修复GET请求附带body导致变POST的问题
* 【core  】     修复double相等判断问题（pr#175@Gitee）
* 【core  】     修复DateSizeUtil.format越界问题（issue#1069@Github）
* 【core  】     修复ChineseDate.getChineseMonth问题（issue#I1UG72@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.4.1 (2020-08-29)

### 新特性
* 【core  】     StrUtil增加firstNonXXX方法（issue#1020@Github）
* 【core  】     BeanCopier修改规则，可选bean拷贝空字段报错问题（pr#160@Gitee）
* 【http  】     HttpUtil增加downloadFileFromUrl（pr#1023@Github）
* 【core  】     增加toEpochMilli方法
* 【core  】     Validator修改isCitizenId校验（pr#1032@Github）
* 【core  】     增加PathUtil和FileNameUtil，分离FileUtil中部分方法
* 【core  】     改造IndexedComparator，增加InstanceComparator
* 【extra 】     增加CglibUtil
* 【core  】     增加Ipv4Util（pr#161@Gitee）
* 【core  】     增加CalendarUtil和DateUtil增加isSameMonth方法（pr#161@Gitee）
* 【core  】     Dict增加of方法（issue#1035@Github）
* 【core  】     StrUtil.wrapAll方法不明确修改改为wrapAllWithPair（issue#1042@Github）
* 【core  】     EnumUtil.getEnumAt负数返回null（pr#167@Gitee）
* 【core  】     ChineseDate增加天干地支和转换为公历方法（pr#169@Gitee）
* 【core  】     Img增加stroke描边方法（issue#1033@Github）

### Bug修复#
* 【poi   】     修复ExcelBase.isXlsx方法判断问题（issue#I1S502@Gitee）
* 【poi   】     修复Excel03SaxReader日期方法判断问题（pr#1026@Github）
* 【core  】     修复StrUtil.indexOf空指针问题（issue#1038@Github）
* 【extra 】     修复VelocityEngine编码问题和路径前缀问题（issue#I1T0IG@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.4.0 (2020-08-06)

### 新特性
* 【socket】     对NioServer和NioClient改造（pr#992@Github）
* 【core  】     StrUtil增加filter方法（pr#149@Gitee）
* 【core  】     DateUtil增加beginOfWeek重载
* 【core  】     将有歧义的BeanUtil.mapToBean方法置为过期（使用toBean方法）
* 【core  】     添加WatchAction（对Watcher的抽象）
* 【core  】     修改UUID正则，更加严谨（issue#I1Q1IW@Gitee）
* 【core  】     ArrayUtil增加isAllNull方法（issue#1004@Github）
* 【core  】     CollUtil增加contains方法（pr#152@Gitee）
* 【core  】     ArrayUtil增加isAllNotNull方法（pr#1008@Github）
* 【poi   】     closeAfterRead参数无效，方法设为过期（issue#1007@Github）
* 【core  】     CollUtil中部分方法返回null变更为返回empty
* 【all   】     添加英文README（pr#153@Gitee）
* 【extra 】     SpringUtil增加getBean(TypeReference)（pr#1009@Github）
* 【core  】     Assert增加方法，支持自定义异常处理（pr#154@Gitee）
* 【core  】     BooleanConverter增加数字转换规则（issue#I1R2AB@Gitee）
* 【poi   】     sax方式读取增加一个sheet结束的回调（issue#155@Gitee）
* 【db    】     增加BeeCP连接池支持
* 【core  】     改进Img.pressImage方法，避免变色问题（issue#1001@Github）

### Bug修复#
* 【core  】     修复原始类型转换时，转换失败没有抛出异常的问题
* 【core  】     修复BeanUtil.mapToBean中bean的class非空构造无法实例化问题
* 【core  】     修复NamedSql多个连续变量出现替换问题
* 【core  】     修复Bean重名字段（大小写区别）获取数据出错的问题（issue#I1QBQ4@Gitee）
* 【http  】     修复SimpleServer响应头无效问题（issue#1006@Github）
* 【core  】     修复ThreadLocalRandom共享seed导致获取随机数一样的问题（pr#151@Gitee）

-------------------------------------------------------------------------------------------------------------

# 5.3.11 (2020-08-01)

### 新特性
* 【captcha】     AbstractCaptcha增加getImageBase64Data方法（pr#985@Github）
* 【core   】     增加PhoneUtil（pr#990@Github）
* 【core   】     改进Img，目标图片类型未定义使用源图片类型（issue#I1PB0B@Gitee）
* 【json   】     JSONConfig增加Transient选项（issue#I1PLHN@Gitee）
* 【core   】     MapUtil增加getXXX的默认值重载（issue#I1PTGI@Gitee）
* 【core   】     CalendarUtil增加parseByPatterns方法（issue#993@Github）

### Bug修复#

-------------------------------------------------------------------------------------------------------------

## 5.3.10 (2020-07-23)

### 新特性
* 【db   】       增加DbUtil.setReturnGeneratedKeyGlobal（issue#I1NM0K@Gitee）
* 【core 】       增加DataSize和DataSizeUtil（issue#967@Github）
* 【core 】       ImgUtil增加异常，避免空指针（issue#I1NKXG@Gitee）
* 【core 】       增加CRC16算法若干（pr#963@Github）
* 【core 】       LocalDateTimeUtil增加format等方法（pr#140@Gitee）
* 【http 】       UserAgentUtil增加Android原生浏览器识别（pr#975@Github）
* 【crypto 】     增加ECIES算法类（issue#979@Github）
* 【crypto 】     CollUtil增加padLeft和padRight方法（pr#141@Gitee）
* 【core 】       IdCardUtil香港身份证去除首字母校验（issue#I1OOTB@Gitee）

### Bug修复
* 【core   】     修复ZipUtil中finish位于循环内的问题（issue#961@Github）
* 【core   】     修复CollUtil.page未越界检查的问题（issue#I1O2LR@Gitee）
* 【core   】     修复StrUtil.removeAny的bug（issue#977@Github）

-------------------------------------------------------------------------------------------------------------

## 5.3.9 (2020-07-12)

### 新特性
* 【core   】     DateUtil增加formatChineseDate（pr#932@Github）
* 【core   】     ArrayUtil.isEmpty修改逻辑（pr#948@Github）
* 【core   】     增强StrUtil中空判断后返回数据性能（pr#949@Github）
* 【core   】     deprecate掉millsecond，改为millisecond（issue#I1M9P8@Gitee）
* 【core   】     增加LocalDateTimeUtil（issue#I1KUVC@Gitee）
* 【core   】     Month增加getLastDay方法
* 【core   】     ChineseDate支持到2099年

### Bug修复
* 【core   】     修复NumberUtil.partValue有余数问题（issue#I1KX66@Gitee）
* 【core   】     修复BeanUtil.isEmpty不能忽略static字段问题（issue#I1KZI6@Gitee）
* 【core   】     修复StrUtil.brief长度问题（pr#930@Github）
* 【socket 】     修复AioSession构造超时无效问题（pr#941@Github）
* 【setting】     修复GroupSet.contains错误（pr#943@Github）
* 【core   】     修复ZipUtil没有调用finish问题（issue#944@Github）
* 【extra  】     修复Ftp中ArrayList长度为负问题（pr#136@Github）
* 【core   】     修复Dict中putAll大小写问题（issue#I1MU5B@Gitee）
* 【core   】     修复POI中sax读取数字判断错误问题（issue#931@Github）
* 【core   】     修复DateUtil.endOfQuarter错误问题（issue#I1NGZ7@Gitee）
* 【core   】     修复URL中有空格转为+问题（issue#I1NGW4@Gitee）
* 【core   】     修复CollUtil.intersectionDistinct空集合结果错误问题
* 【core   】     修复ChineseDate在1996年计算错误问题（issue#I1N96I@Gitee）

-------------------------------------------------------------------------------------------------------------

## 5.3.8 (2020-06-16)

### 新特性
* 【core   】     增加ISO8601日期格式（issue#904@Github）
* 【setting】     Props异常规则修改（issue#907@Github）
* 【setting】     增加GIF支持
* 【core   】     复制创建一个Bean对象, 并忽略某些属性(pr#130@Gitee)
* 【core   】     DateUtil.parse支持更多日期格式(issue#I1KHTB@Gitee)
* 【crypto 】     增加获取密钥空指针的检查(issue#925@Github)
* 【core   】     增加StrUtil.removeAny方法(issue#923@Github)
* 【db     】     增加部分Connection参数支持(issue#924@Github)
* 【core   】     FileUtil增加别名方法(pr#926@Github)
* 【poi    】     ExcelReader中增加read重载，提供每个单元格单独处理的方法(issue#I1JZTL@Gitee)

### Bug修复
* 【json   】     修复append方法导致的JSONConfig传递失效问题（issue#906@Github）
* 【core   】     修复CollUtil.subtractToList判断错误（pr#915@Github）
* 【poi    】     修复WordWriter写表格问题（pr#914@Github）
* 【core   】     修复IoUtil.readBytes缓存数组长度问题（issue#I1KIUE@Gitee）
* 【core   】     修复BigExcelWriter多次flush导致的问题（issue#920@Github）
* 【extra  】     绕过Pinyin4j最后一个分隔符失效的bug（issue#921@Github）

-------------------------------------------------------------------------------------------------------------

## 5.3.7 (2020-06-03)

### 新特性
* 【core   】     ThreadFactoryBuilder的setUncaughtExceptionHandler返回this（issue#I1J4YJ@Gitee）

### Bug修复
* 【core   】     修复DateUtil.parse解析2020-5-8 3:12:13错误问题（issue#I1IZA3@Gitee）
* 【core   】     修复Img.pressImg大小无效问题(issue#I1HSWU@Gitee)
* 【core   】     修复CronUtil.stop没有清除任务的问题(issue#I1JACI@Gitee)

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
* 【core   】     BeanValueProvider转换失败时，返回原数据，而非null
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
