IO工具类-IoUtil
===

## 由来
IO工具类的存在主要针对InputStream、OutputStream、Reader、Writer封装简化，并对NIO相关操作做封装简化。总体来说，Hutool对IO的封装，主要是工具层面，我们努力做到在便捷、性能和灵活之间找到最好的平衡点。

## 方法

### 拷贝
流的读写可以总结为从输入流读取，从输出流写出，这个过程我们定义为**拷贝**。这个是一个基本过程，也是文件、流操作的基础。

以文件流拷贝为例：
```java
BufferedInputStream in = FileUtil.getInputStream("d:/test.txt");
BufferedOutputStream out = FileUtil.getOutputStream("d:/test2.txt");
long copySize = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
```

copy方法同样针对Reader、Writer、Channel等对象有一些重载方法，并提供可选的缓存大小。默认的，缓存大小为`1024`个字节，如果拷贝大文件或流数据较大，可以适当调整这个参数。

针对NIO，提供了`copyByNIO`方法，以便和BIO有所区别。我查阅过一些资料，使用NIO对文件流的操作有一定的提升，我并没有做具体实验。相关测试请参阅博客：[http://www.cnblogs.com/gaopeng527/p/4896783.html](http://www.cnblogs.com/gaopeng527/p/4896783.html)

### Stream转Reader、Writer
- `IoUtil.getReader`：将`InputStream`转为`BufferedReader`用于读取字符流，它是部分readXXX方法的基础。
- `IoUtil.getWriter`：将`OutputStream`转为`OutputStreamWriter`用于写入字符流，它是部分writeXXX的基础。

本质上这两个方法只是简单new一个新的Reader或者Writer对象，但是封装为工具方法配合IDE的自动提示可以大大减少查阅次数（例如你对BufferedReader、OutputStreamWriter不熟悉，是不需要搜索一下相关类？）

### 读取流中的内容
读取流中的内容总结下来，可以分为read方法和readXXX方法。

1. `read`方法有诸多的重载方法，根据参数不同，可以读取不同对象中的内容，这包括：
 - `InputStream`
 - `Reader`
 - `FileChannel`

这三个重载大部分返回String字符串，为字符流读取提供极大便利。

2. `readXXX`方法主要针对返回值做一些处理，例如：
 - `readBytes` 返回byte数组（读取图片等）
 - `readHex` 读取16进制字符串
 - `readObj` 读取序列化对象（反序列化）
 - `readLines` 按行读取

3. `toStream`方法则是将某些对象转换为流对象，便于在某些情况下操作：
 - `String` 转换为`ByteArrayInputStream`
 - `File` 转换为`FileInputStream`

### 写入到流
- `IoUtil.write`方法有两个重载方法，一个直接调用`OutputStream.write`方法，另一个用于将对象转换为字符串（调用toString方法），然后写入到流中。
- `IoUtil.writeObjects` 用于将可序列化对象序列化后写入到流中。

`write`方法并没有提供writeXXX，需要自己转换为String或byte[]。

### 关闭
对于IO操作来说，使用频率最高（也是最容易被遗忘）的就是`close`操作，好在Java规范使用了优雅的`Closeable`接口，这样我们只需简单封装调用此接口的方法即可。

关闭操作会面临两个问题：
1. 被关闭对象为空
2. 对象关闭失败（或对象已关闭）

`IoUtil.close`方法很好的解决了这两个问题。

在JDK1.7中，提供了`AutoCloseable`接口，在`IoUtil`中同样提供相应的重载方法，在使用中并不能感觉到有哪些不同。

