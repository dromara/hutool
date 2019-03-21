## 介绍
转义和反转义工具类Escape / Unescape。escape采用ISO Latin字符集对指定的字符串进行编码。所有的空格符、标点符号、特殊字符以及其他非ASCII字符都将被转化成%xx格式的字符编码(xx等于该字符在字符集表里面的编码的16进制数字)。

此类中的方法对应Javascript中的`escape()`函数和`unescape()`函数。

## 方法
1. `EscapeUtil.escape` Escape编码（Unicode），该方法不会对 ASCII 字母和数字进行编码，也不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . / 。其他所有的字符都会被转义序列替换。

2. `EscapeUtil.unescape` Escape解码。

3. `EscapeUtil.safeUnescape` 安全的unescape文本，当文本不是被escape的时候，返回原文。