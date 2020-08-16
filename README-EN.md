<p align="center">
	<a href="https://hutool.cn/"><img src="https://cdn.jsdelivr.net/gh/looly/hutool-site/images/logo.jpg" width="45%"></a>
</p>
<p align="center">
	<strong>A set of tools that keep Java sweet.</strong>
</p>
<p align="center">
	<a href="https://hutool.cn">https://hutool.cn/</a>
</p>

<p align="center">
	<a target="_blank" href="https://search.maven.org/artifact/cn.hutool/hutool-all">
		<img src="https://img.shields.io/maven-central/v/cn.hutool/hutool-all.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://license.coscl.org.cn/MulanPSL2/">
		<img src="https://img.shields.io/:license-MulanPSL2-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://travis-ci.org/looly/hutool">
		<img src="https://travis-ci.org/looly/hutool.svg?branch=v4-master" />
	</a>
	<a href="https://www.codacy.com/app/looly/hutool?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=looly/hutool&amp;utm_campaign=Badge_Grade">
		<img src="https://api.codacy.com/project/badge/Grade/3e1b8a70248c46579b7b0d01d60c6377"/>
	</a>
	<a href="https://codecov.io/gh/looly/hutool">
		<img src="https://codecov.io/gh/looly/hutool/branch/v4-master/graph/badge.svg" />
	</a>
	<a target="_blank" href="https://gitter.im/hutool/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
		<img src="https://badges.gitter.im/hutool/Lobby.svg" />
	</a>
	<a target="_blank" href='https://gitee.com/loolly/hutool/stargazers'>
		<img src='https://gitee.com/loolly/hutool/badge/star.svg?theme=gvp' alt='star'/>
	</a>
	<a target="_blank" href='https://github.com/looly/hutool'>
		<img src="https://img.shields.io/github/stars/looly/hutool.svg?style=social" alt="github star"/>
	</a>
	<a target="_blank" href='https://app.netlify.com/sites/hutool/deploys'>
		<img src="https://api.netlify.com/api/v1/badges/7e0824f9-5f9a-4df0-89dd-b2fccfbeccb1/deploy-status" alt="netlify"/>
	</a>
</p>

<br/>
<p align="center">
	<a href="https://qm.qq.com/cgi-bin/qm/qr?k=oolXnM7i9pHbiyUOfCdQWSA3RuIXqUsk&jump_from=webapi"><img src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A3-718802356-orange"/></a>
	<a href="https://qm.qq.com/cgi-bin/qm/qr?k=0wwldaU0E8r-ZzHl_wma33W7420zwXYi&jump_from=webapi"><img src="https://img.shields.io/badge/QQ%E7%BE%A4%E2%91%A4-956375658-orange"/></a>
</p>
-------------------------------------------------------------------------------

## Introduction
**Hutool** is a small but comprehensive library of Java tools, encapsulation by static methods, reduce the cost of learning related APIs, increase productivity, and make Java as elegant as a functional programming language,let the Java be "sweet" too.

**Hutool** tools and methods from each user's crafted, it covers all aspects of the underlying code of Java development, it is a powerful tool for large project development to solve small problems, but also the efficiency of small projects;

**Hutool** is a project "util" package friendly alternative, it saves developers on the project of common classes and common tool methods of encapsulation time, so that development focus on business, at the same time can minimize the encapsulation is not perfect to avoid the bugs.

### Origin of the 'Hutool' name

**Hutool = Hu + tool**，Is the original company project after the stripping of the underlying code of the open source library , "Hu" is the short name of the company , 'tool' that tool .

Hutool,' Hútú '(Chinese Pinyin)，On the one hand, it is simple and easy to understand, on the other hand, it means "hard to be confused".(note: confused means 'Hútú (糊涂)' in china )

### How Hutool is changing the way we coding

The goal of  **Hutool**  is to use a simple function instead of a complex piece of code, thus avoiding the problem of "copy and paste" code as much as possible and revolutionizing the way we write code.

To calculate MD5 for example:

- 【Before】Open a search engine -> search "Java MD5 encryption" -> open a blog -> copy and paste -> change it to work.
- 【Now】import Hutool -> SecureUtil.md5()

Hutool exists to reduce code search costs and avoid bugs caused by imperfect code on the web.

