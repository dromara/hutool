package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

public class ZoneUtilTest {

	@Test
	public void test() {
		Assertions.assertEquals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
		Assertions.assertEquals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
	}
}
