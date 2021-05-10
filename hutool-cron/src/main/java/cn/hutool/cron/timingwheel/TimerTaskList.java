package cn.hutool.cron.timingwheel;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @Author: siran.yao
 * @time: 2020/5/8:上午11:13
 */
public class TimerTaskList implements Delayed {
	/**
	 * 过期时间
	 */
	private AtomicLong expiration = new AtomicLong(-1L);

	/**
	 * 根节点
	 */
	private TimerTask root = new TimerTask( null,-1L);

	{
		root.prev = root;
		root.next = root;
	}

	/**
	 * 设置过期时间
	 */
	public boolean setExpiration(long expire) {
		return expiration.getAndSet(expire) != expire;
	}

	/**
	 * 获取过期时间
	 */
	public long getExpiration() {
		return expiration.get();
	}

	/**
	 * 新增任务
	 */
	public void addTask(TimerTask timerTask) {
		synchronized (this) {
			if (timerTask.timerTaskList == null) {
				timerTask.timerTaskList = this;
				TimerTask tail = root.prev;
				timerTask.next = root;
				timerTask.prev = tail;
				tail.next = timerTask;
				root.prev = timerTask;
			}
		}
	}

	/**
	 * 移除任务
	 */
	public void removeTask(TimerTask timerTask) {
		synchronized (this) {
			if (timerTask.timerTaskList.equals(this)) {
				timerTask.next.prev = timerTask.prev;
				timerTask.prev.next = timerTask.next;
				timerTask.timerTaskList = null;
				timerTask.next = null;
				timerTask.prev = null;
			}
		}
	}

	/**
	 * 重新分配
	 */
	public synchronized void flush(Consumer<TimerTask> flush) {
		TimerTask timerTask = root.next;
		while (!timerTask.equals(root)) {
			this.removeTask(timerTask);
			flush.accept(timerTask);
			timerTask = root.next;
		}
		expiration.set(-1L);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
	}

	@Override
	public int compareTo(Delayed o) {
		if (o instanceof TimerTaskList) {
			return Long.compare(expiration.get(), ((TimerTaskList) o).expiration.get());
		}
		return 0;
	}
}
