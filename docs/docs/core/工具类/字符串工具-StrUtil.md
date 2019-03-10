## 由来

这个工具的用处类似于[Apache Commons Lang](http://commons.apache.org/)中的`StringUtil`，之所以使用`StrUtil`而不是使用`StringUtil`是因为前者更短，而且`Str`这个简写我想已经深入人心了，大家都知道是字符串的意思。常用的方法例如`isBlank`、`isNotBlank`、`isEmpty`、`isNotEmpty`这些我就不做介绍了，判断字符串是否为空，下面我说几个比较好用的功能。

## 方法

### 1. `hasBlank`、`hasEmpty`方法
就是给定一些字符串，如果一旦有空的就返回true，常用于判断好多字段是否有空的（例如web表单数据）。

**这两个方法的区别是`hasEmpty`只判断是否为null或者空字符串（""），`hasBlank`则会把不可见字符也算做空，`isEmpty`和`isBlank`同理。**

### 2. `removePrefix`、`removeSuffix`方法
这两个是去掉字符串的前缀后缀的，例如去个文件名的扩展名啥。

```Java
String fileName = StrUtil.removeSuffix("pretty_girl.jpg", ".jpg")  //fileName -> pretty_girl
```
还有忽略大小写的`removePrefixIgnoreCase`和`removeSuffixIgnoreCase`都比较实用。

### 3. `sub`方法
不得不提一下这个方法，有人说String有了subString你还写它干啥，我想说subString方法越界啥的都会报异常，你还得自己判断，难受死了，我把各种情况判断都加进来了，而且index的位置还支持负数哦，-1表示最后一个字符（这个思想来自于[Python](https://www.python.org/)，如果学过[Python](https://www.python.org/)的应该会很喜欢的），还有就是如果不小心把第一个位置和第二个位置搞反了，也会自动修正（例如想截取第4个和第2个字符之间的部分也是可以的哦~）
举个栗子

```Java
String str = "abcdefgh";
String strSub1 = StrUtil.sub(str, 2, 3); //strSub1 -> c
String strSub2 = StrUtil.sub(str, 2, -3); //strSub2 -> cde
String strSub3 = StrUtil.sub(str, 3, 2); //strSub2 -> c
```

### 4. `str`、`bytes`方法
好吧，我承认把`String.getByte(String charsetName)`方法封装在这里了，原生的`String.getByte()`这个方法太坑了，使用系统编码，经常会有人跳进来导致乱码问题，所以我就加了这两个方法强制指定字符集了，包了个try抛出一个运行时异常，省的我得在我业务代码里处理那个恶心的`UnsupportedEncodingException`。

### 5. format方法
我会告诉你这是我最引以为豪的方法吗？灵感来自slf4j，可以使用字符串模板代替字符串拼接，我也自己实现了一个，而且变量的标识符都一样，神马叫无缝兼容~~来，上栗子（吃多了上火吧……）
````Java
String template = "{}爱{}，就像老鼠爱大米";
String str = StrUtil.format(template, "我", "你"); //str -> 我爱你，就像老鼠爱大米
````
参数我定义成了Object类型，如果传别的类型的也可以，会自动调用toString()方法的。

### 6. 定义的一些常量
为了方便，我定义了一些比较常见的字符串常量在里面，像点、空串、换行符等等，还有HTML中的一些转移字符。

更多方法请参阅API文档。