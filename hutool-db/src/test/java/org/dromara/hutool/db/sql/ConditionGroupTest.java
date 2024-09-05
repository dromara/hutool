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

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionGroupTest {
	@Test
	public void ConditionGroupToStringTest() {
		final Condition condition1 = new Condition("a", "A");
		final Condition condition2 = new Condition("b", "B");
		condition2.setLinkOperator(LogicalOperator.OR);
		final Condition condition3 = new Condition("c", "C");
		final Condition condition4 = new Condition("d", "D");

		final ConditionGroup cg = new ConditionGroup();
		cg.addConditions(condition1, condition2);

		// 条件组嵌套情况
		final ConditionGroup cg2 = new ConditionGroup();
		cg2.addConditions(cg, condition3);

		final ConditionBuilder conditionBuilder = ConditionBuilder.of(cg2, condition4);

		Assertions.assertEquals("((a = ? OR b = ?) AND c = ?) AND d = ?", conditionBuilder.build());
		Assertions.assertEquals(ListUtil.view("A", "B", "C", "D"), conditionBuilder.getParamValues());
	}
}
