package cn.hutool.cron.pattern;

import cn.hutool.cron.CronException;
import org.junit.Assert;
import org.junit.Test;

public class CronPatternBuilderTest {

	@Test
	public void buildMatchAllTest(){
		String build = CronPatternBuilder.of().build();
		Assert.assertEquals("* * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.build();
		Assert.assertEquals("* * * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.set(Part.YEAR, "*")
				.build();
		Assert.assertEquals("* * * * * * *", build);
	}

	@Test
	public void buildRangeTest(){
		String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.setRange(Part.HOUR, 2, 9)
				.build();
		Assert.assertEquals("* * 2-9 * * *", build);
	}

	@Test(expected = CronException.class)
	public void buildRangeErrorTest(){
		String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				// 55无效值
				.setRange(Part.HOUR, 2, 55)
				.build();
		Assert.assertEquals("* * 2-9 * * *", build);
	}
}
