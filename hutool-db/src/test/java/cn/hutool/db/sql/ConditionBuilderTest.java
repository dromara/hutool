package cn.hutool.db.sql;

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
