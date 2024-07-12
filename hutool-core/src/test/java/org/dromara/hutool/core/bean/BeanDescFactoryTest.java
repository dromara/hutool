package org.dromara.hutool.core.bean;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanDescFactoryTest {
	@Test
	void getBeanDescTest() {
		final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Food.class);
		final Collection<PropDesc> props = beanDesc.getProps();
		assertEquals(2, props.size());
	}

	@Test
	void getBeanDescWithoutCacheTest() {
		final BeanDesc beanDesc = BeanDescFactory.getBeanDescWithoutCache(Food.class);
		final Collection<PropDesc> props = beanDesc.getProps();
		assertEquals(2, props.size());
	}

	@Data
	public static class Food {
		private String bookID;
		private String code;
	}
}
