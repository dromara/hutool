package cn.hutool.cron.pattern;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.util.StrUtil;

public class CronPatternBuilder implements Builder<String> {

	final String[] parts = new String[7];

	public static CronPatternBuilder of(){
		return new CronPatternBuilder();
	}

	public CronPatternBuilder set(Part part, String value){
		parts[part.ordinal()] = value;
		return this;
	}

	@Override
	public String build() {
		return StrUtil.join(StrUtil.SPACE, (Object[]) parts);
	}
}
