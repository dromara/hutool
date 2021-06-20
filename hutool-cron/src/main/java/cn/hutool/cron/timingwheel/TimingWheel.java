package cn.hutool.cron.timingwheel;

import cn.hutool.log.StaticLog;

import java.util.function.Consumer;

/**
 * 多层时间轮，常用于延时任务。<br>
 * 时间轮是一种环形数据结构，由多个槽组成，每个槽中存放任务集合。<br>
 * 一个单独的线程推进时间一槽一槽的移动，并执行槽中的任务。
 *
 * @author eliasyaoyc, looly
 */
public class TimingWheel {

	/**
	 * 一个时间槽的范围
	 */
	private final long tickMs;

	/**
	 * 时间轮大小，时间轮中时间槽的个数
	 */
	private final int wheelSize;

	/**
	 * 时间跨度，当前时间轮总间隔，即单个槽的跨度*槽个数
	 */
	private final long interval;

	/**
	 * 时间槽
	 */
	private final TimerTaskList[] timerTaskLists;

	/**
	 * 当前时间，指向当前操作的时间格，代表当前时间
	 */
	private long currentTime;

	/**
	 * 上层时间轮
	 */
	private volatile TimingWheel overflowWheel;

	/**
	 * 任务处理器
	 */
	private final Consumer<TimerTaskList> consumer;

	/**
	 * 构造
	 *
	 * @param tickMs    一个时间槽的范围，单位毫秒
	 * @param wheelSize 时间轮大小
	 * @param consumer  任务处理器
	 */
	public TimingWheel(long tickMs, int wheelSize, Consumer<TimerTaskList> consumer) {
		this(tickMs, wheelSize, System.currentTimeMillis(), consumer);
	}

	/**
	 * 构造
	 *
	 * @param tickMs      一个时间槽的范围，单位毫秒
	 * @param wheelSize   时间轮大小
	 * @param currentTime 当前时间
	 * @param consumer    任务处理器
	 */
	public TimingWheel(long tickMs, int wheelSize, long currentTime, Consumer<TimerTaskList> consumer) {
		this.currentTime = currentTime;
		this.tickMs = tickMs;
		this.wheelSize = wheelSize;
		this.interval = tickMs * wheelSize;
		this.timerTaskLists = new TimerTaskList[wheelSize];
		//currentTime为tickMs的整数倍 这里做取整操作
		this.currentTime = currentTime - (currentTime % tickMs);
		this.consumer = consumer;
	}

	/**
	 * 添加任务到时间轮
	 *
	 * @param timerTask 任务
	 * @return 是否成功
	 */
	public boolean addTask(TimerTask timerTask) {
		long expiration = timerTask.getDelayMs();
		//过期任务直接执行
		if (expiration < currentTime + tickMs) {
			return false;
		} else if (expiration < currentTime + interval) {
			//当前时间轮可以容纳该任务 加入时间槽
			long virtualId = expiration / tickMs;
			int index = (int) (virtualId % wheelSize);
			StaticLog.debug("tickMs: {} ------index: {} ------expiration: {}", tickMs, index, expiration);

			TimerTaskList timerTaskList = timerTaskLists[index];
			if (null == timerTaskList) {
				timerTaskList = new TimerTaskList();
				timerTaskLists[index] = timerTaskList;
			}
			timerTaskList.addTask(timerTask);
			if (timerTaskList.setExpiration(virtualId * tickMs)) {
				//添加到delayQueue中
				consumer.accept(timerTaskList);
			}
		} else {
			//放到上一层的时间轮
			TimingWheel timeWheel = getOverflowWheel();
			timeWheel.addTask(timerTask);
		}
		return true;
	}

	/**
	 * 推进时间
	 *
	 * @param timestamp 推进的时间
	 */
	public void advanceClock(long timestamp) {
		if (timestamp >= currentTime + tickMs) {
			currentTime = timestamp - (timestamp % tickMs);
			if (overflowWheel != null) {
				//推进上层时间轮时间
				this.getOverflowWheel().advanceClock(timestamp);
			}
		}
	}

	/**
	 * 创建或者获取上层时间轮
	 */
	private TimingWheel getOverflowWheel() {
		if (overflowWheel == null) {
			synchronized (this) {
				if (overflowWheel == null) {
					overflowWheel = new TimingWheel(interval, wheelSize, currentTime, consumer);
				}
			}
		}
		return overflowWheel;
	}
}
