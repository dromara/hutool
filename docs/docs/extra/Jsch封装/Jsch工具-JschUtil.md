Jsch(SSH)工具-JschUtil
===

## 由来

此工具最早来自于我的早期项目：Common-tools，当时是为了解决在存在堡垒机（跳板机）环境时无法穿透堡垒机访问内部主机端口问题，于是辗转找到了[jsch](http://www.jcraft.com/jsch/)库。为了更加便捷的、且容易理解的方式使用此库，因此有了`JschUtil`。

## 使用

### 引入jsch

```xml
<dependency>
	<groupId>com.jcraft</groupId>
	<artifactId>jsch</artifactId>
	<version>0.1.54</version>
</dependency>
```

> 说明
> 截止本文档撰写完毕，jsch的最新版为`0.1.54`，理论上应引入的版本应大于或等于此版本。

### 使用

### ssh连接到远程主机

```java
//新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
Session session = JschUtil.getSession("10.1.1.1", 22, "test", "123456");
```

### 端口映射
```java
//新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
Session session = JschUtil.getSession("10.1.1.1", 22, "test", "123456");

// 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
JschUtil.bindPort(session, "172.20.12.123", 8080, 8080);
```

### 其它方法

- `generateLocalPort` 生成一个本地端口（从10001开始尝试，找到一个未被使用的本地端口）
- `unBindPort` 解绑端口映射
- `openAndBindPortToLocal` 快捷方法，将连接到跳板机和绑定远程主机端口到本地使用一个方法搞定
- `close` 关闭SSH会话


