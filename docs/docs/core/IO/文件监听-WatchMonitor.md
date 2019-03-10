文件监听-WatchMonitor
===

## 由来

很多时候我们需要监听一个文件的变化或者目录的变动，包括文件的创建、修改、删除，以及目录下文件的创建、修改和删除，在JDK7前我们只能靠轮询方式遍历目录或者定时检查文件的修改事件，这样效率非常低，性能也很差。因此在JDK7中引入了`WatchService`。不过考虑到其API并不友好，于是Hutool便针对其做了简化封装，使监听更简单，也提供了更好的功能，这包括：

- 支持多级目录的监听（WatchService只支持一级目录），可自定义监听目录深度
- 延迟合并触发支持（文件变动时可能触发多次modify，支持在某个时间范围内的多次修改事件合并为一个修改事件）
- 简洁易懂的API方法，一个方法即可搞定监听，无需理解复杂的监听注册机制。
- 多观察者实现，可以根据业务实现多个`Watcher`来响应同一个事件（通过WatcherChain）

### WatchMonitor

在Hutool中，`WatchMonitor`主要针对JDK7中`WatchService`做了封装，针对文件和目录的变动（创建、更新、删除）做一个钩子，在`Watcher`中定义相应的逻辑来应对这些文件的变化。

### 内部应用
在hutool-setting模块，使用WatchMonitor监测配置文件变化，然后自动load到内存中。WatchMonitor的使用可以避免轮询，以事件响应的方式应对文件变化。

## 使用

`WatchMonitor`提供的事件有：

- `ENTRY_MODIFY` 文件修改的事件
- `ENTRY_CREATE` 文件或目录创建的事件
- `ENTRY_DELETE` 文件或目录删除的事件
- `OVERFLOW` 丢失的事件

这些事件对应`StandardWatchEventKinds`中的事件。

下面我们介绍WatchMonitor的使用：

### 监听指定事件

```java
File file = FileUtil.file("example.properties");
//这里只监听文件或目录的修改事件
WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY);
watchMonitor.setWatcher(new Watcher(){
	@Override
	public void onCreate(WatchEvent<?> event, Path currentPath) {
		Object obj = event.context();
		Console.log("创建：{}-> {}", currentPath, obj);
	}

	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
		Object obj = event.context();
		Console.log("修改：{}-> {}", currentPath, obj);
	}

	@Override
	public void onDelete(WatchEvent<?> event, Path currentPath) {
		Object obj = event.context();
		Console.log("删除：{}-> {}", currentPath, obj);
	}

	@Override
	public void onOverflow(WatchEvent<?> event, Path currentPath) {
		Object obj = event.context();
		Console.log("Overflow：{}-> {}", currentPath, obj);
	}
});

//设置监听目录的最大深入，目录层级大于制定层级的变更将不被监听，默认只监听当前层级目录
watchMonitor.setMaxDepth(3);
//启动监听
watchMonitor.start();
```

### 监听全部事件

其实我们不必实现`Watcher`的所有接口方法，Hutool同时提供了`SimpleWatcher`类，只需重写对应方法即可。

同样，如果我们想监听所有事件，可以：
```java
WatchMonitor.createAll(file, new SimpleWatcher(){
	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
		Console.log("EVENT modify");
	}
}).start();
```

`createAll`方法会创建一个监听所有事件的WatchMonitor，同时在第二个参数中定义Watcher来负责处理这些变动。

### 延迟处理监听事件

在监听目录或文件时，如果这个文件有修改操作，JDK会多次触发modify方法，为了解决这个问题，我们定义了`DelayWatcher`，此类通过维护一个Set将短时间内相同文件多次modify的事件合并处理触发，从而避免以上问题。

```java
WatchMonitor monitor = WatchMonitor.createAll("d:/", new DelayWatcher(watcher, 500));
monitor.start();
```

