package cn.hutool.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

public class ZoneUtilTest {
	@Test
	public void toTest() {
		assertEquals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
		assertEquals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
	}
}
