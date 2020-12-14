package cn.hutool.cron;

/**
 * 作业启动器<br>
 * 负责检查<strong>TaskTable</strong>是否有匹配到此时运行的Task<br>
 * 检查完毕后启动器结束
 * 
 * @author Looly
 *
 */
public class TaskLauncher implements Runnable{
	
	private final Scheduler scheduler;
	private final long millis;

	/**
	 * 构造
	 *
	 * @param scheduler {@link Scheduler}
	 * @param millis 毫秒数
	 */
	public TaskLauncher(Scheduler scheduler, long millis) {
		this.scheduler = scheduler;
		this.millis = millis;
	}
	
	@Override
	public void run() {
		//匹配秒部分由用户定义决定，始终不匹配年
		scheduler.taskTable.executeTaskIfMatch(this.scheduler, this.millis);
		
		//结束通知
		scheduler.taskLauncherManager.notifyLauncherCompleted(this);
	}
}
