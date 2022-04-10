package cn.hutool.cron.pattern;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 定时任务表达式构建器
 *
 * @author looly
 * @since 5.8.0
 */
public class CronPatternBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	final String[] parts = new String[7];

	/**
	 * 创建构建器
	 * @return CronPatternBuilder
	 */
	public static CronPatternBuilder of() {
		return new CronPatternBuilder();
	}

	/**
	 * 设置值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param values 时间值列表
	 * @return this
	 */
	public CronPatternBuilder setValues(Part part, int... values) {
		for (int value : values) {
			part.checkValue(value);
		}
		return set(part, ArrayUtil.join(values, ","));
	}

	/**
	 * 设置区间
	 *
	 * @param part  部分，如秒、分、时等
	 * @param begin 起始值
	 * @param end   结束值
	 * @return this
	 */
	public CronPatternBuilder setRange(Part part, int begin, int end) {
		Assert.notNull(part );
		part.checkValue(begin);
		part.checkValue(end);
		return set(part, StrUtil.format("{}-{}", begin, end));
	}

	/**
	 * 设置对应部分的定时任务值
	 *
	 * @param part  部分，如秒、分、时等
	 * @param value 表达式值，如"*"、"1,2"、"5-12"等
	 * @return this
	 */
	public CronPatternBuilder set(Part part, String value) {
		parts[part.ordinal()] = value;
		return this;
	}

	@Override
	public String build() {
		for (int i = Part.MINUTE.ordinal(); i < Part.YEAR.ordinal(); i++) {
			// 从分到周，用户未设置使用默认值
			// 秒和年如果不设置，忽略之
			if(StrUtil.isBlank(parts[i])){
				parts[i] = "*";
			}
		}

		return StrJoiner.of(StrUtil.SPACE)
				.setNullMode(StrJoiner.NullMode.IGNORE)
				.append(this.parts)
				.toString();
	}
}
