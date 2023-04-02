package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class Issue2612Test {

	@Test
	public void parseTest(){
		Assertions.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-08:00")).toString());

		Assertions.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-0800")).toString());
	}
}
