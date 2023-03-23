package cn.hutool.core.lang.func;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.ArrayList;

public class FunctionPoolTest {

	@Test
	public void createStringTest() {
		// 预热
		FunctionPool.createString("123".toCharArray());

		// 测试数据
		final ArrayList<char[]> list = ListUtil.of();
		for (int i = 0; i < 100000; i++) {
			list.add(RandomUtil.randomString(100).toCharArray());
		}

		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start("copy creator");
		for (final char[] value : list) {
			new String(value);
		}
		stopWatch.stop();

		stopWatch.start("zero copy creator");
		for (final char[] value : list) {
			FunctionPool.createString(value);
		}
		stopWatch.stop();

		//Console.log(stopWatch.prettyPrint());
	}
}
