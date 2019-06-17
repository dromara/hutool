
# Changelog

-------------------------------------------------------------------------------------------------------------

## 4.5.12

### 新特性
* 【json】          解析JSON字符串去除两边空白符(同时解决字符串中bom问题（issue#381@Github）
* 【poi】           Sax解析增加在异常后关闭文件的逻辑（issue#IXBOU@Gitee）
* 【core】         MapUtil增加get重载（TypeReference）（issue#IXL81@Gitee）
* 【crypto】      RC4增加encryptHex和encryptBase64方法（issue#387@Github）
* 【core】         DateUtil.parse增加格式（issue#385@Github）
* 【core】         增加CollUtil.containsAny（感谢【北京】宁静）
* 【core】         增加CollUtil.keySet和values（issue#IXYQJ@Gitee）

### Bug修复
* 【poi】           解决三目运算符导致类型转换问题（issue#385@Github）
* 【core】         解决NumberUtil.decimalFormatMoney格式错误问题（issue#391@Github）

-------------------------------------------------------------------------------------------------------------

## 4.5.11

### 新特性
* 【core】           DateUtil.parse方法识别时间增强（issue#IWMM6@Gitee）
* 【extra】           Mail中Files附件可选为空（issue#365@Github）
* 【extra】           EmojiUtil增加containsEmoji方法（pr#373@Github）
* 【core】            Convert.toDBC()增加空校验（issue#369@Github）

### Bug修复
* 【core】           修复NumberUtil.decimalFormatMoney只有整数的bug（issue#IWKVL@Gitee）
* 【bloomFilter】 修复BitMapBloomFilter构造数bug（issue#IWMIN@Gitee）
* 【extra】           MailUtil.send方法传入自定义Setting失效问题（感谢@【上海】康）
* 【core】           修复NetUtil.localIpv4s方法名，改为localIps（issue#IWS2C@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.5.10

### 新特性
* 【extra】         修改MailUtil中的逻辑，默认为非单例邮件客户端（issue#IWFRQ@Gitee）

### Bug修复
* 【http】          修复HttpUtil.toParams方法某些符号未转义问题（issue#356@Github）
* 【captcha】    修复验证码被遮挡问题（issue#IWERW@Gitee）
* 【poi】           修复readBySax重复问题（issue#IVKLQ@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.5.9

### 新特性
* 【core】          修改Singleton单例策略，IdUtil增加getSnowflake（issue#IWA0G@Gitee）
* 【core】          增加RandomUtil.randomBoolean（issue#351@Github）
* 【core】          增加Base62实现，Base62类

### Bug修复
* 【json】          修复JSON中含有日期导致的时间戳包含双引号问题

-------------------------------------------------------------------------------------------------------------

## 4.5.8

### 新特性
* 【cron】          CronPatternUtil增加nextDateAfter方法（issue#IVYNL@Github）
* 【core】          增加RandomUtil.randomDate方法（issue#IW49T@Github）
* 【db】             Table增加comment字段，调整元信息逻辑（issue#IW49S@Gitee）
* 【core】          增加ConcurrencyTester（pr#41@Gitee）
* 【core】          ZipUtil增加对流的解压支持（issue#IW798@Gitee）

### Bug修复
* 【core】          修复Enjoy模板创建多个引擎报错问题（issue#344@Github）
* 【crypto】       修复Linux下RSA/ECB/PKCS1Padding算法无效问题
* 【core】          修复ImgUtil.scale方法操作png图片透明失效问题（issue#341@Github）
* 【core】          修复JSON自定义日期格式无引号问题（issue#IW4F6@Gitee）
* 【core】          修复Android下CallerUtil.getCallerCaller空指针问题（issue#IW68U@Gitee）
* 【cache】        修复Cache中超时太大导致Long越界问题（issue#347@Github）

-------------------------------------------------------------------------------------------------------------

## 4.5.7

### 新特性
* 【core】          新增StrClipboardListener（issue#325@Github）
* 【core】          新增DesktopUtil（issue#326@Github）
* 【core】          CollUtil.getFieldValues增加可选是否忽略null值（issue#IVGEE@Gitee）
* 【http】          新增SoapUtil，SoapClient支持返回SOAPMessage
* 【core】         RobotUtil增加鼠标相关操作
* 【core】         增加DateModifier，DateUtil增加truncate和ceiling方法（issue#IVL9A@Gitee）
* 【core】         PageUtil增加getStart（issue#IVN0C@Gitee）
* 【core】         CopyOptions增加ignoreXXX方法（感谢@【南昌】...）
* 【core】         ObjectUtil增加isEmpty方法（感谢@【成都】AliK）

### Bug修复
* 【core】          修复PatternPool中的URL_HTTP不支持端口的问题（issue#IVF1V@Gitee）
* 【extra】         修复JschUtil.exec多次connect的问题（issue#339@Github）
* 【http】          修复SoapUtil.toString乱码问题（pr#337@Github）
* 【http】          解决Cookie不规范导致的请求响应失败问题（issue#336@Github）
* 【setting】      GroupedMap增加读写锁解决并发问题（issue#336@Github）
* 【json】          修复JSONArray中add方法导致覆盖问题（感谢@【江门】小草哥）
* 【core】         修复Convert对泛型支持不完善的问题（issue#IVMD5@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.5.6

### 新特性
* 【http】           SoapClient增加setParams，增加构造使用默认的namespaceURI方法
* 【core】          FileUtil增加cleanEmpty方法（issue#319@Github）
* 【core】          增加ClipboardMonitor（issue#320@Github）
* 【http】          SoapClient增加部分方法
* 【http】          HttpRequest增加setConnectionTimeout和setReadTimeout（issue#322@Github）
* 【core】         Console增printPrograss
* 【core】         DateBetween增加null校验（issue#IVC23@Gitee）
* 【core】         增加CollUtil.getFieldValues重载（issue#IV96S@Gitee）
* 【db】           SqlExecutor和Db增加executeBatch重载，支持批量SQL（issue#324@Github）

### Bug修复
* 【bloomFilter】修复负数导致的问题（issue#IV6X6@Gitee）
* 【setting】       修复Props监听问题
* 【json】           修复TypeUtil中空指针导致的注入失败问题（issue#IVCLW@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.5.5

### 新特性

### Bug修复
* 【core】     Assert中NullPointerException改为IllegalArgumentException（issue#IV41L@Gitee）
* 【core】     修复创建新sheet时比较器未清空导致的顺序问题（issue#318@Github）

-------------------------------------------------------------------------------------------------------------

## 4.5.4

### 新特性
* 【core】     NetUtil增加getUsableLocalPort方法，并迁移至cn.hutool.core.net包
* 【core】     FileUtil增加isSub方法（pr#39@Gitee）
* 【core】     增加VoidFunc
* 【extra】     mail适配mail.setting和config/mail.setting双配置文件（感谢@【江门】小草哥）
* 【corn】     cron适配cron.setting和config/cron.setting双配置文件（感谢@【江门】小草哥）
* 【poi】       ExcelWriter增加autoSizeColumnAll方法，ExcelBase增加getColumnCount、getRowCount方法（感谢@@【长沙】M）
* 【http】      添加SoapClient，删除SoapRequest

### Bug修复
* 【db】        修复Session中事务问题（issue#IUQMN@Gitee）
* 【db】        修复Db中关闭逻辑错误导致的事务问题（感谢@【宁波】mojie126）
* 【http】      修复form方法使用Resource可能导致的空指针问题
* 【crypto】   修复SM2Engine逻辑错误（感谢bcgit/bc-java）

-------------------------------------------------------------------------------------------------------------

## 4.5.3

### 新特性
* 【core】       Simhash添加读写锁（issue#IUF9O@Gitee）
* 【core】       Img增加round方法，圆角给定图片
* 【extra】      二维码中的图片做圆角处理
* 【core】       CsvData实现Iterable接口
* 【extra】      Ftp增加重连方法（pr#38@Gitee）
* 【extra】      Velocity升级至2.x，不再兼容1.7

### Bug修复
* 【core】       修复ReflectUtil新建Map对象错误问题（issue#IUF9O@Gitee）
* 【core】       修复ImgUtil字体为null导致的空指针问题（issue#IUF3X@Gitee）
* 【extra】      修复Ftp中文件上传mkdirs方法创建多余文件夹的问题（issue#ITAYV@Gitee）
* 【extra】      修复Ftp中文件上传mkdirs方法创建多余文件夹的问题（issue#ITAYV@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.5.2

### 新特性
* 【crypto】    增加读取pem格式私钥文件和公钥证书的方法，位于BCUtil（issue#ISJ5M@Gitee）
* 【core】       增加StrUtil.byteLength（issue#284@Github）
* 【core】       增加GlobalBouncyCastleProvider，单例使用BouncyCastleProvider
* 【crypto】    增强对BC库的兼容性，明确RSA为RSA/ECB/PKCS1Padding
* 【core】       snowflake生成器添加id反推生成时间等信息的方法（pr#293@Github）
* 【poi】         CellUtil.getCellValue增加null验证
* 【core】       增加文件内容跟随器Tailer
* 【crypto】    增加RC4算法
* 【core】       增加FixedLinkedHashMap
* 【extra】       增加ChannelType，JschUtil增加createSession、createChannel、openChannel等方法
* 【core】       WatchUtil增加createModify
* 【core】       新增ImgUtil，废弃ImageUtil

### Bug修复
* 【core】       修复ExceptionUtil（pr#35@Gitee）
* 【core】       修复RandomUtil注释标注问题（pr#288@Github）
* 【core】       修复TimedCache中onRemove失效问题（issue#ITD0O@Gitee）
* 【core】       修复DateConverter日期负数问题（issue#ITWK4@Gitee）
* 【json】       修复toBean时父类定义泛型字段导致的注入问题（issue#ITGGN@Gitee）
* 【cahce】    修复读锁导致的LRU异常（issue#303@Gtihub）
* 【captcha】 修复在某些未知情况下获取字体高度导致的问题

-------------------------------------------------------------------------------------------------------------

## 4.5.1

### 新特性
* 【socket】     socket模块加入到all中
* 【core】        增加Jdk8DateConverter用于支持jdk8中的时间（issue#IS32N@Gitee）
* 【core】        StrUtil.subPreGbk优化代码规范（pull#277@Github）
* 【crypto】      MD5支持16位值生成
* 【crypto】      Digester支持自定义盐所在位置
* 【captcha】    增加算数计算类验证码（issue#282@Github）

### Bug修复
* 【json】         修复JSON中toString导致的中文引号被转义问题（感谢@【内蒙】程序员）
* 【core】        修复15位身份证生日校验问题（issue#ISBUO@Gitee）
* 【extra】        修复部分模板引擎classpath路径获取失败问题

-------------------------------------------------------------------------------------------------------------

## 4.5.0

### 新特性
* 【socket】     增加Socket模块
* 【core】        Validator增加isIpV4方法（issue#IRQ6W@Gitee）
* 【crypto】     增加SM2Engine，支持C1C2C3和C1C3C2两种模式
* 【core】       StrUtil.splitTrim支持其它空白符（issue#IRVPC@Gitee）
* 【http】        请求支持DELETE附带参数模式（issue#IRW9E@Gitee）
* 【bloomFilter】调整BitMap注释

### Bug修复
* 【crypto】     修复KeyUtil中使用BC库导致的其它密钥生成异常
* 【core】        修正DateUtil.formatHttpDate方法
* 【extra】        修复FTP.ls无法遍历文件问题（issue#IRTA3@Gitee）
* 【extra】        修复QrCodeUtil中ratio参数失效问题，调整默认纠错为M（感谢@【上海】皮皮今）
* 【core】        修复FileTypeUtil对jpg文件识别问题（issue#275@Github）
* 【cache】      修复cache使用读锁导致的删除节点并发问题（issue#IRZTL@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.4.5

### 新特性
* 【core】        增加StrFormater代码逻辑可读性（pr#269@Github）
* 【core】        Validator中使用泛型
* 【core】        NumberUtil增加toBytes和toInt方法
* 【core】        XmlUtil增加format方法，支持缩进
* 【http】         SoapRequest增加executeBody方法（issue#IRN6I@Gitee）
* 【core】        调整XmlUtil.toStr方法对编码的逻辑

### Bug修复
* 【core】        修复AnnotationUtil.getAnnotationValue获取对象错误问题（issue#271@Github）

-------------------------------------------------------------------------------------------------------------

## 4.4.4

### 新特性
* 【crypto】     增加EC公钥压缩/解压缩（pr#264@Github）
* 【db】          Entity支持IS NOT NULL形式，调整逻辑，强化Condition的toString（issue#267@Github）

### Bug修复
* 【core】        修复Profile中路径参数失效问题（issue#265@Github）
* 【core】        修复MapConvert中值类型转换错误的问题（issue#268@Github）

-------------------------------------------------------------------------------------------------------------

## 4.4.3

### 新特性
* 【crypto】     MD5以及Digester增加加盐支持（issue#256@Github）
* 【crypto】     整理KeyUtil，减少冗余代码
* 【core】        增加Zodiac类，DateUtil增加getZodiac、getChineseZodiac用于获取星座和生肖（issue#260@Github）

### Bug修复
* 【core】        修复ExceptionUtil.stacktraceToString中limit参数无效问题（issue#IR7UE@Gitee）
* 【core】        修复StrUtil.repeatByLength中数组越界问题（issue#IRB2C@Gitee）
* 【core】        修复FileUtil.remove移动后删除失败问题（issue#IRF8R@Gitee）
* 【extra】       修复Ftp中delDir逻辑导致的问题（issue#IRCQ8@Gitee）
* 【core】        修复XmlUtil.mapToXml中map值为空导致的空指针问题。（issue#IRD7X@Gitee）
* 【poi】          修复ExcelWriter中setOnlyAlias没有排除值的问题。（issue#IRF9L@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.4.2

### 新特性
* 【core】        JSON中添加getStrEscaped方法，并修改原getStr逻辑，不再自动转义（issue#IR7SW@Gitee）
* 【core】        CLassLoaderUtil增加getJarClassLoader和loadClass重载方法（issue#IR94T@Gitee）
* 【crypto】     SM2密钥生成曲线修改为使用sm2p256v1（pr#249@Github）
* 【json】         JSONUtil增加空判断（issue#253@Github）
* 【core】        改进HexUtil.isHexNumber（issue#254@Github）
* 【http】        HttpRequest增加getConnection方法（issue#251@Github）

### Bug修复
* 【core】        修复URL转义问题（issue#IR6QP@Gitee）
* 【core】        修复WeightRandom权重为0的对象问题（issue#252@Github）

-------------------------------------------------------------------------------------------------------------

## 4.4.1

### 新特性
* 【core】        增加Rot（回转N位简易替换密码）、凯撒密码和莫尔斯电码
* 【crypto】     增加Vigenere密码
* 【db】          增加达梦7的驱动识别
* 【extra】       TemplateEngine适配更广泛的参数类型
* 【core】       HexUtil增加toHex方法，增加CRC8和CRC16（issue#IQWNB@Gitee）
* 【http】        添加text/xml ContentType（pr#31@Gitee）
* 【core】        Img、ImageUtil增加Resource和Path参数支持
* 【extra】       ServletUtil.getClientIP增加注释，提示IP伪造风险
* 【poi】          增加Word07Writer
* 【crypto】     增加KeyUtil，SecureUtil中的密钥生成迁移至此工具类中
* 【core】        增加URLEncoder（自行实现解决空格转义问题），HttpUtil废弃encode和decode方法

### Bug修复
* 【poi】          解决ExcelWriter中setSheet报错问题（issue#235@Github）
* 【crypto】     解决SecureUtil.readCertificate密码无效问题（issue#240@Github）
* 【json】        修复JSONUtil.toList针对对象中的类无法实例化导致的null问题（issue#239@Github）
* 【db】          修复MongoDS在Single模式下检查配置文件导致的问题（issue#IR2BF@Github）

-------------------------------------------------------------------------------------------------------------

## 4.4.0

### 新特性
* 【core】        增加MurmurHash（Murmur3算法实现），HashUtil增加murmur32、murmur64、murmur128方法
* 【core】        增加Simhash（用于海量文本去重）
* 【extra】        增加分词封装，封装了ansj、HanLP、IKAnalyzer、Jcseg、Jieba、MMSeg、Lucene-analysis、Word的实现，统一了接口
* 【core】        去除NumberUtil.parseInt和parseLong的8进制支持（issue#234@Github）
* 【extra】        Template部分修改命名减少歧义（Engine->TemplateEngine，EngineFactory->TemplateFactory）
* 【poi】          ExcelWriter中Map支持alias（issue#IQISU@Gitee）

### Bug修复

## 4.3.3

### 新特性
* 【poi】          ExcelWriter增加write重载，可选强制加标题（感谢@【北京】大熊）
* 【core】        ExceptionUtil增加isFromOrSuppressedThrowable（pr#29@Gitee）
* 【core】        ExceptionUtil增加convertFromOrSuppressedThrowable（pr#30@Gitee）
* 【crypto】     非对称和SM2构造传入的私钥和公钥支持Hex和Base64自动识别

### Bug修复
* 【core】        修复padAfter和padPre结果错误问题（issue#IQANO@Gitee）
* 【crypto】     修复SM2签名验证异常（issue#IQAY0@Gitee）
* 【extra】       修复Freemarker字符串模板无效问题（issue#231@Github）
* 【core】        修复StrUtil.strip问题（issue#232@Github）

-------------------------------------------------------------------------------------------------------------

## 4.3.2

### 新特性
* 【core】        StrUtil增加equalsAny和equalsAnyIgnoreCase方法（issue#IPUQK@Gitee）
* 【http】         StrUtil增加equalsAny和equalsAnyIgnoreCase方法（issue#223@Github）
* 【http】         StrUtil增加padPre、padAfter、center方法（issue#IPWR0@Gitee）
* 【core】         ImageUtil增加compress方法（issue#IPYIF@Gitee）
* 【core】         ReflectUtil增加getMethodByName、getMethodByNameIgnoreCase（issue#IQ2BO@Gitee）
* 【crypto】      增加SmUtil国密算法工具类（issue#225@Github）
* 【crypto】      增加SM2非对称加密（issue#225@Github）
* 【db】           增加AbstractDSFactory，减少冗余代码
* 【json】         JSONUtil.toBean增加可选是否忽略错误（issue@227@Gtihub）

### Bug修复
* 【core】         修复FileUtil.lastIndexOfSeparator空指针问题（issue#IPXPK@Gitee）
* 【core】         修复ArrayUtil.newArray泛型问题
* 【core】         修复CsvWriter循环调用问题（issue#IQ8T6@Gitee）
* 【poi】           修复ExcelReader读取Map空头导致的问题（issue#IQ6F2@Gitee）
* 【db】            修复Driver识别导致的SQL Server方言异常（issue#IQ687@Gitee）
* 【core】         修复Number.isInteger和isLong判断问题（issue#229@Github）

-------------------------------------------------------------------------------------------------------------

## 4.3.1

### 新特性
* 【core】        新增DateUtil.dateNew方法（issue#217@Github）
* 【extra】       JschUtil.exec增加重载，可选错误输出（issue#IPNAB@Gitee）
* 【core】       增加NoLock（issue#218@Github）
* 【core】       QrCode.decode改进
* 【core】       合并无必要的构造方法
* 【setting】    Setting.getMap方法在分组不存在时返回空Map而非null（issue#IPU2X@Gitee）

### Bug修复
* 【db】          解决数据源识别错误问题（issue#IPNI7@Gitee）
* 【core】       修复DateField.of缺失字段问题（issue#IPP51@Gitee）
* 【core】       JSONObject中忽略空值失效问题（issue#221@Github）

-------------------------------------------------------------------------------------------------------------

## 4.3.0

### 新特性
* 【core】        增加TypeReference类（issue#IPAML@Gitee）
* 【json】         支持TypeReference类转换，并对toBean逻辑做了大量变动（issue#IPAML@Gitee）
* 【core】        ArrayUtil.get和CollUtil.get返回null而非空指针（issue#IPKZO@Gitee）

### Bug修复
* 【extra】        修复VelocityEngine中模板中文乱码问题（issue#216@Github）

-------------------------------------------------------------------------------------------------------------

## 4.2.2

### 新特性
* 【json】        JSONObject调整构造方法，支持对象转为JSON可选是否有序（issue#IP1Q2@Gitee）
* 【core】        BeanUtil增加hasGetter和hasSetter方法
* 【core】        StrUtil增加isUperCase和isLowerCase方法，增加removeAll和removeAllLineBreaks（issue#IP7PT@Gitee）
* 【db】          增加PostgreSQL的单元测试
* 【core】       ArrayUtil增加sub方法泛型支持
* 【core】       从Apache-commons-lang3移植Builder（issue#IPALY@Gitee）
* 【core】       增加Func1接口，ReUtil和StrUtil增加Func1参数的replace方法（pr#27@Gitee）
* 【db】         Table增加getColumn方法，Column补充注释（issue#209@Github）

### Bug修复
* 【cron】        修复L代表的最后一天无效问题（issue#IP5PB@Gitee）
* 【core】        修复验证15位身份证月的判断问题（issue#IP70D@Gitee）
* 【poi】          修复多次调用write方法写出多个标题问题（issue#212@Github）
* 【extra】       修复模板写出文件空白问题（issue#208@Github）

-------------------------------------------------------------------------------------------------------------

## 4.2.1

### 新特性
* 【extra】       增加基于emoji-java的EmojiUtil
* 【http】        增加User-agent解析
* 【crypto】     引入bouncycastle从而对国密SM2、SM3、SM4支持
* 【poi】          新增ExcelFileUtil，改进错误提示

### Bug修复

-------------------------------------------------------------------------------------------------------------

## 4.1.22

### 新特性
* 【core】        BeanUtil.copyProperties方法支持目标为Map（issue#IOQHZ@Gitee）
* 【poi】          ExcelWriter增加方法setOnlyAlias，用于特定字段剔除（issue#IOOVK@Gitee）
* 【captcha】   增加setBackground方法（issue#200@Github）
* 【core】        NetUtil增加idnToASCII方法（issue#201@Github）
* 【log】          增加JBoss-Logging支持（issue#IOVS1@Gitee）
* 【http】        增加URL标准化，从而支持非http开头的URL字符串

### Bug修复
* 【core】        修复Validator.isBirthday

-------------------------------------------------------------------------------------------------------------

## 4.1.21

### 新特性
* 【core】        RuntimeUtil增加getErrorResult方法（issue#199@Github）
* 【core】        ReflectUtil增加hasField方法（感谢@【杭州】J辉）
* 【core】        BeanUtil增加toBean方法（感谢@【杭州】J辉）
* 【db】           增加对HSQLDB支持，改进Driver自定识别

### Bug修复
* 【core】        修复EnumUtil.getFieldNames定义name属性重复问题（感谢@【杭州】J辉）
* 【json】         修复List多层嵌套toBean转换失败问题
* 【core】        修复ObjectUtil.toString问题（issue#IONLA@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.1.20

### 新特性
* 【http】        增强SoapRequest的兼容性（感谢@【南京】陽光）
* 【core】        改进ZipUtil错误提示
* 【core】        DateUtil.parse方法读取时间时，年月日按照当天计算。（issue#INYCF@Gitee）
* 【core】        DateUtil.parse改进支持UTC时间格式。
* 【db】          MongoDS支持客户端验证（issue#IO2DS@Gitee）
* 【core】        改进字符串转集合和数组（支持逗号分隔形式）（pr#26@Gitee）
* 【core】        改进DateConverter（issue#IOCWR@Gitee）
* 【core】        改进NumberUtil中转数字，支持字母结尾（issue#IOCWR@Gitee）
* 【poi】          ExcelUtil增加indexToColName和colNameToIndex方法（issue#IO8ZH@Gitee）
* 【core】        Convert.toList修改为泛型（issue#IOJZV@Gitee）
* 【core】        BeanDesc中属性修改为使用LinkedHashMap存储
* 【core】        ArrayUtil.get和CollUtil.get对于越界返回null而非抛出异常（issue#IOFKL@Gitee）
* 【core】        EnumUtil增加likeValueOf方法（issue#IOFKL@Gitee）
* 【core】        删除CollUtil.sortPageAll2方法，增加ColllUtil.page方法

### Bug修复
* 【core】        修正CollUtil.sortPageAll逻辑（pr#186@Github）
* 【core】        修复ClassLoaderUtil.loadClass不能加载内部类问题（issue#IO4GF@Gitee）
* 【core】        修复CustomKeyLinkedMap继承问题（issue#IO5Y2@Gitee）
* 【core】        修复NumberUtil.isPrimes没有参数校验导致的问题（issue#IO57Q@Gitee）
* 【extra】       修复QrConfig 引入包错误问题（pr#194@Github）
* 【extra】       修复Sftp创建目录问题（issue#INZUP@Gitee）
* 【core】        修复CollUtil.sortPageAll方法
* 【core】        修复ImageUtil图片旋转出现黑边问题（pr#189@Github）

-------------------------------------------------------------------------------------------------------------

## 4.1.19

### 新特性
* 【extra】        Ftp增加setMode方法（issue#INPMZ@Gitee）
* 【core】         IdUtil增加fastUUID和fastSimpleUUID方法（issue#INU37@Gitee）
* 【core】         DateUtil增加formatChineseDate方法（issue#INT6I@Gitee）
* 【core】         ClassUtil中部分方法迁移至ReflectUtil
* 【json】          新增JSONConfig，统一JSON配置，并添加可选的自定义输出日期格式支持

### Bug修复
* 【core】        修复ImageUtil文件流未关闭问题（感谢@【西安】追寻）
* 【core】        修复ZipUtil中gzip和zlib方法未调用finish导致的问题（issue#INSXF@Gitee）
* 【core】        修复ZipUtil中文件目录同名无法压缩的问题（issue#INQ1K@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.1.18

### 新特性
* 【http】         改进字符串匹配正则（issue#INHPD@Gitee）
* 【core】        增加gzip和UnGzip针对流的方法（issue#INKMP@Gitee）
* 【http】         增加ThreadLocalCookieStore

### Bug修复
* 【core】        修复BeanUtil.copyProperties参数多余问题
* 【cron】        修复表达式匹配错误问题（issue#INLEE@Gitee）
* 【core】        修复ReflectUtil获取空参数方法导致的问题（issue#INN5W@Gitee）
* 【json】         修复JSONArray.toList方法导致的问题（issue#INO3F@Gitee）
* 【core】        修复NumberUtil.parseLong中0转换问题方法导致的问题（issue#INO3F@Gitee）
* 【core】        修复CompareUtil循环引用问题（issue#180@Github）

-------------------------------------------------------------------------------------------------------------

## 4.1.17

### 新特性

### Bug修复
* 【core】         修复JDK7之后比较器中违反自反性导致的问题
* 【cron】         修改部分逻辑

-------------------------------------------------------------------------------------------------------------

## 4.1.16

### 新特性
* 【core】         Convert.增加boolean类型转数字（issue#INCKM@Gitee）
* 【core】         新增BooleanUtil

### Bug修复
* 【core】         修复JDK11下Caller被弃用导致的问题（issue#174@Github）

-------------------------------------------------------------------------------------------------------------

## 4.1.15

### 新特性
* 【core】         Convert.toInt增加容错，NumberUtil增加toNumber方法（issue#IN2LP@Gitee）
* 【core】         ImageUtil增加cut切圆形方法（issue#IN3JJ@Gitee）
* 【core】         Img增加setPositionBaseCentre可选坐标计算基于中心（issue#IN3JM@Gitee）
* 【core】         ImageUtil增加逻辑判断颜色模式，避免失色问题（issue#IN3JK@Gitee）
* 【cron】         改进规则支持20/2这类形式
* 【extra】         ServletUtil.write增加重载方法支持文件（issue#IN9O0@Gitee）

### Bug修复
* 【core】         修复DateUtil.yearAndQuarter计算错误的问题（issue#IN38V@Gitee）
* 【core】         修复ClassUtil.isPublic判断问题（issue#IN38V@Gitee）
* 【extra】        修复JschUtil中Session关闭未移除出池导致的问题（issue#171@Github）
* 【core】        修复NumberUtil.isInteger中0判断问题（issue#IN9BS@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.1.14

### 新特性
* 【core】         StrUtil增加hide方法
* 【core】         PatternPool增加URL_HTTP，原URL规则变更
* 【extra】        统一FTP和SFTP接口规范
* 【extra】        QrCodeUtil支持二维码中贴Logo图片
* 【core】         校准ImageUtil.pressText文字位置
* 【core】         ImageUtil增加getColor等方法
* 【core】         增加RobotUtil提供截屏等封装，增加ScreenUtil用于获取屏幕属性
* 【extra】        QrCodeUtil增加条形码等其它类型支持（issue#IN1CR@Gitee）
* 【core】         增加DateUtil.parseUTC方法（issue#IN1IO@Gitee）
* 【core】         增加DateUtil.isWeekend方法
* 【all】            加入Travis-CI验证项目构建

### Bug修复
* 【core】         修复ImageUtil.convert转换png变色问题（issue#IMWUO@Gitee）
* 【core】         修复FileUtil.newerThan中null判断的问题（issue#165@Github）
* 【extra】        修复Ftp中mkdir方法引起的数组越界问题

-------------------------------------------------------------------------------------------------------------

## 4.1.13

### 新特性
* 【core】         增加RejectPolicy线程池线程拒绝策略枚举
* 【core】         DateUtil增加isSame方法
* 【core】         FileUtil.getAbsolutePath方法在获取不到ClassPath情况下返回原路径
* 【core】         打印SQL日志覆盖每一个方法
* 【core】         Convert.toXXX转数字的时候默认去除两边空白符
* 【poi】           增加BigExcelWriter，支持Excel大数据导出（issue#IK47S@Gitee）
* 【core】         ExceptionUtil增加isCausedBy和getCausedBy方法
* 【poi】           EnumUtil增加toString和fromString
* 【poi】           新增IdUtil工具类

### Bug修复
* 【core】         修复RuntimeUtil.getResultLines未关闭Process问题（pr#164@Github）
* 【core】         修复ClassPathResource在jar运行模式下的空指针问题

-------------------------------------------------------------------------------------------------------------

## 4.1.12

### 新特性
* 【core】         ExcelReader.read方法返回的Map默认有序

### Bug修复
* 【core】         修复ZipUtil以及FileUtil中slip漏洞（issue#162@Github）
* 【core】         修复ZipUtil路径问题（issue#IMUEK@Gitee）
* 【core】         修复FileUtil.getParent方法获取父路径不严格导致空指针问题

-------------------------------------------------------------------------------------------------------------

## 4.1.11

### 新特性
* 【core】         Convert增加toList方法
* 【core】         StrUtil增加containsAny针对char的重载
* 【core】         FileUtil.mainName修正处理逻辑
* 【core】         CharUtil增加isFileSeparator方法
* 【core】         增加UUID类，提升Simple模式下性能
* 【poi】           ExcelUtil增加setStyleSet方法，修改write逻辑，对于单列数据输出，而非忽略（感谢@【宁波】mojie126）
* 【core】         新增WebAppResource类
* 【extra】        新增Thymeleaf模板支持
* 【setting】      去除Setting日志

### Bug修复
* 【script】        修复FullSupportScriptEngine构造中ext和mimeType方式获取引擎丢失问题
* 【cron】         修复定时任务执行阻塞问题

-------------------------------------------------------------------------------------------------------------

## 4.1.10

### 新特性
* 【extra】         Template增加Jfinal的Enjoy模板支持
* 【core】          Assert增加checkBetween方法，Validator增加isBetween和validatorBetween
* 【core】          增加CollUtil.getLast方法（感谢@【帝都】宁静）
* 【core】          修改Assert.notNull注释（issue#IMI3Z@Gitee）
* 【core】          BeanUtil增加isEmpty和hasNullField方法（pr#157@Github）
* 【log】            ConsoleLog增加setLevel方法（issue#IMLZ3@Gitee）
* 【captcha】     解决验证码超出背景的问题（issue#IHWHE@Gitee）

### Bug修复
* 【core】         修复BOMInputStream构造的问题（pr#22@Gitee）
* 【json】          修复toBean中如果字段中为字符串而JSON中为JSONObject对象注入失败问题（issue#IMGBJ@Gitee）
* 【setting】      修复keySet总返回空问题（issue#IMHD7@Gitee）
* 【extra】        修复starttls和SSL连接混淆问题（issue#IMLMD@Gitee）
* 【setting】      修复getStr无法获取默认值问题（issue#IMLMI@Gitee）
* 【core】         修复BeanUtil.mapToBean设置别名失效问题

-------------------------------------------------------------------------------------------------------------

## 4.1.9

### 新特性
* 【core】         MapUtil增加toObjectArray方法
* 【core】         URLUtil.normalize增加反斜杠处理（issue#IM8BI@Gitee）
* 【core】         增加ClassUtil.getShortClassName（issue#IM8XM@Gitee）
* 【core】         增加ThreadFactoryBuilder和ExecutorBuilder
* 【cron】         定时任务改为线程池实现
* 【core】         Assert增加checkIndex方法
* 【core】         parseBoolean增加on、off关键字支持可选字符串
* 【core】         URLUtil.formatUrl方法兼容更多情况（issue#IMAEA@Gitee）
* 【core】         改进NumberUtil.isInteger和isLong判断（issue#IMDGB@Gitee）
* 【http】          HttpResponse增加isOk方法（issue#155@Github）
* 【http】          改进HttpUtil.downloadXXX方法，返回非2XX抛出异常（issue#IMCTT@Gitee）
* 【http】          HttpRequest增加setUrlHandler方法（issue#IMD1X@Gitee）
* 【http】          HttpRequest增加getCookieManager和closeCookie方法（issue#IMDND@Gitee）

### Bug修复
* 【core】         修复IdcardUtil中isValidCard10空指针问题（issue#IMB7R@Gitee）
* 【core】         修复SoapRequest空指针问题（issue#IMBUN@Gitee）
* 【http】          修复文件上传没有关闭File的问题（issue#IMDUY@Gitee）
* 【json】          修复toBean中有Map参数导致的值丢失问题（issue#IMDEM@Gitee）
* 【bloomFilter】修复hash值负数问题（issue#154@Github）
* 【core】          修复Convert中Map强转导致的问题

-------------------------------------------------------------------------------------------------------------

## 4.1.8

### 新特性
* 【http】          HttpRequest增加getUrl、getMethod等方法
* 【core】         Validator增加isWord和ValidateWord（感谢@【帝都】宁静）
* 【core】         增加CollUtil.filter针对List的重载（issue#IM1NI@Gitee）
* 【core】         增加ImageUtil.toBase64
* 【http】          增加SoapRequest
* 【poi】           ExcelWriter增加renameSheet方法（issue#150@Github）
* 【core】         ZipUtil增加unzipFileBytes方法（issue#IM5KO@Gitee）
* 【aop】          加入Cglib实现的切面支持（issue#IM4Y2@Gitee）
* 【extra】         加入FTP客户端支持，基于commons-net封装

### Bug修复
* 【http】          修复编码自动识别的bug（issue#IM33O@Gitee）
* 【db】            修复Session中ds引起的空指针问题（感谢@【武汉】jellard）
* 【core】         修复ReflectUtil.newInstance二次调用资源问题（issue#IM51X@Gitee）
* 【core】         修复ClassScaner包名前缀引起的问题（issue#IM5OJ@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.1.7

### 新特性
* 【db】            SqlRunner被弃用

### Bug修复
* 【db】            修复Oracle分页问题（issue#ILZDA@Gitee）
* 【db】            Dialect使用单例

-------------------------------------------------------------------------------------------------------------

## 4.1.6

### 新特性
* 【core】         OptNullBasicTypeGetter增加getDate方法（issue#ILUQM@Gitee）
* 【core】         RuntimeUtil增加可选环境变量参数（issue#ILV2I@Gitee）
* 【core】         修改Caller结构

### Bug修复
* 【db】            修复Oracle分页多一条问题（issue#ILUQM@Gitee）
* 【poi】           修复ExcelWriter换行问题（issue#ILXLI@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.1.5

### 新特性
* 【poi】           ExcelWriter支持通过别名方式设置Bean写出的顺序（感谢@【武汉】zzz）
* 【db】            SQL日志打印扩展到所有SQL（感谢@【河北】理想主义）
* 【core】         增加FileUtil.copyFilesFromDir方法（issue#ILRLG@Gitee）
* 【core】         EscapeUtil.unescapeHtml4和EscapeUtil.escapeHtml4（issue#112@Github）
* 【http】          增加CustomProtocolsSSLFactory和AndroidSupportSSLFactory（pr#142@Github）
* 【setting】      添加SettingUtil（感谢@【杭州】t-io）
* 【bloomFilter】添加BloomFilterUtil
* 【core】          添加Img类

### Bug修复
* 【http】          修复body方法判断Content-Type失效问题（感谢@【上海】皮皮今）
* 【core】         修复FileUtil.copy方法在目标不存在的情况下报错问题
* 【core】         修复ClassScaner在Spring boot fat jar下扫描失败的问题（issue#IKDJW@Gitee）
* 【json】          修复JSONObject构造names列表为空导致的构造空对象（issue#143@Github ）
* 【core】         修复ImageUtil.pressText图片有黑边的问题（issue#141@Github）


-------------------------------------------------------------------------------------------------------------

## 4.1.4

### 新特性
* 【all】             补充package-info
* 【db】            增加方法SqlExecutor.callQuery(issue#ILJ0N@Gitee)
* 【core】          ExceptionUtil增加部分方法
* 【system】      SystemUtil增加部分方法
* 【core】          新增NamedThreadLocal（issue#ILJ0Z@Gitee）
* 【core】          ZipUtil新增Zlib压缩解压
* 【core】          NumberUtil增加parseInt和parseLong，支持10进制、8进制和16进制自动识别
* 【db】            Table继承自LinkedHashMap保证字段读出有序（感谢@【帝都】宁静）
* 【json】          JSONObject子类自动判断是否有序（感谢@【帝都】宁静）
* 【poi】           抽象ExcelBase，提取共用方法

### Bug修复
* 【http】          修复HttpRequest.setFollowRedirects无效问题（issue#ILIKG@Gitee）
* 【core】         修复CharUtil.isEmoji问题
* 【http】          修复HttpResponse.writeBody同步模式下写出失败问题
* 【http】          修复Cookie机制导致的部分Cookie信息不能在请求时附带的问题
* 【json】          修复JSONArray.toArray转换为原始类型导致的异常问题

-------------------------------------------------------------------------------------------------------------

## 4.1.3

### 新特性
* 【all】             优化db的DsFactory、log的LogFactory、extra的TemplateUtil逻辑，减少异常栈嵌套
* 【core】          Validator增加isMac、validateMac方法（感谢@【上海】阳仔）

### Bug修复
* 【core】          修复ArrayUtil.join前后fix失效问题（@【河北】理想主义）
* 【core】          修复DateRange最后一个元素逻辑问题（issue#ILE38@Gitee）
* 【cron】          修复调用CronUtil.stop()方法无法正常结束作业进程的问题（issue#ILFCZ@Gitee）
* 【db】             修复page方法在Oracle中丢失参数问题（issue#ILGXP@Gitee）
* 【extra】          修复QrCodeUtil.decode对复杂二维码解码失败问题（感谢@【成都】小朋友）

-------------------------------------------------------------------------------------------------------------

## 4.1.2

### 新特性
* 【core】          MapUtil增加getDate方法（感谢@【帝都】宁静）
* 【json】           putByPath方法增加容错性，支持下标越界识别为追加（issue#IKNM6@Gitee）
* 【core】          增加FileUtil.getParent方法（pr#18@Gitee）
* 【core】          ImageUtil.pressText增加抗锯齿（pr#19@Gitee）
* 【core】          BeanUtil.getPropertyDescriptors去除class属性（issue#IKVKR@Gitee）
* 【json】           putByPath方法针对空的规则变更（issue#IKX2H@Gitee）
* 【captcha】     增加CodeGenerator，可自定义验证码文字生成策略（issue#IL3YH@Gitee）
* 【core】          增加CollUtil.list方法，更灵活的创建ArrayList和LinkedList
* 【core】          DateTime增加时区支持（issue#131@Github）
* 【extra】         QrCodeUtil二维码生成支持设置边距、颜色等自定义项（issue#135@Github）

### Bug修复
* 【core】          修复JSONUtil.formatJsonStr引号换行问题（issue#IKMMK@Gitee）
* 【core】          修复URLUtil.getDecodedPath可能导致的空指针问题（issue#IKLRD@Gitee）
* 【core】          修复PinyinUtil.getAllFirstLetter非汉字显示问题（issue#IKM0P@Gitee）
* 【json】          修复当Bean为私有类时无法实例化导致的JSON转换问题（感谢@【上海】风景）
* 【json】          修复Bean中有Object字段时toBean产生的问题（感谢@【上海】风景）
* 【core】          修复XmlUtil关闭XXE避免XXE攻击
* 【poi】            修复Excel03SaxReader读取小数的问题（感谢@【深圳】rm -rf /）
* 【core】          修复CollUtil.findOne空参数导致的空指针问题（issue#133@Github）
* 【core】          修复JSONArray.addAll问题（pr#137@Github）
* 【core】          修复UnicodeUtil单独空格无法转换问题

-------------------------------------------------------------------------------------------------------------

## 4.1.1

### 新特性
* 【poi】            ExcelWriter写出bean使用LinkedHashMap
* 【core】          UnicodeUtil新增：1、\u大小写不区分，2、\u后跟非16进制按照非Unicode符对待，直接输出（issue#IKJGU@Gitee）
* 【crypto】       增加Bcrypt实现（参照：jBCrypt）
* 【core】          XXXIterator修改为XXXIter，同时实现Iterator和Iterable接口
* 【core】          Dict使用LinkedHashMap，Entity也是

### Bug修复
* 【setting】       修复store方法无换行问题
* 【core】          修复UnicodeUtil.toString方法不正确Unicode死循环问题（issue#IKJGU@Gitee）
* 【http】           修复HttpsURLConnectionOLDImpl导致的转换异常（issue#IKKGF@Gitee）
* 【crypto】        修复RSA分段加密解密的bug（感谢@【深圳】Demo）
* 【poi】            修复ExcelWriter写出文件无法覆盖问题（感谢@【宁波】mojie126）
* 【poi】            修复sax方式读取空行空指针问题（issue#124@Github）

-------------------------------------------------------------------------------------------------------------

## 4.1.0

### 新特性
* 【extra】          模板工具改为模板门面，抽象各模板引擎
* 【core】           修改Season为quarter（pr#114@Github）
* 【core】           CollUtil增加removeAny方法
* 【core】           StrUtil增加emptyToDefault和blankToDefault（issue#115@Github）
* 【core】           优化排列组合算法（感谢@【青岛】LQ）
* 【core】           NumberUtil增加roundHalfEven（感谢@【青岛】LQ）
* 【http】            HttpRequest.form支持多文件上传（相同key）（issue#IJYWM@Gitee）
* 【db】              新增SqlLog，独立SQL日志打印配置
* 【poi】             ExcelReader新增readAsText方法，ExcelWriter新增setHeaderOrFooter方法（设置页眉页脚）
* 【crypto】        删除DSA类（DSA算法用在Sign中），修改规则，RSA分段方式变为全局（issue#IKGKG@Gitee）
* 【core】           DateUtil添加range和rangeToList方法，增加DateRange类（issue#119@Github）
* 【core】           StrUtil增加concat方法，可选是否null转""（感谢@【帝都】宁静）

### Bug修复
* 【core】          修复StrUtil.replace方法第一个字符无法替换问题（issue#IJZR0@Gitee）
* 【core】          修复Season计算问题（pr#114@Github）
* 【core】          修复PinyinUtil获取拼音特殊字符转数字问题（issue#IJNWH@Gitee）
* 【core】          修复FileUtil.isAbsolutePath方法正则问题（issue#IJZUB@Gitee）
* 【extra】         修复ServletUtil.getMultipart方法的问题
* 【http】          修复patch方法无效问题（issue#IK2Z8@Gitee）
* 【core】          修复DateUtil.parseTimeToday格式问题（issue#IK25B@Gitee）
* 【poi】            修复设置字体日期和小数无效问题（issue#IK488@Gitee）
* 【core】          修复NumberUtil.partValue的bug（pr#15@Gitee）
* 【poi】            调整了readBySax方式读取导致的部分问题
* 【core】          修复CsvRow的get方法越界问题（issue#IK9CX@Gitee）
* 【core】          修复UnicodeUtil丢失末尾字符串的问题（issue#IKI6T@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.0.13

### 新特性
* 【json】          JSONArray添加jsonIter方法可以实现foreach语法遍历JSONObject（issue#IJPIJ@Gitee）
* 【core】         强化FileTypeUtil中对PDF文件格式的识别兼容性（issue#IJO1K@Gitee）
* 【core】         修改BetweenFormater枚举规则，修复不足1天显示空问题
* 【http】          由于JDK9移除了javax.activation导致的问题，修复移除相关包依赖（issue#109@Github）
* 【core】         改进Resource，增加getName方法，增加构造支持name
* 【core】         RandomUtil增加randomStringUpper方法（issue#IJVLS@Gitee）

### Bug修复
* 【core】         修复XmlUtil.toStr方法注释丢失问题（issue#IJPUA@Gitee）
* 【core】         修复ImageUtil.scale和createFont方法的bug（issue#IJOKE@Gitee）
* 【core】         修复StrUtil.format方法Map参数中值为null导致的空指针问题（issue#IJO31@Gitee）
* 【core】         修复ReUtil.getAllGroups丢失最后一个分组问题（issue#IJRJM@Gitee）
* 【json】         修复Bean中为Map导致的泛型类型不匹配问题（issue#IJRJM@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.0.12

### 新特性
* 【core】         ClassScaner支持jar的嵌套

### Bug修复
* 【setting】      修复Setting中size的bug
* 【cron】         修复Setting修改导致的定时任务读取错误问题（issue#IJMVN@Gitee）
* 【setting】      修复Props中autoLoad无效问题（issue#IJMOE@Gitee）
* 【cron】         修复表达式中年匹配位置的问题（issue#106@Gtihub）
* 【log】           修复log.info(null)空指针问题（issue#IJNRW@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.0.11

### 新特性
* 【core】     Week.toChinese()添加可选参数，选择星期的前缀（比如是“星期”还是“周”）
* 【core】     PinyinUtil增加方法，汉字转拼音（pr#11@Gitee）
* 【core】     Convert增加toList方法
* 【core】     CollUtil增加toList方法（感谢@【帝都】宁静）
* 【poi】       新增FormulaCellValue对象用于写出公式支持（感谢@【宁波】mojie126）

### Bug修复
* 【core】     修复NumberChineseFormater.format()方法无“元”字的问题（issue#IJ6MR@Gitee）
* 【core】     修复FileUtil.loopFile遍历根目录时空指针错误问题
* 【poi】       修复ExcelReader遇到ERROR单元格时报错问题（感谢@夏夜神话）
* 【http】      修复HttpUtil.post传入json字符串导致的问题（issue#99@Github）
* 【json】      修复Unicode不可见字符转义导致的中文双引号等符号显示问题（issue#IJFBD@Gitee）
* 【core】      修复ReferenceUtil中SoftReference错误问题（pr#105@Github）
* 【db】         删除ActiveRsHandler（歧义），修复showSql属性报错问题（issue#IJII8@Gitee）
* 【setting】   大改Setting逻辑，使用GroupedMap代替分组拼接方式，解决了无分组情况下会包含分组的问题

-------------------------------------------------------------------------------------------------------------

## 4.0.10

### 新特性
* 【poi】       ExcelWriter.merge方法加入重载，可选是否加入默认标题样式
* 【poi】       ExcelSaxReader改进按照流读取工作簿的构造，使之对于mark不支持的流也可解析
* 【cron】     添加updatePattern方法，可更新Task执行时间规则(感谢@【上海】嘿)
* 【cache】   添加get方法支持可选的是否更新lastAccess时间（issue#IISC4@Gitee）
* 【core】     StrUtil增加isNullOrUndefined、isEmptyOrUndefined、isBlankOrUndefined方法（issue#IIR44@Gitee）
* 【core】     isBlankChar方法迁移到CharUtil中
* 【db】        增加NamedSql
* 【poi】       对于POI未引入或版本错误提供更加明确的提示
* 【core】     增加UUIDConverter，支持UUID对象的自动转换
* 【core】     IterUtil增加fieldValueList、fieldValueAsMap、join重载方法(issue#IIU4F@Gitee)
* 【core】     IoUtil增加checksum、toBuffered方法，StrUtil增加maxLength方法（参考osgl-tool）
* 【poi】       ExcelReader支持自定义sheet

### Bug修复
* 【poi】       修复ExcelWriter合并单元格后样式失效问题
* 【http】      修复HttpUtil.download方法遇到特殊Disposition时处理异常问题（感谢@【深圳】Bomb）
* 【core】     修复StrUtil.toUnderlineCase方法中下划线转下划线导致的问题
* 【core】     修复RandomUtil.randomEles方法计数错误问题（issue#98@Github）
* 【core】     修复NumberChineseFormater负数小数结果错误问题（pr#10@Gitee）
* 【captcha】修复验证码无法序列化的问题（issue#IJ2MI@Gitee）

-------------------------------------------------------------------------------------------------------------

## 4.0.9

### 新特性
* 【core】     SecureUtil增加signParamsSha1方法（感谢@【帝都】宁静）
* 【core】     XmlUtil增加mapToXml和xmlToMap（感谢@【杭州】小宙子）
* 【captcha】修改逻辑：在创建验证码对象时生成一个验证码（感谢@【重庆】liuuuu）
* 【core】     CopiedIterator使用LinkedList替代ArrayList（issue#III8K@Gitee）
* 【poi】       ExcelWriter增加getOrCreateCell、createStyleForCell方法，便于自定义特殊单元格
* 【core】     增加AnnotationUtil类
* 【core】     IoUtil增加toMarkSupportStream方法
* 【poi】       ExcelReader改进按照流读取工作簿的构造，使之对于mark不支持的流也可解析
* 【core】     新增BytesResource和InputStreamResource
* 【core】     RandomUtil新增randomBigDecimal（感谢@【帝都】宁静）
* 【db】        Column对象添加comment字段
* 【core】     Base64增加encode方法，参数为Inputstream和File，新增decodeToFile、decodeToStream（issue#IILZS@Gitee）
* 【core】     扩充XmlUtil部分方法

### Bug修复
* 【core】修复StrUtil.replace问题（感谢@【上海】piaohao）
* 【mail】解决在javax.mail大于1.5版本时，附件名过长在国内邮箱导致的显示错误问题（添加splitlongparameters参数）
* 【core】修复ZipUtil.zip压缩目录时加入盘符问题（感谢@【深圳】Vmo ）
* 【core】修复PropertyComparator失效问题（感谢@【长沙】哼哼 ）
* 【cron】修复20/2此类表达式无效问题（感谢@【广州】杨小过 ）
* 【core】修复XmlUtil.toStr编码设置无效问题

-------------------------------------------------------------------------------------------------------------

## 4.0.8

### 新特性
* 【core】新增PinyinComparator、CollUtil新增sortByPinyin（感谢@【帝都】宁静）
* 【json】JSONUtil增加xmlToJson方法
* 【poi】 ExcelWriter增加setColumnWidth和setRowHeight方法
* 【core】FileUtil.clean增加字符串重载（感谢@【帝都】宁静）
* 【core】ArrayUtil增加insert方法（感谢@【帝都】宁静）
* 【core】RandomUtil.randomDouble增加可选保留小数重载（感谢@【帝都】宁静）
* 【core】增加RandomUtil.randomDay随机天（感谢@【帝都】宁静）
* 【poi】  ExcelWriter增加setOrCreateSheet方法，从而支持多sheet生成

### Bug修复
* 【json】修复JSONArray中addAll加入两次的bug（感谢@【天津】〓下页）
* 【core】修复BeanDesc中对static属性未忽略的问题（感谢@【深圳】枫林晓寒）
* 【http】解决无法移除默认头信息的问题
* 【core】修复Base64在decode时针对urlSafe乱码问题（issue#89@Github）
* 【core】修复ReUtil.extractMulti(感谢@【杭州】徐承恩)
* 【core】修复DESede类中算法错误问题（issue#93@Github）

-------------------------------------------------------------------------------------------------------------

## 4.0.7

### 新特性
* 【core】新加math包，并添加MathUtil工具类（排列组合迁入此）
* 【core】StrUtil增加move方法，字符串位移（感谢@【帝都】宁静）
* 【core】ArrayUtil的max和min采用可变参数（T[]除外）（感谢@【帝都】宁静）
* 【core】NumberUtil增加max和min方法，与ArrayUtil一致（感谢@【帝都】宁静）
* 【poi】  去除InternalExcelUtil，根据功能新增WorkbookUtil、RowUtil、CellUtil、ExcelPicUtil
* 【core】新增PinyinUtil（感谢@【帝都】宁静）
* 【core】StrUtil增加wrapAll、wrapAllIfMissing（感谢@【帝都】宁静）
* 【core】Singleton增加put方法
* 【core】Convert增加convertByClassName方法
* 【json】JSONUtil增加toList快捷方法

### Bug修复
* 【core】修复排列组合结果错误问题（感谢@【帝都】宁静）
* 【poi】  修复StrUtil.unWrap传入null导致的越界问题（issue#II1VU@Gitee）
* 【core】修复ImageUtil.sliceByRowsAndCols方法计算错误（感谢@【唐山】小虫）
* 【core】修复StrUtil.replace问题（感谢@【霾都】QQ小冰）
* 【core】修复FileTypeUtil对jpg的识别范围（issue#91@Github）

-------------------------------------------------------------------------------------------------------------

## 4.0.6

### 新特性
* 【poi】  ExcelReader增加getWriter、getOrCreateCell方法
* 【core】NetUtil增加isInRange方法（感谢@【成都】小邓）
* 【core】新增BeanPath（仅支持部分JSONPath语法）
* 【core】CollUtil新增reverse、reverseNew方法
* 【core】集合中新增排列（Arrangement）和组合（Combination）类（感谢@【北京】宁静）
* 【core】StrUtil新增splitToLong和splitToInt方法
* 【core】MapUtil增加getXXX方法
* 【core】扩充Dict构造
* 【core】CollUtil新增sortByProperty方法
* 【json】toBean支持下划线转驼峰
* 【core】FileUtil新增更多方法，包括路径拼接
* 【core】新增LineIterator、NullOutputStream两个类

### Bug修复
* 【core】修复IdcardUtil中身份证15转18位年的问题（Issue#IHT1Q@Gitee）
* 【http】忽略Premature EOF错误（感谢@【南京】peckey）
* 【core】修复ArrayConvert中集合转原始类型数组导致的异常

-------------------------------------------------------------------------------------------------------------

## 4.0.5

### 新特性
* 【json】 toBean方法支持Map.class参数，消除歧义
* 【core】FileWriter和FileUtil增加writeMap方法
* 【core】新增CsvWriter和CsvUtil
* 【poi】  改进ExcelWriter.flush未指定文件时的报错信息
* 【db】   在配置文件不存在时优化错误提示
* 【core】BeanUtil.beanToMap方法支持自定义key
* 【core】增加ModifierUtil，修饰符工具类
* 【http】下载文件时文件名首先从头信息中获取
* 【poi】  ExcelReader增加getCell方法
* 【db】   Oracle驱动变更
* 【extra】扩充Sftp方法（感谢@【广西】Succy）
* 【core】ImageUtil增加binary方法，生成二值化图片（感谢@【天津】〓下页）

### Bug修复
* 【poi】  修复ExcelReader获取Workbook为空的问题
* 【core】修复ImageUtil.scale的问题（感谢@【北京】千古不见一人闲）
* 【json】 修复JSON转字符串时值中双引号转义问题（感谢@【深圳】jae）

-------------------------------------------------------------------------------------------------------------

## 4.0.4

### 新特性
* 【http】    HttpUtil.downloadFile增加超时重载（感谢@【深圳】富）
* 【setting】Setting增加构造重载（pr#8@Gitee）
* 【core】   IterUtil增加fieldValueMap方法（感谢@【苏州】陈华 万缕数据@【北京】宁静）

### Bug修复
* 【log】  修复StaticLog.warn打印级别错误问题（issue#IHMF9@Gitee）
* 【core】修复MapUtil.newHashMap中isOrder（感谢@【珠海】hzhhui）
* 【core】修复DateTime.season获取的问题（感谢@西湖断桥）
* 【cron】修复在秒匹配关闭时无法匹配的问题（感谢@【北京】宁静）

-------------------------------------------------------------------------------------------------------------

## 4.0.3

### 新特性
* 【core】新增LocalPortGenerater，本地端口生成器
* 【extra】新增Sftp类，用于SFTP支持
* 【core】StrUtil增加replace（支持参数从某个位置开始）和replaceIgnoreCase方法（感谢@【贵阳】shadow ）
* 【core】Number.equals方法迁移到CharUtil（NumberUtil中依旧保留）
* 【extra】mail增加抄送和密送支持（感谢【成都】出错）
* 【poi】ExcelReader别名在返回List时也被支持（第一行）
* 【poi】ExcelReader增加getSheets和getSheetNames方法（感谢@【帝都】宁静）
* 【poi】ExcelReader增加readCellValue和readRow方法（感谢@【苏州】马克）
* 【db】全局数据源工厂独立，使用懒加载方式，消除歧义
* 【log】全局日志工厂独立，懒加载方式，消除歧义
* 【extra】MailUtil增加快捷方法支持抄送和密送参数

### Bug修复
* 【core】修复获取子路径bug（issue#IHI5K@Gitee）
* 【poi】修复ExcelReader在读取文件后未关闭导致文件被占用问题（感谢@【昆明】-@_@）
* 【log】解决Tinylog实现显示类名和行行错误问题
* 【extra】修复Mail构造在MailAccount传入null时读取错误的问题

-------------------------------------------------------------------------------------------------------------

## 4.0.2

### 新特性
* 【core】优化BeanDesc，适配更多Getter和Setter方法
* 【extra】增加基于zxing的二维码生成和解码（zxing可选依赖）
* 【core】增加VersionComparator用于版本比较，同时添加StrUtil.compareVersion
* 【core】Convert支持Map、Bean之间的转换、enum，新增BeanConverter和CastBeanConverter
* 【extra】ServletUtil中增加获取body和上传文件支持
* 【json】在json与bean互相转换时支持enum和字符串转换（感谢@【帝都】宁静）
* 【core】增加OptArrayTypeGetter接口
* 【http】HttpUtil增加decodeParamMap方法，返回单值map（感谢@【帝都】宁静）
* 【poi】ExcelWriter增加writeCellValue方法
* 【cron】去除CronUtil以及Scheduler中的isMatchYear方法（年的匹配通过表达式自动判断）
* 【extra】邮件Mail对象增加setUseGlobalSession方法，用于自定义是否使用单例会话

### Bug修复
* 【setting】修复clear方法未清空group的问题，store方法未换行问题，set方法分组丢失问题（感谢@【广西】Succy）
* 【json】修复Map嵌套转JSONObject时判断失误导致的值错误（issue#@Gitee）
* 【core】修复betweenYear注释错误（感谢@【常州】在校学生）
* 【core】修复Convert.digitToChinese方法中角为0时显示问题（issue#IHHE1@Gitee）
* 【cron】修复在秒匹配模式下5位表达式执行异常问题，修复cron.setting文件不存在报错问题
* 【extra】邮件配置中参数值转为String解决可能存在的bug

-------------------------------------------------------------------------------------------------------------

## 4.0.1

### 新特性
* 新增CharUtil
* 新增ASCIIStrCache，对ASCII字符做String对应表，提升字符转字符串性能
* 去除JschUtil中的同步修饰，改为锁
* 新增MapUtil.sort
* SymmetricCrypto支持加密后转为Base64和从Base64解密
* AsymmetricCrypto支持Hex和Base64加密解密
* 新增SecureUtil.signParams方法用于参数签名（感谢@【帝都】宁静）
* 新增Loader和LazyLoader，抽象懒加载
* 新增CsvReader,CSV读取
* HttpRequest支持可选get请求下的url参数编码
* ExcelReader增加read重载方法，ExcelUtil增加isEmpty(Sheet)方法（pr#5@Gitee）
* db模块针对IS NULL优化

### Bug修复
* 修复db模块中数据库为下划线而Bean为驼峰导致的注入失败问题（感谢@【广西】Succy）
* 修复findLike的bug（感谢@cici）
* 修复ArrayUtil.join循环引用bug
* FileTypeUtil针对pdf格式做修改（issue#IHDNH@Gitee）
* 修复Http模块中get方法拼接参数问题
* 修复db模块in方式查询错误问题
* 修复CollUtil.disjunction计算差集修复一个集合为空的情况（感谢@【天津】〓下页）
* 修复Db模块中Number参数丢失问题（感谢@【山东】小灰灰）

-------------------------------------------------------------------------------------------------------------

## 4.0.0

### 新特性
* 变更包名为cn.hutool.xxx
* 新增ObjecIdt类，用于实现MongoDB的ID生成策略
* 验证码单独成为一个模块hutool-captcha
* 新增NamedThreadFactory
* 新增BufferUtil
* POI新增StyleUtil，StyleSet新增方法可设置背景、边框等样式
* JDBC参数针对BigInteger处理
* db模块支持显示和格式化显示SQL
* 调整日志优先级：ConsoleLog优先于JDKLog，Log4j2优先于Log4j
* db模块的SqlRunner中可自定义Wrapper
* ExcelReader增加read重载方法（pr#4@Gitee）
* Convert.convert增加Class的重载，解决返回值歧义（感谢@t-io）
* Http中使用byte[]存储body，减少转换
* ExcelReader增加getWorkbook、getSheet方法
* 新增StrBuilder
* 新增JschUtil
* 新增UnicodeUtil
* db模块的BeanListHandler和BeanHandler支持Map、Collection、Array等类型
* NumberUtil加减乘支持多个值，解决float和double混合运算导致的坑

### Bug修复
* 修复ExcelReader空行导致空指针问题（pr#4@Gitee）
* 修复BeanUtil.getProperty不能获取父类属性的问题
* 修复BeanDesc类中boolean类型字段名为isXXX的情况无法注入问题
* 解决类扫描后加载类中引用依赖导致的报错（感谢@【帝都】宁静）