### Thanks
> this README is PR by [chengxian-yi](https://gitee.com/yichengxian)
-------------------------------------------------------------------------------

## Module
A Java-based tool class for files, streams, encryption and decryption, transcoding, regular, thread, XML and other JDK methods for encapsulation，composing various Util tool classes, as well as providing the following modules：

| module          |     description                                                              |
| -------------------|---------------------------------------------------------------------------------- |
| hutool-aop         | JDK dynamic proxy encapsulation to provide non-IOC faceting support |
| hutool-bloomFilter | Bloom filtering to provide some Hash algorithm Bloom filtering |
| hutool-cache       |     Simple cache                                                     |
| hutool-core        | Core, including Bean operations, dates, various Utils, etc. |
| hutool-cron        |     Task scheduling with Cron expressions     |
| hutool-crypto      | Provides symmetric, asymmetric and digest algorithm encapsulation |
| hutool-db          | Db operations based on ActiveRecord thinking. |
| hutool-dfa         |     DFA models, such as multi-keyword lookups                |
| hutool-extra       | Extension modules, third-party wrappers (template engine, mail, servlet, QR code, Emoji, FTP, word splitting, etc.) |
| hutool-http        |     Http client                                 |
| hutool-log         |     Log (facade)                                           |
| hutool-script      |     Script execution encapsulation, e.g. Javascript      |
| hutool-setting     | Stronger Setting Profile tools and Properties tools          |
| hutool-system      | System parameter tools (JVM information, etc.)               |
| hutool-json        |     JSON                                                                      |
| hutool-captcha     |     Image Captcha                                                      |
| hutool-poi         |     Tools for working with Excel and Word in POI           |
| hutool-socket      |     Java-based tool classes for NIO and AIO sockets    |

Each module can be introduced individually, or all modules can be introduced by introducing `hutool-all` as required.

-------------------------------------------------------------------------------

## Doc

[Chinese documentation](https://www.hutool.cn/docs/)

[API](https://apidoc.gitee.com/loolly/hutool/)

[Video](https://www.bilibili.com/video/BV1bQ4y1M7d9?p=2)

-------------------------------------------------------------------------------

## Install

### Maven
```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.4.1</version>
</dependency>
```

### Gradle
```
compile 'cn.hutool:hutool-all:5.4.1'
```

## Download

- [Maven1](https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.4.1/)
- [Maven2](http://repo2.maven.org/maven2/cn/hutool/hutool-all/5.4.1/)

> note:
> Hutool 5.x supports JDK8+ and is not tested on Android platforms, and cannot guarantee that all tool classes or tool methods are available.
> If your project uses JDK7, please use Hutool 4.x version.

### Compile and install

Download the entire project source code

gitee：[https://gitee.com/loolly/hutool](https://gitee.com/loolly/hutool) 

github:https://github.com/looly/hutool

```sh
cd ${hutool}
./hutool.sh install
```

-------------------------------------------------------------------------------

## Other

### Branch Description

Hutool's source code is divided into two branches:

| branch | description                                               |
|-----------|---------------------------------------------------------------|
| v5-master | The master branch, the branch used by the release version, is the same as the jar committed to the central repository and does not receive any pr or modifications. |
| v5-dev    | Development branch, which defaults to the next SNAPSHOT version, accepts modifications or pr |

### Provide feedback or suggestions on bugs

When submitting feedback, please indicate which JDK version, Hutool version, and related dependency library version you are using.

- [Gitee issue](https://gitee.com/loolly/hutool/issues)
- [Github issue](https://github.com/looly/hutool/issues)

### Principles of PR(pull request)

Hutool welcomes anyone to contribute code to Hutool, but the author suffers from OCD and needs to submit a pr (pull request) that meets some specifications in order to care for the patient.：

1. Improve the comments, especially each new method should follow the Java documentation specification to indicate the method description, parameter description, return value description and other information, if necessary, please add unit tests, if you want, you can also add your name.
2. Code indentation according to Eclipse.
3. Newly added methods do not use third-party library methods，Unless the method tool is added to the '**extra module**'.
4. Please pull request to the `v5-dev` branch. Hutool uses a new branch after 5.x: `v5-master` is the master branch, which indicates the version of the central library that has been released, and this branch does not allow pr or modifications.

-------------------------------------------------------------------------------

## Donate

If you think Hutool is good, you can donate to buy tshe author a pack of chili~, thanks in advance ^_^.

[gitee donate](https://gitee.com/loolly/hutool)