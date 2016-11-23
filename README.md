hutool
======

[![Version](https://img.shields.io/badge/version-2.16.0-brightgreen.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool%22)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

<center>![封面](http://looly-blog.qiniudn.com/hutool.jpg)</center>

## Hutool交流QQ群：** 537116831 **

## 功能
一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：
1. 布隆过滤
2. 缓存
3. 数据库ORM（基于ActiveRecord思想）
4. HTTP客户端
5. IO
6. JSON
7. 日志
8. System（JVM和系统信息等）
9. Setting（一种扩展Properties的配置文件）

## 简介
[Hutool](https://github.com/looly/hutool)是一个工具包，我日常写项目的一些积累，参考了一些[Apache Commons Lang](http://commons.apache.org/)和[JODD](http://jodd.org/)里的一些写法，不过大部分还是自己写的，希望你看了之后会有所启发或者能给你工作中带来帮助。说实话我现在写代码已经离不开自己这个工具包了，叫做Hutool也有“糊涂”之意，表示很多功能糊里糊涂就实现了。好吧，言归正传，说说里面一些好玩的方法（工具包中大部分是一些静态方法）。

## 设计哲学
[Hutool](https://github.com/looly/hutool)的设计思想是尽量减少重复的定义，让项目中的`util`这个package尽量少，总的来说有如下的几个思想：

1. 减少代码录入。
2. 常用功能组合起来，实现一个功能只用一个方法。
3. 简化Java API，原来需要几个类实现的功能我也只是用一个类甚至一个方法（想想为了个线程池我得new多少类……而且名字还不好记）
4. 对于null的处理我没有可以回避，而是采取“你给我null我也给你返回null”这种思想，尽量不在工具类里抛空指针异常（这思想稍猥琐啊……直接把包袱扔给调用者了，好吧，谁让你给我null了）。
5. 一些固定使用的算法收集到一起，不用每次问度娘了（例如Base64算法、MD5、Sha-1，还有Hash算法）
6. 借鉴[Python](https://www.python.org/)的很多小技巧（例如列表切片，列表支持负数index），让Java更加好用。
7. 非常好用的ORM框架，同样借鉴[Python](https://www.python.org/)的[Django](https://www.djangoproject.com/)框架，以键值对的实体代替对象实体，大大降低数据库访问的难度（再也不用像Hibernate一样配置半天ORM Mapping了）。
8. 极大简化了文件、日期的操作，尤其是相对路径和绝对路径问题做了非常好的封装，降低学习成本。

## 安装
### Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>hutool</artifactId>
    <version>X.X.X</version>
</dependency>
```

注：工具包的**版本**可以通过 [http://search.maven.org/](http://search.maven.org/) 搜索`hutool`找到项目。

### 非Maven项目
可以从[http://search.maven.org/](http://search.maven.org/) 搜索`hutool`找到项目，点击对应版本，下面是相应的Jar包，导入即可使用。

最新的2.14.0版本Jar下载地址：[http://search.maven.org/remotecontent?filepath=com/xiaoleilu/hutool/2.14.0/hutool-2.14.0.jar](http://search.maven.org/remotecontent?filepath=com/xiaoleilu/hutool/2.14.0/hutool-2.14.0.jar)

Java doc下载地址：[http://search.maven.org/remotecontent?filepath=com/xiaoleilu/hutool/2.14.0/hutool-2.14.0-javadoc.jar](http://search.maven.org/remotecontent?filepath=com/xiaoleilu/hutool/2.14.0/hutool-2.14.0-javadoc.jar)

## 文档请移步 

[Hutool Wiki @ osc](http://hutool.mydoc.io/)

[Hutool Wiki @ github](https://github.com/looly/hutool/wiki)
