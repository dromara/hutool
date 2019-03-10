LRUCache
===

## 介绍
LRU (least recently used)最近最久未使用缓存。根据使用时间来判定对象是否被持续缓存，当对象被访问时放入缓存，当缓存满了，最久未被使用的对象将被移除。此缓存基于LinkedHashMap，因此当被缓存的对象每被访问一次，这个对象的key就到链表头部。这个算法简单并且非常快，他比FIFO有一个显著优势是经常使用的对象不太可能被移除缓存。缺点是当缓存满时，不能被很快的访问。

## 使用
```java
Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
//通过实例化对象创建
//LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
lruCache.get("key1");//使用时间推近
lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
String value2 = lruCache.get("key");//null
```

