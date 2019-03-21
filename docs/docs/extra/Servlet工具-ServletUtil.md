Servlet工具-ServletUtil
===

## 由来
最早Servlet相关的工具并不在Hutool的封装考虑范围内，但是后来很多人提出需要一个Servlet Cookie工具，于是我决定建立ServletUtil，这样工具的使用范围就不仅限于Cookie，还包括参数等等。

其实最早的Servlet封装来自于作者的一个MVC框架：[Hulu](https://gitee.com/loolly/hulu)，这个MVC框架对Servlet做了一层封装，使请求处理更加便捷。于是Hutool将Hulu中Request类和Response类中的方法封装于此。

## 使用

### 加入依赖
```
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.1.0</version>
	<!-- 此包一般在Servlet容器中都有提供 -->
	<scope>provided</scope>
</dependency>
```

### 方法

- `getParamMap` 获得所有请求参数
- `fillBean` 将请求参数转为Bean
- `getClientIP` 获取客户端IP，支持从Nginx头部信息获取，也可以自定义头部信息获取位置
- `getHeader`、`getHeaderIgnoreCase` 获得请求header中的信息
- `isIE` 客户浏览器是否为IE
- `isMultipart` 是否为Multipart类型表单，此类型表单用于文件上传
- `getCookie` 获得指定的Cookie
- `readCookieMap` 将cookie封装到Map里面
- `addCookie` 设定返回给客户端的Cookie
- `write` 返回数据给客户端
- `setHeader` 设置响应的Header

