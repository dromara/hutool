package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class XMLTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.createObj().put("aaa", "你好").put("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assert.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}
}
