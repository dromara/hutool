## 类处理工具 `ClassUtil`
这个工具主要是封装了一些反射的方法，使调用更加方便。而这个类中最有用的方法是`scanPackage`方法，这个方法会扫描classpath下所有类，这个在Spring中是特性之一，主要为[Hulu](https://github.com/looly/hulu)框架中类扫描的一个基础。下面介绍下这个类中的方法。

### `getShortClassName`
获取完整类名的短格式如：`cn.hutool.core.util.StrUtil` -> `c.h.c.u.StrUtil`

### `isAllAssignableFrom`
比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回true

### `isPrimitiveWrapper`
是否为包装类型

### `isBasicType`
是否为基本类型（包括包装类和原始类）

### `getPackage`
获得给定类所在包的名称，例如：
`cn.hutool.util.ClassUtil` -> `cn.hutool.util`

### `scanPackage`方法
此方法唯一的参数是包的名称，返回结果为此包以及子包下所有的类。方法使用很简单，但是过程复杂一些，包扫面首先会调用 `getClassPaths`方法获得ClassPath，然后扫描ClassPath，如果是目录，扫描目录下的类文件，或者jar文件。如果是jar包，则直接从jar包中获取类名。这个方法的作用显而易见，就是要找出所有的类，在Spring中用于依赖注入，我在[Hulu](https://github.com/looly/hulu)中则用于找到Action类。当然，你也可以传一个`ClassFilter`对象，用于过滤不需要的类。

### `getClassPaths`方法
此方法是获得当前线程的ClassPath，核心是`Thread.currentThread().getContextClassLoader().getResources`的调用。

### `getJavaClassPaths`方法
此方法用于获得java的系统变量定义的ClassPath。

### `getClassLoader`和`getContextClassLoader`方法
后者只是获得当前线程的ClassLoader，前者在获取失败的时候获取`ClassUtil`这个类的ClassLoader。

### `getDefaultValue`
获取指定类型分的默认值，默认值规则为：
1. 如果为原始类型，返回0
2. 非原始类型返回null

### 其它

更多详细的方法描述见：

[https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/util/ClassUtil.html](https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/util/ClassUtil.html)