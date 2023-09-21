/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
