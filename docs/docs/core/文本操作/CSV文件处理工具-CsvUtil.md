CSV文件处理工具-CsvUtil
===

## 介绍
逗号分隔值（Comma-Separated Values，CSV，有时也称为字符分隔值，因为分隔字符也可以不是逗号），其文件以纯文本形式存储表格数据（数字和文本）。

Hutool针对此格式，参考FastCSV项目做了对CSV文件读写的实现(Hutool实现完全独立，不依赖第三方)

`CsvUtil`是CSV工具类，主要封装了两个方法：

- getReader 用于对CSV文件读取
- getWriter 用于生成CSV文件

这两个方法分别获取`CsvReader`对象和`CsvWriter`，从而独立完成CSV文件的读写。

## 使用

### 读取CSV文件

```java
CsvReader reader = CsvUtil.getReader();
//从文件中读取CSV数据
CsvData data = reader.read(FileUtil.file("test.csv"));
List<CsvRow> rows = data.getRows();
//遍历行
for (CsvRow csvRow : data) {
	//getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
	Console.log(csvRow.getRawList());
}
```

`CsvRow`对象还记录了一些其他信息，包括原始行号等。

### 生成CSV文件

```
//指定路径和编码
CsvWriter writer = CsvUtil.getWriter("e:/testWrite.csv", CharsetUtil.CHARSET_UTF_8);
//按行写出
writer.write(
	new String[] {"a1", "b1", "c1"}, 
	new String[] {"a2", "b2", "c2"}, 
	new String[] {"a3", "b3", "c3"}
);
```

效果如下：
![](https://static.oschina.net/uploads/img/201809/05222906_kF5o.png)

> 注意
> CSV文件本身为一种简单文本格式，有编码区分。Excel读取CSV文件中含有中文时时必须为GBK编码（Windows平台下），否则会出现乱码。

