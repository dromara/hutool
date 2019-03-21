系统属性调用-SystemUtil
===

## 概述

此工具是针对`System.getProperty(name)`的封装，通过此工具，可以获取如下信息：

### Java Virtual Machine Specification信息

```java
SystemUtil.getJvmSpecInfo();
```

### Java Virtual Machine Implementation信息

```java
SystemUtil.getJvmInfo();
```

### Java Specification信息

```java
SystemUtil.getJavaSpecInfo();
```

### Java Implementation信息

```java
SystemUtil.getJavaInfo();
```

### Java运行时信息

```java
SystemUtil.getJavaRuntimeInfo();
```

### 系统信息

```java
SystemUtil.getOsInfo();
```

### 用户信息

```java
SystemUtil.getUserInfo();
```

### 当前主机网络地址信息

```java
SystemUtil.getHostInfo();
```

### 运行时信息，包括内存总大小、已用大小、可用大小等

```java
SystemUtil.getRuntimeInfo();
```

