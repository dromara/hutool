LFUCache
===

## 介绍
LFU(least frequently used) 最少使用率策略。根据使用次数来判定对象是否被持续缓存（使用率是通过访问次数计算），当缓存满时清理过期对象，清理后依旧满的情况下清除最少访问（访问计数最小）的对象并将其他对象的访问数减去这个最小访问数，以便新对象进入后可以公平计数。

## 使用
```java
Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
//通过实例化对象创建
//LFUCache<String, String> lfuCache = new LFUCache<String, String>(3);

lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
lfuCache.get("key1");//使用次数+1
lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2,3被移除）
String value2 = lfuCache.get("key2");//null
String value3 = lfuCache.get("key3");//null
```

