可复用字符串生成器-StrBuilder
===

## 介绍
在JDK提供的`StringBuilder`中，拼接字符串变得更加高效和灵活，但是生成新的字符串需要重新构建`StringBuilder`对象，造成性能损耗和内存浪费，因此Hutool提供了可复用的`StrBuilder`。

## 使用
`StrBuilder`和`StringBuilder`使用方法基本一致，只是多了`reset`方法可以重新构建一个新的字符串而不必开辟新内存。

```java
StrBuilder builder = StrBuilder.create();
builder.append("aaa").append("你好").append('r');
//结果：aaa你好r
```

## 多次构建字符串性能测试

我们模拟创建1000000次字符串对两者性能对比，采用`TimeInterval`计时：

```
//StringBuilder 
TimeInterval timer = DateUtil.timer();
StringBuilder b2 = new StringBuilder();
for(int i =0; i< 1000000; i++) {
	b2.append("test");
	b2 = new StringBuilder();
}
Console.log(timer.interval());
```

```
//StrBuilder
TimeInterval timer = DateUtil.timer();
StrBuilder builder = StrBuilder.create();
for(int i =0; i< 1000000; i++) {
	builder.append("test");
	builder.reset();
}
Console.log(timer.interval());
```

测试结果为：
```
StringBuilder: 39ms
StrBuilder   : 20ms
```

性能几乎翻倍。也欢迎用户自行测试。

