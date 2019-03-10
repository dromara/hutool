## 介绍
在Hutool群友的强烈要求下，在3.2.0+ 中新增了`ClipboardUtil`这个类用于简化操作剪贴板（当然使用场景被局限）。

## 使用

`ClipboardUtil` 封装了几个常用的静态方法:

### 通用方法
1. `getClipboard` 获取系统剪贴板
2. `set` 设置内容到剪贴板
3. `get` 获取剪贴板内容

### 针对文本
1. `setStr` 设置文本到剪贴板
2. `getStr` 从剪贴板获取文本

### 针对Image对象（图片）
1. `setImage` 设置图片到剪贴板
2. `getImage` 从剪贴板获取图片