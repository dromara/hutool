Base32编码解码-Base32
===

## 介绍

Base32就是用32（2的5次方）个特定ASCII码来表示256个ASCII码。所以，5个ASCII字符经过base32编码后会变为8个字符（公约数为40），长度增加3/5.不足8n用“=”补足。

## 使用

```java
String a = "伦家是一个非常长的字符串";

String encode = Base32.encode(a);
Assert.assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI", encode);
		
String decodeStr = Base32.decodeStr(encode);
Assert.assertEquals(a, decodeStr);
```

