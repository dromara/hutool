package cn.hutool.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DateFieldTest {

	@Test
	public void ofTest() {
		DateField field = DateField.of(11);
		assertEquals(DateField.HOUR_OF_DAY, field);
		field = DateField.of(12);
		assertEquals(DateField.MINUTE, field);
	}
}
