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

package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 测试同一对象作为对象的字段是否会有null的问题，
 * 此问题原来出在BeanCopier中，判断循环引用使用了equals，并不严谨。
 * 修复后使用==判断循环引用。
 */
public class IssueI1H2VNTest {

	@Test
	public void toBeanTest() {
		final String jsonStr = "{'conditionsVo':[{'column':'StockNo','value':'abc','type':'='},{'column':'CheckIncoming','value':'1','type':'='}]," +
				"'queryVo':{'conditionsVo':[{'column':'StockNo','value':'abc','type':'='},{'column':'CheckIncoming','value':'1','type':'='}],'queryVo':null}}";
		final QueryVo vo = JSONUtil.toBean(jsonStr, QueryVo.class);
		Assertions.assertEquals(2, vo.getConditionsVo().size());
		final QueryVo subVo = vo.getQueryVo();
		Assertions.assertNotNull(subVo);
		Assertions.assertEquals(2, subVo.getConditionsVo().size());
		Assertions.assertNull(subVo.getQueryVo());
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
