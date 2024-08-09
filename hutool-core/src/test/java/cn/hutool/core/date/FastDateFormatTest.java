package cn.hutool.core.date;

import cn.hutool.core.date.format.FastDateFormat;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class FastDateFormatTest {
	private static final TimeZone timezone = TimeZone.getTimeZone("Etc/Utc");

	private static FastDateFormat getHutoolInstance(String pattern) {
		return FastDateFormat.getInstance(pattern, timezone);
	}

	@Test
	public void yearTest() {
		Date date = DateUtil.date(0L);

		assertEquals(
			"1970-01-01 00:00:00",
			getHutoolInstance("yyyy-MM-dd HH:mm:ss").format(date)
		);

		assertEquals(
			"1970-01-01 00:00:00",
			getHutoolInstance("YYYY-MM-dd HH:mm:ss").format(date)
		);

		assertEquals(
			"1970",
			getHutoolInstance("YYYY").format(date)
		);

		assertEquals(
			"70",
			getHutoolInstance("yy").format(date)
		);
	}

	@SuppressWarnings("SuspiciousDateFormat")
	@Test
	public void weekYearTest() {
		Date date = DateUtil.date(0L);

		assertEquals(
			"70",
			new SimpleDateFormat("YY").format(date)
		);

		assertEquals(
			"70",
			getHutoolInstance("YY").format(date)
		);
	}
}
