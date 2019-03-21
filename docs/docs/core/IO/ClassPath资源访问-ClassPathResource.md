ClassPath资源访问-ClassPathResource
===

## 什么是ClassPath
简单说来ClassPath就是查找class文件的路径，在Tomcat等容器下，ClassPath一般是`WEB-INF/classes`，在普通java程序中，我们可以通过定义`-cp`或者`-classpath`参数来定义查找class文件的路径，这些路径就是ClassPath。

为了项目方便，我们定义的配置文件肯定不能使用绝对路径，所以需要使用相对路径，这时候最好的办法就是把配置文件和class文件放在一起，便于查找。

## 由来
在Java编码过程中，我们常常希望读取项目内的配置文件，按照Maven的习惯，这些文件一般放在项目的`src/main/resources`下，读取的时候使用：

```java
String path = "config.properties";
InputStream in = this.class.getResource(path).openStream();
```

使用当前类来获得资源其实就是使用当前类的类加载器获取资源，最后openStream()方法获取输入流来读取文件流。

## 封装
面对这种复杂的读取操作，我们封装了`ClassPathResource`类来简化这种资源的读取：

```java
ClassPathResource resource = new ClassPathResource("test.properties");
Properties properties = new Properties();
properties.load(resource.getStream());

Console.log("Properties: {}", properties);
```

这样就大大简化了ClassPath中资源的读取。

> Hutool提供针对properties的封装类`Props`，同时提供更加强大的配置文件Setting类，这两个类已经针对ClassPath做过相应封装，可以以更加便捷的方式读取配置文件。相关文档请参阅Hutool-setting章节

