
# 🚀Changelog

-------------------------------------------------------------------------------------------------------------
# 5.7.22 (2022-03-01)

### 🐣新特性
* 【poi    】     ExcelUtil.readBySax增加对POI-5.2.0的兼容性（[issue#I4TJF4@gitee](https://gitee.com/dromara/hutool/issues/I4TJF4)）
* 【extra  】     Ftp增加构造（[issue#I4TKXP@gitee](https://gitee.com/dromara/hutool/issues/I4TKXP)）
* 【core   】     GenericBuilder支持Map构建（[pr#540@Github](https://github.com/dromara/hutool/pull/540)）
* 【json   】     新增TemporalAccessorSerializer
* 【core   】     使多个xxxBuilder实现Builder接口，扩展CheckedUtil（[pr#545@Gitee](https://gitee.com/dromara/hutool/pulls/545)）
* 【core   】     CheckedUtil删除第二个参数为RuntimeException的方法
* 【core   】     FileUtil增加getTotalLines方法
* 【db     】     MetaUtil增加getTableMeta重载（[issue#2157@Github](https://github.com/dromara/hutool/issues/2157)）
* 【http   】     增加HttpGlobalConfig.setDecodeUrl（[issue#I4U8YQ@Gitee](https://gitee.com/dromara/hutool/issues/I4U8YQ)）
* 【core   】     增加Base58（[pr#2162@Github](https://github.com/dromara/hutool/pull/2162)）
* 【core   】     增加AntPathMatcher（[issue#I4T7K5@Gitee](https://gitee.com/dromara/hutool/issues/I4T7K5)）
* 【core   】     StrJoiner修改toString策略，调用不再修改Appendable
* 【core   】     StrJoiner增加length和merge方法
* 【core   】     CRC16增加getHexValue方法（[issue#I4VO3U@Gitee](https://gitee.com/dromara/hutool/issues/I4VO3U)）

### 🐞Bug修复
* 【cache  】     修复ReentrantCache.toString方法线程不安全问题（[issue#2140@Github](https://github.com/dromara/hutool/issues/2140)）
* 【core   】     修复SystemPropsUtil.getInt返回long问题（[pr#546@Gitee](https://gitee.com/dromara/hutool/pulls/546)）
* 【crypto 】     修复SM2.getD前导0问题（[pr#2149@Github](https://github.com/dromara/hutool/pull/2149)）
* 【core   】     修复ChineseDate在1970年之前农历差一天问题（[issue#I4UTPK@Gitee](https://gitee.com/dromara/hutool/issues/I4UTPK)）
* 【core   】     修复CoordinateUtil精准问题及转换bug（[pr#551@Gitee](https://gitee.com/dromara/hutool/pulls/551)）
* 【json   】     修复JSONObject解析XML后没有返回的bug（[issue#2160@Github](https://github.com/dromara/hutool/issues/2160)）
* 【extra  】     修复GanymedUtil错误信息读取位置问题（[issue#I4VDZ2@Gitee](https://gitee.com/dromara/hutool/issues/I4VDZ2)）

-------------------------------------------------------------------------------------------------------------
# 5.7.21 (2022-02-14)

### 🐣新特性
* 【extra  】     增加jetbrick模板支持
* 【extra  】     EmojiUtil增加方法（[pr#519@Gitee](https://gitee.com/dromara/hutool/pulls/519)）
* 【core   】     DateUtil 添加两个日期是否同一周方法（[pr#516@Gitee](https://gitee.com/dromara/hutool/pulls/516)）
* 【db     】     新增条件组，用于处理复杂的where条件（[pr#514@Gitee](https://gitee.com/dromara/hutool/pulls/514)）
* 【core   】     新增LocalDateTimeUtil.weekOfYear（[issue#I4RWXC@Gitee](https://gitee.com/dromara/hutool/issues/I4RWXC)）
* 【core   】     Month增加toJdkMonth、getValueBaseOne
* 【core   】     CsvWriter修改规则，去除末尾多余换行符（[issue#I4RSQY@Gitee](https://gitee.com/dromara/hutool/issues/I4RSQY)）
* 【core   】     DateUtil增加rangeFunc和rangeConsume（[issue#I4RSQY@Gitee](https://gitee.com/dromara/hutool/issues/I4RSQY)）
* 【core   】     DateTime增加setUseJdkToStringStyle方法
* 【core   】     CharSequenceUtil增加replace重载([issue#2122@Github](https://github.com/dromara/hutool/issues/2122))
* 【core   】     IntMap和LongMap使用位运算快速求解取余运算([pr#2123@Github](https://github.com/dromara/hutool/pull/2123))
* 【core   】     新增通用builder类：GenericBuilder([pr#526@Gitee](https://gitee.com/dromara/hutool/pulls/526))
* 【core   】     新增copySafely方法与mkdirsSafely方法([pr#527@Gitee](https://gitee.com/dromara/hutool/pulls/527))
* 【core   】     新增MetroHash([pr#532@Gitee](https://gitee.com/dromara/hutool/pulls/532))
* 【core   】     SpringUtil增加publishEvent重载([pr#2139@Github](https://github.com/dromara/hutool/pull/2139))
* 【core   】     DateUtil增加rangeContains、rangeNotContains([pr#537@Gitee](https://gitee.com/dromara/hutool/pulls/537))
* 【core   】     Resource增加isModified默认方法
* 【core   】     增加VfsResource
* 【json   】     JSONConfig增加setKeyComparator、setNatureKeyComparator方法，支持自定义排序（[issue#I4RBZ4@Gitee](https://gitee.com/dromara/hutool/issues/I4RBZ4)）

### 🐞Bug修复
* 【core   】     修复ChineseDate农历获取正月出现数组越界BUG（[issue#2112@Github](https://github.com/dromara/hutool/issues/2112)）
* 【extra  】     修复EmojiUtil.toHtmlHex()方法（[pr#519@Gitee](https://gitee.com/dromara/hutool/pulls/519)）
* 【system 】     修复CpuInfo.getUsed()方法（[issue#2116@Github](https://github.com/dromara/hutool/issues/2116)）
* 【dfa    】     修复密集匹配和贪婪匹配冲突问题（[issue#2126@Github](https://github.com/dromara/hutool/issues/2126)）
* 【db     】     修复c3p0丢失信息问题（[issue#I4T7XZ@Gitee](https://gitee.com/dromara/hutool/issues/I4T7XZ)）
* 【http   】     修复Action中HttpExchange没有关闭问题
* 【http   】     修复Action中HttpExchange没有关闭问题

-------------------------------------------------------------------------------------------------------------
# 5.7.20 (2022-01-20)

### 🐣新特性
* 【core   】     增加对null值友好的groupingBy操作的Collector实现，可指定map类型（[pr#498@Gitee](https://gitee.com/dromara/hutool/pulls/498)）
* 【core   】     增加KetamaHash（[issue#2084@Github](https://github.com/dromara/hutool/issues/2084)）
* 【crypto 】     增加SignUtil
* 【json   】     JSONGetter增加getBeanList方法
* 【core   】     ObjectUtil 添加三个defaultIfXxxx方法，用于节省CPU及内存损耗([pr#2094@Github](https://github.com/dromara/hutool/pull/2094))
* 【db     】     增加单条数据原生upsert语义支持([pr#501@Gitee](https://gitee.com/dromara/hutool/pulls/501))
* 【core   】     在CollectorUtil提交Collectors.toMap的对null友好实现，避免NPE([pr#502@Gitee](https://gitee.com/dromara/hutool/pulls/502))
* 【http   】     增加HttpGlobalConfig.setIgnoreEOFError([issue#2092@Github](https://github.com/dromara/hutool/issues/2092))
* 【core   】     RandomUtil.randomStringWithoutStr排除字符串兼容大写字母([pr#503@Gitee](https://gitee.com/dromara/hutool/pulls/503))
* 【core   】     LocalDateTime增加isOverlap方法([pr#512@Gitee](https://gitee.com/dromara/hutool/pulls/512))
* 【core   】     Ipv4Util.getBeginIpLong、getEndIpLong改为public([pr#508@Gitee](https://gitee.com/dromara/hutool/pulls/508))
* 
### 🐞Bug修复
* 【core   】     修复setter重载导致匹配错误（[issue#2082@Github](https://github.com/dromara/hutool/issues/2082)）
* 【core   】     修复RegexPool汉字匹配范围小问题（[pr#2081@Github](https://github.com/dromara/hutool/pull/2081)）
* 【core   】     修复OS中的拼写错误（[pr#500@Gitee](https://gitee.com/dromara/hutool/pulls/500)）
* 【core   】     修复CustomKeyMap的merge失效问题（[issue#2086@Github](https://github.com/dromara/hutool/issues/2086)）
* 【core   】     修复FileUtil.appendLines换行问题（[issue#I4QCEZ@Gitee](https://gitee.com/dromara/hutool/issues/I4QCEZ)）
* 【core   】     修复java.time.Month解析问题（[issue#2090@Github](https://github.com/dromara/hutool/issues/2090)）
* 【core   】     修复PathUtil.moveContent移动覆盖导致的问题（[issue#I4QV0L@Gitee](https://gitee.com/dromara/hutool/issues/I4QV0L)）
* 【core   】     修复Opt.ofTry中并发环境下线程安全问题（[pr#504@Gitee](https://gitee.com/dromara/hutool/pulls/504)）
* 【core   】     修复PatternFinder中end边界判断问题（[issue#2099@Github](https://github.com/dromara/hutool/issues/2099)）
* 【core   】     修复格式化为中文日期时，0被处理为空串（[pr#507@Gitee](https://gitee.com/dromara/hutool/pulls/507)）
* 【core   】     修复UrlPath转义冒号问题（[issue#I4RA42@Gitee](https://gitee.com/dromara/hutool/issues/I4RA42)）

-------------------------------------------------------------------------------------------------------------
# 5.7.19 (2022-01-07)

### 🐣新特性
* 【db     】     优化Condition参数拆分（[pr#2046@Github](https://github.com/dromara/hutool/pull/2046)）
* 【core   】     优化ArrayUtil.isAllEmpty性能（[pr#2045@Github](https://github.com/dromara/hutool/pull/2045)）
* 【core   】     CharSequenceUtil.replace方法支持增补字符（[pr#2041@Github](https://github.com/dromara/hutool/pull/2041)）
* 【extra  】     增加SshjSftp（[pr#493@Gitee](https://gitee.com/dromara/hutool/pulls/493)）
* 【core   】     增加CheckedUtil（[pr#491@Gitee](https://gitee.com/dromara/hutool/pulls/491)）
* 【extra  】     增加Sftp.isDir中的抛异常判断条件（[issues#I4P9ED@Gitee](https://gitee.com/dromara/hutool/issues/I4P9ED)）

### 🐞Bug修复
* 【http   】     HttpUtil重定向次数失效问题（[issue#I4O28Q@Gitee](https://gitee.com/dromara/hutool/issues/I4O28Q)）
* 【core   】     修复UrlPath空白path多/问题（[issue#I49KAL@Gitee](https://gitee.com/dromara/hutool/issues/I49KAL)）
* 【core   】     修复ServletUtil写出文件时未添加双引号导致逗号等特殊符号引起的问题（[issue#I4P1BF@Gitee](https://gitee.com/dromara/hutool/issues/I4P1BF)）
* 【core   】     NumberUtil增加equals重载解决long传入判断问题（[pr#2064@Github](https://github.com/dromara/hutool/pull/2064)）
* 【core   】     修复CsvParser行号有误问题（[pr#2065@Github](https://github.com/dromara/hutool/pull/2065)）
* 【http   】     修复HttpRequest.of无法自动添加http前缀问题（[issue#I4PEYL@Gitee](https://gitee.com/dromara/hutool/issues/I4PEYL)）
* 【core   】     修复 `CharSequenceUtil.brief(str, maxLength)` 方法字符串越界问题，以及 `maxLength` 部分值时结果与预期不符的问题（[pr#2068@Github](https://github.com/dromara/hutool/pull/2068)）
* 【core   】     修复NamingCase中转换下划线字母+数字转换问题（[issue#2070@Github](https://github.com/dromara/hutool/issues/2070)）
* 【core   】     修复split空判断不一致问题（[pr#496@Gitee](https://gitee.com/dromara/hutool/pulls/496)）
* 【crypto 】     修复SM2.getDHex()前导0丢失,然后导致获取密钥错误（[pr#2073@Github](https://github.com/dromara/hutool/pull/2073)）
* 【core   】     修复关于Calculator.conversion()方法EmptyStackException的bug（[pr#2076@Github](https://github.com/dromara/hutool/pull/2076)）
* 【core   】     修复StrUtil.subBetweenAll循环bug（[issue#I4PT3M@Gitee](https://gitee.com/dromara/hutool/issues/I4PT3M)）

-------------------------------------------------------------------------------------------------------------
# 5.7.18 (2021-12-25)

### 🐣新特性
* 【core   】     新增CollStreamUtil.groupKeyValue（[pr#479@Gitee](https://gitee.com/dromara/hutool/pulls/479)）
* 【core   】     新增DatePattern.createFormatter（[pr#483@Gitee](https://gitee.com/dromara/hutool/pulls/483)）
* 【core   】     增加IdUtil.getSnowflakeNextId（[pr#485@Gitee](https://gitee.com/dromara/hutool/pulls/485)）
* 【log    】     log4j2的编译依赖改为api，core为test依赖（[pr#2019@Github](https://github.com/dromara/hutool/pull/2019)）
* 【core   】     Img.scale缩小默认使用平滑模式，增加scale方法重载可选模式（[issue#I4MY6X@Gitee](https://gitee.com/dromara/hutool/issues/I4MY6X)）
* 【core   】     excel添加写入图片的方法（[pr#486@Gitee](https://gitee.com/dromara/hutool/pulls/486)）
* 【core   】     增加CollStreamUtil.groupBy（[pr#484@Gitee](https://gitee.com/dromara/hutool/pulls/484)）
* 【core   】     增加CollUtil.setValueByMap（[pr#482@Gitee](https://gitee.com/dromara/hutool/pulls/482)）
* 【core   】     LocalDateTimeUtil增加endOfDay重载（[issue#2025@Github](https://github.com/dromara/hutool/issues/2025)）
* 【core   】     IoCopier增加setFlushEveryBuffer方法（[issue#2022@Github](https://github.com/dromara/hutool/issues/2022)）
* 
### 🐞Bug修复
* 【core   】     LineReadWatcher#onModify文件清空判断问题（[issue#2013@Github](https://github.com/dromara/hutool/issues/2013)）
* 【core   】     修复4位bytes转换float问题（[issue#I4M0E4@Gitee](https://gitee.com/dromara/hutool/issues/I4M0E4)）
* 【core   】     修复CharSequenceUtil.replace问题（[issue#I4M16G@Gitee](https://gitee.com/dromara/hutool/issues/I4M16G)）
* 【json   】     修复JSONObject 初始化大小值未被使用问题（[issue#2016@Github](https://github.com/dromara/hutool/issues/2016)）
* 【core   】     修复StrUtil.startWith都为null返回错误问题（[issue#I4MV7Q@Gitee](https://gitee.com/dromara/hutool/issues/I4MV7Q)）
* 【core   】     修复PasswdStrength检测问题（[issue#I4N48X@Gitee](https://gitee.com/dromara/hutool/issues/I4N48X)）
* 【core   】     修复UserAgentUtil解析EdgA无法识别问题（[issue#I4MCBP@Gitee](https://gitee.com/dromara/hutool/issues/I4MCBP)）
* 【extra  】     修复Archiver路径前带/问题（[issue#I4NS0F@Gitee](https://gitee.com/dromara/hutool/issues/I4NS0F)）
* 【extra  】     修复getMainColor方法中参数rgbFilters无效问题（[pr#2034@Github](https://github.com/dromara/hutool/pull/2034)）
* 【core   】     修复ChineseDate无法区分闰月问题（[issue#I4NQQW@Gitee](https://gitee.com/dromara/hutool/issues/I4NQQW)）
* 【core   】     修复BeanDesc大小写误判问题（[issue#2009@Github](https://github.com/dromara/hutool/issues/2009)）

-------------------------------------------------------------------------------------------------------------
# 5.7.17 (2021-12-09)

### 🐣新特性
* 【core   】     增加AsyncUtil（[pr#457@Gitee](https://gitee.com/dromara/hutool/pulls/457)）
* 【http   】     增加HttpResource（[issue#1943@Github](https://github.com/dromara/hutool/issues/1943)）
* 【http   】     增加BytesBody、FormUrlEncodedBody
* 【cron   】     TaskTable.remove增加返回值（[issue#I4HX3B@Gitee](https://gitee.com/dromara/hutool/issues/I4HX3B)）
* 【core   】     Tree增加filter、filterNew、cloneTree、hasChild方法（[issue#I4HFC6@Gitee](https://gitee.com/dromara/hutool/issues/I4HFC6)）
* 【poi    】     增加ColumnSheetReader及ExcelReader.readColumn，支持读取某一列
* 【core   】     IdCardUtil.isValidCard不再自动trim（[issue#I4I04O@Gitee](https://gitee.com/dromara/hutool/issues/I4I04O)）
* 【core   】     改进TextFinder，支持限制结束位置及反向查找模式
* 【core   】     Opt增加部分方法（[pr#459@Gitee](https://gitee.com/dromara/hutool/pulls/459)）
* 【core   】     增加DefaultCloneable（[pr#459@Gitee](https://gitee.com/dromara/hutool/pulls/459)）
* 【core   】     CollStreamUtil增加是否并行的重载（[pr#467@Gitee](https://gitee.com/dromara/hutool/pulls/467)）
* 【core   】     ResourceClassLoader增加缓存（[pr#1959@Github](https://github.com/dromara/hutool/pull/1959)）
* 【crypto 】     增加CipherWrapper，增加setRandom（[issue#1958@Github](https://github.com/dromara/hutool/issues/1958)）
* 【core   】     Opt增加ofTry方法（[pr#1956@Github](https://github.com/dromara/hutool/pull/1956)）
* 【core   】     DateUtil.toIntSecond标记为弃用（[issue#I4JHPR@Gitee](https://gitee.com/dromara/hutool/issues/I4JHPR)）
* 【db     】     Db.executeBatch标记一个重载为弃用（[issue#I4JIPH@Gitee](https://gitee.com/dromara/hutool/issues/I4JIPH)）
* 【core   】     增加CharSequenceUtil.subPreGbk重载（[issue#I4JO2E@Gitee](https://gitee.com/dromara/hutool/issues/I4JO2E)）
* 【core   】     ReflectUtil.getMethod排除桥接方法（[pr#1965@Github](https://github.com/dromara/hutool/pull/1965)）
* 【http   】     completeFileNameFromHeader在使用path为路径时，自动解码（[issue#I4K0FS@Gitee](https://gitee.com/dromara/hutool/issues/I4K0FS)）
* 【core   】     CopyOptions增加override配置（[issue#I4JQ1N@Gitee](https://gitee.com/dromara/hutool/issues/I4JQ1N)）
* 【poi    】     SheetRidReader可以获取所有sheet名（[issue#I4JA3M@Gitee](https://gitee.com/dromara/hutool/issues/I4JA3M)）
* 【core   】     AsyncUtil.waitAny增加返回值（[pr#473@Gitee](https://gitee.com/dromara/hutool/pulls/473)）
* 【core   】     Calculator.compare改为private（[issue#1982@Github](https://github.com/dromara/hutool/issues/1982)）
* 【core   】     NumberUtil增加isOdd、isEven方法（[pr#474@Gitee](https://gitee.com/dromara/hutool/pulls/474)）
* 【http   】     增加HttpGlobalConfig.setBoundary，删除MultipartBody.BOUNDARY和getContentType（[issue#I4KSLY@Gitee](https://gitee.com/dromara/hutool/issues/I4KSLY)）
* 【core   】     DateTime增加setMinimalDaysInFirstWeek（[issue#1988@Github](https://github.com/dromara/hutool/issues/1988)）
* 【db     】     Db增加query重载，可支持自定义PreparedStatement，从而支持游标（[issue#I4JXWN@Gitee](https://gitee.com/dromara/hutool/issues/I4JXWN)）
* 【cache  】     CacheObj增加getExpiredTime等方法（[issue#I4LE80@Gitee](https://gitee.com/dromara/hutool/issues/I4LE80)）
* 【extra  】     Ftp增加backToPwd方法（[issue#2004@Github](https://github.com/dromara/hutool/issues/2004)）
* 【core   】     CollStreamUtil修改集合中null处理问题（[pr#478@Gitee](https://gitee.com/dromara/hutool/pulls/478)）
* 
### 🐞Bug修复
* 【core   】     修复FileResource构造fileName参数无效问题（[issue#1942@Github](https://github.com/dromara/hutool/issues/1942)）
* 【cache  】     修复WeakCache键值强关联导致的无法回收问题（[issue#1953@Github](https://github.com/dromara/hutool/issues/1953)）
* 【core   】     修复ZipUtil相对路径父路径获取null问题（[issue#1961@Github](https://github.com/dromara/hutool/issues/1961)）
* 【http   】     修复HttpUtil.normalizeParams未判空导致的问题（[issue#1975@Github](https://github.com/dromara/hutool/issues/1975)）
* 【poi    】     修复读取日期类型的自定义样式单元格时间结果为1899年问题（[pr#1977@Github](https://github.com/dromara/hutool/pull/1977)）
* 【poi    】     修复SoapClient参数未使用问题
* 【core   】     修复HashUtil.cityHash128参数未使用问题
* 【core   】     修复DateUtil.formatChineseDate显示问题（[issue#I4KK5F@Gitee](https://gitee.com/dromara/hutool/issues/I4KK5F)）
* 【poi    】     修复CellUtil.setCellValueStyle空导致值无法写入问题（[issue#1995@Github](https://github.com/dromara/hutool/issues/1995)）
* 【poi    】     修复CellUtil.setComment参数设置错误问题
* 【core   】     修复QueryBuilder解析路径导致的错误（[issue#1989@Github](https://github.com/dromara/hutool/issues/1989)）
* 【core   】     修复DateTime.between中DateUnit无效问题
* 【poi    】     修复StyleUtil.getFormat非static问题（[issue#I4LGNP@Gitee](https://gitee.com/dromara/hutool/issues/I4LGNP)）
* 【crypto 】     修复SM2.getD返回bytes包含符号位的问题（[issue#2001@Github](https://github.com/dromara/hutool/issues/2001)）

-------------------------------------------------------------------------------------------------------------

# 5.7.16 (2021-11-07)

### 🐣新特性
* 【core   】     增加DateTime.toLocalDateTime
* 【core   】     CharSequenceUtil增加normalize方法（[pr#444@Gitee](https://gitee.com/dromara/hutool/pulls/444)）
* 【core   】     MailAccount增加setEncodefilename()方法，可选是否编码附件的文件名（[issue#I4F160@Gitee](https://gitee.com/dromara/hutool/issues/I4F160)）
* 【core   】     MailAccount中charset增加null时的默认规则
* 【core   】     NumberUtil.compare修正注释说明（[issue#I4FAJ1@Gitee](https://gitee.com/dromara/hutool/issues/I4FAJ1)）
* 【core   】     增加RFC3986类
* 【extra  】     Sftp增加put和upload重载（[issue#I4FGDH@Gitee](https://gitee.com/dromara/hutool/issues/I4FGDH)）
* 【core   】     TemporalUtil增加toChronoUnit、toTimeUnit方法（[issue#I4FGDH@Gitee](https://gitee.com/dromara/hutool/issues/I4FGDH)）
* 【core   】     StopWatch增加prettyPrint重载（[issue#1910@Github](https://github.com/dromara/hutool/issues/1910)）
* 【core   】     修改RegexPool中Ipv4正则
* 【json   】     Filter改为MutablePair，以便编辑键值对（[issue#1921@Github](https://github.com/dromara/hutool/issues/1921)）
* 【core   】     Opt增加peeks方法（[pr#445@Gitee](https://gitee.com/dromara/hutool/pulls/445)）
* 【extra  】     MailAccount中user默认值改为邮箱全称（[issue#I4FYVY@Gitee](https://gitee.com/dromara/hutool/issues/I4FYVY)）
* 【core   】     增加CoordinateUtil（[pr#446@Gitee](https://gitee.com/dromara/hutool/pulls/446)）
* 【core   】     DateUtil增加rangeToList重载（[pr#1925@Github](https://github.com/dromara/hutool/pull/1925)）
* 【core   】     CollUtil增加safeContains方法（[pr#1926@Github](https://github.com/dromara/hutool/pull/1926)）
* 【core   】     ActualTypeMapperPool增加getStrKeyMap方法（[pr#447@Gitee](https://gitee.com/dromara/hutool/pulls/447)）
* 【core   】     TreeUtil增加walk方法（[pr#1932@Gitee](https://gitee.com/dromara/hutool/pulls/1932)）
* 【crypto 】     SmUtil增加sm3WithSalt（[pr#454@Gitee](https://gitee.com/dromara/hutool/pulls/454)）
* 【http   】     增加HttpInterceptor（[issue#I4H1ZV@Gitee](https://gitee.com/dromara/hutool/issues/I4H1ZV)）
* 【core   】     Opt增加flattedMap（[issue#I4H1ZV@Gitee](https://gitee.com/dromara/hutool/issues/I4H1ZV)）

### 🐞Bug修复
* 【core   】     修复UrlBuilder.addPath歧义问题（[issue#1912@Github](https://github.com/dromara/hutool/issues/1912)）
* 【core   】     修复StrBuilder中总长度计算问题（[issue#I4F9L7@Gitee](https://gitee.com/dromara/hutool/issues/I4F9L7)）
* 【core   】     修复CharSequenceUtil.wrapIfMissing预定义长度计算问题（[issue#I4FDZ2@Gitee](https://gitee.com/dromara/hutool/issues/I4FDZ2)）
* 【poi    】     修复合并单元格为日期时，导出单元格数据为数字问题（[issue#1911@Github](https://github.com/dromara/hutool/issues/1911)）
* 【core   】     修复CompilerUtil.getFileManager参数没有使用的问题（[issue#I4FIO6@Gitee](https://gitee.com/dromara/hutool/issues/I4FIO6)）
* 【core   】     修复NetUtil.isInRange的cidr判断问题（[pr#1917@Github](https://github.com/dromara/hutool/pull/1917)）
* 【core   】     修复RegexPool中对URL正则匹配问题（[issue#I4GRKD@Gitee](https://gitee.com/dromara/hutool/issues/I4GRKD)）
* 【core   】     修复UrlQuery对于application/x-www-form-urlencoded问题（[issue#1931@Github](https://github.com/dromara/hutool/issues/1931)）

-------------------------------------------------------------------------------------------------------------

# 5.7.15 (2021-10-21)

### 🐣新特性
* 【db     】     Db.quietSetAutoCommit增加判空（[issue#I4D75B@Gitee](https://gitee.com/dromara/hutool/issues/I4D75B)）
* 【core   】     增加RingIndexUtil（[pr#438@Gitee](https://gitee.com/dromara/hutool/pulls/438)）
* 【core   】     Assert增加checkBetween重载（[pr#436@Gitee](https://gitee.com/dromara/hutool/pulls/436)）
* 【core   】     ReUtil增加命名分组重载（[pr#439@Gitee](https://gitee.com/dromara/hutool/pulls/439)）
* 【json   】     toString和writer增加Filter（[issue#I4DQNQ@Gitee](https://gitee.com/dromara/hutool/issues/I4DQNQ)）
* 【core   】     ContentType增加build重载（[pr#1898@Github](https://github.com/dromara/hutool/pull/1898)）
* 【bom    】     支持scope=import方式引入（[issue#1561@Github](https://github.com/dromara/hutool/issues/1561)）
* 【core   】     新增Hash接口，HashXXX继承此接口
* 【core   】     ZipUtil增加append方法（[pr#441@Gitee](https://gitee.com/dromara/hutool/pulls/441)）
* 【core   】     CollUtil增加重载（[issue#I4E9FS@Gitee](https://gitee.com/dromara/hutool/issues/I4E9FS)）
* 【core   】     CopyOptions新增setFieldValueEditor（[issue#I4E08T@Gitee](https://gitee.com/dromara/hutool/issues/I4E08T)）
* 【core   】     增加SystemPropsUtil（[issue#1918@Gitee](https://gitee.com/dromara/hutool/issues/1918)）
* 【core   】     增加`hutool.date.lenient`系统属性（[issue#1918@Gitee](https://gitee.com/dromara/hutool/issues/1918)）

### 🐞Bug修复
* 【core   】     修复CollUtil.isEqualList两个null返回错误问题（[issue#1885@Github](https://github.com/dromara/hutool/issues/1885)）
* 【poi    】     修复ExcelWriter多余调试信息导致的问题（[issue#1884@Github](https://github.com/dromara/hutool/issues/1884)）
* 【poi    】     修复TemporalAccessorUtil.toInstant使用DateTimeFormatter导致问题（[issue#1891@Github](https://github.com/dromara/hutool/issues/1891)）
* 【poi    】     修复sheet.getRow(y)为null导致的问题（[issue#1893@Github](https://github.com/dromara/hutool/issues/1893)）
* 【cache  】     修复LRUCache线程安全问题（[issue#1895@Github](https://github.com/dromara/hutool/issues/1895)）
* 【crypto 】     修复KeyUtil异常信息参数丢失问题（[issue#1902@Github](https://github.com/dromara/hutool/issues/1902)）
* 【core   】     修复StrUtil.split和splittoArray不一致问题（[issue#I4ELU5@Github](https://github.com/dromara/hutool/issues/I4ELU5)）
* 【core   】     修复SymmetricCrypto未关闭CipherOutputStream导致的问题（[issue#I4EMST@Gitee](https://gitee.com/dromara/hutool/issues/I4EMST)）
* 【core   】     修复QueryBuilder对/转义问题（[issue#1904@Github](https://github.com/dromara/hutool/issues/1904)）

-------------------------------------------------------------------------------------------------------------

# 5.7.14 (2021-10-09)

### 🐣新特性
* 【extra  】     修复HttpCookie设置cookies的方法，不符合RFC6265规范问题（[issue#I4B70D@Gitee](https://gitee.com/dromara/hutool/issues/I4B70D)）
* 【http   】     优化Browser版本正则判断
* 【setting】     增加YamlUtil
* 【extra  】     SenvenZExtractor改名为SevenZExtractor，增加getFirst、get方法
* 【core   】     DateConverter修改返回java.util.Date而非DateTime（[issue#I4BOAP@Gitee](https://gitee.com/dromara/hutool/issues/I4BOAP)）
* 【core   】     增加IterableIter、ComputeIter
* 【core   】     CsvConfig增加disableComment方法（[issue#1842@Github](https://github.com/dromara/hutool/issues/1842)）
* 【core   】     DateTime构造和DateUtil.parse可选是否宽松模式（[issue#1849@Github](https://github.com/dromara/hutool/issues/1849)）
* 【core   】     TreeBuilder增加部分根节点set方法（[issue#1848@Github](https://github.com/dromara/hutool/issues/1848)）
* 【core   】     优化Base64.isBase64方法：减少一次多余的判断（[pr#1860@Github](https://github.com/dromara/hutool/pull/1860)）
* 【cache  】     优化FIFOCache未设置过期策略时，无需遍历判断过期对象（[pr#425@Gitee](https://gitee.com/dromara/hutool/pulls/425)）
* 【core   】     增加Opt类（[pr#426@Gitee](https://gitee.com/dromara/hutool/pulls/426)）
* 【core   】     Week增加of重载，支持DayOfWek（[pr#1872@Github](https://github.com/dromara/hutool/pull/1872)）
* 【poi    】     优化read，避免多次创建CopyOptions（[issue#1875@Github](https://github.com/dromara/hutool/issues/1875)）
* 【core   】     优化CsvReader，实现可控遍历（[pr#1873@Github](https://github.com/dromara/hutool/pull/1873)）
* 【core   】     优化Base64.isBase64判断（[pr#1879@Github](https://github.com/dromara/hutool/pull/1879)）
* 【core   】     新增StrFormatter.formatWith（[pr#430@Gitee](https://gitee.com/dromara/hutool/pulls/430)）

### 🐞Bug修复
* 【http   】     修复HttpCookie设置cookies的方法，不符合RFC6265规范问题（[pr#418@Gitee](https://gitee.com/dromara/hutool/pulls/418)）
* 【http   】     修复Extractor中filter无效问题
* 【json   】     修复JSONGetter.getJSONArray判断null的问题（[issue#I4C15H@Gitee](https://gitee.com/dromara/hutool/issues/I4C15H)）
* 【db     】     修复Condition没占位符的情况下sql没引号问题（[issue#1846@Github](https://github.com/dromara/hutool/issues/1846)）
* 【cache  】     修复FIFOCache中remove回调无效问题（[pr#1856@Github](https://github.com/dromara/hutool/pull/1856)）
* 【json   】     修复JSONArray.set中，index为0报错问题（[issue#1858@Github](https://github.com/dromara/hutool/issues/1858)）
* 【core   】     修复FileUtil.checkSlip中getCanonicalPath异常引起的问题（[issue#1858@Github](https://github.com/dromara/hutool/issues/1858)）

-------------------------------------------------------------------------------------------------------------

# 5.7.13 (2021-09-17)

### 🐣新特性
* 【core   】     CsvReadConfig增加trimField选项（[issue#I49M0C@Gitee](https://gitee.com/dromara/hutool/issues/I49M0C)）
* 【http   】     HttpBase增加clearHeaders方法（[issue#I49P23@Gitee](https://gitee.com/dromara/hutool/issues/I49P23)）
* 【core   】     CsvWriter的write和writeBeans参数改为Iterable（[issue#I49O4S@Gitee](https://gitee.com/dromara/hutool/issues/I49O4S)）
* 【core   】     BitStatusUtil添加来源声明（[issue#1824@Github](https://github.com/dromara/hutool/issues/1824)）
* 【core   】     UrlQuery.build增加重载，支持可选是否转义（[issue#I4AIX1@Gitee](https://gitee.com/dromara/hutool/issues/I4AIX1)）
* 【core   】     ListUtil增加swapTo和swapElement方法（[pr#416@Gitee](https://gitee.com/dromara/hutool/pulls/416)）
* 【poi    】     ExcelWriter支持Hyperlink（[issue#I49QAL@Gitee](https://gitee.com/dromara/hutool/issues/I49QAL)）
* 
### 🐞Bug修复
* 【core   】     修复FuncKey函数无效问题
* 【core   】     修复ImgUtil.copyImage读取网络URL后宽高报错问题（[issue#1821@Github](https://github.com/dromara/hutool/issues/1821)）
* 【core   】     修复StrJoiner.append配置丢失问题（[issue#I49K1L@Gitee](https://gitee.com/dromara/hutool/issues/I49K1L)）
* 【core   】     修复EscapeUtil特殊字符的hex长度不足导致的问题（[issue#I49JU8@Gitee](https://gitee.com/dromara/hutool/issues/I49JU8)）
* 【core   】     修复UrlBuilder对Fragment部分编码问题（[issue#I49KAL@Gitee](https://gitee.com/dromara/hutool/issues/I49KAL)）
* 【core   】     修复Enum转换的bug（[issue#I49VZB@Gitee](https://gitee.com/dromara/hutool/issues/I49VZB)）
* 【json   】     修复JSONUtil.parse对于MapWrapper识别问题
* 【core   】     修复IdcardUtil.isValidCard判断问题（[issue#I4AJ8S@Gitee](https://gitee.com/dromara/hutool/issues/I4AJ8S)）

-------------------------------------------------------------------------------------------------------------

# 5.7.12 (2021-09-09)

### 🐣新特性
* 【system 】     OshiUtil增加getCurrentProcess方法
* 【extra  】     SpringUtil增加getApplicationName、publishEvent方法（[issue#I485NZ@Gitee](https://gitee.com/dromara/hutool/issues/I485NZ)）
* 【core   】     BeanUtil.getProperty增加判空（[issue#I488HA@Gitee](https://gitee.com/dromara/hutool/issues/I488HA)）
* 【core   】     OptionalBean弃用（[pr#1182@Github](https://github.com/dromara/hutool/pull/1182)）
* 【setting】     Setting、Props持有URL改为持有Resource（[pr#1182@Github](https://github.com/dromara/hutool/pull/1182)）
* 【json   】     JSONUtil.toJsonStr增加重载，支持JSONConfig（[issue#I48H5L@Gitee](https://gitee.com/dromara/hutool/issues/I48H5L)）
* 【crypto 】     SymmetricCrypto增加setMode方法，update采用累加模式（[pr#1642@Github](https://github.com/dromara/hutool/pull/1642)）
* 【core   】     ZipReader支持Filter
* 【all    】     Sftp、Ftp、HttpDownloader增加download重载，支持避免传输文件损坏（[pr#407@Gitee](https://gitee.com/dromara/hutool/pulls/407)）
* 【crypto 】     AES修改构造的IvParameterSpec为AlgorithmParameterSpec（[issue#1814@Gitee](https://gitee.com/dromara/hutool/issues/1814)）
* 【crypto 】     增加FPE、ZUC（[issue#1814@Gitee](https://gitee.com/dromara/hutool/issues/1814)）

### 🐞Bug修复
* 【core   】     修复ListUtil.split方法越界问题（[issue#I48Q0P@Gitee](https://gitee.com/dromara/hutool/issues/I48Q0P)）
* 【core   】     修复QrCode的isTryHarder、isPureBarcode设置无效问题（[issue#1815@Github](https://github.com/dromara/hutool/issues/1815)）
* 【core   】     修复DatePattern.CHINESE_DATE_FORMATTER错误问题（[issue#I48ZE3@Gitee](https://gitee.com/dromara/hutool/issues/I48ZE3)）
* 【core   】     修复ListUtil.split错误问题
* 【core   】     修复NumberUtil.parseNumber长数字越界问题（[issue#1818@Github](https://github.com/dromara/hutool/issues/1818)）

-------------------------------------------------------------------------------------------------------------

# 5.7.11 (2021-08-31)

### 🐣新特性
* 【crypto 】     修改SymmetricCrypto初始化逻辑
* 【core   】     FileTypeUtil增加对wps编辑的docx的识别（[issue#I47JGH@Gitee](https://gitee.com/dromara/hutool/issues/I47JGH)）
* 【core   】     Money修改构造，0表示读取所有分（[issue#1796@Github](https://github.com/dromara/hutool/issues/1796)）
* 【json   】     增加JSONXMLParser和JSONXMLSerializer
* 【json   】     XML支持自定义内容标签（[issue#I47TV8@Gitee](https://gitee.com/dromara/hutool/issues/I47TV8)）
### 🐞Bug修复
* 【cron   】     **重要**修复Scheduler启动默认线程池为null的bug（[issue#I47PZW@Gitee](https://gitee.com/dromara/hutool/issues/I47PZW)）

-------------------------------------------------------------------------------------------------------------

# 5.7.10 (2021-08-26)

### 🐣新特性
* 【core   】     增加NamingCase类
* 【core   】     ListUtil增加page方法重载（[pr#1761@Github](https://github.com/dromara/hutool/pull/1761)）
* 【crypto 】     增加ASN1Util
* 【core   】     CsvConfig改为泛型形式
* 【core   】     增加Partition
* 【http   】     SoapClient.sendForResponse改为public（[issue#I466NN@Gitee](https://gitee.com/dromara/hutool/issues/I466NN)）
* 【core   】     XmlUtil增加append重载（[issue#I466Q0@Gitee](https://gitee.com/dromara/hutool/issues/I466Q0)）
* 【poi    】     增加EscapeStrCellSetter（[issue#I466ZZ@Gitee](https://gitee.com/dromara/hutool/issues/I466ZZ)）
* 【poi    】     ExcelBase增加renameSheet、cloneSheet（[issue#I466ZZ@Gitee](https://gitee.com/dromara/hutool/issues/I466ZZ)）
* 【core   】     ListUtil增加splitAvg方法（[pr#397@Gitee](https://gitee.com/dromara/hutool/pulls/397)）
* 【poi    】     Excel07SaxReader支持数字类型sheet名称、支持sheetName:名称前缀（[issue#I46OMA@Gitee](https://gitee.com/dromara/hutool/issues/I46OMA)）
* 【extra  】     Mail增加build方法（[issue#I46LGE@Gitee](https://gitee.com/dromara/hutool/issues/I46LGE)）
* 【core   】     XmlUtil增加beanToXml重载，支持忽略null
* 【core   】     添加NullComparator、FuncComparator（[issue#I471X7@Gitee](https://gitee.com/dromara/hutool/issues/I471X7)）
* 【core   】     LambdaUtil添加getFieldName（[issue#I4750U@Gitee](https://gitee.com/dromara/hutool/issues/I4750U)）
* 【cron   】     Scheduler增加setThreadExecutor（[issue#I47A6N@Gitee](https://gitee.com/dromara/hutool/issues/I47A6N)）
* 【core   】     CharsetDetector增加detect重载，支持自定义缓存大小（[issue#I478E5@Gitee](https://gitee.com/dromara/hutool/issues/I478E5)）
* 【core   】     增加PartitionIter（[pr#402@Gitee](https://gitee.com/dromara/hutool/pulls/402)）
* 【all    】     增加异常爬栈开关（[pr#403@Gitee](https://gitee.com/dromara/hutool/pulls/403)）
* 【core   】     优化Combination中C(n,n)的逻辑（[pr#1792@Github](https://github.com/dromara/hutool/pull/1792)）
* 【core   】     Csv读写支持别名（[issue#1791@Github](https://github.com/dromara/hutool/issues/1791)）

### 🐞Bug修复
* 【core   】     修复MapUtil.sort比较器不一致返回原map的问题（[issue#I46AQJ@Gitee](https://gitee.com/dromara/hutool/issues/I46AQJ)）
* 【core   】     修复JSONSupport默认循环引用导致的问题（[issue#1779@Github](https://github.com/dromara/hutool/issues/1779)）
* 【poi    】     修复ExcelUtil.readBySax资源没有释放问题（[issue#1789@Github](https://github.com/dromara/hutool/issues/1789)）

-------------------------------------------------------------------------------------------------------------

# 5.7.9 (2021-08-16)

### 🐣新特性
* 【extra  】    FileUtil增加moveContent方法（[issue#I45H30@Gitee](https://gitee.com/dromara/hutool/issues/I45H30)）
* 【extra  】    JschPool.getSession获取时检查是否连接状态（[issue#I45N5I@Gitee](https://gitee.com/dromara/hutool/issues/I45N5I)）
* 
### 🐞Bug修复
* 【extra  】    修复TinyPinyinEngine空构造造成可能的误判问题
* 【http   】    修复在gzip模式下Content-Length服务端设置异常导致的问题（[issue#1766@Github](https://github.com/dromara/hutool/issues/1766)）
* 【db     】    修复PooledDataSource关闭逻辑错误问题

-------------------------------------------------------------------------------------------------------------

# 5.7.8 (2021-08-11)

### 🐣新特性
* 【core   】     MapProxy支持return this的setter方法（[pr#392@Gitee](https://gitee.com/dromara/hutool/pulls/392)）
* 【core   】     BeeDSFactory移除sqlite事务修复代码，新版本BeeCP已修复
* 【core   】     增加compress包，扩充Zip操作灵活性
* 【json   】     增加JSONBeanParser
* 【poi    】     增加CellSetter，可以自定义单元格值写出
* 【poi    】     CsvReader增加readFromStr（[pr#1755@Github](https://github.com/dromara/hutool/pull/1755)）
* 【socket 】     SocketUtil增加connection方法
* 【extra  】     JschUtil增加bindPort重载方法（[issue#I44UTH@Github](https://github.com/dromara/hutool/issues/I44UTH)）
* 【core   】     DefaultTrustManager改为继承X509ExtendedTrustManager
* 【core   】     增加IoCopier

### 🐞Bug修复
* 【core   】     改进NumberChineseFormatter算法，补充完整单元测试，解决零问题
* 【core   】     修复Img变换操作图片格式问题（[issue#I44JRB@Gitee](https://gitee.com/dromara/hutool/issues/I44JRB)）

-------------------------------------------------------------------------------------------------------------

# 5.7.7 (2021-08-02)

### 🐣新特性
* 【core   】     增加LookupFactory和MethodHandleUtil（[issue#I42TVY@Gitee](https://gitee.com/dromara/hutool/issues/I42TVY)）
* 【core   】     改进RegexPool.TEL支持无-号码（[pr#387@Gitee](https://gitee.com/dromara/hutool/pulls/387)）
* 【core   】     PhoneUtil中新增获取固话号码中区号,以及固话号码中号码的方法（[pr#387@Gitee](https://gitee.com/dromara/hutool/pulls/387)）
* 【json   】     JSONGetter增加getLocalDateTime方法（[pr#387@Gitee](https://gitee.com/dromara/hutool/pulls/387)）
* 【core   】     增加JNDIUtil（[issue#1727@Github](https://github.com/dromara/hutool/issues/1727)）
* 【core   】     NetUtil增加getDnsInfo方法（[issue#1727@Github](https://github.com/dromara/hutool/issues/1727)）
* 【core   】     SpringUtil增加unregisterBean方法（[pr#388@Gitee](https://gitee.com/dromara/hutool/pulls/388)）
* 【core   】     优化TextSimilarity公共子串算法（[issue#I42A6V@Gitee](https://gitee.com/dromara/hutool/issues/I42A6V)）
* 【core   】     优化DateUtil.parse对UTC附带时区字符串解析（[issue#I437AP@Gitee](https://gitee.com/dromara/hutool/issues/I437AP)）

### 🐞Bug修复
* 【jwt    】     修复JWTUtil中几个方法非static的问题（[issue#1735@Github](https://github.com/dromara/hutool/issues/1735)）
* 【core   】     修复SpringUtil无法处理autowired问题（[pr#388@Gitee](https://gitee.com/dromara/hutool/pulls/388)）
* 【core   】     修复AbsCollValueMap中常量拼写错误（[pr#1736@Github](https://github.com/dromara/hutool/pull/1736)）
* 【core   】     修复FileUtil.del在文件只读情况下无法删除的问题（[pr#389@Gitee](https://gitee.com/dromara/hutool/pulls/389)）
* 【core   】     修复FileUtil.move在不同分区下失败的问题（[pr#390@Gitee](https://gitee.com/dromara/hutool/pulls/390)）
* 【core   】     修复FileUtil.copy强制覆盖参数无效问题
* 【core   】     修复NumberChineseFormatter转换金额多零问题（[issue#1739@Github](https://github.com/dromara/hutool/issues/1739)）

-------------------------------------------------------------------------------------------------------------

# 5.7.6 (2021-07-28)

### 🐣新特性
* 【core   】     增加FieldsComparator（[pr#374@Gitee](https://gitee.com/dromara/hutool/pulls/374)）
* 【core   】     FileUtil.del采用Files.delete实现
* 【core   】     改进Base64.isBase64方法增加等号判断（[issue#1710@Github](https://github.com/dromara/hutool/issues/1710)）
* 【core   】     Sftp增加syncUpload方法（[pr#375@Gitee](https://gitee.com/dromara/hutool/pulls/375)）
* 【core   】     改进NetUtil.getLocalHost逻辑（[issue#1717@Github](https://github.com/dromara/hutool/issues/1717)）
* 【core   】     UseragentUtil增加QQ、alipay、taobao、uc等浏览器识别支持（[issue#1719@Github](https://github.com/dromara/hutool/issues/1719)）
* 【http   】     HttpRequest.form方法判断集合增强（[pr#381@Gitee](https://gitee.com/dromara/hutool/pulls/381)）
* 【core   】     NumberUtil增加calculate方法
* 【core   】     优化TextSimilarity.longestCommonSubstring性能（[issue#I42A6V@Gitee](https://gitee.com/dromara/hutool/issues/I42A6V)）
* 【core   】     MultipartRequestInputStream改为使用long以支持大文件（[issue#I428AN@Gitee](https://gitee.com/dromara/hutool/issues/I428AN)）
* 【core   】     RobotUtil增加getDelay、getRobot方法（[pr#1725@Github](https://github.com/dromara/hutool/pull/1725)）
* 【json   】     JSON输出支持ignoreNull（[issue#1728@Github](https://github.com/dromara/hutool/issues/1728)）
* 【core   】     DateUtil和LocalDateTimeUtil增加isWeekend方法（[issue#I42N5A@Gitee](https://gitee.com/dromara/hutool/issues/I42N5A)）

### 🐞Bug修复
* 【core   】     修复RobotUtil双击右键问题（[pr#1721@Github](https://github.com/dromara/hutool/pull/1721)）
* 【core   】     修复FileTypeUtil判断wps修改过的xlsx误判为jar的问题（[pr#380@Gitee](https://gitee.com/dromara/hutool/pulls/380)）
* 【core   】     修复Sftp.isDir异常bug（[pr#378@Gitee](https://gitee.com/dromara/hutool/pulls/378)）
* 【core   】     修复BeanUtil.copyProperties集合元素复制成功，读取失败的问题（[issue#I41WKP@Gitee](https://gitee.com/dromara/hutool/issues/I41WKP)）
* 【core   】     修复NumberChineseFormatter.chineseToNumber十位数错误（[issue#1726@github](https://github.com/dromara/hutool/issues/1726)）
* 【poi    】     修复BeanSheetReader.read中字段对象为空导致的报错（[issue#1729@Github](https://github.com/dromara/hutool/issues/1729)）
* 【core   】     修复DateConverter转换java.sql.Date问题（[issue#1729@Github](https://github.com/dromara/hutool/issues/1729)）
* 【extra  】     修复CompressUtil中部分方法非static的问题（[pr#385@Gitee](https://gitee.com/dromara/hutool/pulls/385)）
* 【core   】     修复ByteUtil转换端序错误问题（[pr#384@Gitee](https://gitee.com/dromara/hutool/pulls/384)）
* 【core   】     修复UserAgentUtil判断浏览器顺序问题（[issue#I42LYW@Gitee](https://gitee.com/dromara/hutool/issues/I42LYW)）

-------------------------------------------------------------------------------------------------------------

# 5.7.5 (2021-07-19)

### 🐣新特性
* 【core   】     DateUtil增加ceiling重载，可选是否归零毫秒
* 【core   】     IterUtil增加firstMatch方法
* 【core   】     增加NanoId
* 【core   】     MapBuilder增加put方法（[pr#367@Gitee](https://gitee.com/dromara/hutool/pulls/367)）
* 【core   】     StrUtil.insert支持负数index
* 【core   】     Calculator类支持取模运算（[issue#I40DUW@Gitee](https://gitee.com/dromara/hutool/issues/I40DUW)）
* 【core   】     增加Base64.isBase64方法（[issue#1710@Github](https://github.com/dromara/hutool/issues/1710)）
* 【core   】     ManifestUtil新增方法getManifest(Class<?> cls)（[pr#370@Gitee](https://gitee.com/dromara/hutool/pulls/370)）
* 【extra  】     AbstractFtp增加isDir方法（[issue#1716@Github](https://github.com/dromara/hutool/issues/1716)）
* 【core   】     修改FileUtil异常信息内容（[pr#1713@Github](https://github.com/dromara/hutool/pull/1713)）

### 🐞Bug修复
* 【core   】     修复FileUtil.normalize处理上级路径的问题（[issue#I3YPEH@Gitee](https://gitee.com/dromara/hutool/issues/I3YPEH)）
* 【core   】     修复ClassScanner扫描空包遗漏问题
* 【core   】     修复FastDatePrinter歧义问题（[pr#366@Gitee](https://gitee.com/dromara/hutool/pulls/366)）
* 【core   】     修复DateUtil.format格式化Instant报错问题（[issue#I40CY2@Gitee](https://gitee.com/dromara/hutool/issues/I40CY2)）
* 【core   】     修复StrUtil.toUnderlineCase大写问题（[issue#I40CGS@Gitee](https://gitee.com/dromara/hutool/issues/I40CGS)）
* 【jwt    】     修复JWT.validate报错问题（[issue#I40MR2@Gitee](https://gitee.com/dromara/hutool/issues/I40MR2)）
* 【core   】     修复StrUtil.brief越界问题

-------------------------------------------------------------------------------------------------------------

# 5.7.4 (2021-07-10)

### 🐣新特性
* 【crypto 】     SmUtil.sm4统一返回类型（[issue#I3YKD4@Gitee](https://gitee.com/dromara/hutool/issues/I3YKD4)）
* 【core   】     修改MapUtil.get传入null返回默认值而非null（[issue#I3YKBC@Gitee](https://gitee.com/dromara/hutool/issues/I3YKBC)）
* 【core   】     HexUtil增加hexToLong、hexToInt（[issue#I3YQEV@Gitee](https://gitee.com/dromara/hutool/issues/I3YQEV)）
* 【core   】     CsvWriter增加writer.write(csvData)的方法重载（[pr#353@Gitee](https://gitee.com/dromara/hutool/pulls/353)）
* 【core   】     新增AbsCollValueMap（[issue#I3YXF0@Gitee](https://gitee.com/dromara/hutool/issues/I3YXF0)）
* 【crypto 】     HOTP缓存改为8位，新增方法（[pr#356@Gitee](https://gitee.com/dromara/hutool/pulls/356)）
* 【setting】     Props增加toProperties方法（[issue#1701@Github](https://github.com/dromara/hutool/issues/1701)）
* 【http   】     UserAgent增加getOsVersion方法（[issue#I3YZUQ@Gitee](https://gitee.com/dromara/hutool/issues/I3YZUQ)）
* 【jwt    】     JWT增加validate方法（[issue#I3YDM4@Gitee](https://gitee.com/dromara/hutool/issues/I3YDM4)）
* 【core   】     CscReader支持指定读取开始行号和结束行号（[issue#I3ZMZL@Gitee](https://gitee.com/dromara/hutool/issues/I3ZMZL)）

### 🐞Bug修复
* 【core   】     修复RadixUtil.decode非static问题（[issue#I3YPEH@Gitee](https://gitee.com/dromara/hutool/issues/I3YPEH)）
* 【core   】     修复EqualsBuilder数组判断问题（[pr#1694@Github](https://github.com/dromara/hutool/pull/1694)）
* 【setting】     修复Props中Charset对象无法序列化的问题（[pr#1694@Github](https://github.com/dromara/hutool/pull/1694)）
* 【db     】     修复PageResult首页判断逻辑问题（[issue#1699@Github](https://github.com/dromara/hutool/issues/1699)）
* 【core   】     修复IdcardUtil可能数组越界问题（[pr#1702@Github](https://github.com/dromara/hutool/pull/1702)）
* 【core   】     修复FastByteArrayOutputStream索引越界问题（[issue#I402ZP@Github](https://github.com/dromara/hutool/issues/I402ZP)）

-------------------------------------------------------------------------------------------------------------

# 5.7.3 (2021-06-29)

### 🐣新特性
* 【core   】     增加Convert.toSet方法（[issue#I3XFG2@Gitee](https://gitee.com/dromara/hutool/issues/I3XFG2)）
* 【core   】     CsvWriter增加writeBeans方法（[pr#345@Gitee](https://gitee.com/dromara/hutool/pulls/345)）
* 【core   】     新增JAXBUtil（[pr#346@Gitee](https://gitee.com/dromara/hutool/pulls/346)）
* 【poi    】     ExcelWriter新增setColumnStyleIfHasData和setRowStyleIfHasData（[pr#347@Gitee](https://gitee.com/dromara/hutool/pulls/347)）
* 【json   】     用户自定义日期时间格式时，解析也读取此格式
* 【core   】     增加可自定义日期格式GlobalCustomFormat
* 【jwt    】     JWT修改默认有序，并规定payload日期格式为秒数
* 【json   】     增加JSONWriter
* 【core   】     IdUtil增加getWorkerId和getDataCenterId（[issueI3Y5NI@Gitee](https://gitee.com/dromara/hutool/issues/I3Y5NI)）
* 【core   】     JWTValidator增加leeway重载
* 【core   】     增加RegexPool（[issue#I3W9ZF@gitee](https://gitee.com/dromara/hutool/issues/I3W9ZF)）

### 🐞Bug修复
* 【json   】     修复XML转义字符的问题（[issue#I3XH09@Gitee](https://gitee.com/dromara/hutool/issues/I3XH09)）
* 【core   】     修复FormatCache中循环引用异常（[pr#1673@Github](https://github.com/dromara/hutool/pull/1673)）
* 【core   】     修复IdcardUtil.getIdcardInfo.getProvinceCode获取为汉字的问题（[issue#I3XP4Q@Gitee](https://gitee.com/dromara/hutool/issues/I3XP4Q)）
* 【core   】     修复CollUtil.subtract使用非标准Set等空指针问题（[issue#I3XN1Z@Gitee](https://gitee.com/dromara/hutool/issues/I3XN1Z)）
* 【core   】     修复SqlFormatter部分SQL空指针问题（[issue#I3XS44@Gitee](https://gitee.com/dromara/hutool/issues/I3XS44)）
* 【core   】     修复DateRange计算问题（[issue#I3Y1US@Gitee](https://gitee.com/dromara/hutool/issues/I3Y1US)）
* 【core   】     修复BeanCopier中setFieldNameEditor失效问题（[pr#349@Gitee](https://gitee.com/dromara/hutool/pulls/349)）
* 【core   】     修复ArrayUtil.indexOfSub查找bug（[issue#1683@Github](https://github.com/dromara/hutool/issues/1683)）
* 【core   】     修复Node的权重比较空指针问题（[issue#1681@Github](https://github.com/dromara/hutool/issues/1681)）
* 【core   】     修复UrlQuery传入无参数路径解析问题（[issue#1688@Github](https://github.com/dromara/hutool/issues/1688)）

-------------------------------------------------------------------------------------------------------------

# 5.7.2 (2021-06-20)

### 🐣新特性
* 【core   】     增加UserPassAuthenticator
* 【db     】     获取分组数据源时，移除公共属性项
* 【core   】     增加StrJoiner
* 【core   】     增加TreeBuilder
* 【core   】     IterUtil增加getFirstNonNull方法
* 【core   】     NumberUtil判空改为isBlank（[issue#1664@Github](https://github.com/dromara/hutool/issues/1664)）
* 【jwt    】     增加JWTValidator、RegisteredPayload
* 【db     】     增加Phoenix方言（[issue#1656@Github](https://github.com/dromara/hutool/issues/1656)）

### 🐞Bug修复
* 【db     】     修复Oracle下别名错误造成的SQL语法啊错误（[issue#I3VTQW@Gitee](https://gitee.com/dromara/hutool/issues/I3VTQW)）
* 【core   】     修复ConcurrencyTester重复使用时开始测试未清空之前任务的问题（[issue#I3VSDO@Gitee](https://gitee.com/dromara/hutool/issues/I3VSDO)）
* 【poi    】     修复使用BigWriter写出，ExcelWriter修改单元格值失败的问题（[issue#I3VSDO@Gitee](https://gitee.com/dromara/hutool/issues/I3VSDO)）
* 【jwt    】     修复Hmac算法下生成签名是hex的问题（[issue#I3W6IP@Gitee](https://gitee.com/dromara/hutool/issues/I3W6IP)）
* 【core   】     修复TreeUtil.build中deep失效问题（[issue#1661@Github](https://github.com/dromara/hutool/issues/1661)）
* 【json   】     修复XmlUtil.xmlToBean判断问题（[issue#1663@Github](https://github.com/dromara/hutool/issues/1663)）

-------------------------------------------------------------------------------------------------------------

# 5.7.1 (2021-06-16)

### 🐣新特性
* 【db     】     NamedSql支持in操作([issue#1652@Github](https://github.com/dromara/hutool/issues/1652))
* 【all    】     JWT模块加入到all和bom包中([issue#1654@Github](https://github.com/dromara/hutool/issues/1654))
* 【core   】     CollUtil删除所有Map相关操作
* 【all    】     **重要！** 删除过期方法
* 【core   】     增加IterChian类

### 🐞Bug修复

-------------------------------------------------------------------------------------------------------------

# 5.7.0 (2021-06-15)

### 🐣新特性
* 【jwt    】     添加JWT模块，实现了JWT的创建、解析和验证
* 【crypto 】     SymmetricCrypto增加update方法（[pr#1642@Github](https://github.com/dromara/hutool/pull/1642)）
* 【crypto 】     MacEngine增加接口update,doFinal,reset等接口
* 【core   】     StrSpliter更名为StrSplitter
* 【core   】     NumberUtil的decimalFormat增加数字检查
* 【http   】     HttpBase的httpVersion方法设置为无效([issue#1644@Github](https://github.com/dromara/hutool/issues/1644))
* 【extra  】     Sftp增加download重载([issue#I3VBSL@Gitee](https://gitee.com/dromara/hutool/issues/I3VBSL))
* 【cache  】     修改FIFOCache初始大小([issue#1647@Github](https://github.com/dromara/hutool/issues/1647))

### 🐞Bug修复
* 【db     】     修复count方法丢失参数问题([issue#I3VBSL@Gitee](https://gitee.com/dromara/hutool/issues/I3VBSL))
* 【db     】     修复SpringUtil工具在`@PostConstruct` 注解标注的方法下失效问题([pr#341@Gitee](https://gitee.com/dromara/hutool/pulls/341))
* 【json   】     修复JSONUtil.parse方法未判断有序问题([issue#I3VHVY@Gitee](https://gitee.com/dromara/hutool/issues/I3VHVY))
* 【json   】     修复JSONArray.put越界无法加入问题([issue#I3VMLU@Gitee](https://gitee.com/dromara/hutool/issues/I3VMLU))

-------------------------------------------------------------------------------------------------------------

# 5.6.7 (2021-06-08)

### 🐣新特性
* 【core   】     CharSequenceUtil增加join重载（[issue#I3TFJ5@Gitee](https://gitee.com/dromara/hutool/issues/I3TFJ5)）
* 【http   】     HttpRequest增加form方法重载（[pr#337@Gitee](https://gitee.com/dromara/hutool/pulls/337)）
* 【http   】     ImgUtil增加getMainColor方法（[pr#338@Gitee](https://gitee.com/dromara/hutool/pulls/338)）
* 【core   】     改进TreeUtil.buid算法性能（[pr#1594@Github](https://github.com/dromara/hutool/pull/1594)）
* 【core   】     CsvConfig的setXXX返回this（[issue#I3UIQF@Gitee](https://gitee.com/dromara/hutool/issues/I3UIQF)）
* 【all    】     增加jmh基准测试
* 【core   】     增加StreamUtil和CollectorUtil
* 【poi    】     增加content-type([pr#1639@Github](https://github.com/dromara/hutool/pull/1639))

### 🐞Bug修复
* 【core   】     修复FileUtil.normalize去掉末尾空格问题（[issue#1603@Github](https://github.com/dromara/hutool/issues/1603)）
* 【core   】     修复CharsetDetector流关闭问题（[issue#1603@Github](https://github.com/dromara/hutool/issues/1603)）
* 【core   】     修复RuntimeUtil.exec引号内空格被切分的问题（[issue#I3UAYB@Gitee](https://gitee.com/dromara/hutool/issues/I3UAYB)）

-------------------------------------------------------------------------------------------------------------

# 5.6.6 (2021-05-26)

### 🐣新特性
* 【cron   】     增加时间轮简单实现
* 【core   】     BeanUtil.copyToList增加重载（[pr#321@Gitee](https://gitee.com/dromara/hutool/pulls/321)）
* 【core   】     SyncFinisher增加stop方法（[issue#1578@Github](https://github.com/dromara/hutool/issues/1578)）
* 【cache  】     CacheObj默认方法改为protected（[issue#I3RIEI@Gitee](https://gitee.com/dromara/hutool/issues/I3RIEI)）
* 【core   】     FileUtil.isEmpty不存在时返回true（[issue#1582@Github](https://github.com/dromara/hutool/issues/1582)）
* 【core   】     PhoneUtil增加中国澳门和中国台湾手机号校检方法（[pr#331@Gitee](https://gitee.com/dromara/hutool/pulls/331)）
* 【db     】     分页查询，自定义sql查询，添加参数（[pr#332@Gitee](https://gitee.com/dromara/hutool/pulls/332)）
* 【core   】     IdCardUtil.isValidCard增加非空判断
* 【json   】     JSONObject构造增加SortedMap判断（[pr#333@Gitee](https://gitee.com/dromara/hutool/pulls/333)）
* 【core   】     Tuple增加部分方法（[pr#333@Gitee](https://gitee.com/dromara/hutool/pulls/333)）
* 【log    】     增加LogTube支持
* 【core   】     增加BitStatusUtil（[pr#1600@Github](https://github.com/dromara/hutool/pull/1600)）

### 🐞Bug修复
* 【core   】     修复XmlUtil中omitXmlDeclaration参数无效问题（[issue#1581@Github](https://github.com/dromara/hutool/issues/1581)）
* 【core   】     修复NumberUtil.decimalFormat参数传错的问题（[issue#I3SDS3@Gitee](https://gitee.com/dromara/hutool/issues/I3SDS3)）
* 【json   】     修复JSONArray.put方法不能覆盖值的问题
* 【poi    】     修复sax方式读取xls无法根据sheet名称获取数据（[issue#I3S4NH@Gitee](https://gitee.com/dromara/hutool/issues/I3S4NH)）
* 【core   】     修复路径中多个~都被替换的问题（[pr#1599@Github](https://github.com/dromara/hutool/pull/1599)）
* 【core   】     修复CRC16构造非public问题（[issue#1601@Github](https://github.com/dromara/hutool/issues/1601)）

-------------------------------------------------------------------------------------------------------------
# 5.6.5 (2021-05-08)

### 🐣新特性
* 【http   】     HttpUtil增加closeCookie方法
* 【core   】     NumberUtil增加方法decimalFormat重载（[issue#I3OSA2@Gitee](https://gitee.com/dromara/hutool/issues/I3OSA2)）
* 【extra  】     Ftp的remoteVerificationEnabled改为false（[issue#I3OSA2@Gitee](https://gitee.com/dromara/hutool/issues/I3OSA2)）
* 【core   】     MaskBit增加掩码反向转换的方法getMaskBit()（[pr#1563@Github](https://github.com/dromara/hutool/pull/1563)）
* 【core   】     ReUtil等增加indexOf、delLast等方法（[pr#1555@Github](https://github.com/dromara/hutool/pull/1555)）
* 【poi    】     ExcelWriter增加writeSecHeadRow，增加合并单元格边框颜色样式（[pr#318@Gitee](https://gitee.com/dromara/hutool/pulls/318)）

### 🐞Bug修复
* 【core   】     修复createScheduledExecutor单位不是毫秒的问题（[issue#I3OYIW@Gitee](https://gitee.com/dromara/hutool/issues/I3OYIW)）
* 【core   】     修复Tailer无stop问题（[issue#I3PQLQ@Gitee](https://gitee.com/dromara/hutool/issues/I3PQLQ)）
* 【core   】     修复空白excel读取报错问题（[issue#1552@Github](https://github.com/dromara/hutool/issues/1552)）
* 【extra  】     修复Sftp.mkDirs报错问题（[issue#1536@Github](https://github.com/dromara/hutool/issues/1536)）
* 【core   】     修复Bcrypt不支持$2y$盐前缀问题（[pr#1560@Github](https://github.com/dromara/hutool/pull/1560)）
* 【system 】     修复isWindows8拼写问题（[pr#1557@Github](https://github.com/dromara/hutool/pull/1557)）
* 【db     】     修复MongoDS默认分组参数失效问题（[issue#1548@Github](https://github.com/dromara/hutool/issues/1548)）
* 【core   】     修复UrlPath编码的字符问题导致的URL编码异常（[issue#1537@Github](https://github.com/dromara/hutool/issues/1537)）

-------------------------------------------------------------------------------------------------------------

# 5.6.4 (2021-04-25)

### 🐣新特性
* 【core   】     DatePattern补充DateTimeFormatter（[pr#308@Gitee](https://gitee.com/dromara/hutool/pulls/308)）
* 【core   】     DateUtil.compare增加支持给定格式比较（[pr#310@Gitee](https://gitee.com/dromara/hutool/pulls/310)）
* 【core   】     BeanUtil增加edit方法（[issue#I3J6BG@Gitee](https://gitee.com/dromara/hutool/issues/I3J6BG)）
* 【db     】     Column中加入columnDef字段默认值（[issue#I3J6BG@Gitee](https://gitee.com/dromara/hutool/issues/I3J6BG)）
* 【core   】     BeanUtil增加copyToList方法（[issue#1526@Github](https://github.com/dromara/hutool/issues/1526)）
* 【extra  】     MailAccount增加customProperty可以用户自定义属性（[pr#317@Gitee](https://gitee.com/dromara/hutool/pulls/317)）
* 【system 】     SystemUtil.getUserInfo()中所有平台路径统一末尾加/（[issue#I3NM39@Gitee](https://gitee.com/dromara/hutool/issues/I3NM39)）
* 【http   】     新增HttpDownloader，默认开启自动跳转（[issue#I3NM39@Gitee](https://gitee.com/dromara/hutool/issues/I3NM39)）

### 🐞Bug修复
* 【db     】     修复SQL分页时未使用别名导致的错误，同时count时取消order by子句（[issue#I3IJ8X@Gitee](https://gitee.com/dromara/hutool/issues/I3IJ8X)）
* 【extra  】     修复Sftp.reconnectIfTimeout方法判断错误（[issue#1524@Github](https://github.com/dromara/hutool/issues/1524)）
* 【core   】     修复NumberChineseFormatter转数字问题（[issue#I3IS3S@Gitee](https://gitee.com/dromara/hutool/issues/I3IS3S)）

-------------------------------------------------------------------------------------------------------------

# 5.6.3 (2021-04-10)

### 🐣新特性
* 【core   】     修改数字转换的实现，增加按照指定端序转换（[pr#1492@Github](https://github.com/dromara/hutool/pull/1492)）
* 【core   】     修改拆分byte数组时最后一组长度的规则（[pr#1494@Github](https://github.com/dromara/hutool/pull/1494)）
* 【core   】     新增根据日期获取节气（[pr#1496@Github](https://github.com/dromara/hutool/pull/1496)）
* 【core   】     mapToBean()添加对布尔值is前缀的识别（[pr#294@Gitee](https://gitee.com/dromara/hutool/pulls/294)）
* 【core   】     农历十月十一月改为寒月和冬月（[pr#301@Gitee](https://gitee.com/dromara/hutool/pulls/301)）
* 【core   】     增加港澳台电话正则（[pr#301@Gitee](https://gitee.com/dromara/hutool/pulls/301)）
* 【core   】     增加银行卡号脱敏（[pr#301@Gitee](https://gitee.com/dromara/hutool/pulls/301)）
* 【cache  】     使用LongAddr代替AtomicLong（[pr#301@Gitee](https://gitee.com/dromara/hutool/pulls/301)）
* 【cache  】     EnumUtil使用LinkedHashMap（[pr#304@Gitee](https://gitee.com/dromara/hutool/pulls/304)）
* 【crypto 】     SymmetricCrypto支持大量数据加密解密（[pr#1497@Gitee](https://gitee.com/dromara/hutool/pulls/1497)）
* 【http   】     SoapClient增加针对不同协议的头信息（[pr#305@Gitee](https://gitee.com/dromara/hutool/pulls/305)）
* 【http   】     HttpRequest支持307、308状态码识别（[issue#1504@Github](https://github.com/dromara/hutool/issues/1504)）
* 【core   】     CharUtil.isBlankChar增加\u0000判断（[pr#1505@Github](https://github.com/dromara/hutool/pull/1505)）
* 【extra  】     添加Houbb Pinyin支持（[pr#1506@Github](https://github.com/dromara/hutool/pull/1506)）
* 【core   】     添加LambdaUtil（[pr#295@Gitee](https://gitee.com/dromara/hutool/pulls/295)）
* 【core   】     添加StrPool和CharPool
* 【extra  】     CglibUtil增加toBean和fillBean方法
* 【db     】     增加DriverNamePool

### 🐞Bug修复
* 【core   】     修复Validator.isUrl()传空返回true（[issue#I3ETTY@Gitee](https://gitee.com/dromara/hutool/issues/I3ETTY)）
* 【db     】     修复数据库driver根据url的判断识别错误问题（[issue#I3EWBI@Gitee](https://gitee.com/dromara/hutool/issues/I3EWBI)）
* 【json   】     修复JSONStrFormatter换行多余空行问题（[issue#I3FA8B@Gitee](https://gitee.com/dromara/hutool/issues/I3FA8B)）
* 【core   】     修复UrlPath中的+被转义为空格%20的问题（[issue#1501@Github](https://github.com/dromara/hutool/issues/1501)）
* 【core   】     修复DateUtil.parse方法对UTC时间毫秒少于3位不识别问题（[issue#1503@Github](https://github.com/dromara/hutool/issues/1503)）

-------------------------------------------------------------------------------------------------------------

# 5.6.2 (2021-03-28)

### 🐣新特性
* 【core   】     Validator增加车架号(车辆识别码)验证、驾驶证（驾驶证档案编号）的正则校验（[pr#280@Gitee](https://gitee.com/dromara/hutool/pulls/280)）
* 【core   】     CopyOptions增加propertiesFilter（[pr#281@Gitee](https://gitee.com/dromara/hutool/pulls/281)）
* 【extra  】     增加Wit模板引擎支持
* 【core   】     增加DesensitizedUtil（[pr#282@Gitee](https://gitee.com/dromara/hutool/pulls/282)）
* 【core   】     增加DateTime字符串构造（[issue#I3CQZG@Gitee](https://gitee.com/dromara/hutool/issues/I3CQZG)）
* 【core   】     修改ArrayUtil代码风格（[pr#287@Gitee](https://gitee.com/dromara/hutool/pulls/287)）
* 【json   】     JSONConfig增加setStripTrailingZeros配置（[issue#I3DJI8@Gitee](https://gitee.com/dromara/hutool/issues/I3DJI8)）
* 【db     】     升级兼容BeeCP3.x

### 🐞Bug修复
* 【core   】     修复FileTypeUtil中OFD格式判断问题（[pr#1489@Github](https://github.com/dromara/hutool/pull/1489)）
* 【core   】     修复CamelCaseLinkedMap和CaseInsensitiveLinkedMap的Linked失效问题（[pr#1490@Github](https://github.com/dromara/hutool/pull/1490)）
* 【core   】     修复UrlPath中=被转义的问题

-------------------------------------------------------------------------------------------------------------

# 5.6.1 (2021-03-18)

### 🐣新特性
* 【crypto 】     SecureUtil去除final修饰符（[issue#1474@Github](https://github.com/dromara/hutool/issues/1474)）
* 【core   】     IoUtil增加lineIter方法
* 【core   】     新增函数式懒加载加载器([pr#275@Gitee](https://gitee.com/dromara/hutool/pulls/275))
* 【http   】     UserAgentUtil增加miniProgram判断([issue#1475@Github](https://github.com/dromara/hutool/issues/1475))
* 【db     】     增加Ignite数据库驱动识别
* 【core   】     DateUtil.parse支持带毫秒的UTC时间
* 【core   】     IdcardUtil.Idcard增加toString（[pr#1487@Github](https://github.com/dromara/hutool/pull/1487)）
* 【core   】     ChineseDate增加getGregorianXXX方法（[issue#1481@Github](https://github.com/dromara/hutool/issues/1481)）

### 🐞Bug修复
* 【core   】     修复IoUtil.readBytes的FileInputStream中isClose参数失效问题（[issue#I3B7UD@Gitee](https://gitee.com/dromara/hutool/issues/I3B7UD)）
* 【core   】     修复DataUnit中KB不大写的问题
* 【json   】     修复JSONUtil.getByPath类型错误问题（[issue#I3BSDF@Gitee](https://gitee.com/dromara/hutool/issues/I3BSDF)）
* 【core   】     修复BeanUtil.toBean提供null未返回null的问题（[issue#I3BQPV@Gitee](https://gitee.com/dromara/hutool/issues/I3BQPV)）
* 【core   】     修复ModifierUtil#modifiersToInt中逻辑判断问题（[issue#1486@Github](https://github.com/dromara/hutool/issues/1486)）

-------------------------------------------------------------------------------------------------------------

# 5.6.0 (2021-03-12)

### 🐣新特性
* 【poi    】     重要：不再兼容POI-3.x，增加兼容POI-5.x（[issue#I35J6B@Gitee](https://gitee.com/dromara/hutool/issues/I35J6B)）
* 【core   】     FileTypeUtil使用长匹配优先（[pr#1457@Github](https://github.com/dromara/hutool/pull/1457)）
* 【core   】     IterUtil和CollUtil增加isEqualList方法（[issue#I3A3PY@Gitee](https://gitee.com/dromara/hutool/issues/I3A3PY)）
* 【crypto 】     增加PBKDF2（[issue#1416@Github](https://github.com/dromara/hutool/issues/1416)）
* 【core   】     增加FuncKeyMap（[issue#1402@Github](https://github.com/dromara/hutool/issues/1402)）
* 【core   】     增加StrMatcher（[issue#1379@Github](https://github.com/dromara/hutool/issues/1379)）
* 【core   】     NumberUtil增加factorial针对BigInterger方法（[issue#1379@Github](https://github.com/dromara/hutool/issues/1379)）
* 【core   】     TreeNode增加equals方法（[issue#1467@Github](https://github.com/dromara/hutool/issues/1467)）
* 【core   】     增加汉字转阿拉伯数字Convert.chineseToNumber（[pr#1469@Github](https://github.com/dromara/hutool/pull/1469)）
* 【json   】     JSONUtil增加getByPath方法支持默认值（[issue#1470@Github](https://github.com/dromara/hutool/issues/1470)）
* 【crypto 】     SecureUtil增加hmacSha256方法（[pr#1473@Github](https://github.com/dromara/hutool/pull/1473)）
* 【core   】     FileTypeUtil判断流增加文件名辅助判断（[pr#1471@Github](https://github.com/dromara/hutool/pull/1471)）

### 🐞Bug修复
* 【socket 】     修复Client创建失败资源未释放问题。
* 【core   】     修复DataSizeUtil中EB单位错误问题（[issue#I39O7I@Gitee](https://gitee.com/dromara/hutool/issues/I39O7I)）
* 【core   】     修复BeanDesc.isMatchSetter的ignoreCase未使用问题（[issue#I3AXIJ@Gitee](https://gitee.com/dromara/hutool/issues/I3AXIJ)）
* 【core   】     修复CRC16Checksum中（[issue#I3AXIJ@Gitee](https://gitee.com/dromara/hutool/issues/I3AXIJ)）
* 【core   】     修复UrlQuery中对空key解析丢失问题（[issue#I3B3J6@Gitee](https://gitee.com/dromara/hutool/issues/I3B3J6)）

-------------------------------------------------------------------------------------------------------------

# 5.5.9 (2021-02-26)

### 🐣新特性
* 【crypto 】     PemUtil.readPemKey支持EC（[pr#1366@Github](https://github.com/dromara/hutool/pull/1366)）
* 【extra  】     Ftp等cd方法增加同步（[issue#1397@Github](https://github.com/dromara/hutool/issues/1397)）
* 【core   】     StrUtil增加endWithAnyIgnoreCase（[issue#I37I0B@Gitee](https://gitee.com/dromara/hutool/issues/I37I0B)）
* 【crypto 】     Sm2增加getD和getQ方法（[issue#I37Z4C@Gitee](https://gitee.com/dromara/hutool/issues/I37Z4C)）
* 【cache  】     AbstractCache增加keySet方法（[issue#I37Z4C@Gitee](https://gitee.com/dromara/hutool/issues/I37Z4C)）
* 【core   】     NumberWordFormatter增加formatSimple方法（[pr#1436@Github](https://github.com/dromara/hutool/pull/1436)）
* 【crypto 】     增加读取openSSL生成的sm2私钥
* 【crypto 】     增加众多方法，SM2兼容各类密钥格式（[issue#I37Z75@Gitee](https://gitee.com/dromara/hutool/issues/I37Z75)）

### 🐞Bug修复
* 【json   】     JSONUtil.isJson方法改变trim策略，解决特殊空白符导致判断失败问题
* 【json   】     修复SQLEXception导致的栈溢出（[issue#1399@Github](https://github.com/dromara/hutool/issues/1399)）
* 【extra  】     修复Ftp中异常参数没有传入问题（[issue#1397@Github](https://github.com/dromara/hutool/issues/1397)）
* 【crypto 】     修复Sm2使用D构造空指针问题（[issue#I37Z4C@Gitee](https://gitee.com/dromara/hutool/issues/I37Z4C)）
* 【poi    】     修复ExcelPicUtil中图表报错问题（[issue#I38857@Gitee](https://gitee.com/dromara/hutool/issues/I38857)）
* 【core   】     修复ListUtil.page方法返回空列表无法编辑问题（[issue#1415@Github](https://github.com/dromara/hutool/issues/1415)）
* 【core   】     修复ListUtil.sub中step不通结果不一致问题（[issue#1409@Github](https://github.com/dromara/hutool/issues/1409)）
* 【db     】     修复Condition转换参数值时未转换数字异常（[issue#I38LTM@Gitee](https://gitee.com/dromara/hutool/issues/I38LTM)）

-------------------------------------------------------------------------------------------------------------

# 5.5.8 (2021-01-30)

### 🐣新特性
* 【extra  】     增加自动装配SpringUtil类（[pr#1366@Github](https://github.com/dromara/hutool/pull/1366)）
* 【extra  】     ArrayUtil增加map方法重载
* 【crypto 】     AsymmetricAlgorithm增加RSA_ECB("RSA/ECB/NoPadding")（[issue#1368@Github](https://github.com/dromara/hutool/issues/1368)）
* 【core   】     补充StrUtil.padXXX注释（[issue#I2E1S7@Gitee](https://gitee.com/dromara/hutool/issues/I2E1S7)）
* 【core   】     修改上传文件检查逻辑
* 【core   】     修正LocalDateTimeUtil.offset方法注释问题（[issue#I2EEXC@Gitee](https://gitee.com/dromara/hutool/issues/I2EEXC)）
* 【extra  】     VelocityEngine的getRowEngine改为getRawEngine（[issue#I2EGRG@Gitee](https://gitee.com/dromara/hutool/issues/I2EGRG)）
* 【cache  】     缓存降低锁的粒度，提高并发能力（[pr#1385@Github](https://github.com/dromara/hutool/pull/1385)）
* 【core   】     SimpleCache缓存降低锁的粒度，提高并发能力（[pr#1385@Github](https://github.com/dromara/hutool/pull/1385)）
* 【core   】     增加RadixUtil（[pr#260@Gitee](https://gitee.com/dromara/hutool/pulls/260)）
* 【core   】     BeanUtil.getFieldValue支持获取字段集合（[pr#254@Gitee](https://gitee.com/dromara/hutool/pulls/254)）
* 【core   】     DateConvert转换失败默认抛出异常（[issue#I2M5GN@Gitee](https://gitee.com/dromara/hutool/issues/I2M5GN)）
* 【http   】     HttpServerRequest增加getParam方法
* 【http   】     RootAction增加可选name参数，返回指定文件名称
* 【db     】     支持人大金仓8的驱动识别
* 【db     】     ThreadUtil增加createScheduledExecutor和schedule方法（[issue#I2NUTC@Gitee](https://gitee.com/dromara/hutool/issues/I2NUTC)）
* 【core   】     ImgUtil增加getImage方法（[issue#I2DU1Z@Gitee](https://gitee.com/dromara/hutool/issues/I2DU1Z)）
* 【core   】     DateUtil.beginOfHour（[pr#269@Gitee](https://gitee.com/dromara/hutool/pulls/269)）
* 【core   】     MapUtil增加sortByValue（[pr#259@Gitee](https://gitee.com/dromara/hutool/pulls/259)）
* 【core   】     TypeUtil修正hasTypeVeriable为hasTypeVariable
* 【core   】     RandomUtil.getRandom改为new SecureRandom，避免阻塞

### 🐞Bug修复
* 【core   】     修复FileUtil.move以及PathUtil.copy等无法自动创建父目录的问题（[issue#I2CKTI@Gitee](https://gitee.com/dromara/hutool/issues/I2CKTI)）
* 【core   】     修复Console.input读取不全问题（[pr#263@Gitee](https://gitee.com/dromara/hutool/pulls/263)）
* 【core   】     修复URLUtil.encodeAll未检查空指针问题（[issue#I2CNPS@Gitee](https://gitee.com/dromara/hutool/issues/I2CNPS)）
* 【core   】     修复UrlBuilder.of的query中含有?丢失问题（[issue#I2CNPS@Gitee](https://gitee.com/dromara/hutool/issues/I2CNPS)）
* 【crypto 】     修复BCrypt.checkpw报错问题（[issue#1377@Github](https://github.com/dromara/hutool/issues/1377)）
* 【extra  】     修复Fftp中cd失败导致的问题（[issue#1371@Github](https://github.com/dromara/hutool/issues/1371)）
* 【poi    】     修复ExcelWriter.merge注释问题（[issue#I2DNPG@Gitee](https://gitee.com/dromara/hutool/issues/I2DNPG)）
* 【core   】     修复CsvReader读取注释行错误问题（[issue#I2D87I@Gitee](https://gitee.com/dromara/hutool/issues/I2D87I)）

-------------------------------------------------------------------------------------------------------------

# 5.5.7 (2021-01-07)

### 🐣新特性
* 【core   】     DynaBean.create增加重载方法（[pr#245@Gitee](https://gitee.com/dromara/hutool/pulls/245)）
* 【core   】     IdcardUtil增加重载是否忽略大小写（[issue#1348@Github](https://github.com/dromara/hutool/issues/1348)）
* 【poi    】     SheetRidReader增加getRidByIndex方法（[issue#1342@Github](https://github.com/dromara/hutool/issues/1342)）
* 【extra  】     MailAccount增加sslProtocols配置项（[issue#IZN95@Gitee](https://gitee.com/dromara/hutool/issues/IZN95)）
* 【extra  】     MailUtil增加getSession方法
* 【setting】     新增setByGroup和putByGroup，set和put标记为过期（[issue#I2C42H@Gitee](https://gitee.com/dromara/hutool/issues/I2C42H)）
* 【crypto 】     修改SymmetricAlgorithm注释（[issue#1360@Github](https://github.com/dromara/hutool/issues/1360)）
* 【all    】     pom中将META-INF/maven下全部exclude（[pr#1355@Github](https://github.com/dromara/hutool/pull/1355)）
* 【http   】     SimpleServer中增加addFilter等方法，并使用全局线程池
* 【core   】     CollUtil.forEach 增加null 判断（[pr#250@Gitee](https://gitee.com/dromara/hutool/pulls/250)）
* 【extra  】     FtpConfig增加serverLanguageCode和systemKey配置,Ftp.download增加重载（[pr#248@Gitee](https://gitee.com/dromara/hutool/pulls/248)）

### 🐞Bug修复
* 【core   】     修复CsvReader读取双引号未转义问题（[issue#I2BMP1@Gitee](https://gitee.com/dromara/hutool/issues/I2BMP1)）
* 【json   】     JSONUtil.parse修复config无效问题（[issue#1363@Github](https://github.com/dromara/hutool/issues/1363)）
* 【http   】     修复SimpleServer返回响应内容Content-Length不正确的问题（[issue#1358@Github](https://github.com/dromara/hutool/issues/1358)）
* 【http   】     修复Https请求部分环境下报证书验证异常问题（[issue#I2C1BZ@Gitee](https://gitee.com/dromara/hutool/issues/I2C1BZ)）

-------------------------------------------------------------------------------------------------------------

# 5.5.6 (2020-12-29)

### 🐣新特性
* 【core   】     手机号工具类 座机正则表达式统一管理（[pr#243@Gitee](https://gitee.com/dromara/hutool/pulls/243)）
* 【extra  】     Mail增加setDebugOutput方法（[issue#1335@Gitee](https://gitee.com/dromara/hutool/issues/1335)）

### 🐞Bug修复
* 【core   】     修复ZipUtil.unzip从流解压关闭问题（[issue#I2B0S1@Gitee](https://gitee.com/dromara/hutool/issues/I2B0S1)）
* 【poi    】     修复Excel07Writer写出表格错乱问题（[issue#I2B57B@Gitee](https://gitee.com/dromara/hutool/issues/I2B57B)）
* 【poi    】     修复SheetRidReader读取字段错误问题（[issue#1342@Github](https://github.com/dromara/hutool/issues/1342)）
* 【core   】     修复FileUtil.getMimeType不支持css和js（[issue#1341@Github](https://github.com/dromara/hutool/issues/1341)）

-------------------------------------------------------------------------------------------------------------

# 5.5.5 (2020-12-27)

### 🐣新特性
* 【core   】     URLUtil.normalize新增重载（[pr#233@Gitee](https://gitee.com/dromara/hutool/pulls/233)）
* 【core   】     PathUtil增加isSub和toAbsNormal方法
* 【db     】     RedisDS实现序列化接口（[pr#1323@Github](https://github.com/dromara/hutool/pull/1323)）
* 【poi    】     StyleUtil增加getFormat方法（[pr#235@Gitee](https://gitee.com/dromara/hutool/pulls/235)）
* 【poi    】     增加ExcelDateUtil更多日期格式支持（[issue#1316@Github](https://github.com/dromara/hutool/issues/1316)）
* 【core   】     NumberUtil.toBigDecimal支持各类数字格式，如1,234.56等（[issue#1334@Github](https://github.com/dromara/hutool/issues/1334)）
* 【core   】     NumberUtil增加parseXXX方法（[issue#1334@Github](https://github.com/dromara/hutool/issues/1334)）
* 【poi    】     Excel07SaxReader支持通过sheetName读取（[issue#I2AOSE@Gitee](https://gitee.com/dromara/hutool/issues/I2AOSE)）

### 🐞Bug修复
* 【core   】     FileUtil.isSub相对路径判断问题（[pr#1315@Github](https://github.com/dromara/hutool/pull/1315)）
* 【core   】     TreeUtil增加空判定（[issue#I2ACCW@Gitee](https://gitee.com/dromara/hutool/issues/I2ACCW)）
* 【db     】     解决Hive获取表名失败问题（[issue#I2AGLU@Gitee](https://gitee.com/dromara/hutool/issues/I2AGLU)）
* 【core   】     修复DateUtil.parse未使用严格模式导致结果不正常的问题（[issue#1332@Github](https://github.com/dromara/hutool/issues/1332)）
* 【core   】     修复RuntimeUtil.getUsableMemory非static问题（[issue#I2AQ2M@Gitee](https://gitee.com/dromara/hutool/issues/I2AQ2M)）
* 【core   】     修复ArrayUtil.equals方法严格判断问题（[issue#I2AO8B@Gitee](https://gitee.com/dromara/hutool/issues/I2AO8B)）
* 【poi    】     修复SheetRidReader在获取rid时读取错误问题（[issue#I2AOQW@Gitee](https://gitee.com/dromara/hutool/issues/I2AOQW)）
* 【core   】     修复强依赖了POI的问题（[issue#1336@Github](https://github.com/dromara/hutool/issues/1336)）

-------------------------------------------------------------------------------------------------------------

# 5.5.4 (2020-12-16)

### 🐣新特性
### 🐞Bug修复
* 【core   】     修复IoUtil.readBytes的问题

-------------------------------------------------------------------------------------------------------------

# 5.5.3 (2020-12-11)

### 🐣新特性
* 【core   】     IdcardUtil增加行政区划83（[issue#1277@Github](https://github.com/dromara/hutool/issues/1277)）
* 【core   】     multipart中int改为long，解决大文件上传越界问题（[issue#I27WZ3@Gitee](https://gitee.com/dromara/hutool/issues/I27WZ3)）
* 【core   】     ListUtil.page增加检查（[pr#224@Gitee](https://gitee.com/dromara/hutool/pulls/224)）
* 【db     】     Db增加使用sql的page方法（[issue#247@Gitee](https://gitee.com/dromara/hutool/issues/247)）
* 【cache  】     CacheObj的isExpired()逻辑修改（[issue#1295@Github](https://github.com/dromara/hutool/issues/1295)）
* 【json   】     JSONStrFormater改为JSONStrFormatter
* 【dfa    】     增加FoundWord（[pr#1290@Github](https://github.com/dromara/hutool/pull/1290)）
* 【core   】     增加Segment（[pr#1290@Github](https://github.com/dromara/hutool/pull/1290)）
* 【core   】     增加CharSequenceUtil
* 【poi    】     Excel07SaxReader拆分出SheetDataSaxHandler
* 【core   】     CollUtil.addAll增加判空（[pr#228@Gitee](https://gitee.com/dromara/hutool/pulls/228)）
* 【core   】     修正DateUtil.betweenXXX注释错误（[issue#I28XGW@Gitee](https://gitee.com/dromara/hutool/issues/I28XGW)）
* 【core   】     增加NioUtil
* 【core   】     增加GanymedUtil
* 【poi    】     增加OFD支持，OfdWriter
* 【poi    】     修复NumberUtil属性拼写错误（[pr#1311@Github](https://github.com/dromara/hutool/pull/1311)）
* 【core   】     MapUtil增加getQuietly方法（[issue#I29IWO@Gitee](https://gitee.com/dromara/hutool/issues/I29IWO)）

### 🐞Bug修复
* 【cache  】     修复Cache中get重复misCount计数问题（[issue#1281@Github](https://github.com/dromara/hutool/issues/1281)）
* 【poi    】     修复sax读取自定义格式单元格无法识别日期类型的问题（[issue#1283@Github](https://github.com/dromara/hutool/issues/1283)）
* 【core   】     修复CollUtil.get越界问题（[issue#1292@Github](https://github.com/dromara/hutool/issues/1292)）
* 【core   】     修复TemporalAccessorUtil无法格式化LocalDate带时间问题（[issue#1289@Github](https://github.com/dromara/hutool/issues/1289)）
* 【json   】     修复自定义日期格式的LocalDateTime没有包装引号问题（[issue#1289@Github](https://github.com/dromara/hutool/issues/1289)）
* 【cache  】     get中unlock改为unlockRead（[issue#1294@Github](https://github.com/dromara/hutool/issues/1294)）
* 【db     】     修复表名包含点导致的问题（[issue#1300@Github](https://github.com/dromara/hutool/issues/1300)）
* 【poi    】     修复xdr:row标签导致的问题（[issue#1297@Github](https://github.com/dromara/hutool/issues/1297)）
* 【core   】     修复FileUtil.loopFiles使用FileFilter无效问题（[issue#I28V48@Gitee](https://gitee.com/dromara/hutool/issues/I28V48)）
* 【extra  】     修复JschUtil.execByShell返回空的问题（[issue#1067@Github](https://github.com/dromara/hutool/issues/1067)）
* 【poi    】     修复特殊的excel使用sax读取时未读到值的问题（[issue#1303@Github](https://github.com/dromara/hutool/issues/1303)）
* 【http   】     修复HttpUtil类条件判断错误（[pr#232@Gitee](https://gitee.com/dromara/hutool/pulls/232)）

-------------------------------------------------------------------------------------------------------------

# 5.5.2 (2020-12-01)

### 🐣新特性
* 【crypto 】     KeyUtil增加重载，AES构造增加重载（[issue#I25NNZ@Gitee](https://gitee.com/dromara/hutool/issues/I25NNZ)）
* 【json   】     JSONUtil增加toList重载（[issue#1228@Github](https://github.com/dromara/hutool/issues/1228)）
* 【core   】     新增CollStreamUtil（[issue#1228@Github](https://github.com/dromara/hutool/issues/1228)）
* 【extra  】     新增Rhino表达式执行引擎（[pr#1229@Github](https://github.com/dromara/hutool/pull/1229)）
* 【crypto 】     增加判空（[issue#1230@Github](https://github.com/dromara/hutool/issues/1230)）
* 【core   】     xml.setXmlStandalone(true)格式优化（[pr#1234@Github](https://github.com/dromara/hutool/pull/1234)）
* 【core   】     AnnotationUtil增加setValue方法（[pr#1250@Github](https://github.com/dromara/hutool/pull/1250)）
* 【core   】     ZipUtil增加get方法（[issue#I27CUF@Gitee](https://gitee.com/dromara/hutool/issues/I27CUF)）
* 【cache  】     对CacheObj等变量使用volatile关键字
* 【core   】     Base64增加encodeWithoutPadding方法（[issue#I26J16@Gitee](https://gitee.com/dromara/hutool/issues/I26J16)）
* 【core   】     ExceptionUtil增加message消息包装为运行时异常的方法（[pr#1253@Gitee](https://gitee.com/dromara/hutool/pulls/1253)）
* 【core   】     DatePattern增加年月格式化常量（[pr#220@Gitee](https://gitee.com/dromara/hutool/pulls/220)）
* 【core   】     ArrayUtil增加shuffle方法（[pr#1255@Github](https://github.com/dromara/hutool/pull/1255)）
* 【core   】     ArrayUtil部分方法分离至PrimitiveArrayUtil
* 【crypto 】     opt改为otp包（[issue#1257@Github](https://github.com/dromara/hutool/issues/1257)）
* 【cache  】     增加CacheListener（[issue#1257@Github](https://github.com/dromara/hutool/issues/1257)）
* 【core   】     TimeInterval支持分组（[issue#1238@Github](https://github.com/dromara/hutool/issues/1238)）
* 【core   】     增加compile包（[pr#1243@Github](https://github.com/dromara/hutool/pull/1243)）
* 【core   】     增加ResourceClassLoader、CharSequenceResource、FileObjectResource
* 【core   】     修改IoUtil.read(Reader)逻辑默认关闭Reader
* 【core   】     ZipUtil增加Zip方法（[pr#222@Gitee](https://gitee.com/dromara/hutool/pulls/222)）
* 【all    】     增加Hutool.getAllUtils和printAllUtils方法
* 【core   】     增加PunyCode（[issue#1268@Gitee](https://gitee.com/dromara/hutool/issues/1268)）
* 【core   】     ArrayUtil增加isSorted方法（[pr#1271@Github](https://github.com/dromara/hutool/pull/1271)）
* 【captcha】     增加GifCaptcha（[pr#1273@Github](https://github.com/dromara/hutool/pull/1273)）
* 【core   】     增加SSLUtil、SSLContextBuilder

### 🐞Bug修复
* 【cron   】     修复CronTimer可能死循环的问题（[issue#1224@Github](https://github.com/dromara/hutool/issues/1224)）
* 【core   】     修复Calculator.conversion单个数字越界问题（[issue#1222@Github](https://github.com/dromara/hutool/issues/1222)）
* 【poi    】     修复ExcelUtil.getSaxReader使用非MarkSupport流报错问题（[issue#1225@Github](https://github.com/dromara/hutool/issues/1225)）
* 【core   】     修复HexUtil.format问题（[issue#I268XT@Gitee](https://gitee.com/dromara/hutool/issues/I268XT)）
* 【core   】     修复ZipUtil判断压缩文件是否位于压缩目录内的逻辑有误的问题（[issue#1251@Github](https://github.com/dromara/hutool/issues/1251)）
* 【json   】     修复JSONObject.accumulate问题
* 【poi    】     修复部分xlsx文件sax方式解析空指针问题（[issue#1265@Github](https://github.com/dromara/hutool/issues/1265)）
* 【core   】     修复PatternPool中邮编的正则（[issue#1274@Github](https://github.com/dromara/hutool/issues/1274)）

-------------------------------------------------------------------------------------------------------------

# 5.5.1 (2020-11-16)

### 🐣新特性
* 【core   】     增加CopyVisitor和DelVisitor

### 🐞Bug修复
* 【core   】     修复在Linux下FileUtil.move失败问题（[issue#I254Y3@Gitee](https://gitee.com/dromara/hutool/issues/I254Y3)）
* 【http   】     修复UrlUtil和UrlBuilder中多个/被替换问题（[issue#I25MZL@Gitee](https://gitee.com/dromara/hutool/issues/I25MZL)）

-------------------------------------------------------------------------------------------------------------

# 5.5.0 (2020-11-14)

### 大版本特性
* 【extra  】     增加jakarta.validation-api封装：ValidationUtil（[pr#207@Gitee](https://gitee.com/dromara/hutool/pulls/207)）
* 【extra  】     增加表达式引擎封装：ExpressionUtil（[pr#1203@Github](https://github.com/dromara/hutool/pull/1203)）
* 【extra  】     新增基于Apache-FtpServer封装：SimpleFtpServer
* 【extra  】     新增基于Commons-Compress封装：CompressUtil

### 🐣新特性
* 【core   】     NumberUtil.parseInt等支持123,2.00这类数字（[issue#I23ORQ@Gitee](https://gitee.com/dromara/hutool/issues/I23ORQ)）
* 【core   】     增加ArrayUtil.isSub、indexOfSub、lastIndexOfSub方法（[issue#I23O1K@Gitee](https://gitee.com/dromara/hutool/issues/I23O1K)）
* 【core   】     反射调用支持传递参数的值为null（[pr#1205@Github](https://github.com/dromara/hutool/pull/1205)）
* 【core   】     HexUtil增加format方法（[issue#I245NF@Gitee](https://gitee.com/dromara/hutool/issues/I245NF)）
* 【poi    】     ExcelWriter增加setCurrentRowToEnd方法（[issue#I24A2R@Gitee](https://gitee.com/dromara/hutool/issues/I24A2R)）
* 【core   】     ExcelWriter增加setCurrentRowToEnd方法（[issue#I24A2R@Gitee](https://gitee.com/dromara/hutool/issues/I24A2R)）
* 【core   】     增加enum转数字支持（[issue#I24QZY@Gitee](https://gitee.com/dromara/hutool/issues/I24QZY)）
* 【core   】     NumberUtil.toBigDecimal空白符转换为0（[issue#I24MRP@Gitee](https://gitee.com/dromara/hutool/issues/I24MRP)）
* 【core   】     CollUtil和IterUtil增加size方法（[pr#208@Gitee](https://gitee.com/dromara/hutool/pulls/208)）
* 【poi    】     ExcelReader的read方法读取空单元格增加CellEditor处理（[issue#1213@Github](https://github.com/dromara/hutool/issues/1213)）

### 🐞Bug修复
* 【core   】     修复DateUtil.current使用System.nanoTime的问题（[issue#1198@Github](https://github.com/dromara/hutool/issues/1198)）
* 【core   】     修复Excel03SaxReader判断日期出错问题（[issue#I23M9H@Gitee](https://gitee.com/dromara/hutool/issues/I23M9H)）
* 【core   】     修复ClassUtil.getTypeArgument方法在判断泛型时导致的问题（[issue#1207@Github](https://github.com/dromara/hutool/issues/1207)）
* 【core   】     修复Ipv4Util分隔符问题（[issue#I24A9I@Gitee](https://gitee.com/dromara/hutool/issues/I24A9I)）
* 【core   】     修复Ipv4Util.longToIp的问题
* 【poi    】     修复Excel07SaxReader读取公式的错误的问题（[issue#I23VFL@Gitee](https://gitee.com/dromara/hutool/issues/I23VFL)）
* 【http   】     修复HttpUtil.isHttp判断问题（[pr#1208@Github](https://github.com/dromara/hutool/pull/1208)）
* 【http   】     修复Snowflake时间回拨导致ID重复的bug（[issue#1206@Github](https://github.com/dromara/hutool/issues/1206)）
* 【core   】     修复StrUtil.lastIndexOf查找位于首位的字符串找不到的bug（[issue#I24RSV@Gitee](https://gitee.com/dromara/hutool/issues/I24RSV)）
* 【poi    】     修复BigExcelWriter的autoSizeColumnAll问题（[pr#1221@Github](https://github.com/dromara/hutool/pull/1221)）
* 【core   】     修复StrUtil.subBetweenAll不支持相同字符的问题（[pr#1217@Github](https://github.com/dromara/hutool/pull/1217)）

-------------------------------------------------------------------------------------------------------------

# 5.4.7 (2020-10-31)

### 🐣新特性
* 【core   】     增加OptionalBean（[pr#1182@Github](https://github.com/dromara/hutool/pull/1182)）
* 【core   】     Ganzhi增加方法（[issue#1186@Github](https://github.com/dromara/hutool/issues/1186)）
* 【core   】     CollUtil增加forEach重载（[issue#I22NA4@Gitee](https://gitee.com/dromara/hutool/issues/I22NA4)）
* 【core   】     CollUtil.map忽略空值改规则为原数组中的元素和处理后的元素都会忽略空值（[issue#I22N08@Gitee](https://gitee.com/dromara/hutool/issues/I22N08)）
* 【http   】     增加SoapClient增加addSOAPHeader重载
* 【http   】     ArrayUtil增加containsAll方法
* 【core   】     增加CharsetDetector
* 【cron   】     增加CronTask，监听支持获取id（[issue#I23315@Gitee](https://gitee.com/dromara/hutool/issues/I23315)）

### 🐞Bug修复
* 【core   】     修复BeanUtil.beanToMap方法中editor返回null没有去掉的问题
* 【core   】     修复ImgUtil.toBufferedImage颜色模式的问题（[issue#1194@Github](https://github.com/dromara/hutool/issues/1194)）
* 【cron   】     修复TimeZone设置无效的问题（[issue#I23315@Gitee](https://gitee.com/dromara/hutool/issues/I23315)）

-------------------------------------------------------------------------------------------------------------

# 5.4.6 (2020-10-23)

### 🐣新特性
* 【http   】     HttpRequest增加basicProxyAuth方法（[issue#I1YQGM@Gitee](https://gitee.com/dromara/hutool/issues/I1YQGM)）
* 【core   】     NumberUtil.toStr修改逻辑，去掉BigDecimal的科学计数表示（[pr#196@Gitee](https://gitee.com/dromara/hutool/pulls/196)）
* 【core   】     ListUtil.page第一页页码使用PageUtil（[pr#198@Gitee](https://gitee.com/dromara/hutool/pulls/198)）
* 【http   】     增加微信、企业微信ua识别（[pr#1179@Github](https://github.com/dromara/hutool/pull/1179)）
* 【core   】     ObjectUtil增加defaultIfXXX（[pr#199@Gitee](https://gitee.com/dromara/hutool/pulls/199)）
* 【json   】     JSONObject构建时不支持的对象类型抛出异常

### 🐞Bug修复
* 【core   】     修复ChineseDate没有忽略时分秒导致计算错误问题（[issue#I1YW12@Gitee](https://gitee.com/dromara/hutool/issues/I1YW12)）
* 【core   】     修复FileUtil中，copyFile方法断言判断参数传递错误（[issue#I1Z2NY@Gitee](https://gitee.com/dromara/hutool/issues/I1Z2NY)）
* 【core   】     修复BeanDesc读取父类属性覆盖子类属性导致的问题（[pr#1175@Github](https://github.com/dromara/hutool/pull/1175)）
* 【aop    】     修复SimpleAspect一个重载导致的问题，去掉重载的after方法（[issue#I1YUG9@Gitee](https://gitee.com/dromara/hutool/issues/I1YUG9)）
* 【poi    】     修复03 sax读取日期问题（[issue#I1Z83N@Gitee](https://gitee.com/dromara/hutool/issues/I1Z83N)）
* 【core   】     修复FileUtil.size软链导致的问题（[pr#200@Gitee](https://gitee.com/dromara/hutool/pulls/200)）
* 【core   】     修复JSONObject构造时传入JSONArray结果出错问题（[issue#I22FDS@Gitee](https://gitee.com/dromara/hutool/issues/I22FDS)）

-------------------------------------------------------------------------------------------------------------

# 5.4.5 (2020-10-18)

### 🐣新特性
* 【core   】     ConsoleTable代码优化（[pr#190@Gitee](https://gitee.com/dromara/hutool/pulls/190)）
* 【http   】     HttpRequest增加setProxy重载（[pr#190@Gitee](https://gitee.com/dromara/hutool/pulls/190)）
* 【core   】     XmlUtil.cleanComment（[pr#191@Gitee](https://gitee.com/dromara/hutool/pulls/191)）
* 【core   】     ArrayUtil.unWrap增加默认值（[pr#1149@Github](https://github.com/dromara/hutool/pull/1149)）
* 【core   】     ArrayUtil.indexOf修改double的equals判断（[pr#1147@Github](https://github.com/dromara/hutool/pull/1147)）
* 【core   】     优化StrUtil中部分参数校验以及逻辑处理（[pr#1144@Github](https://github.com/dromara/hutool/pull/1144)）
* 【core   】     简化CreditCode逻辑去除无用Character.toUpperCase（[pr#1145@Github](https://github.com/dromara/hutool/pull/1145)）
* 【core   】     NumberUtil增加generateRandomNumber重载，可自定义seed（[issue#I1XTUT@Gitee](https://gitee.com/dromara/hutool/issues/I1XTUT)）
* 【core   】     DataSizeUtil支持小数（[pr#1158@Github](https://github.com/dromara/hutool/pull/1158)）
* 【core   】     完善注释（[pr#193@Gitee](https://gitee.com/dromara/hutool/pulls/193)）
* 【core   】     优化Combination.countAll（[pr#1159@Github](https://github.com/dromara/hutool/pull/1159)）
* 【core   】     优化针对list的split方法（[pr#194@Gitee](https://gitee.com/dromara/hutool/pulls/194)）
* 【poi    】     ExcelWriter增加setRowStyle方法
* 【core   】     Assert增加函数接口（[pr#1166@Github](https://github.com/dromara/hutool/pull/1166)）
* 【core   】     新增AtomicIntegerArray、AtomicLongArray转换
* 【extra  】     PinyinUtil新增Bopomofo4j支持
* 【core   】     新增TemporalUtil工具类，新增时间相关方法

### 🐞Bug修复
* 【core   】     解决农历判断节日未判断大小月导致的问题（[issue#I1XHSF@Gitee](https://gitee.com/dromara/hutool/issues/I1XHSF)）
* 【core   】     解决ListUtil计算总量可能的int溢出问题（[pr#1150@Github](https://github.com/dromara/hutool/pull/1150)）
* 【json   】     解决JSON中转换为double小数精度丢失问题（[pr#192@Gitee](https://gitee.com/dromara/hutool/pulls/192)）
* 【core   】     修复CaseInsensitiveMap的remove等方法并没有忽略大小写的问题（[pr#1163@Gitee](https://gitee.com/dromara/hutool/pulls/1163)）
* 【poi    】     修复合并单元格值读取错误的问题
* 【poi    】     修复NamedSql解析形如col::numeric出错问题（[issue#I1YHBX@Gitee](https://gitee.com/dromara/hutool/issues/I1YHBX)）
* 【core   】     修复计算相差天数导致的问题

-------------------------------------------------------------------------------------------------------------

# 5.4.4 (2020-09-28)

### 🐣新特性
* 【core   】     ServiceLoaderUtil改为使用contextClassLoader（[pr#183@Gitee](https://gitee.com/dromara/hutool/pulls/183)）
* 【core   】     NetUtil增加getLocalHostName（[pr#1103@Github](https://github.com/dromara/hutool/pull/1103)）
* 【extra  】     FTP增加stat方法（[issue#I1W346@Gitee](https://gitee.com/dromara/hutool/issues/I1W346)）
* 【core   】     Convert.toNumber支持类似12.2F这种形式字符串转换（[issue#I1VYLJ@Gitee](https://gitee.com/dromara/hutool/issues/I1VYLJ)）
* 【core   】     使用静态变量替换999等（[issue#I1W8IB@Gitee](https://gitee.com/dromara/hutool/issues/I1W8IB)）
* 【core   】     URLUtil自动trim（[issue#I1W803@Gitee](https://gitee.com/dromara/hutool/issues/I1W803)）
* 【crypto 】     RC4增加ecrypt（[pr#1108@Github](https://github.com/dromara/hutool/pull/1108)）
* 【core   】     CharUtil and StrUtil增加@（[pr#1106@Github](https://github.com/dromara/hutool/pull/1106)）
* 【extra  】     优化EMOJ查询逻辑（[pr#1112@Github](https://github.com/dromara/hutool/pull/1112)）
* 【extra  】     优化CollUtil交并集结果集合设置初始化大小，避免扩容成本（[pr#1110@Github](https://github.com/dromara/hutool/pull/1110)）
* 【core   】     优化PageUtil彩虹算法（[issue#1110@Github](https://github.com/dromara/hutool/issues/1110)）
* 【core   】     IoUtil增加readUtf8方法
* 【core   】     优化全局邮箱账户初始化逻辑（[pr#1114@Github](https://github.com/dromara/hutool/pull/1114)）
* 【http   】     SoapClient增加addSOAPHeader方法
* 【http   】     完善StrUtil的注释（[pr#186@Gitee](https://gitee.com/dromara/hutool/pulls/186)）
* 【aop    】     去除调试日志（[issue#1116@Github](https://github.com/dromara/hutool/issues/1116)）
* 【core   】     增加&apos;反转义（[pr#1121@Github](https://github.com/dromara/hutool/pull/1121)）
* 【poi    】     增加SheetReader和XXXRowHandler（[issue#I1WHJP@Gitee](https://gitee.com/dromara/hutool/issues/I1WHJP)）
* 【dfa    】     增加过滤符号（[pr#1122@Github](https://github.com/dromara/hutool/pull/1122)）
* 【dfa    】     SensitiveUtil增加setCharFilter方法（[pr#1123@Github](https://github.com/dromara/hutool/pull/1123)）
* 【all    】     优化常量大小写规范（[pr#188@Gitee](https://gitee.com/dromara/hutool/pulls/188)）
* 【core   】     优化NumberUtil中针对BigDecimal的一些处理逻辑（[pr#1127@Github](https://github.com/dromara/hutool/pull/1127)）
* 【core   】     NumberUtil.factorial注释明确（[pr#1126@Github](https://github.com/dromara/hutool/pull/1126)）
* 【core   】     NumberUtil增加isPowerOfTwo方法（[pr#1132@Github](https://github.com/dromara/hutool/pull/1132)）
* 【core   】     优化BooleanUtil的校验逻辑（[pr#1137@Github](https://github.com/dromara/hutool/pull/1137)）
* 【poi    】     改进sax方式读取逻辑，支持sheetId（[issue#1141@Github](https://github.com/dromara/hutool/issues/1141)）
* 【core   】     XmlUtil增加readBySax方法

### 🐞Bug修复
* 【crypto 】     修复SM2验签后无法解密问题（[issue#I1W0VP@Gitee](https://gitee.com/dromara/hutool/issues/I1W0VP)）
* 【core   】     修复新建默认TreeSet没有默认比较器导致的问题（[issue#1101@Github](https://github.com/dromara/hutool/issues/1101)）
* 【core   】     修复Linux下使用Windows路径分隔符导致的解压错误（[issue#I1MW0E@Gitee](https://gitee.com/dromara/hutool/issues/I1MW0E)）
* 【core   】     修复Word07Writer写出map问题（[issue#I1W49R@Gitee](https://gitee.com/dromara/hutool/issues/I1W49R)）
* 【script 】     修复函数库脚本执行问题
* 【core   】     修复RGB随机颜色的上限值不对且API重复（[pr#1136@Gihub](https://github.com/dromara/hutool/pull/1136)）

-------------------------------------------------------------------------------------------------------------

# 5.4.3 (2020-09-16)

### 🐣新特性
* 【core   】     使用静态的of方法来new对象（[pr#177@Gitee](https://gitee.com/dromara/hutool/pulls/177)）
* 【setting】     Setting增加store无参方法（[issue#1072@Github](https://github.com/dromara/hutool/issues/1072)）
* 【setting】     StatementUtil增加null缓存（[pr#1076@Github](https://github.com/dromara/hutool/pull/1076)）
* 【core   】     扩充Console功能，支持可变参数（[issue#1077@Github](https://github.com/dromara/hutool/issues/1077)）
* 【crypto 】     增加ECKeyUtil（[issue#I1UOF5@Gitee](https://gitee.com/dromara/hutool/issues/I1UOF5)）
* 【core   】     增加TransXXX（[issue#I1TU1Y@Gitee](https://gitee.com/dromara/hutool/issues/I1TU1Y)）
* 【core   】     增加Generator
* 【db     】     Column增加是否主键、保留位数等字段
* 【cache  】     Cache接口增加get重载（[issue#1080@Github](https://github.com/dromara/hutool/issues/1080)）
* 【core   】     增加Interner和InternUtil（[issue#I1TU1Y@Gitee](https://gitee.com/dromara/hutool/issues/I1TU1Y)）
* 【core   】     增加Calculator（[issue#1090@Github](https://github.com/dromara/hutool/issues/1090)）
* 【core   】     IdcardUtil增加getIdcardInfo方法（[issue#1092@Github](https://github.com/dromara/hutool/issues/1092)）
* 【core   】     改进ObjectUtil.equal，支持BigDecimal判断
* 【core   】     ArrayConverter增加可选是否忽略错误（[issue#I1VNYQ@Gitee](https://gitee.com/dromara/hutool/issues/I1VNYQ)）
* 【db     】     增加ConditionBuilder
* 【setting】     Setting和Props增加create方法
* 【log    】     增加TinyLog2支持（[issue#1094@Github](https://github.com/dromara/hutool/issues/1094)）

### 🐞Bug修复
* 【core   】     修复Dict.of错误（[issue#I1UUO5@Gitee](https://gitee.com/dromara/hutool/issues/I1UUO5)）
* 【core   】     修复UrlBuilder地址参数问题（[issue#I1UWCA@Gitee](https://gitee.com/dromara/hutool/issues/I1UWCA)）
* 【core   】     修复StrUtil.toSymbolCase转换问题（[issue#1075@Github](https://github.com/dromara/hutool/issues/1075)）
* 【log    】     修复打印null对象显示{msg}异常问题（[issue#1084@Github](https://github.com/dromara/hutool/issues/1084)）
* 【extra  】     修复ServletUtil.getReader中未关闭的问题
* 【extra  】     修复QrCodeUtil在新版本zxing报错问题（[issue#1088@Github](https://github.com/dromara/hutool/issues/1088)）
* 【core   】     修复LocalDateTimeUtil.parse无法解析yyyyMMddHHmmssSSS的bug（[issue#1082@Github](https://github.com/dromara/hutool/issues/1082)）
* 【core   】     修复VersionComparator.equals递归调用问题（[issue#1093@Github](https://github.com/dromara/hutool/issues/1093)）

-------------------------------------------------------------------------------------------------------------

# 5.4.2 (2020-09-09)

### 🐣新特性
* 【core  】     lock放在try外边（[pr#1050@Github](https://github.com/dromara/hutool/pull/1050)）
* 【core  】     MailUtil增加错误信息（[issue#I1TAKJ@Gitee](https://gitee.com/dromara/hutool/issues/I1TAKJ)）
* 【core  】     JschUtil添加远程转发功能（[pr#171@Gitee](https://gitee.com/dromara/hutool/pulls/171)）
* 【db    】     AbstractDb增加executeBatch重载（[issue#1053@Github](https://github.com/dromara/hutool/issues/1053)）
* 【extra 】     新增方便引入SpringUtil的注解@EnableSpringUtil（[pr#172@Gitee](https://gitee.com/dromara/hutool/pulls/172)）
* 【poi   】     RowUtil增加插入和删除行（[pr#1060@Github](https://github.com/dromara/hutool/pull/1060)）
* 【extra 】     SpringUtil增加注册bean（[pr#174@Gitee](https://gitee.com/dromara/hutool/pulls/174)）
* 【core  】     修改NetUtil.getMacAddress避免空指针（[issue#1057@Github](https://github.com/dromara/hutool/issues/1057)）
* 【core  】     增加EnumItem接口，枚举扩展转换，增加SPI自定义转换（[pr#173@Github](https://github.com/dromara/hutool/pull/173)）
* 【core  】     TypeUtil增加getActualType，增加ActualTypeMapperPool类（[issue#I1TBWH@Gitee](https://gitee.com/dromara/hutool/issues/I1TBWH)）
* 【extra 】     QRConfig中添加qrVersion属性（[pr#1068@Github](https://github.com/dromara/hutool/pull/1068)）
* 【core  】     ArrayUtil增加equals方法
* 【core  】     BeanDesc增加方法
* 【core  】     增加@PropIgnore注解（[issue#I1U846@Gitee](https://gitee.com/dromara/hutool/issues/I1U846)）

### 🐞Bug修复
* 【core  】     重新整理农历节假日，解决一个pr过来的玩笑导致的问题
* 【poi   】     修复ExcelFileUtil.isXls判断问题（[pr#1055@Github](https://github.com/dromara/hutool/pull/1055)）
* 【poi   】     修复CglibUtil.copyList参数错误导致的问题
* 【http  】     修复GET请求附带body导致变POST的问题
* 【core  】     修复double相等判断问题（[pr#175@Gitee](https://gitee.com/dromara/hutool/pulls/175)）
* 【core  】     修复DateSizeUtil.format越界问题（[issue#1069@Github](https://github.com/dromara/hutool/issues/1069)）
* 【core  】     修复ChineseDate.getChineseMonth问题（[issue#I1UG72@Gitee](https://gitee.com/dromara/hutool/issues/I1UG72)）

-------------------------------------------------------------------------------------------------------------

# 5.4.1 (2020-08-29)

### 🐣新特性
* 【core  】     StrUtil增加firstNonXXX方法（[issue#1020@Github](https://github.com/dromara/hutool/issues/1020)）
* 【core  】     BeanCopier修改规则，可选bean拷贝空字段报错问题（[pr#160@Gitee](https://gitee.com/dromara/hutool/pulls/160)）
* 【http  】     HttpUtil增加downloadFileFromUrl（[pr#1023@Github](https://github.com/dromara/hutool/pull/1023)）
* 【core  】     增加toEpochMilli方法
* 【core  】     Validator修改isCitizenId校验（[pr#1032@Github](https://github.com/dromara/hutool/pull/1032)）
* 【core  】     增加PathUtil和FileNameUtil，分离FileUtil中部分方法
* 【core  】     改造IndexedComparator，增加InstanceComparator
* 【extra 】     增加CglibUtil
* 【core  】     增加Ipv4Util（[pr#161@Gitee](https://gitee.com/dromara/hutool/pulls/161)）
* 【core  】     增加CalendarUtil和DateUtil增加isSameMonth方法（[pr#161@Gitee](https://gitee.com/dromara/hutool/pulls/161)）
* 【core  】     Dict增加of方法（[issue#1035@Github](https://github.com/dromara/hutool/issues/1035)）
* 【core  】     StrUtil.wrapAll方法不明确修改改为wrapAllWithPair（[issue#1042@Github](https://github.com/dromara/hutool/issues/1042)）
* 【core  】     EnumUtil.getEnumAt负数返回null（[pr#167@Gitee](https://gitee.com/dromara/hutool/pulls/167)）
* 【core  】     ChineseDate增加天干地支和转换为公历方法（[pr#169@Gitee](https://gitee.com/dromara/hutool/pulls/169)）
* 【core  】     Img增加stroke描边方法（[issue#1033@Github](https://github.com/dromara/hutool/issues/1033)）

### 🐞Bug修复#
* 【poi   】     修复ExcelBase.isXlsx方法判断问题（[issue#I1S502@Gitee](https://gitee.com/dromara/hutool/issues/I1S502)）
* 【poi   】     修复Excel03SaxReader日期方法判断问题（[pr#1026@Github](https://github.com/dromara/hutool/pull/1026)）
* 【core  】     修复StrUtil.indexOf空指针问题（[issue#1038@Github](https://github.com/dromara/hutool/issues/1038)）
* 【extra 】     修复VelocityEngine编码问题和路径前缀问题（[issue#I1T0IG@Gitee](https://gitee.com/dromara/hutool/issues/I1T0IG)）

-------------------------------------------------------------------------------------------------------------

# 5.4.0 (2020-08-06)

### 🐣新特性
* 【socket】     对NioServer和NioClient改造（[pr#992@Github](https://github.com/dromara/hutool/pull/992)）
* 【core  】     StrUtil增加filter方法（[pr#149@Gitee](https://gitee.com/dromara/hutool/pulls/149)）
* 【core  】     DateUtil增加beginOfWeek重载
* 【core  】     将有歧义的BeanUtil.mapToBean方法置为过期（使用toBean方法）
* 【core  】     添加WatchAction（对Watcher的抽象）
* 【core  】     修改UUID正则，更加严谨（[issue#I1Q1IW@Gitee](https://gitee.com/dromara/hutool/issues/I1Q1IW)）
* 【core  】     ArrayUtil增加isAllNull方法（[issue#1004@Github](https://github.com/dromara/hutool/issues/1004)）
* 【core  】     CollUtil增加contains方法（[pr#152@Gitee](https://gitee.com/dromara/hutool/pulls/152)）
* 【core  】     ArrayUtil增加isAllNotNull方法（[pr#1008@Github](https://github.com/dromara/hutool/pull/1008)）
* 【poi   】     closeAfterRead参数无效，方法设为过期（[issue#1007@Github](https://github.com/dromara/hutool/issues/1007)）
* 【core  】     CollUtil中部分方法返回null变更为返回empty
* 【all   】     添加英文README（[pr#153@Gitee](https://gitee.com/dromara/hutool/pulls/153)）
* 【extra 】     SpringUtil增加getBean(TypeReference)（[pr#1009@Github](https://github.com/dromara/hutool/pull/1009)）
* 【core  】     Assert增加方法，支持自定义异常处理（[pr#154@Gitee](https://gitee.com/dromara/hutool/pulls/154)）
* 【core  】     BooleanConverter增加数字转换规则（[issue#I1R2AB@Gitee](https://gitee.com/dromara/hutool/issues/I1R2AB)）
* 【poi   】     sax方式读取增加一个sheet结束的回调（[issue#155@Gitee](https://gitee.com/dromara/hutool/issues/155)）
* 【db    】     增加BeeCP连接池支持
* 【core  】     改进Img.pressImage方法，避免变色问题（[issue#1001@Github](https://github.com/dromara/hutool/issues/1001)）

### 🐞Bug修复#
* 【core  】     修复原始类型转换时，转换失败没有抛出异常的问题
* 【core  】     修复BeanUtil.mapToBean中bean的class非空构造无法实例化问题
* 【core  】     修复NamedSql多个连续变量出现替换问题
* 【core  】     修复Bean重名字段（大小写区别）获取数据出错的问题（[issue#I1QBQ4@Gitee](https://gitee.com/dromara/hutool/issues/I1QBQ4)）
* 【http  】     修复SimpleServer响应头无效问题（[issue#1006@Github](https://github.com/dromara/hutool/issues/1006)）
* 【core  】     修复ThreadLocalRandom共享seed导致获取随机数一样的问题（[pr#151@Gitee](https://gitee.com/dromara/hutool/pulls/151)）

-------------------------------------------------------------------------------------------------------------

# 5.3.11 (2020-08-01)

### 🐣新特性
* 【captcha】     AbstractCaptcha增加getImageBase64Data方法（[pr#985@Github](https://github.com/dromara/hutool/pull/985)）
* 【core   】     增加PhoneUtil（[pr#990@Github](https://github.com/dromara/hutool/pull/990)）
* 【core   】     改进Img，目标图片类型未定义使用源图片类型（[issue#I1PB0B@Gitee](https://gitee.com/dromara/hutool/issues/I1PB0B)）
* 【json   】     JSONConfig增加Transient选项（[issue#I1PLHN@Gitee](https://gitee.com/dromara/hutool/issues/I1PLHN)）
* 【core   】     MapUtil增加getXXX的默认值重载（[issue#I1PTGI@Gitee](https://gitee.com/dromara/hutool/issues/I1PTGI)）
* 【core   】     CalendarUtil增加parseByPatterns方法（[issue#993@Github](https://github.com/dromara/hutool/issues/993)）

### 🐞Bug修复#

-------------------------------------------------------------------------------------------------------------

## 5.3.10 (2020-07-23)

### 🐣新特性
* 【db   】       增加DbUtil.setReturnGeneratedKeyGlobal（[issue#I1NM0K@Gitee](https://gitee.com/dromara/hutool/issues/I1NM0K)）
* 【core 】       增加DataSize和DataSizeUtil（[issue#967@Github](https://github.com/dromara/hutool/issues/967)）
* 【core 】       ImgUtil增加异常，避免空指针（[issue#I1NKXG@Gitee](https://gitee.com/dromara/hutool/issues/I1NKXG)）
* 【core 】       增加CRC16算法若干（[pr#963@Github](https://github.com/dromara/hutool/pull/963)）
* 【core 】       LocalDateTimeUtil增加format等方法（[pr#140@Gitee](https://gitee.com/dromara/hutool/pulls/140)）
* 【http 】       UserAgentUtil增加Android原生浏览器识别（[pr#975@Github](https://github.com/dromara/hutool/pull/975)）
* 【crypto 】     增加ECIES算法类（[issue#979@Github](https://github.com/dromara/hutool/issues/979)）
* 【crypto 】     CollUtil增加padLeft和padRight方法（[pr#141@Gitee](https://gitee.com/dromara/hutool/pulls/141)）
* 【core 】       IdCardUtil香港身份证去除首字母校验（[issue#I1OOTB@Gitee](https://gitee.com/dromara/hutool/issues/I1OOTB)）

### 🐞Bug修复
* 【core   】     修复ZipUtil中finish位于循环内的问题（[issue#961@Github](https://github.com/dromara/hutool/issues/961)）
* 【core   】     修复CollUtil.page未越界检查的问题（[issue#I1O2LR@Gitee](https://gitee.com/dromara/hutool/issues/I1O2LR)）
* 【core   】     修复StrUtil.removeAny的bug（[issue#977@Github](https://github.com/dromara/hutool/issues/977)）

-------------------------------------------------------------------------------------------------------------

## 5.3.9 (2020-07-12)

### 🐣新特性
* 【core   】     DateUtil增加formatChineseDate（[pr#932@Github](https://github.com/dromara/hutool/pull/932)）
* 【core   】     ArrayUtil.isEmpty修改逻辑（[pr#948@Github](https://github.com/dromara/hutool/pull/948)）
* 【core   】     增强StrUtil中空判断后返回数据性能（[pr#949@Github](https://github.com/dromara/hutool/pull/949)）
* 【core   】     deprecate掉millsecond，改为millisecond（[issue#I1M9P8@Gitee](https://gitee.com/dromara/hutool/issues/I1M9P8)）
* 【core   】     增加LocalDateTimeUtil（[issue#I1KUVC@Gitee](https://gitee.com/dromara/hutool/issues/I1KUVC)）
* 【core   】     Month增加getLastDay方法
* 【core   】     ChineseDate支持到2099年

### 🐞Bug修复
* 【core   】     修复NumberUtil.partValue有余数问题（[issue#I1KX66@Gitee](https://gitee.com/dromara/hutool/issues/I1KX66)）
* 【core   】     修复BeanUtil.isEmpty不能忽略static字段问题（[issue#I1KZI6@Gitee](https://gitee.com/dromara/hutool/issues/I1KZI6)）
* 【core   】     修复StrUtil.brief长度问题（[pr#930@Github](https://github.com/dromara/hutool/pull/930)）
* 【socket 】     修复AioSession构造超时无效问题（[pr#941@Github](https://github.com/dromara/hutool/pull/941)）
* 【setting】     修复GroupSet.contains错误（[pr#943@Github](https://github.com/dromara/hutool/pull/943)）
* 【core   】     修复ZipUtil没有调用finish问题（[issue#944@Github](https://github.com/dromara/hutool/issues/944)）
* 【extra  】     修复Ftp中ArrayList长度为负问题（[pr#136@Github](https://github.com/dromara/hutool/pull/136)）
* 【core   】     修复Dict中putAll大小写问题（[issue#I1MU5B@Gitee](https://gitee.com/dromara/hutool/issues/I1MU5B)）
* 【core   】     修复POI中sax读取数字判断错误问题（[issue#931@Github](https://github.com/dromara/hutool/issues/931)）
* 【core   】     修复DateUtil.endOfQuarter错误问题（[issue#I1NGZ7@Gitee](https://gitee.com/dromara/hutool/issues/I1NGZ7)）
* 【core   】     修复URL中有空格转为+问题（[issue#I1NGW4@Gitee](https://gitee.com/dromara/hutool/issues/I1NGW4)）
* 【core   】     修复CollUtil.intersectionDistinct空集合结果错误问题
* 【core   】     修复ChineseDate在1996年计算错误问题（[issue#I1N96I@Gitee](https://gitee.com/dromara/hutool/issues/I1N96I)）

-------------------------------------------------------------------------------------------------------------

## 5.3.8 (2020-06-16)

### 🐣新特性
* 【core   】     增加ISO8601日期格式（[issue#904@Github](https://github.com/dromara/hutool/issues/904)）
* 【setting】     Props异常规则修改（[issue#907@Github](https://github.com/dromara/hutool/issues/907)）
* 【setting】     增加GIF支持
* 【core   】     复制创建一个Bean对象, 并忽略某些属性([pr#130@Gitee](https://gitee.com/dromara/hutool/pulls/130))
* 【core   】     DateUtil.parse支持更多日期格式([issue#I1KHTB@Gitee](https://gitee.com/dromara/hutool/issues/I1KHTB))
* 【crypto 】     增加获取密钥空指针的检查([issue#925@Github](https://github.com/dromara/hutool/issues/925))
* 【core   】     增加StrUtil.removeAny方法([issue#923@Github](https://github.com/dromara/hutool/issues/923))
* 【db     】     增加部分Connection参数支持([issue#924@Github](https://github.com/dromara/hutool/issues/924))
* 【core   】     FileUtil增加别名方法([pr#926@Github](https://github.com/dromara/hutool/pull/926))
* 【poi    】     ExcelReader中增加read重载，提供每个单元格单独处理的方法([issue#I1JZTL@Gitee](https://gitee.com/dromara/hutool/issues/I1JZTL))

### 🐞Bug修复
* 【json   】     修复append方法导致的JSONConfig传递失效问题（[issue#906@Github](https://github.com/dromara/hutool/issues/906)）
* 【core   】     修复CollUtil.subtractToList判断错误（[pr#915@Github](https://github.com/dromara/hutool/pull/915)）
* 【poi    】     修复WordWriter写表格问题（[pr#914@Github](https://github.com/dromara/hutool/pull/914)）
* 【core   】     修复IoUtil.readBytes缓存数组长度问题（[issue#I1KIUE@Gitee](https://gitee.com/dromara/hutool/issues/I1KIUE)）
* 【core   】     修复BigExcelWriter多次flush导致的问题（[issue#920@Github](https://github.com/dromara/hutool/issues/920)）
* 【extra  】     绕过Pinyin4j最后一个分隔符失效的bug（[issue#921@Github](https://github.com/dromara/hutool/issues/921)）

-------------------------------------------------------------------------------------------------------------

## 5.3.7 (2020-06-03)

### 🐣新特性
* 【core   】     ThreadFactoryBuilder的setUncaughtExceptionHandler返回this（[issue#I1J4YJ@Gitee](https://gitee.com/dromara/hutool/issues/I1J4YJ)）

### 🐞Bug修复
* 【core   】     修复DateUtil.parse解析2020-5-8 3:12:13错误问题（[issue#I1IZA3@Gitee](https://gitee.com/dromara/hutool/issues/I1IZA3)）
* 【core   】     修复Img.pressImg大小无效问题([issue#I1HSWU@Gitee](https://gitee.com/dromara/hutool/issues/I1HSWU))
* 【core   】     修复CronUtil.stop没有清除任务的问题([issue#I1JACI@Gitee](https://gitee.com/dromara/hutool/issues/I1JACI))

-------------------------------------------------------------------------------------------------------------
## 5.3.6 (2020-05-30)

### 🐣新特性
* 【core   】     NumberConverter Long类型增加日期转换（[pr#872@Github](https://github.com/dromara/hutool/pull/872)）
* 【all    】     StrUtil and SymmetricCrypto注释修正（[pr#873@Github](https://github.com/dromara/hutool/pull/873)）
* 【core   】     CsvReader支持返回Bean（[issue#869@Github](https://github.com/dromara/hutool/issues/869)）
* 【core   】     Snowflake循环等待下一个时间时避免长时间循环，加入对时钟倒退的判断（[pr#874@Github](https://github.com/dromara/hutool/pull/874)）
* 【extra  】     新增 QRCode base64 编码形式返回（[pr#878@Github](https://github.com/dromara/hutool/pull/878)）
* 【core   】     ImgUtil增加toBase64DateUri，URLUtil增加getDataUri方法
* 【core   】     IterUtil添加List转Map的工具方法（[pr#123@Gitee](https://gitee.com/dromara/hutool/pulls/123)）
* 【core   】     BeanValueProvider转换失败时，返回原数据，而非null
* 【core   】     支持BeanUtil.toBean(object, Map.class)转换（[issue#I1I4HC@Gitee](https://gitee.com/dromara/hutool/issues/I1I4HC)）
* 【core   】     MapUtil和CollUtil增加clear方法（[issue#I1I4HC@Gitee](https://gitee.com/dromara/hutool/issues/I1I4HC)）
* 【core   】     增加FontUtil，可定义pressText是否从中间（[issue#I1HSWU@Gitee](https://gitee.com/dromara/hutool/issues/I1HSWU)）
* 【http   】     SoapClient支持自定义请求头（[issue#I1I0AO@Gitee](https://gitee.com/dromara/hutool/issues/I1I0AO)）
* 【script 】     ScriptUtil增加evalInvocable和invoke方法（[issue#I1HHCP@Gitee](https://gitee.com/dromara/hutool/issues/I1HHCP)）
* 【core   】     ImgUtil增加去除背景色的方法（[pr#124@Gitee](https://gitee.com/dromara/hutool/pulls/124)）
* 【system 】     OshiUtil增加获取CPU使用率的方法（[pr#124@Gitee](https://gitee.com/dromara/hutool/pulls/124)）
* 【crypto 】     AsymmetricAlgorithm去除EC（[issue#887@Github](https://github.com/dromara/hutool/issues/887)）
* 【cache  】     超时缓存使用的线程池大小默认为1（[issue#890@Github](https://github.com/dromara/hutool/issues/890)）
* 【poi    】     ExcelSaxReader支持handleCell方法
* 【core   】     Snowflake容忍2秒内的时间回拨（[issue#I1IGDX@Gitee](https://gitee.com/dromara/hutool/issues/I1IGDX)）
* 【core   】     StrUtil增加isAllNotEmpty、isAllNotBlank方法（[pr#895@Github](https://github.com/dromara/hutool/pull/895)）
* 【core   】     DateUtil增加dayOfYear方法（[pr#895@Github](https://github.com/dromara/hutool/pull/895)）
* 【core   】     DateUtil增加dayOfYear方法（[pr#895@Github](https://github.com/dromara/hutool/pull/895)）
* 【http   】     HttpUtil增加downloadBytes方法（[pr#895@Github](https://github.com/dromara/hutool/pull/895)）
* 【core   】     isMactchRegex失效标记，增加isMatchRegex（[issue#I1IPJG@Gitee](https://gitee.com/dromara/hutool/issues/I1IPJG)）
* 【core   】     优化Validator.isChinese
* 【core   】     ArrayUtil.addAll增加原始类型支持（[issue#898@Github](https://github.com/dromara/hutool/issues/898)）
* 【core   】     DateUtil.parse支持2020-1-1这类日期解析（[issue#I1HGWW@Github](https://github.com/dromara/hutool/issues/I1HGWW)）

### 🐞Bug修复
* 【core   】     修复SimpleCache死锁问题（[issue#I1HOKB@Gitee](https://gitee.com/dromara/hutool/issues/I1HOKB)）
* 【core   】     修复SemaphoreRunnable释放问题（[issue#I1HLQQ@Gitee](https://gitee.com/dromara/hutool/issues/I1HLQQ)）
* 【poi    】     修复Sax方式读取Excel行号错误问题（[issue#882@Github](https://github.com/dromara/hutool/issues/882)）
* 【poi    】     修复Sax方式读取Excel日期类型数据03和07不一致问题（[issue#I1HL1C@Gitee](https://gitee.com/dromara/hutool/issues/I1HL1C)）
* 【poi    】     修复CamelCaseLinkedMap构造错误（[issue#I1IZ30@Gitee](https://gitee.com/dromara/hutool/issues/I1IZ30)）

-------------------------------------------------------------------------------------------------------------

## 5.3.5 (2020-05-13)

### 🐣新特性
* 【core   】     增加CollUtil.map方法
* 【extra  】     增加Sftp.lsEntries方法，Ftp和Sftp增加recursiveDownloadFolder（[pr#121@Gitee](https://gitee.com/dromara/hutool/pulls/121)）
* 【system 】     OshiUtil增加getNetworkIFs方法
* 【core   】     CollUtil增加unionDistinct、unionAll方法（[pr#122@Gitee](https://gitee.com/dromara/hutool/pulls/122)）
* 【core   】     增加IoUtil.readObj重载，通过ValidateObjectInputStream由用户自定义安全检查。
* 【http   】     改造HttpRequest中文件上传部分，增加MultipartBody类

### 🐞Bug修复
* 【core   】     修复IoUtil.readObj中反序列化安全检查导致的一些问题，去掉安全检查。
* 【http   】     修复SimpleServer文件访问404问题（[issue#I1GZI3@Gitee](https://gitee.com/dromara/hutool/issues/I1GZI3)）
* 【core   】     修复BeanCopier中循环引用逻辑问题（[issue#I1H2VN@Gitee](https://gitee.com/dromara/hutool/issues/I1H2VN)）

-------------------------------------------------------------------------------------------------------------

## 5.3.4 (2020-05-10)

### 🐣新特性
* 【core   】     增加URLUtil.getContentLength方法（[issue#I1GB1Z@Gitee](https://gitee.com/dromara/hutool/issues/I1GB1Z)）
* 【extra  】     增加PinyinUtil（[issue#I1GMIV@Gitee](https://gitee.com/dromara/hutool/issues/I1GMIV)）

### 🐞Bug修复
* 【extra  】     修复Ftp设置超时问题（[issue#I1GMTQ@Gitee](https://gitee.com/dromara/hutool/issues/I1GMTQ)）
* 【core   】     修复TreeUtil根据id查找子节点时的NPE问题（[pr#120@Gitee](https://gitee.com/dromara/hutool/pulls/120)）
* 【core   】     修复BeanUtil.copyProperties中Alias注解无效问题（[issue#I1GK3M@Gitee](https://gitee.com/dromara/hutool/issues/I1GK3M)）
* 【core   】     修复CollUtil.containsAll空集合判断问题（[issue#I1G9DE@Gitee](https://gitee.com/dromara/hutool/issues/I1G9DE)）
* 【core   】     修复XmlUtil.xmlToBean失败问题（[issue#865@Github](https://github.com/dromara/hutool/issues/865)）

-------------------------------------------------------------------------------------------------------------

## 5.3.3 (2020-05-05)

### 🐣新特性
* 【core   】     ImgUtil.createImage支持背景透明（[issue#851@Github](https://github.com/dromara/hutool/issues/851)）
* 【json   】     更改JSON转字符串时"</"被转义的规则为不转义（[issue#852@Github](https://github.com/dromara/hutool/issues/852)）
* 【cron   】     表达式的所有段支持L关键字（[issue#849@Github](https://github.com/dromara/hutool/issues/849)）
* 【extra  】     增加PinyinUtil，封装TinyPinyin
* 【extra  】     Ftp和Sftp增加FtpConfig，提供超时等更多可选参数
* 【extra  】     SpringUtil增加getActiveProfiles、getBeansOfType、getBeanNamesForType方法（[issue#I1FXF3@Gitee](https://gitee.com/dromara/hutool/issues/I1FXF3)）
* 【bloomFilter】 避免布隆过滤器数字溢出（[pr#119@Gitee](https://gitee.com/dromara/hutool/pulls/119)）
* 【core   】     增加IoUtil.writeObj（[issue#I1FZIE](https://gitee.com/dromara/hutool/issues/I1FZIE)）
* 【core   】     增加FastStringWriter
* 【core   】     增加NumberUtil.ceilDiv方法（[pr#858@Github](https://github.com/dromara/hutool/pull/858)）
* 【core   】     IdcardUtil增加省份校验（[issue#859@Github](https://github.com/dromara/hutool/issues/859)）
* 【extra  】     TemplateFactory和TokenizerFactory增加单例的get方法

### 🐞Bug修复
* 【core   】     修复URLBuilder中请求参数有`&amp;`导致的问题（[issue#850@Github](https://github.com/dromara/hutool/issues/850)）
* 【core   】     修复URLBuilder中路径以`/`结尾导致的问题（[issue#I1G44J@Gitee](https://gitee.com/dromara/hutool/issues/I1G44J)）
* 【db     】     修复SqlBuilder中orderBy无效问题（[issue#856@Github](https://github.com/dromara/hutool/issues/856)）
* 【core   】     修复StrUtil.subBetweenAll错误问题（[issue#861@Github](https://github.com/dromara/hutool/issues/861)）

-------------------------------------------------------------------------------------------------------------

## 5.3.2 (2020-04-23)

### 🐣新特性
* 【core   】     增加NetUtil.isOpen方法
* 【core   】     增加ThreadUtil.sleep和safeSleep的重载
* 【core   】     Sftp类增加toString方法（[issue#I1F2T4@Gitee](https://gitee.com/dromara/hutool/issues/I1F2T4)）
* 【core   】     修改FileUtil.size逻辑，不存在的文件返回0
* 【extra  】     Sftp.ls遇到文件不存在返回空集合，而非抛异常（[issue#844@Github](https://github.com/dromara/hutool/issues/844)）
* 【http   】     改进HttpRequest.toString()格式，添加url

### 🐞Bug修复
* 【db     】     修复PageResult.isLast计算问题
* 【cron   】     修复更改系统时间后CronTimer被阻塞的问题（[issue#838@Github](https://github.com/dromara/hutool/issues/838)）
* 【db     】     修复Page.addOrder无效问题（[issue#I1F9MZ@Gitee](https://gitee.com/dromara/hutool/issues/I1F9MZ)）
* 【json   】     修复JSONConvert转换日期空指针问题（[issue#I1F8M2@Gitee](https://gitee.com/dromara/hutool/issues/I1F8M2)）
* 【core   】     修复XML中带注释Xpath解析导致空指针问题（[issue#I1F2WI@Gitee](https://gitee.com/dromara/hutool/issues/I1F2WI)）
* 【core   】     修复FileUtil.rename原文件无扩展名多点的问题（[issue#839@Github](https://github.com/dromara/hutool/issues/839)）
* 【db     】     修复DbUtil.close可能存在的空指针问题（[issue#847@Github](https://github.com/dromara/hutool/issues/847)）

-------------------------------------------------------------------------------------------------------------
## 5.3.1 (2020-04-17)

### 🐣新特性
* 【core   】     ListUtil、MapUtil、CollUtil增加empty方法
* 【poi    】     调整别名策略，clearHeaderAlias和addHeaderAlias同时清除aliasComparator（[issue#828@Github](https://github.com/dromara/hutool/issues/828)）
* 【core   】     修改StrUtil.equals逻辑，改为contentEquals
* 【core   】     增加URLUtil.UrlDecoder
* 【core   】     增加XmlUtil.setNamespaceAware，getByPath支持UniversalNamespaceCache
* 【aop    】     增加Spring-cglib支持，改为SPI实现
* 【json   】     增加JSONUtil.parseXXX增加JSONConfig参数
* 【core   】     RandomUtil.randomNumber改为返回char
* 【crypto 】     SM2支持设置Digest和DSAEncoding（[issue#829@Github](https://github.com/dromara/hutool/issues/829)）

### 🐞Bug修复
* 【json   】     修复解析JSON字符串时配置无法传递问题（[issue#I1EIDN@Gitee](https://gitee.com/dromara/hutool/issues/I1EIDN)）
* 【core   】     修复ServletUtil.readCookieMap空指针问题（[issue#827@Github](https://github.com/dromara/hutool/issues/827)）
* 【crypto 】     修复SM2中检查密钥导致的问题（[issue#I1EC47@Gitee](https://gitee.com/dromara/hutool/issues/I1EC47)）
* 【core   】     修复TableMap.isEmpty判断问题
* 【http   】     修复编码后的URL传入导致二次编码的问题（[issue#I1EIMN@Gitee](https://gitee.com/dromara/hutool/issues/I1EIMN)）

-------------------------------------------------------------------------------------------------------------

## 5.3.0 (2020-04-07)

### 🐣新特性
* 【extra  】     JschUtil增加execByShell方法([issue#I1CYES@Gitee](https://gitee.com/dromara/hutool/issues/I1CYES))
* 【core   】     StrUtil增加subBetweenAll方法，Console增加where和lineNumber方法([issue#812@Github](https://github.com/dromara/hutool/issues/812))
* 【core   】     TableMap增加getKeys和getValues方法
* 【json   】     JSONObject和JSONArray增加set方法，标识put弃用
* 【http   】     增加SimpleHttpServer
* 【script 】     增加createXXXScript，区别单例
* 【core   】     修改FileUtil.writeFileToStream等方法返回值为long
* 【core   】     CollUtil.split增加空集合判定（[issue#814@Github](https://github.com/dromara/hutool/issues/814)）
* 【core   】     NetUtil增加parseCookies方法
* 【core   】     CollUtil增加toMap方法
* 【core   】     CollUtil和IterUtil废弃一些方法
* 【core   】     添加ValidateObjectInputStream避免对象反序列化漏洞风险
* 【core   】     添加BiMap
* 【all    】     cn.hutool.extra.servlet.multipart包迁移到cn.hutool.core.net下
* 【core   】     XmlUtil.mapToXml方法支持集合解析（[issue#820@Github](https://github.com/dromara/hutool/issues/820)）
* 【json   】     解析Object中对是否为bean单独判断，而不是直接解析
* 【core   】     SimHash锁改为StampedLock
* 【core   】     Singleton改为SimpleCache实现
* 【core   】     增加CalendarUtil，DateUtil相关方法全部迁移到此

### 🐞Bug修复
* 【extra  】     修复SpringUtil使用devtools重启报错问题
* 【http   】     修复HttpUtil.encodeParams针对无参数URL问题（[issue#817@Github](https://github.com/dromara/hutool/issues/817)）
* 【extra  】     修复模板中无效引用的问题
* 【extra  】     修复读取JSON文本配置未应用到子对象的问题（[issue#818@Github](https://github.com/dromara/hutool/issues/818)）
* 【extra  】     修复XmlUtil.createXml中namespace反向问题
* 【core   】     修复WatchMonitor默认无event问题

-------------------------------------------------------------------------------------------------------------

## 5.2.5 (2020-03-26)

### 🐣新特性
* 【core   】     增加逻辑，对于原始类型注入，使用默认值（[issue#797@Github](https://github.com/dromara/hutool/issues/797)）
* 【core   】     增加CityHash算法
* 【core   】     PageUtil支持setFirstPageNo自定义第一页的页码（[issue#I1CGNZ@Gitee](https://gitee.com/dromara/hutool/issues/I1CGNZ)）
* 【http   】     UserAgentUtil增加Chromium内核的Edge浏览器支持（[issue#800@Github](https://github.com/dromara/hutool/issues/800)）
* 【cache  】     修改FIFOCache中linkedHashMap的初始容量策略（[pr#801@Github](https://github.com/dromara/hutool/pull/801)）
* 【core   】     修改XmlUtil中setNamespaceAware默认为true
* 【core   】     TreeNode增加extra
* 【core   】     CollUtil.newHashSet重载歧义，更换为set方法
* 【core   】     增加ListUtil，增加Hash32、Hash64、Hash128接口
* 【crypto 】     BCUtil增加readPemPrivateKey和readPemPublicKey方法
* 【cache  】     替换读写锁为StampedLock，增加LockUtil

### 🐞Bug修复
* 【core   】     修复NumberWordFormatter拼写错误（[issue#799@Github](https://github.com/dromara/hutool/issues/799)）
* 【poi    】     修复xls文件下拉列表无效问题（[issue#I1C79P@Gitee](https://gitee.com/dromara/hutool/issues/I1C79P)）
* 【poi    】     修复使用Cglib代理问题（[issue#I1C79P@Gitee](https://gitee.com/dromara/hutool/issues/I1C79P)）
* 【core   】     修复DateUtil.weekCount跨年计算问题

-------------------------------------------------------------------------------------------------------------
## 5.2.4

### 🐣新特性
* 【setting】     Setting中增加addSetting和autoLoad重载（[pr#104@Gitee](https://gitee.com/dromara/hutool/pulls/104)）
* 【core   】     增加copyProperties，根据Class创建对象并进行属性拷贝（[pr#105@Gitee](https://gitee.com/dromara/hutool/pulls/105)）
* 【core   】     添加获取class当前文件夹名称方法（[pr#106@Gitee](https://gitee.com/dromara/hutool/pulls/106)）
* 【core   】     BooleanUtil中重载歧义修正，修改了包装参数的方法名（[issue#I1BSK8@Gitee](https://gitee.com/dromara/hutool/issues/I1BSK8)）
* 【core   】     XmlUtil增加xmlToBean和beanToXml方法
* 【db     】     设置全局忽略大小写DbUtil.setCaseInsensitiveGlobal(true)（[issue#784@Github](https://github.com/dromara/hutool/issues/784)）
* 【core   】     增加CallerUtil.getCallerMethodName方法
* 【core   】     Tree增加getParent方法，可以获取父节点，抽象Node接口
* 【core   】     增加社会信用代码工具CreditCodeUtil（[pr#112@Gitee](https://gitee.com/dromara/hutool/pulls/112)）
* 【core   】     ChineseDate增加构造重载，增加toStringNormal（[issue#792@Github](https://github.com/dromara/hutool/issues/792)）
* 【core   】     BeanUtil.toBean增加重载（[issue#797@Github](https://github.com/dromara/hutool/issues/797)）

### 🐞Bug修复
* 【core   】     修复TypeUtil无法获取泛型接口的泛型参数问题（[issue#I1BRFI@Gitee](https://gitee.com/dromara/hutool/issues/I1BRFI)）
* 【core   】     修复MySQL中0000报错问题
* 【core   】     修复BeanPath从Map取值为空的问题（[issue#790@Github](https://github.com/dromara/hutool/issues/790)）
* 【poi    】     修复添加图片尺寸的单位问题（[issue#I1C2ER@Gitee](https://gitee.com/dromara/hutool/issues/I1C2ER)）
* 【setting】     修复getStr中逻辑问题（[pr#113@Gitee](https://gitee.com/dromara/hutool/pulls/113)）
* 【json   】     修复JSONUtil.toXml汉字被编码的问题（[pr#795@Gitee](https://gitee.com/dromara/hutool/pulls/795)）
* 【poi    】     修复导出的Map列表中每个map长度不同导致的对应不一致的问题（[issue#793@Gitee](https://gitee.com/dromara/hutool/issues/793)）

-------------------------------------------------------------------------------------------------------------
## 5.2.3

### 🐣新特性
* 【http   】     UserAgentUtil增加识别ios和android等（[issue#781@Github](https://github.com/dromara/hutool/issues/781)）
* 【core   】     支持新领车牌（[issue#I1BJHE@Gitee](https://gitee.com/dromara/hutool/issues/I1BJHE)）

### 🐞Bug修复
* 【core   】     修复PageUtil第一页语义不明确的问题（[issue#782@Github](https://github.com/dromara/hutool/issues/782)）
* 【extra  】     修复TemplateFactory引入包导致的问题
* 【core   】     修复ServiceLoaderUtil.loadFirstAvailable问题

-------------------------------------------------------------------------------------------------------------
## 5.2.2

### 🐣新特性

### 🐞Bug修复
* 【http   】     修复body方法添加多余头的问题（[issue#769@Github](https://github.com/dromara/hutool/issues/769)）
* 【bloomFilter 】修复默认为int类型,左移超过32位后,高位丢失问题（[pr#770@Github](https://github.com/dromara/hutool/pull/770)）
* 【core   】     修复beginOfWeek和endOfWeek一周开始计算错误问题（[issue#I1BDPW@Gitee](https://gitee.com/dromara/hutool/issues/I1BDPW)）
* 【db     】     修复Db.query使用命名方式查询产生的歧义（[issue#776@Github](https://github.com/dromara/hutool/issues/776)）

-------------------------------------------------------------------------------------------------------------

## 5.2.1

### 🐣新特性
* 【core   】     修改FastDateParser策略，与JDK保持一致（[issue#I1AXIN@Gitee](https://gitee.com/dromara/hutool/issues/I1AXIN)）
* 【core   】     增加tree（树状结构）（[pr#100@Gitee](https://gitee.com/dromara/hutool/pulls/100)）
* 【core   】     增加randomEleList（[pr#764@Github](https://github.com/dromara/hutool/pull/764)）
### 🐞Bug修复
* 【setting】     修复Props.toBean方法null的问题
* 【core   】     修复DataUtil.parseLocalDateTime无时间部分报错问题（[issue#I1B18H@Gitee](https://gitee.com/dromara/hutool/issues/I1B18H)）
* 【core   】     修复NetUtil.isUsableLocalPort()判断问题（[issue#765@Github](https://github.com/dromara/hutool/issues/765)）
* 【poi    】     修复ExcelWriter写出多个sheet错误的问题（[issue#766@Github](https://github.com/dromara/hutool/issues/766)）
* 【extra  】     修复模板引擎自定义配置失效问题（[issue#767@Github](https://github.com/dromara/hutool/issues/767)）

-------------------------------------------------------------------------------------------------------------

## 5.2.0

### 🐣新特性
* 【core  】     NumberUtil.decimalFormat增加Object对象参数支持
* 【core  】     增加ReflectUtil.getFieldValue支持Alias注解
* 【core  】     Bean字段支持Alias注解（包括转map,转bean等）
* 【core  】     增加ValueListHandler，优化结果集获取方式
* 【http  】     支持patch方法（[issue#666@Github](https://github.com/dromara/hutool/issues/666)）
* 【crypto】     BCUtil支持更加灵活的密钥类型，增加writePemObject方法
* 【core  】     增加ServiceLoaderUtil
* 【core  】     增加EnumUtil.getEnumAt方法
* 【core  】     增强EnumConvert判断能力（[issue#I17082@Gitee](https://gitee.com/dromara/hutool/issues/I17082)）
* 【all   】     log、template、tokenizer使用SPI机制代替硬编码
* 【poi   】     Word07Writer增加addPicture
* 【crypto】     RSA算法中，BlockSize长度策略调整（[issue#721@Github](https://github.com/dromara/hutool/issues/721)）
* 【crypto】     删除SM2Engine，使用BC库中的对象替代
* 【crypto】     增加PemUtil工具类
* 【dfa   】     WordTree增加Filter，支持自定义特殊字符过滤器
* 【poi   】     对于POI依赖升级到4.1.2
* 【crypto】     增加国密SM2验签密钥格式支持（[issue#686@Github](https://github.com/dromara/hutool/issues/686)）

### 🐞Bug修复

-------------------------------------------------------------------------------------------------------------

## 5.1.5

### 🐣新特性
* 【poi  】     Excel合并单元格读取同一个值，不再为空
* 【core 】     增加EscapeUtil.escapeAll（[issue#758@Github](https://github.com/dromara/hutool/issues/758)）
* 【core 】     增加formatLocalDateTime和parseLocalDateTime方法（[pr#97@Gitee](https://gitee.com/dromara/hutool/pulls/97)）

### 🐞Bug修复
* 【core 】     修复EscapeUtil.escape转义错误（[issue#758@Github](https://github.com/dromara/hutool/issues/758)）
* 【core 】     修复Convert.toLocalDateTime(Object value, Date defaultValue)返回结果不是LocalDateTime类型的问题（[pr#97@Gitee](https://gitee.com/dromara/hutool/pulls/97)）

-------------------------------------------------------------------------------------------------------------

## 5.1.4

### 🐣新特性
* 【poi  】     增加单元格位置引用（例如A11等方式获取单元格）
* 【extra】     ServletUtil.fillBean支持数据和集合字段（[issue#I19ZMK@Gitee](https://gitee.com/dromara/hutool/issues/I19ZMK)）
* 【core 】     修改ThreadUtil.newSingleExecutor默认队列大小（[issue#754@Github](https://github.com/dromara/hutool/issues/754)）
* 【core 】     修改ExecutorBuilder默认队列大小（[issue#753@Github](https://github.com/dromara/hutool/issues/753)）
* 【core 】     FileTypeUtil增加mp4的magic（[issue#756@Github](https://github.com/dromara/hutool/issues/756)）

### 🐞Bug修复
* 【core 】     修复CombinationAnnotationElement数组判断问题（[issue#752@Github](https://github.com/dromara/hutool/issues/752)）
* 【core 】     修复log4j2使用debug行号打印问题（[issue#I19NFJ@Github](https://github.com/dromara/hutool/issues/I19NFJ)）
* 【poi  】     修复sax读取excel03数组越界问题（[issue#750@Github](https://github.com/dromara/hutool/issues/750)）

-------------------------------------------------------------------------------------------------------------

## 5.1.3

### 🐣新特性
* 【core 】     废弃isMactchRegex，改为isMatchRegex（方法错别字）
* 【core 】     修正hasNull()方法上注释错误（[issue#I18TAG@Gitee](https://gitee.com/dromara/hutool/issues/I18TAG)）
* 【core 】     Snowflake的起始时间可以被指定（[pr#95@Gitee](https://gitee.com/dromara/hutool/pulls/95)）
* 【core 】     增加PropsUtil及getFirstFound方法（[issue#I1960O@Gitee](https://gitee.com/dromara/hutool/issues/I1960O)）
### 🐞Bug修复
* 【core 】     CharsetUtil在不支持GBK的系统中运行报错问题（[issue#731@Github](https://github.com/dromara/hutool/issues/731)）
* 【core 】     RandomUtil的randomEleSet方法顺序不随机的问题（[pr#741@Github](https://github.com/dromara/hutool/pull/741)）
* 【core 】     修复StopWatch的toString判断问题（[issue#I18VIK@Gitee](https://gitee.com/dromara/hutool/issues/I18VIK)）

-------------------------------------------------------------------------------------------------------------

## 5.1.2

### 🐣新特性
* 【core 】     XmlUtil支持可选是否输出omit xml declaration（[pr#732@Github](https://github.com/dromara/hutool/pull/732)）
* 【core 】     车牌号校验兼容新能源车牌（[pr#92@Gitee](https://gitee.com/dromara/hutool/pulls/92)）
* 【core 】     在NetUtil中新增ping功能（[pr#91@Gitee](https://gitee.com/dromara/hutool/pulls/91)）
* 【core 】     DateUtil.offset不支持ERA，增加异常提示（[issue#I18KD5@Gitee](https://gitee.com/dromara/hutool/issues/I18KD5)）
* 【http 】     改进HttpUtil访问HTTPS接口性能问题，SSL证书使用单例（[issue#I18AL1@Gitee](https://gitee.com/dromara/hutool/issues/I18AL1)）

### 🐞Bug修复
* 【core 】     修复isExpired的bug（[issue#733@Gtihub](https://github.com/dromara/hutool/issues/733)）

-------------------------------------------------------------------------------------------------------------

## 5.1.1

### 🐣新特性
* 【core 】     ClassUtil.isSimpleValueType增加TemporalAccessor支持（[issue#I170HK@Gitee](https://gitee.com/dromara/hutool/issues/I170HK)）
* 【core 】     增加Convert.toPrimitiveByteArray方法，Convert支持对象序列化和反序列化
* 【core 】     DateUtil增加isExpired(Date startDate, Date endDate, Date checkDate)（[issue#687@Github](https://github.com/dromara/hutool/issues/687)）
* 【core 】     增加Alias注解
* 【core 】     修正NumberChineseFormatter和NumberWordFormatter（类名拼写错误）
* 【all  】     修正equals，避免可能存在的空指针问题（[pr#692@Github](https://github.com/dromara/hutool/pull/692)）
* 【core  】    提供一个自带默认值的Map（[pr#87@Gitee](https://gitee.com/dromara/hutool/pulls/87)）
* 【core  】    修改Dict在非大小写敏感状态下get也不区分大小写（[issue#722@Github](https://github.com/dromara/hutool/issues/722)）
* 【core  】    StrUtil增加contains方法（[issue#716@Github](https://github.com/dromara/hutool/issues/716)）
* 【core  】    QrCodeUtil增加背景透明支持（[pr#89@Gitee](https://gitee.com/dromara/hutool/pulls/89)）
* 【core  】    增加农历ChineseDate（[pr#90@Gitee](https://gitee.com/dromara/hutool/pulls/90)）
* 【core  】    ZipUtil增加zip方法写出到流（[issue#I17SCT@Gitee](https://gitee.com/dromara/hutool/issues/I17SCT)）
* 【db    】    Db.use().query的方法中增加Map参数接口（[issue#709@Github](https://github.com/dromara/hutool/issues/709)）
* 【db    】    getDialect使用数据源作为锁（[issue#720@Github](https://github.com/dromara/hutool/issues/720)）

### 🐞Bug修复
* 【core 】     修复NumberUtil.mul中null的结果错误问题（[issue#I17Y4J@Gitee](https://gitee.com/dromara/hutool/issues/I17Y4J)）
* 【core 】     修复当金额大于等于1亿时，转换会多出一个万字的bug（[pr#715@Github](https://github.com/dromara/hutool/pull/715)）
* 【core 】     修复FileUtil.listFileNames位于jar内导致的文件找不到问题
* 【core 】     修复TextSimilarity.similar去除字符导致的问题（[issue#I17K2A@Gitee](https://gitee.com/dromara/hutool/issues/I17K2A)）
* 【core 】     修复unzip文件路径问题（[issue#I17VU7@Gitee](https://gitee.com/dromara/hutool/issues/I17VU7)）

-------------------------------------------------------------------------------------------------------------

## 5.1.0

### 🐣新特性
* 【core 】     新增WatchServer（[issue#440@Github](https://github.com/dromara/hutool/issues/440)）
* 【core 】     ReflectUtil.getFieldValue支持static（[issue#662@Github](https://github.com/dromara/hutool/issues/662)）
* 【core 】     改进Bean判断和注入逻辑：支持public字段注入（[issue#I1689L@Gitee](https://gitee.com/dromara/hutool/issues/I1689L)）
* 【extra】     新增SpringUtil
* 【http 】     Get请求支持body，移除body（JSON）方法（[issue#671@Github](https://github.com/dromara/hutool/issues/671)）
* 【core 】     ReflectUtil修正getFieldValue逻辑，防止歧义


### 🐞Bug修复
* 【db  】      修复SqlExecutor.callQuery关闭Statement导致的问题（[issue#I16981@Gitee](https://gitee.com/dromara/hutool/issues/I16981)）
* 【db  】      修复XmlUtil.xmlToMap中List节点的问题（[pr#82@Gitee](https://gitee.com/dromara/hutool/pulls/82)）
* 【core】      修复ZipUtil中对于/结尾路径处理的问题（[issue#I16PKP@Gitee](https://gitee.com/dromara/hutool/issues/I16PKP)）
* 【core】      修复DateConvert对int不支持导致的问题（[issue#677@Github](https://github.com/dromara/hutool/issues/677)）

-------------------------------------------------------------------------------------------------------------

## 5.0.7

### 🐣新特性
* 【core 】      解决NumberUtil导致的ambiguous问题（[issue#630@Github](https://github.com/dromara/hutool/issues/630)）
* 【core 】      BeanUtil.isEmpty()忽略字段支持，增加isNotEmpty（[issue#629@Github](https://github.com/dromara/hutool/issues/629)）
* 【extra】      邮件发送后获取message-id（[issue#I15FKR@Gitee](https://gitee.com/dromara/hutool/issues/I15FKR)）
* 【core 】      CaseInsensitiveMap/CamelCaseMap增加toString（[issue#636@Github](https://github.com/dromara/hutool/issues/636)）
* 【core 】      XmlUtil多节点改进（[issue#I15I0R@Gitee](https://gitee.com/dromara/hutool/issues/I15I0R)）
* 【core 】      Thread.excAsync修正为execAsync（[issue#642@Github](https://github.com/dromara/hutool/issues/642)）
* 【core 】      FileUtil.getAbsolutePath修正正则（[issue#648@Github](https://github.com/dromara/hutool/issues/648)）
* 【core 】      NetUtil增加getNetworkInterface方法（[issue#I15WEL@Gitee](https://gitee.com/dromara/hutool/issues/I15WEL)）
* 【core 】      增加ReflectUtil.getFieldMap方法（[issue#I15WJ7@Gitee](https://gitee.com/dromara/hutool/issues/I15WJ7)）

### 🐞Bug修复
* 【extra】      修复SFTP.upload上传失败的问题（[issue#I15O40@Gitee](https://gitee.com/dromara/hutool/issues/I15O40)）
* 【db】         修复findLike匹配错误问题
* 【core 】      修复scale方法透明无效问题（[issue#I15L5S@Gitee](https://gitee.com/dromara/hutool/issues/I15L5S)）
* 【extra】      修复exec返回无效（[issue#I15L5S@Gitee](https://gitee.com/dromara/hutool/issues/I15L5S)）
* 【cron】       修复CronPattern注释（[pr#646@Github](https://github.com/dromara/hutool/pull/646)）
* 【json】       修复LocalDateTime等JDK8时间对象不被支持的问题（[issue#644@Github](https://github.com/dromara/hutool/issues/644)）

-------------------------------------------------------------------------------------------------------------

## 5.0.6

### 🐣新特性
* 【setting】    toBean改为泛型，增加class参数重载（[pr#80@Gitee](https://gitee.com/dromara/hutool/pulls/80)）
* 【core】       XmlUtil使用JDK默认的实现，避免第三方实现导致的问题（[issue#I14ZS1@Gitee](https://gitee.com/dromara/hutool/issues/I14ZS1)）
* 【poi】        写入单元格数据类型支持jdk8日期格式（[pr#628@Github](https://github.com/dromara/hutool/pull/628)）

### 🐞Bug修复
* 【core】       修复DateUtil.format使用DateTime时区失效问题（[issue#I150I7@Gitee](https://gitee.com/dromara/hutool/issues/I150I7)）
* 【core】       修复ZipUtil解压目录遗留问题（[issue#I14NO3@Gitee](https://gitee.com/dromara/hutool/issues/I14NO3)）
* 【core】       修复等比缩放给定背景色无效问题（[pr#625@Github](https://github.com/dromara/hutool/pull/625)）
* 【poi 】       修复sax方式读取excel中无样式表导致的空指针问题
* 【core】       修复标准化URL时domain被转义的问题（[pr#654@Github](https://github.com/dromara/hutool/pull/654)）

-------------------------------------------------------------------------------------------------------------

## 5.0.5

### 🐣新特性
* 【core】       增加MapUtil.removeAny（[issue#612@Github](https://github.com/dromara/hutool/issues/612)）
* 【core】       Convert.toList支持[1,2]字符串（[issue#I149XN@Gitee](https://gitee.com/dromara/hutool/issues/I149XN)）
* 【core】       修正DateUtil.thisWeekOfMonth注释错误（[issue#614@Github](https://github.com/dromara/hutool/issues/614)）
* 【core】       DateUtil增加toLocalDate等方法，DateTime更好的支持时区
* 【core】       BeanUtil.getProperty返回泛型对象（[issue#I14PIW@Gitee](https://gitee.com/dromara/hutool/issues/I14PIW)）
* 【core】       FileTypeUtil使用扩展名辅助判断类型（[issue#I14JBH@Gitee](https://gitee.com/dromara/hutool/issues/I14JBH)）

### 🐞Bug修复
* 【db】         修复MetaUtil.getTableMeta()方法未释放ResultSet的bug（[issue#I148GH@Gitee](https://gitee.com/dromara/hutool/issues/I148GH)）
* 【core】       修复DateUtil.age闰年导致的问题（[issue#I14BVN@Gitee](https://gitee.com/dromara/hutool/issues/I14BVN)）
* 【extra】      修复ServletUtil.getCookie大小写问题（[pr#79@Gitee](https://gitee.com/dromara/hutool/pulls/79)）
* 【core】       修复IdcardUtil.isValidCard18报错问题（[issue#I14LTJ@Gitee](https://gitee.com/dromara/hutool/issues/I14LTJ)）
* 【poi】        修复double值可能存在的精度问题（[issue#I14FG1@Gitee](https://gitee.com/dromara/hutool/issues/I14FG1)）
* 【core】       修复Linux下解压目录不正确的问题（[issue#I14NO3@Gitee](https://gitee.com/dromara/hutool/issues/I14NO3)）

-------------------------------------------------------------------------------------------------------------

## 5.0.4

### 🐣新特性
* 【setting】    增加System.getenv变量替换支持
* 【core】       XmlUtil中mapToStr支持namespace（[pr#599@Github](https://github.com/dromara/hutool/pull/599)）
* 【core】       ZipUtil修改策略:默认关闭输入流（[issue#604@Github](https://github.com/dromara/hutool/issues/604)）
* 【core】       改进CsvReader，支持RowHandler按行处理（[issue#608@Github](https://github.com/dromara/hutool/issues/608)）
* 【core】       增加MapUtil.sortJoin，改进SecureUtil.signParams支持补充字符串（[issue#606@Github](https://github.com/dromara/hutool/issues/606)）
* 【core】       增加Money类（[issue#605@Github](https://github.com/dromara/hutool/issues/605)）

### 🐞Bug修复
* 【core】       解决ConcurrentHashSet不能序列化的问题（[issue#600@Github](https://github.com/dromara/hutool/issues/600)）
* 【core】       解决CsvReader.setErrorOnDifferentFieldCount循环调用问题

-------------------------------------------------------------------------------------------------------------

## 5.0.3

### 🐣新特性
### 🐞Bug修复
* 【extra】      修复遗留的getSession端口判断错误（[issue#594@Github](https://github.com/dromara/hutool/issues/594)）

-------------------------------------------------------------------------------------------------------------

## 5.0.2

### 🐣新特性
* 【core】       强化java.time包的对象转换支持

### 🐞Bug修复
* 【db】         修正字段中含有as导致触发关键字不能包装字段的问题（[issue#I13ML7@Gitee](https://gitee.com/dromara/hutool/issues/I13ML7)）
* 【extra】      修复QrCode中utf-8不支持大写的问题。（[issue#I13MT6@Gitee](https://gitee.com/dromara/hutool/issues/I13MT6)）
* 【http】       修复请求defalte数据解析错误问题。（[pr#593@Github](https://github.com/dromara/hutool/pull/593)）

-------------------------------------------------------------------------------------------------------------

## 5.0.1

### 🐣新特性
* 【json】       JSONUtil.toBean支持JSONArray
### 🐞Bug修复
* 【extra】      修复getSession端口判断错误

-------------------------------------------------------------------------------------------------------------

## 5.0.0

### 🐣新特性
* 【all】        升级JDK最低 支持到8
* 【log】        Log接口添加get的static方法
* 【all】        部分接口添加FunctionalInterface修饰
* 【crypto】     KeyUtil增加readKeyStore重载
* 【extra】      JschUtil增加私钥传入支持（[issue#INKDR@Gitee](https://gitee.com/dromara/hutool/issues/INKDR)）
* 【core】       DateUtil、DateTime、Convert全面支持jdk8的time包

### 🐞Bug修复
* 【http】       修复Cookie中host失效导致的问题（[issue#583@Github](https://github.com/dromara/hutool/issues/583)）
