hutool
======

[![Version](https://img.shields.io/badge/version-3.0.1-brightgreen.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool%22)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

<center>![封面](https://raw.githubusercontent.com/looly/hutool/master/docs/resources/hutool.jpg)</center>

## 全新项目主页：[http://www.hutool.cn/](http://www.hutool.cn/)

## Hutool交流QQ群：== [537116831](http://shang.qq.com/wpa/qunwpa?idkey=382bb37ce779c11da77577f69d92d5171b340e3e7343d5ae0521f237c82c7810) ==

## 功能
一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：
* 布隆过滤
* 缓存
* 克隆接口
* 类型转换
* 日期处理
* 数据库ORM（基于ActiveRecord思想）
* 基于DFA有限自动机的多个关键字查找
* HTTP客户端
* IO和文件
* 有用的一些数据结构
* 日志
* 反射代理类的简化（AOP切面实现）
* Setting（一种扩展Properties的配置文件）
* System（JVM和系统信息等）
* WatchService的封装（文件变动监控）
* XXXUtil各种有用的工具类

## 简介
[Hutool](https://github.com/looly/hutool)是一个工具包，我日常写项目的一些积累，希望你看了之后会有所启发或者能给你工作中带来帮助。这个工具包叫做Hutool也有“糊涂”之意，意为很多时候我们并不需要关注细节，专注业务。

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

[http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool%22](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool%22)

点击链接后点选择对应版本，点击列表尾部的“Download”下载jar、API文档、源码

## 文档请移步 

[Hutool Wiki @ osc](http://hutool.mydoc.io/)

## 版本变更

[CHANGELOG.md](https://github.com/looly/hutool/blob/master/CHANGELOG.md)
