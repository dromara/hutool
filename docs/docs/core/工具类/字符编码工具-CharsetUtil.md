## 介绍
CharsetUtil主要针对编码操作做了工具化封装，同时提供了一些常用编码常量。

## 常量
常量在需要编码的地方直接引用，可以很好的提高便利性。

### 字符串形式
1. ISO_8859_1
2. UTF_8
3. GBK

### Charset对象形式
1. CHARSET_ISO_8859_1
2. CHARSET_UTF_8
3. CHARSET_GBK

## 方法

### 编码字符串转为Charset对象
`CharsetUtil.charset`方法用于将编码形式字符串转为Charset对象。

### 转换编码
`CharsetUtil.convert`方法主要是在两种编码中转换。主要针对因为编码识别错误而导致的乱码问题的一种解决方法。

### 系统默认编码
`CharsetUtil.defaultCharset`方法是`Charset.defaultCharset()`的封装方法。返回系统编码。
`CharsetUtil.defaultCharsetName`方法返回字符串形式的编码类型。