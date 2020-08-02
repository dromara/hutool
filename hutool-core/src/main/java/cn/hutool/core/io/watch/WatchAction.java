package cn.hutool.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 监听事件处理函数接口
 *
 * @author looly
 * @since 5.4.0
 */
@FunctionalInterface
public interface WatchAction {

	/**
	 * 事件处理，通过实现此方法处理各种事件。
	 *
	 * 事件可以调用 {@link WatchEvent#kind()}获取，对应事件见{@link WatchKind}
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void doAction(WatchEvent<?> event, Path currentPath);
}
