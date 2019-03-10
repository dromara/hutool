Script工具-ScriptUtil
===

## 介绍

针对Script执行工具化封装

## 使用
1. `ScriptUtil.eval` 执行Javascript脚本，参数为脚本字符串。

栗子：
```java
ScriptUtil.eval("print('Script test!');");
```

2. `ScriptUtil.compile` 编译脚本，返回一个`CompiledScript`对象

栗子：
```java
CompiledScript script = ScriptUtil.compile("print('Script test!');");
try {
	script.eval();
} catch (ScriptException e) {
	throw new ScriptRuntimeException(e);
}
```

