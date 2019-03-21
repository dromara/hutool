CacheUtil
===

## 概述
CacheUtil是缓存创建的快捷工具类。用于快速创建不同的缓存对象。

## 使用

```java
//新建FIFOCache
Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
```

同样其它类型的Cache也可以调用newXXX的方法创建。

