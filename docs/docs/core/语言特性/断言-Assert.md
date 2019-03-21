## 由来
Java中有`assert`关键字，但是存在许多问题：
1. assert关键字需要在运行时候显式开启才能生效，否则你的断言就没有任何意义。
2. 用assert代替if是陷阱之二。assert的判断和if语句差不多，但两者的作用有着本质的区别：assert关键字本意上是为测试调试程序时使用的，但如果不小心用assert来控制了程序的业务流程，那在测试调试结束后去掉assert关键字就意味着修改了程序的正常的逻辑。
3. assert断言失败将面临程序的退出。

因此，并不建议使用此关键字。相应的，在Hutool中封装了更加友好的Assert类，用于断言判定。

## 介绍
Assert类更像是Junit中的Assert类，也很像Guava中的Preconditions，主要作用是在方法或者任何地方对参数的有效性做校验。当不满足断言条件时，会抛出IllegalArgumentException或IllegalStateException异常。

## 使用

```java
String a = null;
cn.hutool.lang.Assert.isNull(a);
```

## 更多方法
- isTrue 是否True
- isNull 是否是null值，不为null抛出异常
- notNull 是否非null值
- notEmpty 是否非空
- notBlank 是否非空白符
- notContain 是否为子串
- notEmpty 是否非空
- noNullElements 数组中是否包含null元素
- isInstanceOf 是否类实例
- isAssignable 是子类和父类关系
- state 会抛出IllegalStateException异常