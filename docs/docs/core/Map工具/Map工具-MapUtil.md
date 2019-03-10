Map工具-MapUtil
===

## 介绍
MapUtil是针对Map的一一列工具方法的封装，包括getXXX的快捷值转换方法。

## 方法

- `isEmpty`、`isNotEmpty` 判断Map为空和非空方法，空的定义为null或没有值
- `newHashMap` 快速创建多种类型的HashMap实例
- `createMap` 创建自定义的Map类型的Map
- `of` 此方法将一个或多个键值对加入到一个新建的Map中，下面是栗子:

```java
Map<Object, Object> colorMap = MapUtil.of(new String[][] {{
     {"RED", "#FF0000"},
     {"GREEN", "#00FF00"},
     {"BLUE", "#0000FF"}
});
```

- `toListMap` 行转列，合并相同的键，值合并为列表，将Map列表中相同key的值组成列表做为Map的value，例如传入数据是：

```java
[
  {a: 1, b: 1, c: 1}
  {a: 2, b: 2}
  {a: 3, b: 3}
  {a: 4}
]
```

结果为：

```java
{
   a: [1,2,3,4]
   b: [1,2,3,]
   c: [1]
}
```

- `toMapList` 列转行。将Map中值列表分别按照其位置与key组成新的map，例如传入数据：

```java
{
   a: [1,2,3,4]
   b: [1,2,3,]
   c: [1]
}
```

结果为：
```java
[
  {a: 1, b: 1, c: 1}
  {a: 2, b: 2}
  {a: 3, b: 3}
  {a: 4}
]
```

- `join`、`joinIgnoreNull` 将Map按照给定的分隔符转换为字符串
- `filter` 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：1、过滤出需要的对象，如果返回null表示这个元素对象抛弃 2、修改元素对象，返回集合中为修改后的对象
- `reverse` Map的键和值互换
- `sort` 排序Map
- `getAny` 获取Map的部分key生成新的Map
- `get`、`getXXX` 获取Map中指定类型的值


