## 由来

我一直对Slf4j的字符串格式化情有独钟，通过`{}`这种简单的占位符完成字符串的格式化。于是参考Slf4j的源码，便有了`StrFormatter`。

> StrFormatter.format的快捷使用方式为`StrUtil.format`，推荐使用后者。

## 使用

```java
//通常使用
String result1 = StrFormatter.format("this is {} for {}", "a", "b");
Assert.assertEquals("this is a for b", result1);

//转义{}
String result2 = StrFormatter.format("this is \\{} for {}", "a", "b");
Assert.assertEquals("this is {} for a", result2);

//转义\
String result3 = StrFormatter.format("this is \\\\{} for {}", "a", "b");
Assert.assertEquals("this is \\a for b", result3);
```