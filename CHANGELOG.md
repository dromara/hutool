
# Changelog

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
