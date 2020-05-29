package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 测试同一对象作为对象的字段是否会有null的问题，
 * 此问题原来出在BeanCopier中，判断循环引用使用了equals，并不严谨。
 * 修复后使用==判断循环引用。
 */
public class IssueI1H2VN {

	@Test
	public void toBeanTest() {
		String jsonStr = "{'conditionsVo':[{'column':'StockNo','value':'abc','type':'='},{'column':'CheckIncoming','value':'1','type':'='}]," +
				"'queryVo':{'conditionsVo':[{'column':'StockNo','value':'abc','type':'='},{'column':'CheckIncoming','value':'1','type':'='}],'queryVo':null}}";
		QueryVo vo = JSONUtil.toBean(jsonStr, QueryVo.class);
		Assert.assertEquals(2, vo.getConditionsVo().size());
		final QueryVo subVo = vo.getQueryVo();
		Assert.assertNotNull(subVo);
		Assert.assertEquals(2, subVo.getConditionsVo().size());
		Assert.assertNull(subVo.getQueryVo());
	}

	@Data
	public static class ConditionVo {
		private String column;
		private String value;
		private String type;
	}

	@Data
	public static class QueryVo {
		private List<ConditionVo> conditionsVo;
		private QueryVo queryVo;
	}
}
