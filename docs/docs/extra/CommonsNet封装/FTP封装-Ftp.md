FTP封装-Ftp
===

## 介绍

FTP客户端封装，此客户端基于[Apache Commons Net](http://commons.apache.org/proper/commons-net/)。

## 使用

### 引入依赖

```
<dependency>
	<groupId>commons-net</groupId>
	<artifactId>commons-net</artifactId>
	<version>3.6</version>
</dependency>
```

### 使用

```java
//匿名登录（无需帐号密码的FTP服务器）
Ftp ftp = new Ftp("172.0.0.1");
//进入远程目录
ftp.cd("/opt/upload");
//上传本地文件
ftp.upload("/opt/upload", FileUtil.file("e:/test.jpg"));
//下载远程文件
ftp.download("/opt/upload", "test.jpg", FileUtil.file("e:/test2.jpg"));

//关闭连接
ftp.close();
```

