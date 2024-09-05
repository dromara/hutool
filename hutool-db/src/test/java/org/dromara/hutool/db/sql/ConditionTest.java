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
