package cn.hutool.db.sql;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ConditionTest {

	@Test
	public void toStringTest() {
		Condition conditionNull = new Condition("user", null);
		Assert.assertEquals("user IS NULL", conditionNull.toString());

		Condition conditionNotNull = new Condition("user", "!= null");
		Assert.assertEquals("user IS NOT NULL", conditionNotNull.toString());

		Condition condition2 = new Condition("user", "= zhangsan");
		Assert.assertEquals("user = ?", condition2.toString());

		Condition conditionLike = new Condition("user", "like %aaa");
		Assert.assertEquals("user LIKE ?", conditionLike.toString());

		Condition conditionIn = new Condition("user", "in 1,2,3");
		Assert.assertEquals("user IN (?,?,?)", conditionIn.toString());

		Condition conditionBetween = new Condition("user", "between 12 and 13");
		Assert.assertEquals("user BETWEEN ? AND ?", conditionBetween.toString());
	}

	@Test
	public void toStringNoPlaceHolderTest() {
		Condition conditionNull = new Condition("user", null);
		conditionNull.setPlaceHolder(false);
		Assert.assertEquals("user IS NULL", conditionNull.toString());

		Condition conditionNotNull = new Condition("user", "!= null");
		conditionNotNull.setPlaceHolder(false);
		Assert.assertEquals("user IS NOT NULL", conditionNotNull.toString());

		Condition conditionEquals = new Condition("user", "= zhangsan");
		conditionEquals.setPlaceHolder(false);
		Assert.assertEquals("user = zhangsan", conditionEquals.toString());

		Condition conditionLike = new Condition("user", "like %aaa");
		conditionLike.setPlaceHolder(false);
		Assert.assertEquals("user LIKE %aaa", conditionLike.toString());

		Condition conditionIn = new Condition("user", "in 1,2,3");
		conditionIn.setPlaceHolder(false);
		Assert.assertEquals("user IN (1,2,3)", conditionIn.toString());

		Condition conditionBetween = new Condition("user", "between 12 and 13");
		conditionBetween.setPlaceHolder(false);
		Assert.assertEquals("user BETWEEN 12 AND 13", conditionBetween.toString());
	}

	@Test
	public void parseTest(){
		final Condition age = Condition.parse("age", "< 10");
		Assert.assertEquals("age < ?", age.toString());
		// issue I38LTM
		Assert.assertSame(BigDecimal.class, age.getValue().getClass());
	}

	@Test
	public void parseInTest(){
		final Condition age = Condition.parse("age", "in 1,2,3");
		Assert.assertEquals("age IN (?,?,?)", age.toString());
	}
}
