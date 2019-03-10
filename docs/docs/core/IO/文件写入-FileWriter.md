文件写入-FileWriter
===

相应的，文件读取有了，自然有文件写入类，使用方式与`FileReader`也类似：

```java
FileWriter writer = new FileWriter("test.properties");
writer.write("test");
```

写入文件分为追加模式和覆盖模式两类，追加模式可以用`append`方法，覆盖模式可以用`write`方法，同时也提供了一个write方法，第二个参数是可选覆盖模式。

同样，此类提供了：
- `getOutputStream`
- `getWriter`
- `getPrintWriter`

这些方法用于转换为相应的类提供更加灵活的写入操作。

