DFA查找
===

## 使用

### 1. 构建关键词树
```java
WordTree tree = new WordTree();
tree.addWord("大");
tree.addWord("大土豆");
tree.addWord("土豆");
tree.addWord("刚出锅");
tree.addWord("出锅");
```

### 2. 查找关键词
```java
//正文
String text = "我有一颗大土豆，刚出锅的";
```

1. 情况一：标准匹配，匹配到最短关键词，并跳过已经匹配的关键词

```java
// 匹配到【大】，就不再继续匹配了，因此【大土豆】不匹配
// 匹配到【刚出锅】，就跳过这三个字了，因此【出锅】不匹配（由于刚首先被匹配，因此长的被匹配，最短匹配只针对第一个字相同选最短）
List<String> matchAll = tree.matchAll(text, -1, false, false);
Assert.assertEquals(matchAll.toString(), "[大, 土豆, 刚出锅]");
```

2. 情况二：匹配到最短关键词，不跳过已经匹配的关键词

```java
// 【大】被匹配，最短匹配原则【大土豆】被跳过，【土豆继续被匹配】
// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
matchAll = tree.matchAll(text, -1, true, false);
Assert.assertEquals(matchAll.toString(), "[大, 土豆, 刚出锅, 出锅]");
```

3. 情况三：匹配到最长关键词，跳过已经匹配的关键词

```java
// 匹配到【大】，由于到最长匹配，因此【大土豆】接着被匹配
// 由于【大土豆】被匹配，【土豆】被跳过，由于【刚出锅】被匹配，【出锅】被跳过
matchAll = tree.matchAll(text, -1, false, true);
Assert.assertEquals(matchAll.toString(), "[大, 大土豆, 刚出锅]");
```

4. 情况四：匹配到最长关键词，不跳过已经匹配的关键词（最全关键词）

```java
// 匹配到【大】，由于到最长匹配，因此【大土豆】接着被匹配，由于不跳过已经匹配的关键词，土豆继续被匹配
// 【刚出锅】被匹配，由于不跳过已经匹配的词，【出锅】被匹配
matchAll = tree.matchAll(text, -1, true, true);
Assert.assertEquals(matchAll.toString(), "[大, 大土豆, 土豆, 刚出锅, 出锅]");
```

> 除了`matchAll`方法，`WordTree`还提供了`match`和`isMatch`两个方法，这两个方法只会查找第一个匹配的结果，这样一旦找到第一个关键字，就会停止继续匹配，大大提高了匹配效率。

## 针对特殊字符

有时候，正文中的关键字常常包含特殊字符，比如:"〓关键☆字"，针对这种情况，Hutool提供了`StopChar`类，专门针对特殊字符做跳过处理，这个过程是在`match`方法或`matchAll`方法执行的时候自动去掉特殊字符。

