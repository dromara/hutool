## 由来
Excel有读取也便有写出，Hutool针对将数据写出到Excel做了封装。

## 原理
Hutool将Excel写出封装为`ExcelWriter`，原理为包装了Workbook对象，每次调用`merge`（合并单元格）或者`write`（写出数据）方法后只是将数据写入到Workbook，并不写出文件，只有调用`flush`或者`close`方法后才会真正写出文件。

由于机制原因，在写出结束后需要关闭`ExcelWriter`对象，调用`close`方法即可关闭，此时才会释放Workbook对象资源，否则带有数据的Workbook一直会常驻内存。

## 使用例子

### 1. 将行列对象写出到Excel

我们先定义一个嵌套的List，List的元素也是一个List，内层的一个List代表一行数据，每行都有4个单元格，最终`list`对象代表多行数据。

```java
List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
```

然后我们创建`ExcelWriter`对象后写出数据：

```java
//通过工具类创建writer
ExcelWriter writer = ExcelUtil.getWriter("d:/writeTest.xlsx");
//通过构造方法创建writer
//ExcelWriter writer = new ExcelWriter("d:/writeTest.xls");

//跳过当前行，既第一行，非必须，在此演示用
writer.passCurrentRow();

//合并单元格后的标题行，使用默认标题样式
writer.merge(list1.size() - 1, "测试标题");
//一次性写出内容，强制输出标题
writer.write(rows, true);
//关闭writer，释放内存
writer.close();
```

