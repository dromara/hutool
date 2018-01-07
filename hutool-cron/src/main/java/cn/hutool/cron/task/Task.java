package cn.hutool.cron.task;

/**
 * 定时作业接口，通过实现execute方法执行具体的任务<br>
 * @author Looly
 *
 */
public interface Task {
	
	/**
	 * 执行作业
	 */
	public void execute();
}
