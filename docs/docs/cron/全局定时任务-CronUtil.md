全局定时任务-CronUtil
===

## 介绍

CronUtil通过一个全局的定时任务配置文件，实现统一的定时任务调度。

## 使用

### 1、配置文件

对于Maven项目，首先在`src/main/resources/config`下放入cron.setting文件（默认是这个路径的这个文件），然后在文件中放入定时规则，规则如下：

```    
# 我是注释
[com.company.aaa.job]
TestJob.run = */10 * * * *
TestJob2.run = */10 * * * *
```

中括号表示分组，也表示需要执行的类或对象方法所在包的名字，这种写法有利于区分不同业务的定时任务。

`TestJob.run`表示需要执行的类名和方法名（通过反射调用），`*/10 * * * *`表示定时任务表达式，此处表示每10分钟执行一次，以上配置等同于：

```
com.company.aaa.job.TestJob.run = */10 * * * *
com.company.aaa.job.TestJob2.run = */10 * * * *
```

> 提示
> 关于表达式语法，见：[http://www.cnblogs.com/peida/archive/2013/01/08/2850483.html](http://www.cnblogs.com/peida/archive/2013/01/08/2850483.html)

### 2、启动

```java
CronUtil.start();
```

如果想让执行的作业同定时任务线程同时结束，可以将定时任务设为守护线程，需要注意的是，此模式下会在调用stop时立即结束所有作业线程，请确保你的作业可以被中断：

```java
//使用deamon模式，
CronUtil.start(true);
```
### 3、关闭

```java
CronUtil.stop();
```

## 更多选项

### 秒匹配和年匹配

考虑到Quartz表达式的兼容性，且存在对于秒级别精度匹配的需求，Hutool可以通过设置使用秒匹配模式来兼容。

```java
//支持秒级别定时任务
CronUtil.setMatchSecond(true);
```

此时Hutool可以兼容Quartz表达式（5位表达式、6位表达式都兼容）

### 动态添加定时任务

当然，如果你想动态的添加定时任务，使用`CronUtil.schedule(String schedulingPattern, Runnable task)`方法即可（使用此方法加入的定时任务不会被写入到配置文件）。

```java
CronUtil.schedule("*/2 * * * * *", new Task() {
	@Override
	public void execute() {
		Console.log("Task excuted.");
	}
});

// 支持秒级别定时任务
CronUtil.setMatchSecond(true);
CronUtil.start();
```

