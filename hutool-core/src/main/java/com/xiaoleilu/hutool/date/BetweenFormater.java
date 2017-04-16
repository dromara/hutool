package com.xiaoleilu.hutool.date;

/**
 * 时长格式化器
 * @author Looly
 *
 */
public class BetweenFormater {
	
	/** 时长毫秒数 */
	private long betweenMs;
	/** 格式化级别 */
	private Level level;
	/** 格式化级别的最大个数 */
	private int levelMaxCount;
	
	/**
	 * 构造
	 * @param betweenMs 日期间隔
	 * @param level 级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
	 */
	public BetweenFormater(long betweenMs, Level level) {
		this(betweenMs, level, 0);
	}
	
	/**
	 * 构造
	 * @param betweenMs 日期间隔
	 * @param level 级别，按照天、小时、分、秒、毫秒分为5个等级，根据传入等级，格式化到相应级别
	 * @param levelMaxCount 格式化级别的最大个数，假如级别个数为1，但是级别到秒，那只显示一个级别
	 */
	public BetweenFormater(long betweenMs, Level level, int levelMaxCount) {
		this.betweenMs = betweenMs;
		this.level = level;
		this.levelMaxCount = levelMaxCount;
	}
	
	/**
	 * 格式化日期间隔输出<br>
	 * 
	 * @return 格式化后的字符串
	 */
	public String format(){
		if(betweenMs == 0){
			return "0";
		}
		
		long day = betweenMs / DateUnit.DAY.getMillis();
		long hour = betweenMs / DateUnit.HOUR.getMillis() - day * 24;
		long minute = betweenMs / DateUnit.MINUTE.getMillis() - day * 24 * 60 - hour * 60;
		long second = betweenMs / DateUnit.SECOND.getMillis() - ((day * 24 + hour) * 60 + minute) * 60;
		long millisecond = betweenMs - (((day * 24 + hour) * 60 + minute) * 60 + second) * 1000;
		
		StringBuilder sb = new StringBuilder();
		final int level = this.level.value;
		int levelCount = 0;
		
		if(isLevelCountValid(levelCount) && 0 != day && level > 0){
			sb.append(day).append("天");
			levelCount++;
		}
		if(isLevelCountValid(levelCount) && 0 != hour && level > 1){
			sb.append(hour).append("小时");
			levelCount++;
		}
		if(isLevelCountValid(levelCount) && 0 != minute && level > 2){
			sb.append(minute).append("分");
			levelCount++;
		}
		if(isLevelCountValid(levelCount) && 0 != second && level > 3){
			sb.append(second).append("秒");
			levelCount++;
		}
		if(isLevelCountValid(levelCount) && 0 != millisecond && level > 4){
			sb.append(millisecond).append("毫秒");
			levelCount++;
		}
		
		return sb.toString();
	}
	
	/**
	 * 获得 时长毫秒数
	 * @return 时长毫秒数
	 */
	public long getBetweenMs() {
		return betweenMs;
	}

	/**
	 * 设置 时长毫秒数
	 * @param betweenMs 时长毫秒数
	 */
	public void setBetweenMs(long betweenMs) {
		this.betweenMs = betweenMs;
	}

	/**
	 * 获得 格式化级别
	 * @return 格式化级别
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * 设置格式化级别
	 * @param level 格式化级别
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * 格式化等级枚举<br>
	 * @author Looly
	 */
	public static enum Level {

		/** 天 */
		DAY(1),
		/** 小时 */
		HOUR(2),
		/** 分钟 */
		MINUTE(3),
		/** 秒 */
		SECOND(4),
		/** 毫秒 */
		MILLSECOND(5);

		private int value;

		private Level(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
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
	private boolean isLevelCountValid(int levelCount){
		return this.levelMaxCount <= 0 || levelCount < this.levelMaxCount;
	}
}
