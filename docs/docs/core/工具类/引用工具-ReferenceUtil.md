## 介绍

引用工具类，主要针对Reference 工具化封装

主要封装包括： 

1. SoftReference 软引用，在GC报告内存不足时会被GC回收
2. WeakReference 弱引用，在GC时发现弱引用会回收其对象
3. PhantomReference 虚引用，在GC时发现虚引用对象，会将PhantomReference插入ReferenceQueue。此时对象未被真正回收，要等到ReferenceQueue被真正处理后才会被回收。

## 方法

### `create`

根据类型枚举创建引用。