package cn.hutool.cron.pattern;

import cn.hutool.cron.CronException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CronPatternBuilderTest {

	@Test
	public void buildMatchAllTest(){
		String build = CronPatternBuilder.of().build();
		assertEquals("* * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.build();
		assertEquals("* * * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.set(Part.YEAR, "*")
				.build();
		assertEquals("* * * * * * *", build);
	}

	@Test
	public void buildRangeTest(){
		String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.setRange(Part.HOUR, 2, 9)
				.build();
		assertEquals("* * 2-9 * * *", build);
	}

	@Test(expected = CronException.class)
	public void buildRangeErrorTest(){
		String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				// 55无效值
				.setRange(Part.HOUR, 2, 55)
				.build();
		assertEquals("* * 2-9 * * *", build);
	}
}
