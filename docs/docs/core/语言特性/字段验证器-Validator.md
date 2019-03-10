## 作用
验证给定字符串是否满足指定条件，一般用在表单字段验证里。

此类中全部为静态方法。

## 使用

### 判断验证
直接调用`Validator.isXXX(String value)`既可验证字段，返回是否通过验证。

例如：

```Java
boolean isEmail = Validator.isEmail("loolly@gmail.com")
```

表示验证给定字符串是否复合电子邮件格式。

其他验证信息请参阅`Validator`类

如果Validator里的方法无法满足自己的需求，那还可以调用

```
Validator.isMactchRegex("需要验证字段的正则表达式", "被验证内容")
```

来通过正则表达式灵活的验证内容。

### 异常验证
除了手动判断，我们有时需要在判断未满足条件时抛出一个异常，Validator同样提供异常验证机制：

```java
Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
```

因为内容中包含非中文字符，因此会抛出ValidateException。