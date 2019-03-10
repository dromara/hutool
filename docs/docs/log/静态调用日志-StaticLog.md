静态调用日志-StaticLog
===

## 由来

很多时候，我们只是想简简单的使用日志，最好一个方法搞定，我也不想创建Log对象，那么`StaticLog`或许是你需要的。

## 使用

```java
StaticLog.info("This is static {} log.", "INFO");
```

同样StaticLog提供了trace、debug、info、warn、error方法，提供变量占位符支持，使项目中日志的使用简单到没朋友。

StaticLog类中同样提供log方法，可能在极致简洁的状况下，提供非常棒的灵活性（打印日志等级由level参数决定）

## 与LogFactory同名方法

假如你只知道StaticLog，不知道LogFactory怎么办？Hutool非常贴心的提供了`get`方法，此方法与Logfactory中的`get`方法一样，同样可以获得Log对象。

