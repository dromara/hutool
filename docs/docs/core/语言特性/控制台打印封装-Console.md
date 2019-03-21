## 由来

编码中我们常常需要调试输出一些信息，除了打印日志，最长用的要数`System.out`和`System.err`

比如我们打印一个Hello World，可以这样写：

```java
System.out.println("Hello World");
```

但是面对纷杂的打印需求，`System.out.println`无法满足，比如：
1. 不支持参数，对象打印需要拼接字符串
2. 不能直接打印数组，需要手动调用`Arrays.toString`

考虑到以上问题，我封装了`Console`对象。

> Console对象的使用更加类似于Javascript的`console.log()`方法，这也是借鉴了JS的一个语法糖。

## 使用

1. `Console.log` 这个方法基本等同于`System.out.println`,但是支持类似于Slf4j的字符串模板语法，同时也会自动将对象（包括数组）转为字符串形式。

```java
String[] a = {"abc", "bcd", "def"};
Console.log(a);//控制台输出：[abc, bcd, def]
```

```java
Console.log("This is Console log for {}.", "test");
//控制台输出：This is Console log for test.
```

2. `Console.error` 这个方法基本等同于`System.err.println`，,但是支持类似于Slf4j的字符串模板语法，同时也会自动将对象（包括数组）转为字符串形式。