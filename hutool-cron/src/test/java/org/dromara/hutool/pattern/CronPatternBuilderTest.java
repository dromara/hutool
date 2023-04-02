package org.dromara.hutool.pattern;

import org.dromara.hutool.CronException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CronPatternBuilderTest {

	@Test
	public void buildMatchAllTest(){
		String build = CronPatternBuilder.of().build();
		Assertions.assertEquals("* * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.build();
		Assertions.assertEquals("* * * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.set(Part.YEAR, "*")
				.build();
		Assertions.assertEquals("* * * * * * *", build);
	}

	@Test
	public void buildRangeTest(){
		final String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.setRange(Part.HOUR, 2, 9)
				.build();
		Assertions.assertEquals("* * 2-9 * * *", build);
	}

	@Test
	public void buildRangeErrorTest(){
		Assertions.assertThrows(CronException.class, ()->{
			final String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				// 55无效值
				.setRange(Part.HOUR, 2, 55)
				.build();
			Assertions.assertEquals("* * 2-9 * * *", build);
		});
	}
}
