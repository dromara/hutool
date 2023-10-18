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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConditionTest {

	@Test
	public void toStringTest() {
		final Condition conditionNull = new Condition("user", null);
		Assertions.assertEquals("user IS NULL", conditionNull.toString());

		final Condition conditionNotNull = new Condition("user", "!= null");
		Assertions.assertEquals("user IS NOT NULL", conditionNotNull.toString());

		final Condition condition2 = new Condition("user", "= zhangsan");
		Assertions.assertEquals("user = ?", condition2.toString());

		final Condition conditionLike = new Condition("user", "like %aaa");
		Assertions.assertEquals("user LIKE ?", conditionLike.toString());

		final Condition conditionIn = new Condition("user", "in 1,2,3");
		Assertions.assertEquals("user IN (?,?,?)", conditionIn.toString());

		final Condition conditionBetween = new Condition("user", "between 12 and 13");
		Assertions.assertEquals("user BETWEEN ? AND ?", conditionBetween.toString());
	}

	@Test
	public void toStringNoPlaceHolderTest() {
		final Condition conditionNull = new Condition("user", null);
		conditionNull.setPlaceHolder(false);
		Assertions.assertEquals("user IS NULL", conditionNull.toString());

		final Condition conditionNotNull = new Condition("user", "!= null");
		conditionNotNull.setPlaceHolder(false);
		Assertions.assertEquals("user IS NOT NULL", conditionNotNull.toString());

		final Condition conditionEquals = new Condition("user", "= zhangsan");
		conditionEquals.setPlaceHolder(false);
		Assertions.assertEquals("user = zhangsan", conditionEquals.toString());

		final Condition conditionLike = new Condition("user", "like %aaa");
		conditionLike.setPlaceHolder(false);
		Assertions.assertEquals("user LIKE '%aaa'", conditionLike.toString());

		final Condition conditionIn = new Condition("user", "in 1,2,3");
		conditionIn.setPlaceHolder(false);
		Assertions.assertEquals("user IN (1,2,3)", conditionIn.toString());

		final Condition conditionBetween = new Condition("user", "between 12 and 13");
		conditionBetween.setPlaceHolder(false);
		Assertions.assertEquals("user BETWEEN 12 AND 13", conditionBetween.toString());
	}

	@Test
	public void parseTest(){
		final Condition age = Condition.parse("age", "< 10");
		Assertions.assertEquals("age < ?", age.toString());
		// issue I38LTM
		Assertions.assertSame(BigDecimal.class, age.getValue().getClass());
	}

	@Test
	public void parseInTest(){
		final Condition age = Condition.parse("age", "in 1,2,3");
		Assertions.assertEquals("age IN (?,?,?)", age.toString());
	}

	@Test
	void notInTest() {
		final Condition age = Condition.parse("age", "not in 1,2,3");
		Assertions.assertEquals("age NOT IN (?,?,?)", age.toString());
	}
}
