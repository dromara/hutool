package cn.hutool.core.text.finder;

import org.junit.Assert;
import org.junit.Test;

public class CharFinderTest {

	@Test
	public void startTest(){
		int start = new CharFinder('a').setText("cba123").start(2);
		Assert.assertEquals(2, start);

		start = new CharFinder('c').setText("cba123").start(2);
		Assert.assertEquals(-1, start);

		start = new CharFinder('3').setText("cba123").start(2);
		Assert.assertEquals(5, start);
	}
	@Test
	public void negativeStartTest(){
		int start = new CharFinder('a').setText("cba123").setNegative(true).start(2);
		Assert.assertEquals(2, start);

		start = new CharFinder('2').setText("cba123").setNegative(true).start(2);
		Assert.assertEquals(-1, start);

		start = new CharFinder('c').setText("cba123").setNegative(true).start(2);
		Assert.assertEquals(0, start);
	}
}
