<p align="center">
	<a href="http://hutool.cn/"><img src="http://www.hutool.cn/images/logo.jpg" width="400"></a>
</p>
<p align="center">
	<strong>A set of tools that keep Java sweet.</strong>
</p>
<p align="center">
	<a target="_blank" href="https://search.maven.org/artifact/cn.hutool/hutool-all/4.1.20/jar">
		<img src="https://img.shields.io/badge/version-4.1.20-blue.svg" ></img>
	</a>
	<a target="_blank" href="http://www.apache.org/licenses/LICENSE-2.0.html">
		<img src="http://img.shields.io/:license-apache-blue.svg" ></img>
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-1.7+-green.svg" ></img>
	</a>
	<a target="_blank" href="https://travis-ci.org/looly/hutool">
		<img src="https://travis-ci.org/looly/hutool.svg?branch=v4-master" ></img>
	</a>
	<a target="_blank" href="https://gitter.im/hutool/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
		<img src="https://badges.gitter.im/hutool/Lobby.svg" ></img>
	</a>
	<a target="_blank" href='https://gitee.com/loolly/hutool/stargazers'>
		<img src='https://gitee.com/loolly/hutool/badge/star.svg?theme=dark' alt='star'></img>
	</a>
</p>
<p align="center">
	-- 主页：<a href="http://hutool.cn">http://hutool.cn/</a> --
</p>
<p align="center">
	-- QQ群②：<a href="http://shang.qq.com/wpa/qunwpa?idkey=717c8e1444c2f3538ea37d792a19a23837cfc201e0d0de783e353b87009252eb">871141901</a> --
</p>

-------------------------------------------------------------------------------

## 简介

Hutool是一个Java工具包，也只是一个工具包，它帮助我们简化每一行代码，减少每一个方法，让Java语言也可以“甜甜的”。Hutool最初是我项目中“util”包的一个整理，后来慢慢积累并加入更多非业务相关功能，并广泛学习其它开源项目精髓，经过自己整理修改，最终形成丰富的开源工具集。

Hutool是Hu + tool的自造词，谐音“糊涂”，寓意追求“万事都作糊涂观，无所谓失，无所谓得”的境界。

-------------------------------------------------------------------------------

## 包含组件
一个Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装，组成各种Util工具类，同时提供以下组件：

- hutool-aop              JDK动态代理封装，提供非IOC下的切面支持
- hutool-bloomFilter   布隆过滤，提供一些Hash算法的布隆过滤
- hutool-cache           缓存
- hutool-core             核心，包括Bean操作、日期、各种Util等
- hutool-cron             定时任务模块，提供类Crontab表达式的定时任务
- hutool-crypto          加密解密模块
- hutool-db               JDBC封装后的数据操作，基于ActiveRecord思想
- hutool-dfa              基于DFA模型的多关键字查找
- hutool-extra            扩展模块，对第三方封装（模板引擎、邮件、Servlet、二维码等）
- hutool-http             基于HttpUrlConnection的Http客户端封装
- hutool-log              自动识别日志实现的日志门面
- hutool-script           脚本执行封装，例如Javascript
- hutool-setting         功能更强大的Setting配置文件和Properties封装
- hutool-system        系统参数调用封装（JVM信息等）
- hutool-json            JSON实现
- hutool-captcha      图片验证码实现
- hutool-poi             针对POI中Excel的封装

-------------------------------------------------------------------------------

## 文档 

[中文文档](http://hutool.mydoc.io/)

[参考API](https://apidoc.gitee.com/loolly/hutool/)

-------------------------------------------------------------------------------

## 安装

### Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>4.1.20</version>
</dependency>
```

### Gradle
```
compile 'cn.hutool:hutool-all:4.1.20'
```

### 非Maven项目

点击以下任一链接，下载`hutool-all-X.X.X.jar`即可：

- [Maven中央库1](http://repo1.maven.org/maven2/cn/hutool/hutool-all/4.1.20/)
- [Maven中央库2](http://repo2.maven.org/maven2/cn/hutool/hutool-all/4.1.20/)

> 注意
> Hutool只支持JDK7+，对应Android平台没有测试，部分方法并不支持。

### 编译安装

访问Hutool的码云主页：[https://gitee.com/loolly/hutool](https://gitee.com/loolly/hutool) 下载整个项目源码（v4-master或v4-dev分支都可）然后进入Hutool项目目录执行：

```sh
bin/hutool.sh install
```

-------------------------------------------------------------------------------

## 添砖加瓦

### 提供bug反馈或建议

- [码云Gitee](https://gitee.com/loolly/hutool/issues)
- [Github](https://github.com/looly/hutool/issues)

### 遵照的原则

Hutool欢迎任何人为Hutool添砖加瓦，贡献代码，不过作者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

1. 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，如果愿意，也可以加上你的大名。
2. Hutool的缩进按照Eclipse（不要跟我说IDEA多好用，作者非常懒，学不会）默认（tab）缩进，所以请遵守（不要和我争执空格与tab的问题，这是一个病人的习惯）。
3. 新加的方法不要使用第三方库的方法，Hutool遵循无依赖原则（除非在extra模块中加方法工具）。
4. 请pull request到`v4-dev`分支。Hutool在4.x版本后使用了新的分支：`v4-master`是主分支，表示已经发布中央库的版本，这个分支不允许pr，也不允许修改。`v4-dev`分支是开发分支，Hutool的下个版本或者SNAPSHOT版本在这个分支上开发，你可以pr到这个分支。

### 贡献代码的步骤

1. 在Gitee或者Github上fork项目到自己的repo
2. 把fork过去的项目也就是你的项目clone到你的本地
3. 修改代码（记得一定要修改v4-dev分支）
4. commit后push到自己的库（v4-dev分支）
5. 登录Gitee或Github在你首页可以看到一个 pull request 按钮，点击它，填写一些说明信息，然后提交即可。
6. 等待作者合并

-------------------------------------------------------------------------------

## 捐赠

如果你觉得Hutool不错，可以捐赠请作者吃包辣条~，在此表示感谢^_^。

点击以下链接，将页面拉到最下方点击“捐赠”即可。

[前往捐赠](https://gitee.com/loolly/hutool)