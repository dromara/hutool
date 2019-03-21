## 由来

在日常编码中，我们接触最多的除了JSON外，就是XML格式了，一般而言，我们首先想到的是引入Dom4j包，却不知JDK已经封装有XML解析和构建工具：w3c dom。但是由于这个API操作比较繁琐，因此Hutool中提供了XmlUtil简化XML的创建、读和写的过程。


## 使用

### 读取XML

读取XML分为两个方法：

- `XmlUtil.readXML` 读取XML文件
- `XmlUtil.parseXml` 解析XML字符串为Document对象

### 写XML

- `XmlUtil.toStr` 将XML文档转换为String
- `XmlUtil.toFile` 将XML文档写入到文件

### 创建XML

- `XmlUtil.createXml` 创建XML文档, 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码。

### XML操作

通过以下工具方法，可以完成基本的节点读取操作。

- `XmlUtil.cleanInvalid` 除XML文本中的无效字符
- `XmlUtil.getElements` 根据节点名获得子节点列表
- `XmlUtil.getElement` 根据节点名获得第一个子节点
- `XmlUtil.elementText` 根据节点名获得第一个子节点
- `XmlUtil.transElements` 将NodeList转换为Element列表

### XML与对象转换

- `writeObjectAsXml` 将可序列化的对象转换为XML写入文件，已经存在的文件将被覆盖。
- `readObjectFromXml` 从XML中读取对象。

> 注意
> 这两个方法严重依赖JDK的`XMLEncoder`和`XMLDecoder`，生成和解析必须成对存在（遵循固定格式），普通的XML转Bean会报错。

### Xpath操作
Xpath的更多介绍请看文章：[https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html](https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html)

- `createXPath` 创建XPath
- `getByXPath` 通过XPath方式读取XML节点等信息

栗子：

```xml
<?xml version="1.0" encoding="utf-8"?>

<returnsms> 
  <returnstatus>Success（成功）</returnstatus>  
  <message>ok</message>  
  <remainpoint>1490</remainpoint>  
  <taskID>885</taskID>  
  <successCounts>1</successCounts> 
</returnsms>
```

```java
Document docResult=XmlUtil.readXML(xmlFile);
//结果为“ok”
Object value = XmlUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
```

## 总结

XmlUtil只是w3c dom的简单工具化封装，减少操作dom的难度，如果项目对XML依赖较大，依旧推荐Dom4j框架。