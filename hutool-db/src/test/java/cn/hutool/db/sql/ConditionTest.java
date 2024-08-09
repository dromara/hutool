package cn.hutool.db.sql;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConditionTest {

	@Test
	public void toStringTest() {
		Condition conditionNull = new Condition("user", null);
		assertEquals("user IS NULL", conditionNull.toString());

		Condition conditionNotNull = new Condition("user", "!= null");
		assertEquals("user IS NOT NULL", conditionNotNull.toString());

		Condition condition2 = new Condition("user", "= zhangsan");
		assertEquals("user = ?", condition2.toString());

		Condition conditionLike = new Condition("user", "like %aaa");
		assertEquals("user LIKE ?", conditionLike.toString());

		Condition conditionIn = new Condition("user", "in 1,2,3");
		assertEquals("user IN (?,?,?)", conditionIn.toString());

		Condition conditionBetween = new Condition("user", "between 12 and 13");
		assertEquals("user BETWEEN ? AND ?", conditionBetween.toString());
	}

	@Test
	public void toStringNoPlaceHolderTest() {
		Condition conditionNull = new Condition("user", null);
		conditionNull.setPlaceHolder(false);
		assertEquals("user IS NULL", conditionNull.toString());

		Condition conditionNotNull = new Condition("user", "!= null");
		conditionNotNull.setPlaceHolder(false);
		assertEquals("user IS NOT NULL", conditionNotNull.toString());

		Condition conditionEquals = new Condition("user", "= zhangsan");
		conditionEquals.setPlaceHolder(false);
		assertEquals("user = zhangsan", conditionEquals.toString());

		Condition conditionLike = new Condition("user", "like %aaa");
		conditionLike.setPlaceHolder(false);
		assertEquals("user LIKE '%aaa'", conditionLike.toString());

		Condition conditionIn = new Condition("user", "in 1,2,3");
		conditionIn.setPlaceHolder(false);
		assertEquals("user IN (1,2,3)", conditionIn.toString());

		Condition conditionBetween = new Condition("user", "between 12 and 13");
		conditionBetween.setPlaceHolder(false);
		assertEquals("user BETWEEN 12 AND 13", conditionBetween.toString());
	}

	@Test
	public void parseTest(){
		final Condition age = Condition.parse("age", "< 10");
		assertEquals("age < ?", age.toString());
		// issue I38LTM
		assertSame(BigDecimal.class, age.getValue().getClass());
	}

	@Test
	public void parseInTest(){
		final Condition age = Condition.parse("age", "in 1,2,3");
		assertEquals("age IN (?,?,?)", age.toString());
	}
}
