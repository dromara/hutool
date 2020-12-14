package cn.hutool.cron.task;

import cn.hutool.cron.pattern.CronPattern;

/**
 * 定时作业，此类除了定义了作业，也定义了作业的执行周期以及ID。
 *
 * @author looly
 * @since 5.4.7
 */
public class CronTask implements Task{

	private final String id;
	private CronPattern pattern;
	private final Task task;

	/**
	 * 构造
	 * @param id ID
	 * @param pattern 表达式
	 * @param task 作业
	 */
	public CronTask(String id, CronPattern pattern, Task task) {
		this.id = id;
		this.pattern = pattern;
		this.task = task;
	}

	@Override
	public void execute() {
		task.execute();
	}

	/**
	 * 获取作业ID
	 *
	 * @return 作业ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取表达式
	 *
	 * @return 表达式
	 */
	public CronPattern getPattern() {
		return pattern;
	}

	/**
	 * 设置新的定时表达式
	 * @param pattern 表达式
	 * @return this
	 */
	public CronTask setPattern(CronPattern pattern){
		this.pattern = pattern;
		return this;
	}

	/**
	 * 获取原始作业
	 *
	 * @return 作业
	 */
	public Task getRaw(){
		return this.task;
	}
}
