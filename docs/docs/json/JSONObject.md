JSONObject
===

## 介绍
JSONObject代表一个JSON中的键值对象，这个对象以大括号包围，每个键值对使用`,`隔开，键与值使用`:`隔开，一个JSONObject类似于这样：
```json
{
  "key1":"value1",
  "key2":"value2"
}
```

此处键部分可以省略双引号，值为字符串时不能省略，为数字或波尔值时不加双引号。

## 使用

### 创建
```java
JSONObject json1 = JSONUtil.createObj();
json1.put("a", "value1");
json1.put("b", "value2");
json1.put("c", "value3");
```

`JSONUtil.createObj()`是快捷新建JSONObject的工具方法，同样我们可以直接new：

```java
JSONObject json1 = new JSONObject();
```

### 转换
```java
String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
//方法一：使用工具类转换
JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
//方法二：new的方式转换
JSONObject jsonObject2 = new JSONObject(jsonStr);

//JSON对象转字符串
jsonObject.toString();
```

同样，`JSONUtil`还可以支持以下对象转为JSONObject对象：
- String对象
- Java Bean对象
- Map对象
- XML字符串（使用`JSONUtil.parseFromXml`方法）
- ResourceBundle(使用`JSONUtil.parseFromResourceBundle`)

`JSONUtil`还提供了JSONObject对象转换为其它对象的方法：
- toJsonStr 转换为JSON字符串
- toXmlStr 转换为XML字符串
- toBean 转换为JavaBean
- 

