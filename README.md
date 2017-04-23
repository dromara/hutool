![封面](https://raw.githubusercontent.com/looly/hutool/master/docs/resources/hutool.jpg)

[![Version](https://img.shields.io/badge/version-3.0.4-brightgreen.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool%22)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

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

### 1. 方法优先于对象
在工具类中，往往以静态方法为主。方法集中在一个类中，配合IDE查找使用起来是十分便利的。于是Hutool将JDK中许多的类总结抽象为一个方法，这一原则使用最多的就是流的相关方法，这些方法很好的隐藏了XXXInputStream、XXXReader等的复杂性。

### 2. 自动识别优于用户定义
其实很多时候，有些参数、设置等是没有必要我们自己传入的，完全可以靠逻辑判断自动完成。一个方法很多时候明明只需要传3个参数，我们非要传4个，这多出的一个参数本身就是代码的一种冗余。

这一原则在Hutool的各个角落都有所体现，尤为明显的比如log模块。构建日志对象的时候，很明显类名可以动态获取，何必让使用者再传入呢？再比如在db模块的数据库配置中，数据库驱动命名完全可以根据连接字符串判断出来，何必要让用户传入？这些问题的在Hutool中都有非常好的封装，而这一原则也渐渐变成Hutool哲学的一部分。

### 3. 便捷性与灵活性并存
所谓便捷性，就是我们在调用一个方法的时候参数要尽量少，只传必要参数即可，非必要参数使用默认值即可（想想一个方法一堆参数的时候，调用者晕头转向不知所云）。

所谓灵活性正好与便捷性相反，要让一个方法的参数尽量多，为用户灵活的操作方法提供最大可能性。

这两个原则看似矛盾，其实只是针对不同场景设定的而已，缺一不可。便捷性强调拿来即用，为快速开发提供可能；灵活性强调最大限度调优，为性能调优和扩展提供便利。

这一原则在针对编码问题上体现尤为突出，我们的大部分方法都是默认“UTF-8”编码的，这也是我们推荐的编码方式，推荐大部分项目使用的编码。但是一旦有遗留项目使用了类似“GBK”等编码，没关系，我们提供在相关方法中提供Charset对象参数，可以自定义编码。这样使用这一原则就兼顾了各种项目的情况。

### 4. 适配与兼容
在Hutool中，适配器模式运用特别广泛，log模块适配主流各大框架，db模块适配主流各种连接池和关系数据库。这种适配一是提高灵活性，二是可以很好的兼容各大框架，让Hutool可以在各种复杂项目环境中生存的很好。

适配兼容产生的另一个原则是：**你有我配，你无我有**。说白了就是：如果你项目中有这个框架，我可以完美适配，如果你没有引入任何框架，Hutool自身实现了一些逻辑可以很好的工作。

### 5. 可选依赖原则
在Java项目中依赖常常是个头疼的问题，不同的框架强依赖另一些框架或包，虽然Maven可以很好的处理冲突问题，但是项目底下满满的依赖jar包，是不是无形中拖慢了项目，也增加了复杂性和不确定性？而很多时候，我们是不是只是为了用一个小小的方法，就要引入一个第三方包，谁喜欢这样臃肿的项目？

Hutool中也会有一些依赖，但是全部都是**optional**的，在使用中不会关联依赖，而这些依赖只有在使用者使用到时才会调用，这时可能会报ClassNotFoundException，不用担心，我们自己引入即可。为什么要这样做呢？以VelocityUtil这个工具类为例，使用Velocity的人占比极少，我们不能为了这些用户而强引入Velocity包，而使用这个工具类的人应该明白，我们应该自己引入这个包。

而更多时候，我们需要用到某个方法时，我的做法是将方法拷贝到项目中（Hutool中的方法正在不断积累），类似于Apache Commons中的方法，Hutool中基本都有取代方法，完全不必要引入。

可选依赖原则让我们的项目更加精简，问题也更容易排查。

### 6. 无侵入原则
Hutool始终是一个工具类而不是框架，这意味着它对项目的侵入几乎为零，每个方法都是可被代替的，甚至整个Hutool也是可被替换的。这种无侵入性，让使用者可以更加放心的在项目中引入，也保证了与其它框架完美的兼容。

## 安装
### Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>hutool-all</artifactId>
    <version>3.0.3</version>
</dependency>
```

注：工具包的**版本**可以通过 [http://search.maven.org/](http://search.maven.org/) 搜索`hutool-all`找到项目。

### 非Maven项目
可以从[http://search.maven.org/](http://search.maven.org/) 搜索`hutool-all`找到项目，点击对应版本，下面是相应的Jar包，导入即可使用。

[http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool-all%22](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.xiaoleilu%22%20AND%20a%3A%22hutool-all%22)

点击链接后点选择对应版本，点击列表尾部的“Download”下载jar、API文档、源码

## 文档请移步 

[Hutool Wiki@OSC](http://hutool.mydoc.io/)

## 版本变更

[CHANGELOG.md](https://github.com/looly/hutool/blob/master/CHANGELOG.md)
