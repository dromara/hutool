切面代理工具-ProxyUtil
===

## 使用

### 使用JDK的动态代理实现切面

1. 我们定义一个接接口：

```java
public interface Animal{
	void eat();
}
```

2. 定义一个实现类：

```java
public class Cat implements Animal{

	@Override
	public void eat() {
		Console.log("猫吃鱼");
	}
	
}
```

3. 我们使用`TimeIntervalAspect`这个切面代理上述对象，来统计猫吃鱼的执行时间：

```java
Animal cat = ProxyUtil.proxy(new Cat(), TimeIntervalAspect.class);
cat.eat();
```

`TimeIntervalAspect`位于`cn.hutool.aop.aspects`包，继承自`SimpleAspect`，代码如下：

```java
public class TimeIntervalAspect extends SimpleAspect{
	//TimeInterval为Hutool实现的一个计时器
	private TimeInterval interval = new TimeInterval();

	@Override
	public boolean before(Object target, Method method, Object[] args) {
		interval.start();
		return true;
	}
	
	@Override
	public boolean after(Object target, Method method, Object[] args) {
		Console.log("Method [{}.{}] execute spend [{}]ms", target.getClass().getName(), method.getName(), interval.intervalMs());
		return true;
	}
}
```

执行结果为：
```
猫吃鱼
Method [cn.hutool.aop.test.AopTest$Cat.eat] execute spend [16]ms
```

> 在调用proxy方法后，IDE自动补全返回对象为Cat，因为JDK机制的原因，我们的返回值必须是被代理类实现的接口，因此需要手动将返回值改为**Animal**，否则会报类型转换失败。

### 使用Cglib实现切面

使用Cglib的好处是无需定义接口即可对对象直接实现切面，使用方式完全一致：

1. 引入Cglib依赖

```
<dependency>
	<groupId>cglib</groupId>
	<artifactId>cglib</artifactId>
	<version>3.2.7</version>
</dependency>
```

2. 定义一个无接口类（此类有无接口都可以）

```java
public class Dog {
	public String eat() {
		Console.log("狗吃肉");
	}
}
```

```java
Dog dog = ProxyUtil.proxy(new Dog(), TimeIntervalAspect.class);
String result = dog.eat();
```

执行结果为：
```
狗吃肉
Method [cn.hutool.aop.test.AopTest$Dog.eat] execute spend [13]ms
```

## 其它方法
ProxyUtil中还提供了一些便捷的Proxy方法封装，例如newProxyInstance封装了Proxy.newProxyInstance方法，提供泛型返回值，并提供更多参数类型支持。

## 原理

动态代理对象的创建原理是假设创建的代理对象名为 $Proxy0：

1. 根据传入的interfaces动态生成一个类，实现interfaces中的接口
2. 通过传入的classloder将刚生成的类加载到jvm中。即将$Proxy0类load
3. 调用$Proxy0的$Proxy0(InvocationHandler)构造函数 创建$Proxy0的对象，并且用interfaces参数遍历其所有接口的方法，并生成实现方法，这些实现方法的实现本质上是通过反射调用被代理对象的方法。
4. 将$Proxy0的实例返回给客户端。 
5. 当调用代理类的相应方法时，相当于调用 InvocationHandler.invoke(Object, Method, Object []) 方法。

