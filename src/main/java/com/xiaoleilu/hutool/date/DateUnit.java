package com.xiaoleilu.hutool.date;

/**
 * 日期时间单位，每个单位都是以毫秒为基数
 * @author Looly
 *
 */
public enum DateUnit {
	MS(1), 
	SECOND(1000), 
	MINUTE(1000  * 60),
	HOUR(1000 * 60 * 60),
	DAY(1000 * 60 * 60 * 24);
	
	private long millis;
	DateUnit(long millis){
		this.millis = millis;
	}
	
	/**
	 * @return 单位对应的毫秒数
	 */
	public long getMillis(){
		return this.millis;
	}
}
