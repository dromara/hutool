package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;

/**
 * 简易值转换器。将给定String值转为int
 * @author Looly
 *
 */
public class SimpleValueParser implements ValueParser {
	
	/** 最小值（包括） */
	protected int min;
	/** 最大值（包括） */
	protected int max;

	/**
	 * 构造
	 *
	 * @param min 最小值（包括）
	 * @param max 最大值（包括）
	 */
	public SimpleValueParser(int min, int max) {
		if(min > max){
			this.min = max;
			this.max = min;
		}else{
			this.min = min;
			this.max = max;
		}
	}

	@Override
	public int parse(String value) throws CronException {
		if("L".equalsIgnoreCase(value)){
			// L表示最大值
			return max;
		}

		int i;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new CronException(e, "Invalid integer value: '{}'", value);
		}
		if (i < min || i > max) {
			throw new CronException("Value {} out of range: [{} , {}]", i, min, max);
		}
		return i;
	}

	@Override
	public int getMin() {
		return this.min;
	}

	@Override
	public int getMax() {
		return this.max;
	}
}
