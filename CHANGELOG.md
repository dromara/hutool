
# Changelog

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