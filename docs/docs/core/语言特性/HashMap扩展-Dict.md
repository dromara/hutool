## 由来

如果你了解Python，你一定知道Python有dict这一数据结构，也是一种KV（Key-Value）结构的数据结构，类似于Java中的Map，但是提供了更加灵活多样的使用。Hutool中的Dict对象旨在实现更加灵活的KV结构，针对强类型，提供丰富的getXXX操作，将HashMap扩展为无类型区别的数据结构。

## 介绍

Dict继承HashMap，其key为String类型，value为Object类型，通过实现`BasicTypeGetter`接口提供针对不同类型的get方法，同时提供针对Bean的转换方法，大大提高Map的灵活性。

> Hutool-db中Entity是Dict子类，做为数据的媒介。

## 使用
### 创建

```java
Dict dict = Dict.create()
	.set("key1", 1)//int
	.set("key2", 1000L)//long
	.set("key3", DateTime.now());//Date
```

通过链式构造，创建Dict对象，同时可以按照Map的方式使用。

### 获取指定类型的值

```java
Long v2 = dict.getLong("key2");//1000
```