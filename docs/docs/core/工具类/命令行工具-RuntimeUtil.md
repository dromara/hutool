## 介绍
在Java世界中，如果想与其它语言打交道，处理调用接口，或者JNI，就是通过本地命令方式调用了。Hutool封装了JDK的Process类，用于执行命令行命令（在Windows下是cmd，在Linux下是shell命令）。

## 方法
### 基础方法
1. `exec` 执行命令行命令，返回Process对象，Process可以读取执行命令后的返回内容的流

### 快捷方法
1. `execForStr` 执行系统命令，返回字符串
2. `execForLines` 执行系统命令，返回行列表

## 使用

```java
String str = RuntimeUtil.execForStr("ipconfig");
```

执行这个命令后，在Windows下可以获取网卡信息。