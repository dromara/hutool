中文分词封装-TokenizerUtil
===

## 介绍

现阶段，应用于搜索引擎和自然语言处理的中文分词库五花八门，使用方式各不统一，虽然有适配于Lucene和Elasticsearch的插件，但是我们想在多个库之间选择更换时，依旧有学习时间。

Hutool针对常见中文分词库做了统一接口封装，既定义一套规范，隔离各个库的差异，做到一段代码，随意更换。

Hutool现在封装的引擎有：

- [Ansj](https://github.com/NLPchina/ansj_seg)
- [HanLP](https://github.com/hankcs/HanLP)
- [IKAnalyzer](https://github.com/yozhao/IKAnalyzer)
- [Jcseg](https://gitee.com/lionsoul/jcseg)
- [Jieba](https://github.com/huaban/jieba-analysis)
- [mmseg4j](https://github.com/chenlb/mmseg4j-core)
- [Word](https://github.com/ysc/word)
- [Smartcn](https://github.com/chenlb/mmseg4j-core)

> 注意
> 此工具和模块从Hutool-4.4.0开始支持。

## 原理

类似于Java日志门面的思想，Hutool将分词引擎的渲染抽象为三个概念：

- TokenizerEngine 分词引擎，用于封装分词库对象
- Result 分词结果接口定义，用于抽象对文本分词的结果，实现了Iterator和Iterable接口，用于遍历分词
- Word 表示分词中的一个词，既分词后的单词，可以获取单词文本、起始位置和结束位置等信息

通过实现这三个接口，用户便可抛开分词库的差异，实现多文本分词。

Hutool同时会通过`TokenizerFactory`**根据用户引入的分词库的jar来自动选择用哪个库实现分词**。

## 使用

### 解析文本并分词

```java
//自动根据用户引入的分词库的jar来自动选择使用的引擎
TokenizerEngine engine = TokenizerUtil.createEngine();

//解析文本
String text = "这两个方法的区别在于返回值";
Result result = engine.parse(text);
//输出：这 两个 方法 的 区别 在于 返回 值
String resultStr = CollUtil.join((Iterator<Word>)result, " ");
```

当你引入Ansj，会自动路由到Ansi的库去实现分词，引入HanLP则会路由到HanLP，依此类推。

也就是说，使用Hutool之后，无论你用任何一种分词库，代码不变。

### 自定义模板引擎

此处以HanLP为例：

```java
TokenizerEngine engine = new HanLPEngine();

//解析文本
String text = "这两个方法的区别在于返回值";
Result result = engine.parse(text);
//输出：这 两个 方法 的 区别 在于 返回 值
String resultStr = CollUtil.join((Iterator<Word>)result, " ");
```