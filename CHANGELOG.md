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
* db模块的SqlRunner中可自定义Wrapper
* ExcelReader增加read重载方法（pr#4@Gitee）
* Convert.convert增加Class的重载，解决返回值歧义（感谢@t-io）
* Http中使用byte[]存储body，减少转换
* ExcelReader增加getWorkbook、getSheet方法

### Bug修复
* 修复ExcelReader空行导致空指针问题（pr#4@Gitee）
* 修复BeanUtil.getProperty不能获取父类属性的问题