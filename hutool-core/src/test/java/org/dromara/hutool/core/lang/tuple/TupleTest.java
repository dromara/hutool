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

package org.dromara.hutool.core.lang.tuple;

import org.dromara.hutool.core.lang.tuple.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.TimeZone;

public class TupleTest {

	@Test
	public void hashCodeTest(){
		final Tuple tuple = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		final Tuple tuple2 = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		Assertions.assertEquals(tuple, tuple2);
	}
}
