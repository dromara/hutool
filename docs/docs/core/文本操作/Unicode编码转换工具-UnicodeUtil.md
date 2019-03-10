Unicode编码转换工具-UnicodeUtil
===

## 介绍
此工具主要针对类似于`\\u4e2d\\u6587`这类Unicode字符做一些特殊转换。

## 使用

### 字符串转Unicode符

```
//第二个参数true表示跳过ASCII字符（只跳过可见字符）
String s = UnicodeUtil.toUnicode("aaa123中文", true);
//结果aaa123\\u4e2d\\u6587
```

### Unicode转字符串

```
String str = "aaa\\U4e2d\\u6587\\u111\\urtyu\\u0026";
String res = UnicodeUtil.toString(str);
//结果aaa中文\\u111\\urtyu&
```

由于`\\u111`为非Unicode字符串，因此原样输出。

