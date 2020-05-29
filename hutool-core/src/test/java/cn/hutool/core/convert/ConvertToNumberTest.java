package cn.hutool.core.convert;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class ConvertToNumberTest {
	@Test
	public void dateToLongTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final Long dateLong = Convert.toLong(date);
		assert date != null;
		Assert.assertEquals(date.getTime(), dateLong.longValue());
	}

	@Test
	public void dateToIntTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final Integer dateInt = Convert.toInt(date);
		assert date != null;
		Assert.assertEquals((int)date.getTime(), dateInt.intValue());
	}

	@Test
	public void dateToAtomicLongTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final AtomicLong dateLong = Convert.convert(AtomicLong.class, date);
		assert date != null;
		Assert.assertEquals(date.getTime(), dateLong.longValue());
	}
}
