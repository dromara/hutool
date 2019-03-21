Properties扩展-Props
===

## 介绍
对于Properties的广泛使用使我也无能为力，有时候遇到Properties文件又想方便的读写也不容易，于是对Properties做了简单的封装，提供了方便的构造方法（与Setting一致），并提供了与Setting一致的getXXX方法来扩展Properties类，`Props`类继承自Properties，所以可以兼容Properties类。

## 使用

Props的使用方法和Properties以及Setting一致（同时支持）：
```java
Props props = new Props("test.properties");
String user = props.getProperty("user");
String driver = props.getStr("driver");
```

