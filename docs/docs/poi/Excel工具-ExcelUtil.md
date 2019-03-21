## 介绍

Excel操作工具封装

## 使用

1. 从文件中读取Excel为ExcelReader

```java
ExcelReader reader = ExcelUtil.getReader(FileUtil.file("test.xlsx"));
```

2. 从流中读取Excel为ExcelReader（比如从ClassPath中读取Excel文件）

```java
ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("aaa.xlsx"));
```

3. 读取指定的sheet

```java
ExcelReader reader;

//通过sheet编号获取
reader = ExcelUtil.getReader(FileUtil.file("test.xlsx"), 0);
//通过sheet名获取
reader = ExcelUtil.getReader(FileUtil.file("test.xlsx"), "sheet1");
```

4. 读取大数据量的Excel

```java
private RowHandler createRowHandler() {
	return new RowHandler() {
		@Override
		public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
			Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
		}
	};
}

ExcelUtil.readBySax("aaa.xlsx", 0, createRowHandler());
```

## 后续
`ExcelUtil.getReader`方法只是将实体Excel文件转换为ExcelReader对象进行操作。接下来请参阅章节ExcelReader对Excel工作簿进行具体操作。