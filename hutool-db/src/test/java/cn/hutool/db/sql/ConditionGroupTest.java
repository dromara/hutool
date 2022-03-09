package cn.hutool.db.sql;

import cn.hutool.core.collection.ListUtil;
import org.junit.Assert;
import org.junit.Test;

public class ConditionGroupTest {
	@Test
	public void ConditionGroupToStringTest() {
		Condition condition1 = new Condition("a", "A");
		Condition condition2 = new Condition("b", "B");
		condition2.setLinkOperator(LogicalOperator.OR);
		Condition condition3 = new Condition("c", "C");
		Condition condition4 = new Condition("d", "D");

		ConditionGroup cg = new ConditionGroup();
		cg.addConditions(condition1, condition2);

		// 条件组嵌套情况
		ConditionGroup cg2 = new ConditionGroup();
		cg2.addConditions(cg, condition3);

		final ConditionBuilder conditionBuilder = ConditionBuilder.of(cg2, condition4);

		Assert.assertEquals("((a = ? OR b = ?) AND c = ?) AND d = ?", conditionBuilder.build());
		Assert.assertEquals(ListUtil.of("A", "B", "C", "D"), conditionBuilder.getParamValues());
	}
}
