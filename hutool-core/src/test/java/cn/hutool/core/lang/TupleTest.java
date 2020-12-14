package cn.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

public class TupleTest {

	@Test
	public void hashCodeTest(){
		final Tuple tuple = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		final Tuple tuple2 = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		Assert.assertEquals(tuple, tuple2);
	}
}
