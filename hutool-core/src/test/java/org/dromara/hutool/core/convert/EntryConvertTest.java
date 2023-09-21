/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
