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

package org.dromara.hutool.setting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingUtilTest {

	@Test
	public void getTest() {
		final String driver = SettingUtil.get("test").getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getTest2() {
		final String driver = SettingUtil.get("example/example").getStrByGroup("key", "demo");
		Assertions.assertEquals("value", driver);
	}

	@Test
	public void getFirstFoundTest() {
		//noinspection ConstantConditions
		final String driver = SettingUtil.getFirstFound("test2", "test")
				.getStrByGroup("driver", "demo");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
