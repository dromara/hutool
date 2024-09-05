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

package org.dromara.hutool.core.data.id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeataSnowflakeTest {
	@Test
	public void nextTest() {
		//构建Snowflake，提供终端ID和数据中心ID
		final SeataSnowflake idWorker = new SeataSnowflake();
		final long nextId = idWorker.next();
		Assertions.assertTrue(nextId > 0);
	}

	@Test
	void testNextId() {
		final SeataSnowflake worker = new SeataSnowflake();
		final long id1 = worker.next();
		final long id2 = worker.next();
		Assertions.assertEquals(1L, id2 - id1, "increment step should be 1");
	}
}
