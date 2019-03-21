JSONUtil
===

## 介绍

`JSONUtil`是针对JSONObject和JSONArray的静态快捷方法集合，在之前的章节我们已经介绍了一些工具方法，在本章节我们将做一些补充。

## 使用
### parseXXX和toXXX

这两种方法主要是针对JSON和其它对象之间的转换。

### readXXX

这类方法主要是从JSON文件中读取JSON对象的快捷方法。包括：
- readJSON
- readJSONObject
- readJSONArray

### 其它方法
除了上面中常用的一些方法，JSONUtil还提供了一些JSON辅助方法：
- quote 对所有双引号做转义处理（使用双反斜杠做转义）
- wrap 包装对象，可以将普通任意对象转为JSON对象
- formatJsonStr 格式化JSON字符串，此方法并不严格检查JSON的格式正确与否

