## 介绍

对于大量数据输出，采用`ExcelWriter`容易引起内存溢出，因此有了`BigExcelWriter`，使用方法与`ExcelWriter`完全一致。

## 使用

```java
List<?> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

BigExcelWriter writer= ExcelUtil.getBigWriter("e:/xxx.xlsx");
// 一次性写出内容，使用默认样式
writer.write(rows);
// 关闭writer，释放内存
writer.close();
```