/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, kvBean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals(1, entry.getValue());
	}

	@Test
	void beanToEntryTest2() {
		final SingleBean bean = new SingleBean();
		bean.setA("1");
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void mapToEntryTest() {
		final Map<String, Integer> bean = new HashMap<>();
		bean.put("a", 1);
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals(1, entry.getValue());
	}

	@Test
	void strToEntryTest() {
		final String bean = "a=1";
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void strToEntryTest2() {
		final String bean = "a:1";
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
			.convert(AbstractMap.SimpleEntry.class, bean);

		Assertions.assertEquals("a", entry.getKey());
		Assertions.assertEquals("1", entry.getValue());
	}

	@Test
	void strToEntryTest3() {
		final String bean = "a,1";
		final AbstractMap.SimpleEntry<?, ?> entry = CompositeConverter.getInstance()
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