效果：
![写出效果图](https://static.oschina.net/uploads/img/201711/12111543_dmjs.png)

### 2. 写出Map数据

构造数据：

```java
Map<String, Object> row1 = new LinkedHashMap<>();
row1.put("姓名", "张三");
row1.put("年龄", 23);
row1.put("成绩", 88.32);
row1.put("是否合格", true);
row1.put("考试日期", DateUtil.date());

Map<String, Object> row2 = new LinkedHashMap<>();
row2.put("姓名", "李四");
row2.put("年龄", 33);
row2.put("成绩", 59.50);
row2.put("是否合格", false);
row2.put("考试日期", DateUtil.date());

ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
```

写出数据：

```java
// 通过工具类创建writer
ExcelWriter writer = ExcelUtil.getWriter("d:/writeMapTest.xlsx");
// 合并单元格后的标题行，使用默认标题样式
writer.merge(row1.size() - 1, "一班成绩单");
// 一次性写出内容，使用默认样式，强制输出标题
writer.write(rows, true);
// 关闭writer，释放内存
writer.close();
```

效果：
![写出效果](https://static.oschina.net/uploads/img/201711/12134150_BDDT.png)

### 3. 写出Bean数据

定义Bean:

```java
public class TestBean {
	private String name;
	private int age;
	private double score;
	private boolean isPass;
	private Date examDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
}
````

构造数据：

```java
TestBean bean1 = new TestBean();
bean1.setName("张三");
bean1.setAge(22);
bean1.setPass(true);
bean1.setScore(66.30);
bean1.setExamDate(DateUtil.date());

TestBean bean2 = new TestBean();
bean2.setName("李四");
bean2.setAge(28);
bean2.setPass(false);
bean2.setScore(38.50);
bean2.setExamDate(DateUtil.date());

List<TestBean> rows = CollUtil.newArrayList(bean1, bean2);
```

写出数据：

```java
// 通过工具类创建writer
ExcelWriter writer = ExcelUtil.getWriter("d:/writeBeanTest.xlsx");
// 合并单元格后的标题行，使用默认标题样式
writer.merge(4, "一班成绩单");
// 一次性写出内容，使用默认样式，强制输出标题
writer.write(rows, true);
// 关闭writer，释放内存
writer.close();
```

效果：
![写出Bean数据](https://static.oschina.net/uploads/img/201711/12143029_3B2E.png)

### 4. 自定义Bean的key别名（排序标题）

在写出Bean的时候，我们可以调用`ExcelWriter`对象的`addHeaderAlias`方法自定义Bean中key的别名，这样就可以写出自定义标题了（例如中文）。

写出数据：

```java
// 通过工具类创建writer
ExcelWriter writer = ExcelUtil.getWriter("d:/writeBeanTest.xlsx");

//自定义标题别名
writer.addHeaderAlias("name", "姓名");
writer.addHeaderAlias("age", "年龄");
writer.addHeaderAlias("score", "分数");
writer.addHeaderAlias("isPass", "是否通过");
writer.addHeaderAlias("examDate", "考试时间");

// 合并单元格后的标题行，使用默认标题样式
writer.merge(4, "一班成绩单");
// 一次性写出内容，使用默认样式，强制输出标题
writer.write(rows, true);
// 关闭writer，释放内存
writer.close();
```

效果：
![](https://static.oschina.net/uploads/img/201808/01220010_Ybbw.png)

> 提示（since 4.1.5）
> 默认情况下Excel中写出Bean字段不能保证顺序，此时可以使用`addHeaderAlias`方法设置标题别名，Bean的写出顺序就会按照标题别名的加入顺序排序。
> 如果不需要设置标题但是想要排序字段，请调用`writer.addHeaderAlias("age", "age")`设置一个相同的别名就可以不更换标题。
> 未设置标题别名的字段不参与排序，会默认排在前面。

### 5. 写出到流

```java
// 通过工具类创建writer，默认创建xls格式
ExcelWriter writer = ExcelUtil.getWriter();
//创建xlsx格式的
//ExcelWriter writer = ExcelUtil.getWriter(true);
// 一次性写出内容，使用默认样式，强制输出标题
writer.write(rows, true);
//out为OutputStream，需要写出到的目标流
writer.flush(out);
// 关闭writer，释放内存
writer.close();
```

### 6. 写出到客户端下载（写出到Servlet）

```java
// 通过工具类创建writer，默认创建xls格式
ExcelWriter writer = ExcelUtil.getWriter();
// 一次性写出内容，使用默认样式，强制输出标题
writer.write(rows, true);
//out为OutputStream，需要写出到的目标流

//response为HttpServletResponse对象
response.setContentType("application/vnd.ms-excel;charset=utf-8"); 
//test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
response.setHeader("Content-Disposition","attachment;filename=test.xls"); 
ServletOutputStream out=response.getOutputStream(); 

writer.flush(out);
// 关闭writer，释放内存
writer.close();
//此处记得关闭输出Servlet流
IoUtil.close(out);
```

> 注意
> `ExcelUtil.getWriter()`默认创建xls格式的Excel，因此写出到客户端也需要自定义文件名为XXX.xls，否则会出现文件损坏的提示。
> 若想生成xlsx格式，请使用`ExcelUtil.getWriter(true)`创建。

## 自定义Excel

### 1. 设置单元格背景色

```java
ExcelWriter writer = ...;

// 定义单元格背景色
StyleSet style = writer.getStyleSet();
// 第二个参数表示是否也设置头部单元格背景
style.setBackgroundColor(IndexedColors.RED, false);
```

### 2. 自定义字体

```java
ExcelWriter writer = ...;
//设置内容字体
Font font = writer.createFont();
font.setBold(true);
font.setColor(Font.COLOR_RED); 
font.setItalic(true); 
//第二个参数表示是否忽略头部样式
writer.getStyleSet().setFont(font, true);
```

### 3. 写出多个sheet

```java
//初始化时定义表名
ExcelWriter writer = new ExcelWriter("d:/aaa.xls", "表1");
//切换sheet，此时从第0行开始写
writer.setSheet("表2");
...
writer.setSheet("表3");
...
```

### 4. 更详细的定义样式

在Excel中，由于样式对象个数有限制，因此Hutool根据样式种类分为4个样式对象，使相同类型的单元格可以共享样式对象。样式按照类别存在于`StyleSet`中，其中包括：

- 头部样式 headCellStyle
- 普通单元格样式 cellStyle
- 数字单元格样式 cellStyleForNumber
- 日期单元格样式 cellStyleForDate

其中`cellStyleForNumber` `cellStyleForDate`用于控制数字和日期的显示方式。

因此我们可以使用以下方式获取`CellStyle`对象自定义指定种类的样式：

```java
StyleSet style = writer.getStyleSet();
CellStyle cellStyle = style.getHeadCellStyle();
...
```
