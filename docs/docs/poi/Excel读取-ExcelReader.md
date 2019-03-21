## 介绍
读取Excel内容的封装，通过构造ExcelReader对象，指定被读取的Excel文件、流或工作簿，然后调用readXXX方法读取内容为指定格式。

## 使用

1. 读取Excel中所有行和列，都用列表表示
```java
ExcelReader reader = ExcelUtil.getReader("d:/aaa.xlsx");
List<List<Object>> readAll = reader.read();
```

2. 读取为Map列表，默认第一行为标题行，Map中的key为标题，value为标题对应的单元格值。
```java
ExcelReader reader = ExcelUtil.getReader("d:/aaa.xlsx");
List<Map<String,Object>> readAll = reader.readAll();
```

3. 读取为Bean列表，Bean中的字段名为标题，字段值为标题对应的单元格值。
```java
ExcelReader reader = ExcelUtil.getReader("d:/aaa.xlsx");
List<Person> all = reader.readAll(Person.class);
```