WeakCache
===

## 介绍
弱引用缓存。对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。丢弃某个键时，其条目从映射中有效地移除。该类使用了WeakHashMap做为其实现，缓存的清理依赖于JVM的垃圾回收。

## 使用
与TimedCache使用方法一致：

```java
WeakCache<String, String> weakCache = CacheUtil.newWeakCache(DateUnit.SECOND.getMillis() * 3);
```

WeakCache也可以像TimedCache一样设置定时清理时间，同时具备垃圾回收清理。

