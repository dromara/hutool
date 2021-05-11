package cn.hutool.cron.timingwheel;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 任务队列，任务双向链表
 *
 * @author siran.yao，looly
 */
public class TimerTaskList implements Delayed {

	/**
	 * 过期时间
	 */
	private final AtomicLong expire;

	/**
	 * 根节点
	 */
	private final TimerTask root;

	/**
	 * 构造
	 */
	public TimerTaskList(){
		expire = new AtomicLong(-1L);

		root = new TimerTask( null,-1L);
		root.prev = root;
		root.next = root;
	}

	/**
	 * 设置过期时间
	 *
	 * @param expire 过期时间，单位毫秒
	 * @return 是否设置成功
	 */
	public boolean setExpiration(long expire) {
		return this.expire.getAndSet(expire) != expire;
	}

	/**
	 * 获取过期时间
	 * @return 过期时间
	 */
	public long getExpire() {
		return expire.get();
	}

	/**
	 * 新增任务，将任务加入到双向链表的头部
	 *
	 * @param timerTask 延迟任务
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
	 *
	 * @param timerTask 任务
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
	 * 重新分配，即将列表中的任务全部处理
	 *
	 * @param flush 任务处理函数
	 */
	public synchronized void flush(Consumer<TimerTask> flush) {
		TimerTask timerTask = root.next;
		while (false == timerTask.equals(root)) {
			this.removeTask(timerTask);
			flush.accept(timerTask);
			timerTask = root.next;
		}
		expire.set(-1L);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return Math.max(0, unit.convert(expire.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
	}

	@Override
	public int compareTo(Delayed o) {
		if (o instanceof TimerTaskList) {
			return Long.compare(expire.get(), ((TimerTaskList) o).expire.get());
		}
		return 0;
	}
}
