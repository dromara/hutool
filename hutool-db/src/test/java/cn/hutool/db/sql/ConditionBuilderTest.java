package cn.hutool.db.sql;

import org.junit.Assert;
import org.junit.Test;

public class ConditionBuilderTest {

	@Test
	public void buildTest(){
		final Condition c1 = new Condition("user", null);
		final Condition c2 = new Condition("name", "!= null");
		c2.setLinkOperator(LogicalOperator.OR);
		final Condition c3 = new Condition("group", "like %aaa");

		final ConditionBuilder builder = ConditionBuilder.of(c1, c2, c3);
		final String sql = builder.build();
		Assert.assertEquals("user IS NULL OR name IS NOT NULL AND group LIKE ?", sql);
		Assert.assertEquals(1, builder.getParamValues().size());
		Assert.assertEquals("%aaa", builder.getParamValues().get(0));
	}
}
