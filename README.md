<p align="center">
	<a href="https://hutool.cn/"><img alt="" src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

<p align="center">
	<a target="_blank" href="https://search.maven.org/artifact/org.dromara.hutool/hutool-all">
		<img alt="" src="https://img.shields.io/maven-central/v/org.dromara.hutool/hutool-all.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://license.coscl.org.cn/MulanPSL2">
		<img alt="" src="https://img.shields.io/:license-MulanPSL2-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img alt="" src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://travis-ci.com/dromara/hutool">
		<img alt="" src="https://travis-ci.com/dromara/hutool.svg?branch=v5-master" />
	</a>
	<a href="https://www.codacy.com/gh/dromara/hutool/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dromara/hutool&amp;utm_campaign=Badge_Grade">
		<img alt="" src="https://app.codacy.com/project/badge/Grade/8a6897d9de7440dd9de8804c28d2871d"/>
	</a>
	<a href="https://codecov.io/gh/dromara/hutool">
		<img alt="" src="https://codecov.io/gh/dromara/hutool/branch/v6-master/graph/badge.svg" />
	</a>
	<a target="_blank" href="https://gitter.im/hutool/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
		<img alt="" src="https://badges.gitter.im/hutool/Lobby.svg" />
	</a>
	<a target="_blank" href='https://gitee.com/dromara/hutool/stargazers'>
		<img alt="star" src='https://gitee.com/dromara/hutool/badge/star.svg?theme=gvp'/>
	</a>
	<a target="_blank" href='https://github.com/dromara/hutool'>
		<img alt="github star" src="https://img.shields.io/github/stars/dromara/hutool.svg?style=social"/>
	</a>
</p>

<br/>
<p align="center">
	<a href="https://qm.qq.com/cgi-bin/qm/qr?k=QtsqXLkHpLjE99tkre19j6pjPMhSay1a&jump_from=webapi">
	<img alt="" src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A6-715292493-orange"/></a>
</p>

-------------------------------------------------------------------------------

<p align="center">
	<a href="#"><img style="width: 45%" alt="" src="https://plus.hutool.cn/images/zanzhu.jpg"/></a>
</p>

-------------------------------------------------------------------------------

[**🌎English Documentation**](README-EN.md)

-------------------------------------------------------------------------------

## 📚简介

`Hutool`是一个功能丰富且易用的**Java工具库**，通过诸多实用工具类的使用，旨在帮助开发者快速、便捷地完成各类开发任务。
这些封装的工具涵盖了字符串、数字、集合、编码、日期、文件、IO、加密、数据库JDBC、JSON、HTTP客户端等一系列操作，
可以满足各种不同的开发需求。

### 🎁Hutool往事

- 2012年，一个刚刚步入职场的少年剥离了业务系统中的公共部分，取名叫做`Common-Tools`。
- 2014年，他离开热爱的互联网和IT行业，将这份热爱倾注于开源，并将项目更名为`Hutool`，
`Hu`是怀念曾经公司中一起并肩作战的小伙伴们和那段美好回忆，人生难得糊涂，我们都在成长，但不变的是对Coding的热爱。
- 现在，“他”变成“他们”——一群热心的Committer。而`Hutool`,数十年如一日，已成为众多Java开发者互助的桥梁。
- 将来，Make Java Sweet Again!

### 🍺Hutool理念

`Hutool`既是一个工具集，也是一个知识库，我们从不自诩代码原创，大多数工具类都是**搬运**而来，因此：

- 你可以引入使用，也可以**拷贝**和修改使用，而**不必标注任何信息**，只是希望能把bug及时反馈回来。
- 我们努力健全**中文**注释，为源码学习者提供良好地学习环境，争取做到人人都能看得懂。

-------------------------------------------------------------------------------

## 🛠️包含组件

| 模块             | 介绍                                              |
|----------------|-------------------------------------------------|
| hutool-core    | 核心，包括Bean操作、日期、各种Util等                          |
| hutool-cron    | 定时任务模块，提供类Crontab表达式的定时任务                       |
| hutool-crypto  | 加密解密模块，提供对称、非对称和摘要算法封装                          |
| hutool-db      | JDBC封装后的数据操作，基于ActiveRecord思想                   |
| hutool-extra   | 扩展模块，对第三方封装（模板引擎、邮件、Servlet、二维码、Emoji、FTP、分词等）  |
| hutool-http    | 基于HttpUrlConnection、HttpClient、OkHttp的Http客户端封装 |
| hutool-log     | 功能强大的日志门面                                       |
| hutool-setting | 功能更强大的配置文件封装和工具                                 |
| hutool-json    | JSON实现                                          |
| hutool-poi     | POI中Excel和Word的封装以及OFD封装                        |
| hutool-socket  | 基于Java的NIO和AIO的Socket封装                         |
| hutool-swing   | Swing和JWT相关封装                                   |

