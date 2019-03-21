Http响应封装-HttpResponse
===

## 介绍
HttpResponse是HttpRequest执行execute()方法后返回的一个对象，我们可以通过此对象获取服务端返回的：

- Http状态码（getStatus方法）
- 返回内容编码（contentEncoding方法）
- 是否Gzip内容（isGzip方法）
- 返回内容（body、bodyBytes、bodyStream方法）
- 响应头信息（header方法）

## 使用

此对象的使用非常简单，最常用的便是body方法，会返回字符串Http响应内容。如果想获取byte[]则调用bodyBytes即可。

### 获取响应状态码

```java
HttpResoonse res = HttpRequest.post(url)..execute();
Console.log(res.getStatus());
```

### 获取响应头信息

```java
HttpResoonse res = HttpRequest.post(url)..execute();
//预定义的头信息
Console.log(res.header(Header.CONTENT_ENCODING));
//自定义头信息
Console.log(res.header("Content-Disposition"));
```

