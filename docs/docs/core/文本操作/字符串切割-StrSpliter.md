## 由来
在Java的String对象中提供了split方法用于通过某种字符串分隔符来把一个字符串分割为数组。但是有的时候我们对这种操作有不同的要求，默认方法无法满足，这包括：

- 分割限制分割数
- 分割后每个字符串是否需要去掉两端空格
- 是否忽略空白片
- 根据固定长度分割
- 通过正则分隔

因此，`StrSpliter`应运而生。`StrSpliter`中全部为静态方法，方便快捷调用。

## 方法

### 基础方法

`split` 切分字符串，众多可选参数，返回结果为List
`splitToArray` 切分字符串，返回结果为数组
`splitsplitByRegex` 根据正则切分字符串
`splitByLength` 根据固定长度切分字符串

栗子：
```java
String str1 = "a, ,efedsfs,   ddf";
//参数：被切分字符串，分隔符逗号，0表示无限制分片数，去除两边空格，忽略空白项
List<String> split = StrSpliter.split(str1, ',', 0, true, true);
```

### 特殊方法

`splitPath` 切分字符串，分隔符为"/"
`splitPathToArray` 切分字符串，分隔符为"/"，返回数组