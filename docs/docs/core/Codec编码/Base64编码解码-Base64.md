Base64编码解码-Base64
===

## 介绍

Base64编码是用64（2的6次方）个ASCII字符来表示256（2的8次方）个ASCII字符，也就是三位二进制数组经过编码后变为四位的ASCII字符显示，长度比原来增加1/3。

## 使用

```java
String a = "伦家是一个非常长的字符串";
String encode = Base64.encode(a);
Assert.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5Liy", encode);

String decodeStr = Base64.decodeStr(encode);
Assert.assertEquals(a, decodeStr);
```

