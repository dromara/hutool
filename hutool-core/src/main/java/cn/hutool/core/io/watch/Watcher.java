package cn.hutool.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 观察者（监视器）
 *
 * @author Looly
 */
public interface Watcher {
	/**
	 * 文件创建时执行的方法
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void onCreate(WatchEvent<?> event, Path currentPath);

	/**
	 * 文件修改时执行的方法<br>
	 * 文件修改可能触发多次
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void onModify(WatchEvent<?> event, Path currentPath);

	/**
	 * 文件删除时执行的方法
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void onDelete(WatchEvent<?> event, Path currentPath);

	/**
	 * 事件丢失或出错时执行的方法
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void onOverflow(WatchEvent<?> event, Path currentPath);
}
