package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class Issue2612Test {

	@Test
	public void parseTest(){
		Assert.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-08:00")).toString());

		Assert.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-0800")).toString());
	}
}
