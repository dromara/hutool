package cn.hutool.core.date;

import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组计时器<br>
 * 计算某几个过程花费的时间，精确到毫秒或纳秒
 *
 * @author Looly
 * @since 5.5.2
 */
public class GroupTimeInterval implements Serializable {
	private static final long serialVersionUID = 1L;

	private final boolean isNano;
	protected final Map<String, Long> groupMap;

	/**
	 * 构造
	 *
	 * @param isNano 是否使用纳秒计数，false则使用毫秒
	 */
	public GroupTimeInterval(boolean isNano) {
		this.isNano = isNano;
		groupMap = new ConcurrentHashMap<>();
	}

	/**
	 * 清空所有定时记录
	 *
	 * @return this
	 */
	public GroupTimeInterval clear(){
		this.groupMap.clear();
		return this;
	}

	/**
	 * 开始计时并返回当前时间
	 *
	 * @param id 分组ID
	 * @return 开始计时并返回当前时间
	 */
	public long start(String id) {
		final long time = getTime();
		this.groupMap.put(id, time);
		return time;
	}

	/**
	 * 重新计时并返回从开始到当前的持续时间秒<br>
	 * 如果此分组下没有记录，则返回0;
	 *
	 * @param id 分组ID
	 * @return 重新计时并返回从开始到当前的持续时间
	 */
	public long intervalRestart(String id) {
		final long now = getTime();
		return now - ObjectUtil.defaultIfNull(this.groupMap.put(id, now), now);
	}

	//----------------------------------------------------------- Interval

	/**
	 * 从开始到当前的间隔时间（毫秒数）<br>
	 * 如果使用纳秒计时，返回纳秒差，否则返回毫秒差<br>
	 * 如果分组下没有开始时间，返回{@code null}
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔时间（毫秒数）
	 */
	public long interval(String id) {
		final Long lastTime = this.groupMap.get(id);
		if (null == lastTime) {
			return 0;
		}
		return getTime() - lastTime;
	}

	/**
	 * 从开始到当前的间隔时间
	 *
	 * @param id       分组ID
	 * @param dateUnit 时间单位
	 * @return 从开始到当前的间隔时间（毫秒数）
	 */
	public long interval(String id, DateUnit dateUnit) {
		final long intervalMs = isNano ? interval(id) / 1000000L : interval(id);
		if (DateUnit.MS == dateUnit) {
			return intervalMs;
		}
		return intervalMs / dateUnit.getMillis();
	}

	/**
	 * 从开始到当前的间隔时间（毫秒数）
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔时间（毫秒数）
	 */
	public long intervalMs(String id) {
		return interval(id, DateUnit.MS);
	}

	/**
	 * 从开始到当前的间隔秒数，取绝对值
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔秒数，取绝对值
	 */
	public long intervalSecond(String id) {
		return interval(id, DateUnit.SECOND);
	}

	/**
	 * 从开始到当前的间隔分钟数，取绝对值
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔分钟数，取绝对值
	 */
	public long intervalMinute(String id) {
		return interval(id, DateUnit.MINUTE);
	}

	/**
	 * 从开始到当前的间隔小时数，取绝对值
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔小时数，取绝对值
	 */
	public long intervalHour(String id) {
		return interval(id, DateUnit.HOUR);
	}

	/**
	 * 从开始到当前的间隔天数，取绝对值
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔天数，取绝对值
	 */
	public long intervalDay(String id) {
		return interval(id, DateUnit.DAY);
	}

	/**
	 * 从开始到当前的间隔周数，取绝对值
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔周数，取绝对值
	 */
	public long intervalWeek(String id) {
		return interval(id, DateUnit.WEEK);
	}

	/**
	 * 从开始到当前的间隔时间（毫秒数），返回XX天XX小时XX分XX秒XX毫秒
	 *
	 * @param id 分组ID
	 * @return 从开始到当前的间隔时间（毫秒数）
	 */
	public String intervalPretty(String id) {
		return DateUtil.formatBetween(intervalMs(id));
	}

	/**
	 * 获取时间的毫秒或纳秒数，纳秒非时间戳
	 *
	 * @return 时间
	 */
	private long getTime() {
		return this.isNano ? System.nanoTime() : System.currentTimeMillis();
	}
}
