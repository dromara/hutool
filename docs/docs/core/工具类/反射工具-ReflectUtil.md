## 介绍
Java的反射机制，可以让语言变得更加灵活，对对象的操作也更加“动态”，因此在某些情况下，反射可以做到事半功倍的效果。Hutool针对Java的反射机制做了工具化封装，封装包括：

1. 获取构造方法
2. 获取字段
3. 获取字段值
4. 获取方法
5. 执行方法（对象方法和静态方法）

## 使用

### 获取某个类的所有方法

```java
Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
```

### 获取某个类的指定方法
```java
Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
```

### 构造对象

```java
ReflectUtil.newInstance(ExamInfoDict.class);
```

### 执行方法

```java
class TestClass {
	private int a;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}
```

```java
TestClass testClass = new TestClass();
ReflectUtil.invoke(testClass, "setA", 10);
```