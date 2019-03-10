异常工具-ExceptionUtil
===

## 介绍

针对异常封装，例如包装为`RuntimeException`。

## 方法

- `getMessage` 获得完整消息，包括异常名
- `wrap` 包装一个异常为指定类型异常
- `wrapRuntime` 使用运行时异常包装编译异常
- `getCausedBy` 获取由指定异常类引起的异常
- `isCausedBy` 判断是否由指定异常类引起
- `stacktraceToString` 堆栈转为完整字符串

其它方法见API文档：

[https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/exceptions/ExceptionUtil.html](https://apidoc.gitee.com/loolly/hutool/cn/hutool/core/exceptions/ExceptionUtil.html)

