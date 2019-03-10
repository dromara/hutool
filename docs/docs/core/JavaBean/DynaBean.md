DynaBean
===

## 介绍

DynaBean是使用反射机制动态操作JavaBean的一个封装类，通过这个类，可以通过字符串传入name方式动态调用get和set方法，也可以动态创建JavaBean对象，亦或者执行JavaBean中的方法。

## 使用

我们先定义一个JavaBean：

```java
public static class User{
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String testMethod(){
		return "test for " + this.name;
	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
}
```

### 创建

```java
DynaBean bean = DynaBean.create(user);
//我们也可以通过反射构造对象
DynaBean bean2 = DynaBean.create(User.class);
```

### 操作

我们通过DynaBean来包装并操作这个Bean

```java
User user = new User();
DynaBean bean = DynaBean.create(user);
bean.set("name", "李华");
bean.set("age", 12);

String name = bean.get("name");//输出“李华”
```
这样我们就可以像操作Map一样动态操作JavaBean

### invoke

除了标准的get和set方法，也可以调用invoke方法执行对象中的任意方法：

```java
//执行指定方法
Object invoke = bean2.invoke("testMethod");
Assert.assertEquals("test for 李华", invoke);
```

>说明:
> DynaBean默认实现了hashCode、equals和toString三个方法，这三个方法也是默认调用原对象的相应方法操作。

