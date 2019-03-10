JSONArray
===

## 介绍
在JSON中，JSONArray代表一个数组，使用中括号包围，每个元素使用逗号隔开。一个JSONArray类似于这样：
```json
["value1","value2","value3"]
```

## 使用
### 创建
```java
//方法1
JSONArray array = JSONUtil.createArray();
//方法2
JSONArray array = new JSONArray();

array.add("value1");
array.add("value2");
array.add("value3");

//转为JSONArray字符串
array.toString();
```

### 转换
```java
String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
JSONArray array = JSONUtil.parseArray(jsonStr);
```

