Emoji工具-EmojiUtil
===

## 由来
考虑到MySQL等数据库中普通的UTF8编码并不支持Emoji（只有utf8mb4支持），因此对于数据中的Emoji字符进行处理（转换、清除）变成一项必要工作。因此Hutool基于`emoji-java`库提供了Emoji工具实现。

此工具在Hutoo-4.2.1之后版本可用。

## 使用

### 加入依赖
```
<dependency>
	<groupId>com.vdurmont</groupId>
	<artifactId>emoji-java</artifactId>
	<version>4.0.0</version>
</dependency>
```

### 使用

1. 转义Emoji字符

```java
String alias = EmojiUtil.toAlias("😄");//:smile:
```

2. 将转义的别名转为Emoji字符

```
String emoji = EmojiUtil.toUnicode(":smile:");//😄
```

3. 将字符串中的Unicode Emoji字符转换为HTML表现形式

```
String alias = EmojiUtil.toHtml("😄");//&#128102;
```