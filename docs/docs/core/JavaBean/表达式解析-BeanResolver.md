表达式解析-BeanResolver
===

## 由来

很多JavaBean嵌套有很多层对象，这其中还夹杂着Map、Collection等对象，因此获取太深的嵌套对象会让代码变得冗长不堪。因此我们可以考虑使用一种表达式还获取指定深度的对象，于是BeanResolver应运而生。

## 原理

通过传入一个表达式，按照表达式的规则获取bean下指定的对象。

表达式分为两种：
- `.`表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值
- `[]`表达式，可以获取集合等对象中对应index的值

栗子：
1. `persion` 获取Bean对象下person字段的值，或者Bean本身如果是Person对象，返回本身。
2. `persion.name` 获取Bean中person字段下name字段的值，或者Bean本身如果是Person对象，返回其name字段的值。
3. `persons[3]` 获取persons字段下第三个元素的值（假设person是数组或Collection对象）
4. `person.friends[5].name` 获取person字段下friends列表（或数组）的第5个元素对象的name属性

## 使用

由于嵌套Bean定义过于复杂，在此我们省略，有兴趣的可以看下这里：cn.hutool.core.lang.test.bean（src/test/java下）下定义了测试用例用的bean。

首先我们创建这个复杂的Bean（实际当中这个复杂的Bean可能是从数据库中获取，或者从JSON转入）

这个复杂Bean的关系是这样的：

定义一个Map包含用户信息（UserInfoDict）和一个标志位（flag），用户信息包括一些基本信息和一个考试信息列表（ExamInfoDict）。

```java
//------------------------------------------------- 考试信息列表
ExamInfoDict examInfoDict = new ExamInfoDict();
examInfoDict.setId(1);
examInfoDict.setExamType(0);
examInfoDict.setAnswerIs(1);

ExamInfoDict examInfoDict1 = new ExamInfoDict();
examInfoDict1.setId(2);
examInfoDict1.setExamType(0);
examInfoDict1.setAnswerIs(0);

ExamInfoDict examInfoDict2 = new ExamInfoDict();
examInfoDict2.setId(3);
examInfoDict2.setExamType(1);
examInfoDict2.setAnswerIs(0);

List<ExamInfoDict> examInfoDicts = new ArrayList<ExamInfoDict>();
examInfoDicts.add(examInfoDict);
examInfoDicts.add(examInfoDict1);
examInfoDicts.add(examInfoDict2);

//------------------------------------------------- 用户信息
UserInfoDict userInfoDict = new UserInfoDict();
userInfoDict.setId(1);
userInfoDict.setPhotoPath("yx.mm.com");
userInfoDict.setRealName("张三");
userInfoDict.setExamInfoDict(examInfoDicts);

Map<String, Object> tempMap = new HashMap<String, Object>();
tempMap.put("userInfo", userInfoDict);
tempMap.put("flag", 1);
```

下面，我们使用`BeanResolver`获取这个Map下此用户第一门考试的ID：

```java
BeanResolver resolver = new BeanResolver(tempMap, "userInfo.examInfoDict[0].id");
Object result = resolver.resolve();//ID为1
```

只需两句（甚至一句）即可完成复杂Bean中各层次对象的获取。

> 说明：
> 为了简化`BeanResolver`的使用，Hutool在BeanUtil中也加入了快捷入口方法：`BeanUtil.getProperty`，这个方法的命名更容易理解（毕竟BeanResolver不但可以解析Bean，而且可以解析Map和集合）。

