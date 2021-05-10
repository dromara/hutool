package cn.hutool.cron.timingwheel;

public class TimerTask {
	/**
	 * 延迟时间
	 */
	private final long delayMs;

	/**
	 * 任务
	 */
	private final Runnable task;

	/**
	 * 时间槽
	 */
	protected TimerTaskList timerTaskList;

	/**
	 * 下一个节点
	 */
	protected TimerTask next;

	/**
	 * 上一个节点
	 */
	protected TimerTask prev;

	/**
	 * 描述
	 */
	public String desc;

	public TimerTask(Runnable task, long delayMs) {
		this.delayMs = System.currentTimeMillis() + delayMs;
		this.task = task;
		this.timerTaskList = null;
		this.next = null;
		this.prev = null;
	}

	public Runnable getTask() {
		return task;
	}

	public long getDelayMs() {
		return delayMs;
	}

	@Override
	public String toString() {
		return desc;
	}
}
