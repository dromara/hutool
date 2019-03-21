## 由来
分页工具类并不是数据库分页的封装，而是分页方式的转换。在我们手动分页的时候，常常使用页码+每页个数的方式，但是有些数据库需要使用开始位置和结束位置来表示。很多时候这种转换容易出错（边界问题），于是封装了PageUtil工具类。

## 使用

### transToStartEnd
将页数和每页条目数转换为开始位置和结束位置。
此方法用于不包括结束位置的分页方法。

例如：
- 页码：1，每页10 -> [0, 10]
- 页码：2，每页10 -> [10, 20]

```java
int[] startEnd1 = PageUtil.transToStartEnd(1, 10);//[0, 10]
int[] startEnd2 = PageUtil.transToStartEnd(2, 10);//[10, 20]
```

> 方法中，页码从1开始，位置从0开始

### totalPage

根据总数计算总页数

```java
int totalPage = PageUtil.totalPage(20, 3);//7
```

### 分页彩虹算法

此方法来自：https://github.com/iceroot/iceroot/blob/master/src/main/java/com/icexxx/util/IceUtil.java

在页面上显示下一页时，常常需要显示前N页和后N页，`PageUtil.rainbow`作用于此。

例如我们当前页为第5页，共有20页，只显示6个页码，显示的分页列表应为：

```
上一页 3 4 [5] 6 7 8 下一页
```

```java
//参数意义分别为：当前页、总页数、每屏展示的页数
int[] rainbow = PageUtil.rainbow(5, 20, 6);
//结果：[3, 4, 5, 6, 7, 8]
```
