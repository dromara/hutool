类型转换工具类-Convert
===

## 痛点

在Java开发中我们要面对各种各样的类型转换问题，尤其是从命令行获取的用户参数、从HttpRequest获取的Parameter等等，这些参数类型多种多样，我们怎么去转换他们呢？常用的办法是先整成String，然后调用XXX.parseXXX方法，还要承受转换失败的风险，不得不加一层try catch，这个小小的过程混迹在业务代码中会显得非常难看和臃肿。

## Convert类

**Convert**类可以说是一个工具方法类，里面封装了针对Java常见类型的转换，用于简化类型转换。**Convert**类中大部分方法为toXXX，参数为Object，可以实现将任意可能的类型转换为指定类型。同时支持第二个参数**defaultValue**用于在转换失败时返回一个默认值。

### Java常见类型转换

1、转换为字符串：

```java
int a = 1;
//aStr为"1"
String aStr = Convert.toStr(a);

long[] b = {1,2,3,4,5};
//bStr为："[1, 2, 3, 4, 5]"
String bStr = Convert.toStr(b);
```

2、转换为指定类型数组：

```java
String[] b = { "1", "2", "3", "4" };
//结果为Integer数组
Integer[] intArray = Convert.toIntArray(b);

long[] c = {1,2,3,4,5};
//结果为Integer数组
Integer[] intArray2 = Convert.toIntArray(c);
```

3、转换为日期对象：

```
String a = "2017-05-06";
Date value = Convert.toDate(a);
```

4、转换为集合
```
Object[] a = {"a", "你", "好", "", 1};
List<?> list = Convert.convert(List.class, a);
//从4.1.11开始可以这么用
List<?> list = Convert.toList(a);
```

### 其它类型转换

通过`Convert.convert(Class<T>, Object)`方法可以将任意类型转换为指定类型，Hutool中预定义了许多类型转换，例如转换为URI、URL、Calendar等等，这些类型的转换都依托于`ConverterRegistry`类。通过这个类和`Converter`接口，我们可以自定义一些类型转换。详细的使用请参阅“自定义类型转换”一节。

### 半角和全角转换
在很多文本的统一化中这两个方法非常有用，主要对标点符号的全角半角转换。

半角转全角：
```java
String a = "123456789";

//结果为："１２３４５６７８９"
String sbc = Convert.toSBC(a);
```

全角转半角：
```java
String a = "１２３４５６７８９";

//结果为"123456789"
String dbc = Convert.toDBC(a);
```

### 16进制（Hex）
在很多加密解密，以及中文字符串传输（比如表单提交）的时候，会用到16进制转换，就是Hex转换，为此Hutool中专门封装了**HexUtil**工具类，考虑到16进制转换也是转换的一部分，因此将其方法也放在Convert类中，便于理解和查找，使用同样非常简单：

转为16进制（Hex）字符串
```java
String a = "我是一个小小的可爱的字符串";

//结果："e68891e698afe4b880e4b8aae5b08fe5b08fe79a84e58fafe788b1e79a84e5ad97e7aca6e4b8b2"
String hex = Convert.toHex(a, CharsetUtil.CHARSET_UTF_8);
```

将16进制（Hex）字符串转为普通字符串:
```java
String hex = "e68891e698afe4b880e4b8aae5b08fe5b08fe79a84e58fafe788b1e79a84e5ad97e7aca6e4b8b2";

//结果为："我是一个小小的可爱的字符串"
String raw = Convert.hexStrToStr(hex, CharsetUtil.CHARSET_UTF_8);

//注意：在4.1.11之后hexStrToStr将改名为hexToStr
String raw = Convert.hexToStr(hex, CharsetUtil.CHARSET_UTF_8);
```

> 因为字符串牵涉到编码问题，因此必须传入编码对象，此处使用UTF-8编码。
> **toHex**方法同样支持传入byte[]，同样也可以使用**hexToBytes**方法将16进制转为byte[]

### Unicode和字符串转换

与16进制类似，Convert类同样可以在字符串和Unicode之间轻松转换：

```java
String a = "我是一个小小的可爱的字符串";

//结果为："\\u6211\\u662f\\u4e00\\u4e2a\\u5c0f\\u5c0f\\u7684\\u53ef\\u7231\\u7684\\u5b57\\u7b26\\u4e32"	
String unicode = Convert.strToUnicode(a);

//结果为："我是一个小小的可爱的字符串"
String raw = Convert.unicodeToStr(unicode);
```
很熟悉吧？如果你在properties文件中写过中文，你会明白这个方法的重要性。

### 编码转换

在接收表单的时候，我们常常被中文乱码所困扰，其实大多数原因是使用了不正确的编码方式解码了数据。于是`Convert.convertCharset`方法便派上用场了，它可以把乱码转为正确的编码方式：

```java
String a = "我不是乱码";
//转换后result为乱码
String result = Convert.convertCharset(a, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1);
String raw = Convert.convertCharset(result, CharsetUtil.ISO_8859_1, "UTF-8");
Assert.assertEquals(raw, a);
```

> 注意
> 经过测试，UTF-8编码后用GBK解码再用GBK编码后用UTF-8解码会存在某些中文转换失败的问题。

### 时间单位转换
`Convert.convertTime`方法主要用于转换时长单位，比如一个很大的毫秒，我想获得这个毫秒数对应多少分：
```java
long a = 4535345;

//结果为：75
long minutes = Convert.convertTime(a, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
```

### 金额大小写转换
面对财务类需求，`Convert.digitToChinese`将金钱数转换为大写形式：
```java
double a = 67556.32;

//结果为："陆万柒仟伍佰伍拾陆元叁角贰分"
String digitUppercase = Convert.digitToChinese(a);
```
> 注意
> 转换为大写只能精确到分（小数点儿后两位），之后的数字会被忽略。

### 原始类和包装类转换
有的时候，我们需要将包装类和原始类相互转换（比如Integer.classs 和 int.class），这时候我们可以：
```java
//去包装
Class<?> wrapClass = Integer.class;

//结果为：int.class
Class<?> unWraped = Convert.unWrap(wrapClass);

//包装
Class<?> primitiveClass = long.class;

//结果为：Long.class
Class<?> wraped = Convert.wrap(primitiveClass);
```

