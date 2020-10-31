package cn.hutool.cron.task;

/**
 * 定时作业接口，通过实现execute方法执行具体的任务
 * <p>
 * 作业执行是异步执行，即不同作业、相同作业在不同时间的执行是相互独立的。<br>
 * 假如前一个作业未完成，下一个调度开始，则不会等待前一个作业，直接执行。<br>
 * 关于作业的互斥，请自行加锁完成。
 * </p>
 *
 * @author Looly
 */
@FunctionalInterface
public interface Task {

	/**
	 * 执行作业
	 * <p>
	 * 作业的具体实现需考虑异常情况，默认情况下任务异常在监听中统一监听处理，如果不加入监听，异常会被忽略<br>
	 * 因此最好自行捕获异常后处理
	 */
	void execute();
}
