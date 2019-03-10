## 由来
在日常开发中，网络连接这块儿必不可少。日常用到的一些功能,隐藏掉部分IP地址、绝对相对路径的转换等等。

## 介绍

`NetUtil` 工具中主要的方法包括：
1. `longToIpv4` 根据long值获取ip v4地址
2. `ipv4ToLong` 根据ip地址计算出long型的数据
3. `isUsableLocalPort` 检测本地端口可用性
4. `isValidPort` 是否为有效的端口
5. `isInnerIP` 判定是否为内网IP
6. `localIpv4s` 获得本机的IP地址列表
7. `toAbsoluteUrl` 相对URL转换为绝对URL
8. `hideIpPart` 隐藏掉IP地址的最后一部分为 * 代替
9. `buildInetSocketAddress` 构建InetSocketAddress
10. `getIpByHost` 通过域名得到IP
11. `isInner` 指定IP的long是否在指定范围内

## 使用

```java
String ip= "127.0.0.1";
long iplong = 2130706433L;

//根据long值获取ip v4地址
String ip= NetUtil.longToIpv4(iplong);


//根据ip地址计算出long型的数据
long ip= NetUtil.ipv4ToLong(ip);

//检测本地端口可用性
boolean result= NetUtil.isUsableLocalPort(6379);

//是否为有效的端口
boolean result= NetUtil.isValidPort(6379);

//隐藏掉IP地址
 String result =NetUtil.hideIpPart(ip);
```

更多方法请见：

[API文档-NetUtil](https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/util/NetUtil.html)