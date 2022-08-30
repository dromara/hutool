package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.util.TimeZone;

public class ZoneUtilTest {

	@Test
	public void test() {
		Assert.assertEquals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
		Assert.assertEquals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
	}
}
