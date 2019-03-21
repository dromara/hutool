日志工厂-LogFactory
===

## 介绍
Hutool-log做为一个日志门面，为了兼容各大日志框架，一个用于自动创建日志对象的日志工厂类必不可少。

`LogFactory`类用于灵活的创建日志对象，通过static方法创建我们需要的日志，主要功能如下：

- `LogFactory.get` 自动识别引入的日志框架，从而创建对应日志框架的门面Log对象（此方法创建一次后，下次再次get会根据传入类名缓存Log对象，对于每个类，Log对象都是单例的），同时自动识别当前类，将当前类做为类名传入日志框架。

- `LogFactory.createLog` 与get方法作用类似。但是此方法调用后会每次创建一个新的Log对象。

- `LogFactory.setCurrentLogFactory` 自定义当前日志门面的日志实现类。当引入多个日志框架时，我们希望自定义所用的日志框架，调用此方法即可。需要注意的是，此方法为全局方法，在获取Log对象前只调用一次即可。

## 使用

### 获取当前类对应的Log对象：

```java
//推荐创建不可变静态类成员变量
private static final Log log = LogFactory.get();
```

如果你想获得自定义name的Log对象（像普通Log日志实现一样），那么可以使用如下方式获取Log：

```java
private static final Log log = LogFactory.get("我是一个自定义日志名");
```

### 自定义日志实现

```java
//自定义日志实现为Apache Commons Logging
LogFactory.setCurrentLogFactory(new ApacheCommonsLogFactory());

//自定义日志实现为JDK Logging
LogFactory.setCurrentLogFactory(new JdkLogFactory());

//自定义日志实现为Console Logging
LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
```

### 自定义日志工厂（自定义日志门面实现）

LogFactory是一个抽象类，我们可以继承此类，实现`createLog`方法即可（同时我们可能需要实现Log接口来达到自定义门面的目的），这样我们就可以自定义一个日志门面。最后通过`LogFactory.setCurrentLogFactory`方法装入这个自定义LogFactory即可实现自定义日志门面。


> PS
> 自定义日志门面的实现可以参考`cn.hutool.log.dialect`包中的实现内容自定义扩展。
> 本质上，实现Log接口，做一个日志实现的Wrapper，然后在相应的工厂类中创建此Log实例即可。同时，LogFactory中还可以初始化一些启动配置参数。

