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

package org.dromara.hutool.json.issues;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue1075Test {

	final String jsonStr = "{\"f1\":\"f1\",\"f2\":\"f2\",\"fac\":\"fac\"}";

	@Test
	public void testToBean() {
		// 在不忽略大小写的情况下，f2、fac都不匹配
		final ObjA o2 = JSONUtil.toBean(jsonStr, ObjA.class);
		Assertions.assertNull(o2.getFAC());
		Assertions.assertNull(o2.getF2());
	}

	@Test
	public void testToBeanIgnoreCase() {
		// 在忽略大小写的情况下，f2、fac都匹配
		final ObjA o2 = JSONUtil.parseObj(jsonStr, JSONConfig.of().setIgnoreCase(true)).toBean(ObjA.class);

		Assertions.assertEquals("fac", o2.getFAC());
		Assertions.assertEquals("f2", o2.getF2());
	}

	@Data
	public static class ObjA {
		private String f1;
		private String F2;
		private String FAC;
	}
}
