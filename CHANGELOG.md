# Changelog

-------------------------------------------------------------------------------------------------------------

## 4.0.0

### 新特性
* 变更包名为cn.hutool.xxx
* 新增Object类，用于实现MongoDB的ID生成策略
* 验证码单独成为一个模块hutool-captcha
* 新增NamedThreadFactory
* 新增BufferUtil
* POI新增StyleUtil，StyleSet新增方法可设置背景、边框等样式
* JDBC参数针对BigInteger处理
* db模块支持显示和格式化显示SQL
* 调整日志优先级：ConsoleLog优先于JDKLog，Log4j2优先于Log4j

### Bug修复