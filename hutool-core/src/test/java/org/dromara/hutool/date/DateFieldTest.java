package org.dromara.hutool.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateFieldTest {

	@Test
	public void ofTest() {
		DateField field = DateField.of(11);
		Assertions.assertEquals(DateField.HOUR_OF_DAY, field);
		field = DateField.of(12);
		Assertions.assertEquals(DateField.MINUTE, field);
	}
}
