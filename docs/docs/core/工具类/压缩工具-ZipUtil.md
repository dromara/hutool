## 由来

在Java中，对文件、文件夹打包，压缩是一件比较繁琐的事情，我们常常引入Zip4j进行此类操作。但是很多时候，JDK中的zip包就可满足我们大部分需求。ZipUtil就是针对java.util.zip做工具化封装，使压缩解压操作可以一个方法搞定，并且自动处理文件和目录的问题，不再需要用户判断，压缩后的文件也会自动创建文件，自动创建父目录，大大简化的压缩解压的复杂度。

## 方法

### Zip

1. 压缩

`ZipUtil.zip` 方法提供一系列的重载方法，满足不同需求的压缩需求，这包括：

- 打包到当前目录（可以打包文件，也可以打包文件夹，根据路径自动判断）

```java
//将aaa目录下的所有文件目录打包到d:/aaa.zip
ZipUtil.zip("d:/aaa");
```

- 指定打包后保存的目的地，自动判断目标是文件还是文件夹

```java
//将aaa目录下的所有文件目录打包到d:/bbb/目录下的aaa.zip文件中
ZipUtil.zip("d:/aaa", "d:/bbb/");

//将aaa目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip");
```

- 可选是否包含被打包的目录。比如我们打包一个照片的目录，打开这个压缩包有可能是带目录的，也有可能是打开压缩包直接看到的是文件。zip方法增加一个boolean参数可选这两种模式，以应对众多需求。

```
//将aaa目录以及其目录下的所有文件目录打包到d:/bbb/目录下的ccc.zip文件中
ZipUtil.zip("d:/aaa", "d:/bbb/ccc.zip", true);
```

- 多文件或目录压缩。可以选择多个文件或目录一起打成zip包。

```java
ZipUtil.zip(FileUtil.file("d:/bbb/ccc.zip"), false, 
    FileUtil.file("d:/test1/file1.txt"),
    FileUtil.file("d:/test1/file2.txt"),
    FileUtil.file("d:/test2/file1.txt"),
    FileUtil.file("d:/test2/file2.txt"),
);
```

2. 解压

`ZipUtil.unzip` 解压。同样提供几个重载，满足不同需求。

```
//将test.zip解压到e:\\aaa目录下，返回解压到的目录
File unzip = ZipUtil.unzip("E:\\aaa\\test.zip", "e:\\aaa");
```

### Gzip

Gzip是网页传输中广泛使用的压缩方式，Hutool同样提供其工具方法简化其过程。

`ZipUtil.gzip` 压缩，可压缩字符串，也可压缩文件
`ZipUtil.unGzip` 解压Gzip文件

### Zlib

`ZipUtil.zlib` 压缩，可压缩字符串，也可压缩文件
`ZipUtil.unZlib` 解压zlib文件

> 注意
> ZipUtil默认情况下使用系统编码，也就是说：
> 1. 如果你在命令行下运行，则调用系统编码（一般Windows下为GBK、Linux下为UTF-8）
> 2. 如果你在IDE（如Eclipse）下运行代码，则读取的是当前项目的编码（详细请查阅IDE设置，我的项目默认都是UTF-8编码，因此解压和压缩都是用这个编码）