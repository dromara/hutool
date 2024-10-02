<p align="center">
	<a href="https://hutool.cn/"><img src="https://plus.hutool.cn/images/hutool.svg" width="45%"></a>
</p>
<p align="center">
	<strong>🍬Make Java Sweet Again.</strong>
</p>
<p align="center">
	👉 <a href="https://hutool.cn">https://hutool.cn/</a> 👈
</p>

## 📚Hutool-jmh 模块介绍

`Hutool-jmh`提供性能对比测试

## 安装

### 引入依赖

```xml
<!-- 基准性能测试 -->
<dependency>
	<groupId>org.openjdk.jmh</groupId>
	<artifactId>jmh-core</artifactId>
	<version>${jmh.version}</version>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>org.openjdk.jmh</groupId>
	<artifactId>jmh-generator-annprocess</artifactId>
	<version>${jmh.version}</version>
	<scope>test</scope>
</dependency>
```

### 安装插件

IDEA安装`JMH Java Microbenchmark Harness`插件，主页见：https://plugins.jetbrains.com/plugin/7529-jmh-java-microbenchmark-harness

## 注解说明

### @BenchmarkMode
使用哪种模式测试，JMH 提供了4种不同的模式：
- `Mode.Throughput`  吞吐量，   ops/time。单位时间内执行操作的平均次数，例如“1秒内可以执行多少次调用”
- `Mode.AverageTime` 平均时间， time/op。 执行每次操作所需的平均时间，例如“每次调用平均耗时xxx毫秒”
- `Mode.SampleTime`  采样时间， time/op。 同 AverageTime。区别在于会统计取样分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
- `Mode.SingleShotTime` 单次时间， time。 同 AverageTime。区别在于只执行一次操作。这种模式的结果存在较大随机性，往往同时把 warmup 次数设为0，用于测试冷启动时的性能。

### @Warmup和 @Measurement
@Warmup 和@Measurement分别用于配置预热迭代和测试迭代。其中：
- iterations 用于指定迭代次数。
- time 和 timeUnit 用于每个迭代的时间。
- batchSize 表示执行多少次 Benchmark为一个invocation。

### @State
类注解，JMH 测试类必须使用 @State 注解，它定义了一个类实例的生命周期，有三种类型：
- `Scope.Thread`：每个线程一个实例，适合不共享数据的测试。
- `Scope.Benchmark`：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；
- `Scope.Group`：每个线程组共享一个实例；

### @Fork
进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。

### @Setup 和 @TearDown
方法注解。
- `@Setup`会在执行 benchmark 之前被执行，主要用于初始化。
- `@TearDown`会在执行 benchmark 之后被执行，主要用于资源的回收等。

### @Benchmark
方法注解，表示该方法是需要进行 benchmark 的对象。

### @OutputTimeUnit
输出的时间单位。

### @Param
成员注解，可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。@Param 注解接收一个String数组，在 @Setup 方法执行前转化为为对应的数据类型。
多个 @Param 注解的成员之间是乘积关系，譬如有两个用 @Param 注解的字段，第一个有5个值，第二个字段有2个值，那么每个测试方法会跑5*2=10次。

## 参考：

[如何在 Java 中使用 JMH 进行基准测试](https://segmentfault.com/a/1190000039902797)