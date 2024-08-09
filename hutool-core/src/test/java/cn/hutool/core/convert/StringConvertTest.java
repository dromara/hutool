package cn.hutool.core.convert;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

public class StringConvertTest {

	@Test
	public void timezoneToStrTest(){
		final String s = Convert.toStr(TimeZone.getTimeZone("Asia/Shanghai"));
		assertEquals("Asia/Shanghai", s);
	}
}
