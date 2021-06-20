package cn.hutool.core.stream;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

public class StreamUtilTest {

	@Test
	public void ofTest(){
		final Stream<Integer> stream = StreamUtil.of(2, x -> x * 2, 4);
		final String result = stream.collect(CollectorUtil.joining(","));
		Assert.assertEquals("2,4,8,16", result);
	}
}