可以根据需求对每个模块单独引入，也可以通过引入`hutool-all`方式引入所有模块。

-------------------------------------------------------------------------------

## 📝文档

[📘中文文档](https://doc.hutool.cn/pages/index/)

[📘中文备用文档](https://plus.hutool.cn/docs/#/)

[📙参考API](https://apidoc.gitee.com/dromara/hutool/)

-------------------------------------------------------------------------------

## 🪙支持Hutool

### 💳捐赠

如果你觉得Hutool不错，可以捐赠请维护者吃包辣条~，在此表示感谢^_^。

[Gitee上捐赠](https://gitee.com/dromara/hutool)

### 👕周边商店

你也可以通过购买Hutool的周边商品来支持Hutool维护哦！

我们提供了印有Hutool Logo的周边商品，欢迎点击购买支持：

👉 [Hutool 周边商店](https://market.m.taobao.com/apps/market/content/index.html?wh_weex=true&contentId=331724720170) 👈

-------------------------------------------------------------------------------

## 📦安装

### 🍊Maven

在项目的pom.xml的dependencies中加入以下内容:

```xml

<dependency>
	<groupId>org.dromara.hutool</groupId>
	<artifactId>hutool-all</artifactId>
	<version>6.0.0-M12</version>
</dependency>
```

### 🍐Gradle

```
implementation 'org.dromara.hutool:hutool-all:6.0.0-M12'
```

### 📥下载jar

点击以下链接，下载`hutool-all-X.X.X.jar`即可：

- [Maven中央库](https://repo1.maven.org/maven2/org/dromara/hutool/hutool-all/6.0.0-M12/)

> 🔔️注意
> Hutool 6.x支持JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。

### 🚽编译安装

访问Hutool的Gitee主页：[https://gitee.com/dromara/hutool](https://gitee.com/dromara/hutool)
下载整个项目源码（v6-master或v6-dev分支都可）然后进入Hutool项目目录执行：

```sh
./hutool.sh install
```

然后就可以使用Maven引入了。

-------------------------------------------------------------------------------

## 🏗️添砖加瓦

### 🎋分支说明

Hutool的源码分为两个分支，功能如下：

| 分支        | 作用                                         |
|-----------|--------------------------------------------|
| v6-master | 主分支，release版本使用的分支，与中央库提交的jar一致，不接收任何pr或修改 |
| v6-dev    | 开发分支，默认为下个版本的SNAPSHOT版本，接受修改或pr            |

### 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本呢、Hutool版本和相关依赖库版本。

- [Gitee issue](https://gitee.com/dromara/hutool/issues)
- [Github issue](https://github.com/dromara/hutool/issues)

### 🧬贡献代码的步骤

1. 在Gitee或者Github上fork项目到自己的repo
2. 把fork过去的项目也就是你的项目clone到你的本地
3. 修改代码（记得一定要修改v6-dev分支）
4. commit后push到自己的库（v6-dev分支）
5. 登录Gitee或Github在你首页可以看到一个 pull request 按钮，点击它，填写一些说明信息，然后提交即可。
6. 等待维护者合并

### 📐PR遵照的原则

Hutool欢迎任何人为Hutool添砖加瓦，贡献代码，不过维护者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

1. 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
2. Hutool的缩进按照Eclipse（~~不要跟我说IDEA多好用，维护者非常懒，学不会~~，IDEA真香，改了Eclipse快捷键后舒服多了）默认（tab）缩进，所以请遵守（不要和我争执空格与tab的问题，这是一个病人的习惯）。
3. 新加的方法不要使用第三方库的方法，Hutool遵循无依赖原则（除非在extra模块中加方法工具）。
4. 请pull request到`v6-dev`分支。Hutool在6.x版本后使用了新的分支：`v6-master`是主分支，表示已经发布中央库的版本，这个分支不允许pr，也不允许修改。

### 💞沟通说明

1. 提交地issue或PR未回复并开启状态表示还未处理，请耐心等待。
2. 为了保证新issue及时被发现和处理，我们会关闭一些描述不足的issue，此时你补充说明重新打开即可。
3. PR被关闭，表示被拒绝或需要修改地地方较多，重新提交即可。

-------------------------------------------------------------------------------

## ⭐Star Hutool

[![Stargazers over time](https://starchart.cc/dromara/hutool.svg)](https://starchart.cc/dromara/hutool)

### GitHub Contributor Over Time
[![Contributor Over Time](https://contributor-overtime-api.git-contributor.com/contributors-svg?chart=contributorOverTime&repo=dromara/hutool)](https://git-contributor.com?chart=contributorOverTime&repo=dromara/hutool)