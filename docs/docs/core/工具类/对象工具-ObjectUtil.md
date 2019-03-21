## 由来

在我们的日常使用中，有些方法是针对Object通用的，这些方法不区分何种对象，针对这些方法，Hutool封装为`ObjectUtil`。

## 方法

### `ObjectUtil.equal`
比较两个对象是否相等，相等需满足以下条件之一：
1. obj1 == null && obj2 == null
2. obj1.equals(obj2)

### `ObjectUtil.length`
计算对象长度，如果是字符串调用其length方法，集合类调用其size方法，数组调用其length属性，其他可遍历对象遍历计算长度。

支持的类型包括：
- CharSequence
- Collection
- Map
- Iterator
- Enumeration
- Array

### `ObjectUtil.contains`
对象中是否包含元素。

支持的对象类型包括：
- String
- Collection
- Map
- Iterator
- Enumeration
- Array

### 判断是否为null
- `ObjectUtil.isNull`
- `ObjectUtil.isNotNull`

### 克隆

- `ObjectUtil.clone` 克隆对象，如果对象实现Cloneable接口，调用其clone方法，如果实现Serializable接口，执行深度克隆，否则返回`null`。

- `ObjectUtil.cloneIfPossible` 返回克隆后的对象，如果克隆失败，返回原对象

- `ObjectUtil.cloneByStream` 序列化后拷贝流的方式克隆，对象必须实现Serializable接口

### 序列化和反序列化

- `serialize` 序列化，调用JDK序列化
- `unserialize`  反序列化，调用JDK

### 判断基本类型

`ObjectUtil.isBasicType` 判断是否为基本类型，包括包装类型和非包装类型。