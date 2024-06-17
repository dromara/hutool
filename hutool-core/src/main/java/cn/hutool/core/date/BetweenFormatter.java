package cn.hutool.core.date;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 时长格式化器，用于格式化输出两个日期相差的时长<br>
 * 根据{@link Level}不同，调用{@link #format()}方法后返回类似于：
 * <ul>
 *    <li>XX小时XX分XX秒</li>
 *    <li>XX天XX小时</li>
 *    <li>XX月XX天XX小时</li>
 * </ul>
 *
 * @author Looly
 */
public class BetweenFormatter implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 时长毫秒数
	 */
	private long betweenMs;
	/**
	 * 格式化级别
	 */
	private Level level;
	/**
	 * 格式化级别的最大个数
	 */
	private final int levelMaxCount;
	/**
	 * 格式化器
	 */
	private Function<Level, String> levelFormatter = Level::getName;
	/**
	 * 分隔符
	 */
	private String separator = StrUtil.EMPTY;

	/**
	 * 构造
	 *
	 * @param betweenMs 日期间隔
	 * @param level     级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
	 */
	public BetweenFormatter(long betweenMs, Level level) {
		this(betweenMs, level, 0);
	}

	/**
	 * 构造
	 *
	 * @param betweenMs     日期间隔
	 * @param level         级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
	 * @param levelMaxCount 格式化级别的最大个数，假如级别个数为1，但是级别到秒，那只显示一个级别
	 */
	public BetweenFormatter(long betweenMs, Level level, int levelMaxCount) {
		this.betweenMs = betweenMs;
		this.level = level;
		this.levelMaxCount = levelMaxCount;
	}

	/**
	 * 格式化日期间隔输出<br>
	 *
	 * @return 格式化后的字符串
	 */
	public String format() {
		final StringBuilder sb = new StringBuilder();
		if (betweenMs > 0) {
			long day = betweenMs / DateUnit.DAY.getMillis();
			long hour = betweenMs / DateUnit.HOUR.getMillis() - day * 24;
			long minute = betweenMs / DateUnit.MINUTE.getMillis() - day * 24 * 60 - hour * 60;

			final long BetweenOfSecond = ((day * 24 + hour) * 60 + minute) * 60;
			long second = betweenMs / DateUnit.SECOND.getMillis() - BetweenOfSecond;
			long millisecond = betweenMs - (BetweenOfSecond + second) * 1000;

			final int level = this.level.ordinal();
			int levelCount = 0;

			if (isLevelCountValid(levelCount) && day > 0) {
				sb.append(day).append(levelFormatter.apply(Level.DAY)).append(separator);
				levelCount++;
			}
			if (isLevelCountValid(levelCount) && 0 != hour && level >= Level.HOUR.ordinal()) {
				sb.append(hour).append(levelFormatter.apply(Level.HOUR)).append(separator);
				levelCount++;
			}
			if (isLevelCountValid(levelCount) && 0 != minute && level >= Level.MINUTE.ordinal()) {
				sb.append(minute).append(levelFormatter.apply(Level.MINUTE)).append(separator);
				levelCount++;
			}
			if (isLevelCountValid(levelCount) && 0 != second && level >= Level.SECOND.ordinal()) {
				sb.append(second).append(levelFormatter.apply(Level.SECOND)).append(separator);
				levelCount++;
			}
			if (isLevelCountValid(levelCount) && 0 != millisecond && level >= Level.MILLISECOND.ordinal()) {
				sb.append(millisecond).append(levelFormatter.apply(Level.MILLISECOND)).append(separator);
				// levelCount++;
			}
		}

		if (StrUtil.isEmpty(sb)) {
			sb.append(0).append(levelFormatter.apply(this.level));
		} else {
			if (StrUtil.isNotEmpty(separator)) {
				sb.delete(sb.length() - separator.length(), sb.length());
			}
		}
		return sb.toString();
	}

	/**
	 * 获得 时长毫秒数
	 *
	 * @return 时长毫秒数
	 */
	public long getBetweenMs() {
		return betweenMs;
	}

	/**
	 * 设置 时长毫秒数
	 *
	 * @param betweenMs 时长毫秒数
	 */
	public void setBetweenMs(long betweenMs) {
		this.betweenMs = betweenMs;
	}

	/**
	 * 获得 格式化级别
	 *
	 * @return 格式化级别
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * 设置格式化级别
	 *
	 * @param level 格式化级别
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * 设置级别格式化器
	 *
	 * @param levelFormatter 级别格式化器
	 * @return this
	 */
	public BetweenFormatter setLevelFormatter(Function<Level, String> levelFormatter) {
		this.levelFormatter = levelFormatter;
		return this;
	}

	/**
	 * 设置分隔符
	 *
	 * @param separator 分割符
	 * @return this
	 */
	public BetweenFormatter setSeparator(String separator) {
		this.separator = StrUtil.nullToEmpty(separator);
		return this;
	}


	/**
	 * 格式化等级枚举
	 *
	 * @author Looly
	 */
	public enum Level {

		/**
		 * 天
		 */
		DAY("天"),
		/**
		 * 小时
		 */
		HOUR("小时"),
		/**
		 * 分钟
		 */
		MINUTE("分"),
		/**
		 * 秒
		 */
		SECOND("秒"),
		/**
		 * 毫秒
		 */
		MILLISECOND("毫秒");

		/**
		 * 级别名称
		 */
		private final String name;

		/**
		 * 构造
		 *
		 * @param name 级别名称
		 */
		Level(String name) {
			this.name = name;
		}

		/**
		 * 获取级别名称
		 *
		 * @return 级别名称
		 */
		public String getName() {
			return this.name;
		}
	}

	@Override
	public String toString() {
		return format();
	}

	/**
	 * 等级数量是否有效<br>
	 * 有效的定义是：levelMaxCount大于0（被设置），当前等级数量没有超过这个最大值
	 *
	 * @param levelCount 登记数量
	 * @return 是否有效
	 */
	private boolean isLevelCountValid(int levelCount) {
		return this.levelMaxCount <= 0 || levelCount < this.levelMaxCount;
	}
}
