<p align="center">
	<a href="https://hutool.cn/"><img src="https://cdn.jsdelivr.net/gh/looly/hutool-site/images/logo.jpg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬A set of tools that keep Java sweet.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-core 模块介绍

`Hutool-core`提供了最常使用的基础工具类，包括集合、Map、IO、线程、Bean、图片处理、线程并发等便捷工具。

-------------------------------------------------------------------------------

## 🛠️包含内容

### 注解(annotation)

提供了注解工具类，以及一些注解封装。如`CombinationAnnotationElement`组合注解以及Alias别名注解等。

### bean(bean)

提供了Bean工具类，以及Bean属性解析、Bean拷贝、动态Bean等。

### 构建器(builder)

抽象了Builder接口，提供建造者模式的封装，并默认提供了包括equals封装、Bean构建封装、比较器封装等。

### 克隆(clone)

提供`Cloneable`接口，明确`clone`方法，并提供默认实现类。

### 编码(codec)

提供了BaseN编码（Base16、Base32、Base58、Base62、Base64）编码实现。并提供了包括BCD、PunyCode、百分号编码的实现。
同时提供了包括莫尔斯电码、凯撒密码、RotN这类有趣功能的实现。

### 集合(collection)

集合中主要是提供了针对`Iterator`实现类的工具封装方法`IterUtil`和集合类封装的工具类`CollUtil`，并提供了一些特别的集合封装。

### 比较器(comparator)

主要是一些比较器的实现，如Bean字段比较器、自定义函数比较器、版本比较器等。

### 动态编译(compiler)

提供`javax.tools.JavaCompiler`的包装简化服务，形成源码动态编译工具类`CompilerUtil`，完成代码动态编译及热部署。

### 压缩(compress)

主要针对`java.util.zip`中的相关类封装工具，提供Zip、Gzip、Zlib等格式的压缩解压缩封装，为`ZipUtil`提供服务。

### 转换(convert)

“万能”转换器，提供整套的类型转换方式。通过`Converter`接口和`ConverterRegistry`转换登记中心，完成任意数据类型转换和自定义转换。

### 日期时间(date)

提供`Date`、`Calendar`、`java.time`相关API的工具化封装。包括时间解析、格式化、偏移等。

### 异常(exceptions)

提供异常工具`ExceptionUtil`，以及一些工具内部使用的异常。

### getter接口(getter)

提供各种类型的get操作接口封装。

### 图片(img)

提供图片、绘图、字体等工具封装，并提供GIF生成器和解析器实现。

### IO流和文件(io)

提供IO流工具、文件工具、文件类型工具等，并提供流拷贝、Checksum、文件监听功能实现。

### 语言特性(lang)

超级大杂项，提供一些设计模式的抽象实现（如单例模式`Singleton`），还有正则、Id生成器、函数、Hash算法、可变对象、树形结构、字典等。

### Map(map)

提供Map工具类和各类Map实现封装，如行列键的Table实现、自定义键值对转换的Map、线程安全的WeakMap实现等。

### 数学(math)

提供简单数学计算封装，如排列组合、货币类等。

### 网络(net)

提供网络相关工具封装，以及Ip地址工具类、SSL工具类、URL编码解码等。

### StreamAPI封装(stream)

提供简单的Stream相关封装。

### Swing和AWT(swing)

提供桌面应用API的工具封装，如启动应用、控制键盘鼠标操作、截屏等功能。

### 文本字符串(text)

提供强大的字符串文本封装，包括字符转换、字符串查找、字符串替换、字符串切分、Unicode工具等，并提供CSV格式封装。

### 线程及并发(thread)

线程并发封装，包括线程工具、锁工具、`CompletableFuture`封装工具、线程池构建等。

### 工具杂项(util)

提供其他不便归类的杂项工具类。如数组、编码、字符、Class、坐标系、身份证、组织机构代码、脱敏、枚举、转义、XML、进制转换、随机数、反射、正则、SPI等各种工具。