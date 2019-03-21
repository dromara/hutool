## 介绍
数组工具中的方法在2.x版本中都在CollectionUtil中存在，3.x之后版本（包括4.x版本）中拆分出来作为ArrayUtil。数组工具类主要针对原始类型数组和泛型数组相关方案进行封装。

数组工具类主要是解决对象数组（包括包装类型数组）和原始类型数组使用方法不统一的问题。

## 方法

### 判空
数组的判空类似于字符串的判空，标准是`null`或者数组长度为0，ArrayUtil中封装了针对原始类型和泛型数组的判空和判非空：

1. 判断空
```java
int[] a = {};
int[] b = null;
ArrayUtil.isEmpty(a);
ArrayUtil.isEmpty(b);
```

2. 判断非空
```java
int[] a = {1,2};
ArrayUtil.isNotEmpty(a);
```

### 新建泛型数组
`Array.newInstance`并不支持泛型返回值，在此封装此方法使之支持泛型返回值。

```java
String[] newArray = ArrayUtil.newArray(String.class, 3);
```

### 调整大小
使用 `ArrayUtil.resize`方法生成一个新的重新设置大小的数组。

### 合并数组
`ArrayUtil.addAll`方法采用可变参数方式，将多个泛型数组合并为一个数组。

### 克隆
数组本身支持clone方法，因此确定为某种类型数组时调用`ArrayUtil.clone(T[])`,不确定类型的使用`ArrayUtil.clone(T)`，两种重载方法在实现上有所不同，但是在使用中并不能感知出差别。

1. 泛型数组调用原生克隆
```java
Integer[] b = {1,2,3};
Integer[] cloneB = ArrayUtil.clone(b);
Assert.assertArrayEquals(b, cloneB);
```
2. 非泛型数组（原始类型数组）调用第二种重载方法
```java
int[] a = {1,2,3};
int[] clone = ArrayUtil.clone(a);
Assert.assertArrayEquals(a, clone);
```

### 有序列表生成
`ArrayUtil.range`方法有三个重载，这三个重载配合可以实现支持步进的有序数组或者步进为1的有序数组。这种列表生成器在Python中做为语法糖存在。

### 拆分数组
`ArrayUtil.split`方法用于拆分一个byte数组，将byte数组平均分成几等份，常用于消息拆分。

### 过滤
`ArrayUtil.filter`方法用于编辑已有数组元素，只针对泛型数组操作，原始类型数组并未提供。
方法中Editor接口用于返回每个元素编辑后的值，返回null此元素将被抛弃。

一个大栗子：过滤数组，只保留偶数
```java
Integer[] a = {1,2,3,4,5,6};
Integer[] filter = ArrayUtil.filter(a, new Editor<Integer>(){
	@Override
	public Integer edit(Integer t) {
		return (t % 2 == 0) ? t : null;
	}});
Assert.assertArrayEquals(filter, new Integer[]{2,4,6});
```

### zip
`ArrayUtil.zip`方法传入两个数组，第一个数组为key，第二个数组对应位置为value，此方法在Python中为zip()函数。

```java
String[] keys = {"a", "b", "c"};
Integer[] values = {1,2,3};
Map<String, Integer> map = ArrayUtil.zip(keys, values, true);

//{a=1, b=2, c=3}
```

### 是否包含元素
`ArrayUtil.contains`方法只针对泛型数组，检测指定元素是否在数组中。

### 包装和拆包
在原始类型元素和包装类型中，Java实现了自动包装和拆包，但是相应的数组无法实现，于是便是用`ArrayUtil.wrap`和`ArrayUtil.unwrap`对原始类型数组和包装类型数组进行转换。

### 判断对象是否为数组
`ArrayUtil.isArray`方法封装了`obj.getClass().isArray()`。

### 转为字符串

1. `ArrayUtil.toString` 通常原始类型的数组输出为字符串时无法正常显示，于是封装此方法可以完美兼容原始类型数组和包装类型数组的转为字符串操作。

2. `ArrayUtil.join` 方法使用间隔符将一个数组转为字符串，比如[1,2,3,4]这个数组转为字符串，间隔符使用“-”的话，结果为 1-2-3-4，join方法同样支持泛型数组和原始类型数组。

### toArray
`ArrayUtil.toArray`方法针对ByteBuffer转数组提供便利。
