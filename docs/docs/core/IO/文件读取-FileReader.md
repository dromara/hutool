文件读取-FileReader
===

## 由来
在`FileUtil`中本来已经针对文件的读操作做了大量的静态封装，但是根据职责分离原则，我觉得有必要针对文件读取单独封装一个类，这样项目更加清晰。当然，使用FileUtil操作文件是最方便的。

## 使用
在JDK中，同样有一个FileReader类，但是并不如想象中的那样好用，于是Hutool便提供了更加便捷FileReader类。

```java
//默认UTF-8编码，可以在构造中传入第二个参数做为编码
FileReader fileReader = new FileReader("test.properties");
String result = fileReader.readString();
```

FileReader提供了以下方法来快速读取文件内容：

- `readBytes`
- `readString`
- `readLines`

同时，此类还提供了以下方法用于转换为流或者BufferedReader：
- `getReader`
- `getInputStream`

