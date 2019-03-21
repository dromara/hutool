## 介绍
在标准的ExcelReader中，如果数据量较大，读取Excel会非常缓慢，并有可能造成内存溢出。因此针对大数据量的Excel，Hutool封装了Sax模式的读取方式。

Excel07SaxReader只支持Excel2007格式的Sax读取。

## 使用

### 定义行处理器

首先我们实现一下`RowHandler`接口，这个接口是Sax读取的核心，通过实现handle方法编写我们要对每行数据的操作方式（比如按照行入库，入List或者写出到文件等），在此我们只是在控制台打印。

```java
private RowHandler createRowHandler() {
	return new RowHandler() {
		@Override
		public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
			Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
		}
	};
}
```

### ExcelUtil快速读取
```java
ExcelUtil.read07BySax("aaa.xlsx", 0, createRowHandler());
```

### 构建对象读取
```java
Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
reader.read("d:/text.xlsx", 0);
```

reader方法的第二个参数是sheet的序号，-1表示读取所有sheet，0表示第一个sheet，依此类推。