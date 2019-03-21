## 介绍

针对 `java.lang.reflect.Type` 的工具类封装，最主要功能包括：

1. 获取方法的参数和返回值类型（包括Type和Class）
2. 获取泛型参数类型（包括对象的泛型参数或集合元素的泛型类型）

## 方法

首先我们定义一个类：

```java
public class TestClass {
	public List<String> getList(){
		return new ArrayList<>();
	}
	
	public Integer intTest(Integer integer) {
		return 1;
	}
}
```

### `getClass`

获得Type对应的原始类

### `getParamType`

```java
Method method = ReflectUtil.getMethod(TestClass.class, "intTest", Integer.class);
Type type = TypeUtil.getParamType(method, 0);
// 结果：Integer.class
```

获取方法参数的泛型类型

### `getReturnType`

获取方法的返回值类型

```java
Method method = ReflectUtil.getMethod(TestClass.class, "getList");
Type type = TypeUtil.getReturnType(method);
// 结果：java.util.List<java.lang.String>
```

### `getTypeArgument`

获取泛型类子类中泛型的填充类型。

```java
Method method = ReflectUtil.getMethod(TestClass.class, "getList");
Type type = TypeUtil.getReturnType(method);

Type type2 = TypeUtil.getTypeArgument(type);
// 结果：String.class
```