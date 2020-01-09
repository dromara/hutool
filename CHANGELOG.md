
# Changelog

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

### Bug修复
* 【core 】     修复NumberUtil.mul中null的结果错误问题（issue#I17Y4J@Gitee）
* 【core 】     修复当金额大于等于1亿时，转换会多出一个万字的bug（pr#715@Github）

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
