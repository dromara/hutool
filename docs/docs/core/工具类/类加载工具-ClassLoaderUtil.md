## 介绍
提供ClassLoader相关的工具类，例如类加载（Class.forName包装）等

## 方法

### 获取ClassLoader

#### `getContextClassLoader` 

获取当前线程的ClassLoader，本质上调用`Thread.currentThread().getContextClassLoader()`

#### `getClassLoader`

按照以下顺序规则查找获取ClassLoader：

1. 获取当前线程的ContextClassLoader
2. 获取ClassLoaderUtil类对应的ClassLoader
3. 获取系统ClassLoader（ClassLoader.getSystemClassLoader()）

### 加载Class

#### `loadClass`

加载类，通过传入类的字符串，返回其对应的类名，使用默认ClassLoader并初始化类（调用static模块内容和可选的初始化static属性）

扩展`Class.forName`方法，支持以下几类类名的加载：

1. 原始类型，例如：int
2. 数组类型，例如：int[]、Long[]、String[]
3. 内部类，例如：java.lang.Thread.State会被转为java.lang.Thread$State加载

同时提供`loadPrimitiveClass`方法用于快速加载原始类型的类。包括原始类型、原始类型数组和void

#### `isPresent`

指定类是否被提供，通过调用`loadClass`方法尝试加载指定类名的类，如果加载失败返回false。

加载失败的原因可能是此类不存在或其关联引用类不存在。