package cn.hutool.core.convert;

import cn.hutool.core.util.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class Issue2611Test {

	@Test
	public void chineseMoneyToNumberTest(){
		final BigDecimal value = Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾柒元");

		Assert.assertEquals("67,557.00", NumberUtil.decimalFormatMoney(value.doubleValue()));
	}
}
