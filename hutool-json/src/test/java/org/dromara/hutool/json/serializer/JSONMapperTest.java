package org.dromara.hutool.json.serializer;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.json.JSONFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JSONMapperTest {

	/**
	 * Mapper性能耗费较多
	 */
	@Test
	@Disabled
	void toJSONTest() {
		final JSONFactory factory = JSONFactory.getInstance();

		final JSONMapper mapper = factory.getMapper();

		final StopWatch stopWatch = DateUtil.createStopWatch();

		final int count = 1000;
		stopWatch.start("use mapper");
		for (int i = 0; i < count; i++) {
			mapper.toJSON("qbw123", false);
		}
		stopWatch.stop();

		stopWatch.start("use ofPrimitive");
		for (int i = 0; i < count; i++) {
			factory.ofPrimitive("qbw123");
		}
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint());
	}
}
