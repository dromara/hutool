日期时间对象-DateTime
===

## 由来
考虑工具类的局限性，在某些情况下使用并不简便，于是`DateTime`类诞生。`DateTime`对象充分吸取Joda-Time库的优点，并提供更多的便捷方法，这样我们在开发时不必再单独导入Joda-Time库便可以享受简单快速的日期时间处理过程。

## 说明
**DateTime**类继承于java.util.Date类，为Date类扩展了众多简便方法，这些方法多是`DateUtil`静态方法的对象表现形式，使用DateTime对象可以完全替代开发中Date对象的使用。

## 使用

### 新建对象
`DateTime`对象包含众多的构造方法，构造方法支持的参数有：
- Date
- Calendar
- String(日期字符串，第二个参数是日期格式)
- long 毫秒数

构建对象有两种方式：`DateTime.of()`和`new DateTime()`：

```java
Date date = new Date();
		
//new方式创建
DateTime time = new DateTime(date);
Console.log(time);

//of方式创建
DateTime now = DateTime.now();
DateTime dt = DateTime.of(date);
```

### 使用对象
`DateTime`的成员方法与`DateUtil`中的静态方法所对应，因为是成员方法，因此可以使用更少的参数操作日期时间。

示例：获取日期成员（年、月、日等）

```java
DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		
//年，结果：2017
int year = dateTime.year();

//季度（非季节），结果：Season.SPRING
Season season = dateTime.seasonEnum();

//月份，结果：Month.JANUARY
Month month = dateTime.monthEnum();

//日，结果：5
int day = dateTime.dayOfMonth();
```
更多成员方法请参阅API文档。


### 对象的可变性
DateTime对象默认是可变对象（调用offset、setField、setTime方法默认变更自身），但是这种可变性有时候会引起很多问题（例如多个地方共用DateTime对象）。我们可以调用`setMutable(false)`方法使其变为不可变对象。在不可变模式下，`offset`、`setField`方法返回一个新对象，`setTime`方法抛出异常。

```java
DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);

//默认情况下DateTime为可变对象，此时offset == dateTime
DateTime offset = dateTime.offset(DateField.YEAR, 0);

//设置为不可变对象后变动将返回新对象，此时offset != dateTime
dateTime.setMutable(false);
offset = dateTime.offset(DateField.YEAR, 0);
```

### 格式化为字符串
调用`toString()`方法即可返回格式为`yyyy-MM-dd HH:mm:ss`的字符串，调用`toString(String format)`可以返回指定格式的字符串。

```java
DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
//结果：2017-01-05 12:34:23
String dateStr = dateTime.toString();

//结果：2017/01/05
```

