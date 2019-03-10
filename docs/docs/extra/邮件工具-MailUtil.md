邮件工具-MailUtil
===

## 概述
在Java中发送邮件主要品依靠javax.mail包，但是由于使用比较繁琐，因此Hutool针对其做了封装。由于依赖第三方包，因此将此工具类归类到extra模块中。

## 使用

### 引入依赖
Hutool对所有第三方都是可选依赖，因此在使用MailUtil时需要自行引入第三方依赖。

```
<dependency>
	<groupId>javax.mail</groupId>
	<artifactId>mail</artifactId>
	<version>1.4.7</version>
</dependency>
```


### 邮件服务器配置

在classpath（在标准Maven项目中为`src/main/resources`）的config目录下新建`mail.setting`文件，最小配置内容如下，在此配置下，smtp服务器和用户名都将通过`from`参数识别：

```properties
# 发件人（必须正确，否则发送失败）
from = hutool@yeah.net
# 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
pass = q1w2e3
```

有时候一些非标准邮箱服务器（例如企业邮箱服务器）的smtp地址等信息并不与发件人后缀一致，端口也可能不同，此时Hutool可以提供完整的配置文件：

完整配置

```properties
# 邮件服务器的SMTP地址，可选，默认为smtp.<发件人邮箱后缀>
host = smtp.yeah.net
# 邮件服务器的SMTP端口，可选，默认25
port = 25
# 发件人（必须正确，否则发送失败）
from = hutool@yeah.net
# 用户名，默认为发件人邮箱前缀
user = hutool
# 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
pass = q1w2e3
```

> 注意
> 邮件服务器必须支持并打开SMTP协议，详细请查看相关帮助说明
> 配置文件的样例中提供的是我专门为测试邮件功能注册的yeah.net邮箱，帐号密码公开，供Hutool用户测试使用。

## 发送邮件

1. 发送普通文本邮件，最后一个参数可选是否添加多个附件：

```java
MailUtil.send("hutool@foxmail.com", "测试", "邮件来自Hutool测试", false);
```

2. 发送HTML格式的邮件并附带附件，最后一个参数可选是否添加多个附件：

```
MailUtil.send("hutool@foxmail.com", "测试", "<h1>邮件来自Hutool测试</h1>", true, FileUtil.file("d:/aaa.xml"));
```

3. 群发邮件，可选HTML或普通文本，可选多个附件：

```java
ArrayList<String> tos = CollUtil.newArrayList(
	"person1@bbb.com", 
	"person2@bbb.com", 
	"person3@bbb.com", 
	"person4@bbb.com");

MailUtil.send(tos, "测试", "邮件来自Hutool群发测试", false);
```

发送邮件非常简单，只需一个方法即可搞定其中按照参数顺序说明如下：
1. tos:     对方的邮箱地址，可以是单个，也可以是多个（Collection表示）
2. subject：标题
3. content：邮件正文，可以是文本，也可以是HTML内容
4. isHtml： 是否为HTML，如果是，那参数3识别为HTML内容
5. files：  可选：附件，可以为多个或没有，将File对象加在最后一个可变参数中即可

### 其它

1. 自定义邮件服务器

除了使用配置文件定义全局账号以外，`MailUtil.send`方法同时提供重载方法可以传入一个`MailAccount`对象，这个对象为一个普通Bean，记录了邮件服务器信息。

```java
MailAccount account = new MailAccount();
account.setHost("smtp.yeah.net");
account.setPort("25");
account.setAuth(true);
account.setFrom("hutool@yeah.net");
account.setUser("hutool");
account.setPass("q1w2e3");

MailUtil.send(account, CollUtil.newArrayList("hutool@foxmail.com"), "测试", "邮件来自Hutool测试", false);
```

2. 使用SSL加密方式发送邮件
在使用QQ或Gmail邮箱时，需要强制开启SSL支持，此时我们只需修改配置文件即可：

```properties
# 发件人（必须正确，否则发送失败），“小磊”可以任意变更，<>内的地址必须唯一，以下方式也对
# from = hutool@yeah.net
from = 小磊<hutool@yeah.net>
# 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
pass = q1w2e3
# 使用SSL安全连接
sslEnable = true
```
在原先极简配置下只需加入`sslEnable`即可完成SSL连接，当然，这是最简单的配置，很多参数根据已有参数已设置为默认。

完整的配置文件如下：

```properties
# 邮件服务器的SMTP地址
host = smtp.yeah.net
# 邮件服务器的SMTP端口
port = 465
# 发件人（必须正确，否则发送失败）
from = hutool@yeah.net
# 用户名（注意：如果使用foxmail邮箱，此处user为qq号）
user = hutool
# 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
pass = q1w2e3
#使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。
startttlsEnable = true

# 使用SSL安全连接
sslEnable = true
# 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
socketFactoryClass = javax.net.ssl.SSLSocketFactory
# 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
socketFactoryFallback = true
# 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口456
socketFactoryPort = 465

# SMTP超时时长，单位毫秒，缺省值不超时
timeout = 0
# Socket连接超时值，单位毫秒，缺省值不超时
connectionTimeout = 0
```

3、针对QQ邮箱和Foxmail邮箱的说明

(1) QQ邮箱中SMTP密码是单独生成的授权码，而非你的QQ密码，至于怎么生成，见腾讯的帮助说明：[http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256](http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256)

使用帮助引导生成授权码后，配置如下即可：

```
pass = 你生成的授权码
```

(2) Foxmail邮箱本质上也是QQ邮箱的一种别名，你可以在你的QQ邮箱中设置一个foxmail邮箱，不过配置上有所区别。在Hutool中`user`属性默认提取你邮箱@前面的部分，但是foxmail邮箱是无效的，需要单独配置为与之绑定的qq号码或者`XXXX@qq.com`的`XXXX`。即：

```
host = smtp.qq.com
from = XXXX@foxmail.com
user = foxmail邮箱对应的QQ号码或者qq邮箱@前面部分
...
```

