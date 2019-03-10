## 介绍
十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。例如十进制数57，在二进制写作111001，在16进制写作39。

像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20。`HexUtil`就是将字符串或byte数组与16进制表示转换的工具类。

## 用于
16进制一般针对无法显示的一些二进制进行显示，常用于：
1、图片的字符串表现形式
2、加密解密
3、编码转换

## 使用

`HexUtil`主要以`encodeHex`和`decodeHex`两个方法为核心，提供一些针对字符串的重载方法。

```java
String str = "我是一个字符串";

String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);

//hex是：
//e68891e698afe4b880e4b8aae5ad97e7aca6e4b8b2

String decodedStr = HexUtil.decodeHexStr(hex);

//解码后与str相同
```