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

package org.dromara.hutool.db.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionBuilderTest {

	@Test
	public void buildTest(){
		final Condition c1 = new Condition("user", null);
		final Condition c2 = new Condition("name", "!= null");
		c2.setLinkOperator(LogicalOperator.OR);
		final Condition c3 = new Condition("group", "like %aaa");

		final ConditionBuilder builder = ConditionBuilder.of(c1, c2, c3);
		final String sql = builder.build();
		Assertions.assertEquals("user IS NULL OR name IS NOT NULL AND group LIKE ?", sql);
		Assertions.assertEquals(1, builder.getParamValues().size());
		Assertions.assertEquals("%aaa", builder.getParamValues().get(0));
	}
}
