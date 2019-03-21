## 由来
在文本处理中，正则表达式几乎是全能的，但是Java的正则表达式有时候处理一些事情还是有些繁琐，所以我封装了部分常用功能。就比如说我要匹配一段文本中的某些部分，我们需要这样做：

```Java
Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
Matcher matcher = pattern.matcher(content);
if (matcher.find()) {
    String result= matcher.group();
}
```

其中牵涉到多个对象，想用的时候真心记不住。好吧，既然功能如此常用，我就封装一下：

```Java
/**
* 获得匹配的字符串
* 
* @param pattern 编译后的正则模式
* @param content 被匹配的内容
* @param groupIndex 匹配正则的分组序号
* @return 匹配后得到的字符串，未匹配返回null
*/
public static String get(Pattern pattern, String content, int groupIndex) {
    Matcher matcher = pattern.matcher(content);
    if (matcher.find()) {
        return matcher.group(groupIndex);
    }
    return null;
}

/**
* 获得匹配的字符串
* 
* @param regex 匹配的正则
* @param content 被匹配的内容
* @param groupIndex 匹配正则的分组序号
* @return 匹配后得到的字符串，未匹配返回null
*/
public static String get(String regex, String content, int groupIndex) {
    Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
    return get(pattern, content, groupIndex);
}
```

## 使用

### ReUtil.extractMulti
抽取多个分组然后把它们拼接起来

```java
String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
Assert.assertEquals("Z-a", resultExtractMulti);
```

### ReUtil.delFirst
删除第一个匹配到的内容

```java
String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
Assert.assertEquals("ZZbbbccc中文1234", resultDelFirst);
```

### ReUtil.findAll
查找所有匹配文本

```java
List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<String>());
ArrayList<String> expected = CollectionUtil.newArrayList("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
Assert.assertEquals(expected, resultFindAll);
```

### ReUtil.getFirstNumber
找到匹配的第一个数字

```java
Integer resultGetFirstNumber = ReUtil.getFirstNumber(content);
Assert.assertEquals(Integer.valueOf(1234), resultGetFirstNumber);
```

### ReUtil.isMatch
给定字符串是否匹配给定正则

```java
boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
Assert.assertTrue(isMatch);
```

### ReUtil.replaceAll
通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串

```java
//此处把1234替换为 ->1234<-
String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
Assert.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
```

### ReUtil.escape
转义给定字符串，为正则相关的特殊符号转义

```java
String escape = ReUtil.escape("我有个$符号{}");
Assert.assertEquals("我有个\\$符号\\{\\}", escape);
```