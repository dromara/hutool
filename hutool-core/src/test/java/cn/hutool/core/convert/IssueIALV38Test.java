package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class IssueIALV38Test {
	@Test
	void name() {
		final Object o = Convert.convertWithCheck(BigDecimal.class, " 111啊", null, false);
		Assertions.assertEquals(new BigDecimal("111"), o);
	}
}
