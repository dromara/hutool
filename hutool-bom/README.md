<p align="center">
	<a href="https://hutool.cn/"><img src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-bom 模块介绍

`Hutool-bom`模块只由一个`pom.xml`组成，同时提供了`dependencyManagement`和`dependencies`两种声明。
于是我们就可以针对不同需要完成引入。

-------------------------------------------------------------------------------

## 🍒使用

### import方式

如果你想像Spring-Boot一样引入Hutool，再由子模块决定用到哪些模块，你可以在父模块中加入：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-bom</artifactId>
            <version>${hutool.version}</version>
            <type>pom</type>
            <!-- 注意这里是import -->
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

在子模块中就可以引入自己需要的模块了：

```xml
<dependencies>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-http</artifactId>
    </dependency>
</dependencies>
```

> 使用import的方式，只会引入hutool-bom内的dependencyManagement的配置，其它配置在这个引用方式下完全不起作用。

### exclude方式

如果你引入的模块比较多，但是某几个模块没用，你可以：

```xml
<dependencies>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-bom</artifactId>
        <version>${hutool.version}</version>
        <!-- 加不加这句都能跑，区别只有是否告警  -->
        <type>pom</type>
        <exclusions>
            <exclusion>
                    <groupId>cn.hutool</groupId>
                    <artifactId>hutool-system</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

> 这个配置会传递依赖hutool-bom内所有dependencies的内容，当前hutool-bom内的dependencies全部设置了version，就意味着在maven resolve的时候hutool-bom内就算存在dependencyManagement也不会产生任何作用。