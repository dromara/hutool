## 🏗️添砖加瓦

### 🎋分支说明

Hutool的源码分为两个分支，功能如下：

| 分支        | 作用                                         |
|-----------|--------------------------------------------|
| v5-master | 主分支，release版本使用的分支，与中央库提交的jar一致，不接收任何pr或修改 |
| v5-dev    | 开发分支，默认为下个版本的SNAPSHOT版本，接受修改或pr            |

### 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本呢、Hutool版本和相关依赖库版本。

- [Gitee   issue](https://gitee.com/chinabugotech/hutool/issues)
- [Github  issue](https://github.com/chinabugotech/hutool/issues)
- [Gitcode issue   Gitcode问题](https://gitcode.com/chinabugotech/hutool/issues)

### 🧬贡献代码的步骤

1. 在Gitee、Github或Gitcode上fork项目到自己的repo
2. 把fork过去的项目也就是你的项目clone到你的本地
3. 修改代码（记得一定要修改v5-dev分支）
4. commit后push到自己的库（v5-dev分支）
5. 登录Gitee、Github或Gitcode在你首页可以看到一个 pull request 按钮，点击它，填写一些说明信息，然后提交即可。
6. 等待维护者合并

### 📐PR遵照的原则

Hutool欢迎任何人为Hutool添砖加瓦，贡献代码，不过维护者是一个强迫症患者，为了照顾病人，需要提交的pr（pull request）符合一些规范，规范如下：

1. 注释完备，尤其每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，必要时请添加单元测试，如果愿意，也可以加上你的大名。
2. Hutool的缩进按照Eclipse（~~不要跟我说IDEA多好用，维护者非常懒，学不会~~，IDEA真香，改了Eclipse快捷键后舒服多了）默认（tab）缩进，所以请遵守（不要和我争执空格与tab的问题，这是一个病人的习惯）。
3. 新加的方法不要使用第三方库的方法，Hutool遵循无依赖原则（除非在extra模块中加方法工具）。
4. 请pull request到`v5-dev`分支。Hutool在7.x版本后使用了新的分支：`v5-master`是主分支，表示已经发布中央库的版本，这个分支不允许pr，也不允许修改。

### 💞沟通说明

1. 提交地issue或PR未回复并开启状态表示还未处理，请耐心等待。
2. 为了保证新issue及时被发现和处理，我们会关闭一些描述不足的issue，此时你补充说明重新打开即可。
3. PR被关闭，表示被拒绝或需要修改地地方较多，重新提交即可。