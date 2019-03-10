注解工具-AnnotationUtil
===

## 介绍

封装了注解获取等方法的工具类。

## 使用

### 方法介绍

1. 注解获取相关方法：

- `getAnnotations` 获取指定类、方法、字段、构造等上的注解列表
- `getAnnotation` 获取指定类型注解
- `getAnnotationValue` 获取指定注解属性的值

2. 注解属性获取相关方法：

- `getRetentionPolicy` 获取注解类的保留时间，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS

- `getTargetType` 获取注解类可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER 等

- `isDocumented` 是否会保存到 Javadoc 文档中
- `isInherited` 是否可以被继承，默认为 false

更多方法见API文档：

[https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/annotation/AnnotationUtil.html](https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/annotation/AnnotationUtil.html)

