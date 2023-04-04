package org.dromara.hutool.core.convert;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class EntryConvertTest {

	@Test
	void beanToEntryTest() {
		final KVBean kvBean = new KVBean();
		kvBean.setKey("a");
		kvBean.setValue(1);
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, kvBean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals(1, entry.getValue());
	}

	@Test
	void beanToEntryTest2() {
		final SingleBean bean = new SingleBean();
		bean.setA("1");
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void mapToEntryTest() {
		final Map<String, Integer> bean = new HashMap<>();
		bean.put("a", 1);
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals(1, entry.getValue());
	}

	@Test
	void strToEntryTest() {
		final String bean = "a=1";
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void strToEntryTest2() {
		final String bean = "a:1";
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void strToEntryTest3() {
		final String bean = "a,1";
		final AbstractMap.SimpleEntry<?, ?> entry = (AbstractMap.SimpleEntry<?, ?>) CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Data
	static class KVBean{
		private String key;
		private Integer value;
	}

	@Data
	static class SingleBean{
		private String a;
	}
}
