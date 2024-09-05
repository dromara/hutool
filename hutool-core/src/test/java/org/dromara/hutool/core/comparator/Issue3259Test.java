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

package org.dromara.hutool.core.comparator;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3259Test {

	@Test
	public void fieldsComparatorTest() {
		final Model x = new Model(1, 1);
		final Model y = new Model(1, RandomUtil.randomInt(2, 100));

		Assertions.assertTrue(new FieldsComparator<>(Model.class, "a", "b").compare(x, y) < 0);
	}

	@Test
	@Disabled
	public void sortTest() {
		for(int i = 2; i < 5; i++) {
			final Model x = new Model(1, 1);
			final Model y = new Model(1, i);

			List<Model> all = ListUtil.of(x, y);
			all = CollUtil.sort(new ArrayList<>(all), new FieldsComparator<>(Model.class, "a", "b"));
			System.out.println(all);
		}
	}

	@AllArgsConstructor
	@ToString
	public static class Model {
		public int a;
		public int b;
	}
}
