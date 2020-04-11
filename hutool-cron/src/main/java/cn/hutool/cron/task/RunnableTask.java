package cn.hutool.cron.task;

/**
 * {@link Runnable} 的 {@link Task}包装
 * @author Looly
 *
 */
public class RunnableTask implements Task{
	private final Runnable runnable;
	
	public RunnableTask(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void execute() {
		runnable.run();
	}
}
