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

import org.dromara.hutool.core.convert.impl.PrimitiveConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveConvertTest {

	@Test
	public void toIntTest(){
		final int convert = ConvertUtil.convert(int.class, "123");
		Assertions.assertEquals(123, convert);
	}

	@Test
	public void toIntErrorTest(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			ConvertUtil.convert(int.class, "aaaa");
		});
	}

	@Test
	public void toIntValueTest() {
		final Object a = PrimitiveConverter.INSTANCE.convert(int.class, null);
		Assertions.assertNull(a);
	}
}
