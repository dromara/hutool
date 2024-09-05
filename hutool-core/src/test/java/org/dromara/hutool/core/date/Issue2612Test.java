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

package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class Issue2612Test {

	@Test
	public void parseTest(){
		Assertions.assertEquals("2022-09-15 15:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-08:00")).toString());

		Assertions.assertEquals("2022-09-15 15:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-0800")).toString());
	}
}
