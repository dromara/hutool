package cn.hutool.core.date;

import cn.hutool.core.date.BetweenFormatter.Level;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class BetweenFormatterTest {

	Function<Level, String> levelFormatterEn = level -> {
		switch (level) {
			case DAY:
				return " day";
			case HOUR:
				return " hour";
			case MINUTE:
				return " minute";
			case SECOND:
				return " second";
			case MILLISECOND:
				return " millisecond";
			default:
				return " " + level.name();
		}
	};

	@Test
	public void formatTest() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.MILLISECOND, 1);
		assertEquals(formater.toString(), "1天");
	}

	@Test
	public void formatTestEn() {
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 1);
		formatter.setLevelFormatter(levelFormatterEn);
		assertEquals(formatter.toString(), "1 day");
	}

	@Test
	public void formatTestEn2() {
		final long betweenMs = 3610001;
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 5);
		formatter.setSeparator(",");
		formatter.setLevelFormatter(levelFormatterEn);
		assertEquals(formatter.toString(), "1 hour,10 second,1 millisecond");
	}

	@Test
	public void formatBetweenTest() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 1);
		assertEquals(formater.toString(), "1秒");
	}

	@Test
	public void formatBetweenTest2() {
		long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
		BetweenFormatter formater = new BetweenFormatter(betweenMs, Level.SECOND, 5);
		assertEquals(formater.toString(), "1小时2分3秒");
	}

	@Test
	public void formatTest2() {
		BetweenFormatter formater = new BetweenFormatter(584, Level.SECOND, 1);
		assertEquals(formater.toString(), "0秒");
	}
}
