package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BetweenFormatterTest {

	@Test
	public void formatTest(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formater = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 1);
		Assertions.assertEquals(formater.toString(), "1天");
	}

	@Test
	public void formatBetweenTest(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formater = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 1);
		Assertions.assertEquals(formater.toString(), "1秒");
	}

	@Test
	public void formatBetweenTest2(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formater = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 5);
		Assertions.assertEquals(formater.toString(), "1小时2分3秒");
	}

	@Test
	public void formatTest2(){
		final BetweenFormatter formater = new BetweenFormatter(584, BetweenFormatter.Level.SECOND, 1);
		Assertions.assertEquals(formater.toString(), "0秒");
	}
}
