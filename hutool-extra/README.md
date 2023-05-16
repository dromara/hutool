<p align="center">
	<a href="https://hutool.cn/"><img src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-extra 模块介绍

`Hutool-extra`提供了第三方库的工具类，用于简化使用。

-------------------------------------------------------------------------------

## 🛠️包含内容

### AOP切面

动态代理封装，提供非IOC下的切面支，门面模式封装了：

- 基于`java.lang.reflect.Proxy`代理
- 基于`Spring-cglib`代理

### 压缩

提供基于[Commons-compress](https://commons.apache.org/proper/commons-compress/)的压缩解压封装。

### Emoji

提供基于[emoji-java](https://github.com/vdurmont/emoji-java)的Emoji表情工具类。

### FTP

- 提供基于[Apache Commons Net](https://commons.apache.org/proper/commons-net/)的FTP封装。
- 提供基于[Apache FtpServer](http://mina.apache.org/ftpserver-project/)的FTP Server封装。

### Mail

提供基于[Jakarta Mail](https://projects.eclipse.org/projects/ee4j.mail)邮件发送封装。

### 系统信息监控

- 提供基于`JMX（Java Management Extensions）`相关封装，用于完成JVM的监测和管理。
- 提供基于[Oshi](https://github.com/oshi/oshi)的封装，用于通过JNI方式获取系统信息。

### 拼音

提供拼音工具库的API，通过门面模式，完成各类拼音库的适配。

### 二维码和条形码

提供基于[Zxing](https://github.com/zxing/zxing)的二维码、条形码的生成和识别封装。

### Spring

提供[Spring](https://spring.io/projects/spring-framework)相关工具类。

### SSH

- 提供基于[Jsch](http://www.jcraft.com/jsch/)的SSH、SFTP封装。
- 提供基于[Ganymed-SSH2](https://www.ganymed.ethz.ch/ssh2/)的SSH封装。
- 提供基于[SSHJ](https://github.com/hierynomus/sshj)的SSH封装。

### 模板引擎

通过门面模式提供统一的接口用于适配第三方模板引擎。

- `TemplateEngine`：模板引擎接口，用于不同引擎的实现。
- `Template`：      模板接口，用于不同引擎模板对象包装。
- `TemplateConfig`：模板配置，用于提供公共配置项。

### 分词器

通过门面模式提供第三方分词库的封装。

- `TokenizerEngine`：分词引擎接口，用于具体实现分词功能。
- `Result`:          分词结果，提供分词后的单词遍历。
- `Word`:            分词，用于表示一个词，以及词的位置。

### 数据校验

基于JSR-380标准的校验工具类，封装了[Jakarta Bean Validation](https://beanvalidation.org/)。

### XML和JAXB

提供基于`javax.xml.bind`的JAXB（Java Architecture for XML Binding）封装，根据XML Schema产生Java对象，即实现xml和Bean互转。