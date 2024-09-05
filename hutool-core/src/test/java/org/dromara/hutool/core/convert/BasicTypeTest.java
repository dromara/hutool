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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicTypeTest {

	@Test
	public void wrapTest(){
		Assertions.assertEquals(Integer.class, BasicType.wrap(int.class));
		Assertions.assertEquals(Integer.class, BasicType.wrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.wrap(String.class));
		Assertions.assertNull(BasicType.wrap(null));
	}

	@Test
	public void unWrapTest(){
		Assertions.assertEquals(int.class, BasicType.unWrap(int.class));
		Assertions.assertEquals(int.class, BasicType.unWrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.unWrap(String.class));
		Assertions.assertNull(BasicType.unWrap(null));
	}

	@Test
	public void getPrimitiveSetTest(){
		Assertions.assertEquals(8, BasicType.getPrimitiveSet().size());
	}

	@Test
	public void getWrapperSetTest(){
		Assertions.assertEquals(8, BasicType.getWrapperSet().size());
	}
}
