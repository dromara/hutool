package org.dromara.hutool;

import org.dromara.hutool.sql.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageTest {

	@Test
	public void addOrderTest() {
		final Page page = new Page();
		page.addOrder(new Order("aaa"));
		Assertions.assertEquals(page.getOrders().length, 1);
		page.addOrder(new Order("aaa"));
		Assertions.assertEquals(page.getOrders().length, 2);
	}
}
