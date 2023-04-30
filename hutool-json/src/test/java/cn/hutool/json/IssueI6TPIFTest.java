/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.json;

import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

public class IssueI6TPIFTest {

	@Test
	@Ignore
	public void toStringTest() {
		TestVo test = new TestVo();
		test.setBigValue(new BigDecimal("1234567899876543210.000000000000000000000000001"));
		test.setSmallValue(new BigDecimal("0.00000000000000000005"));
		System.out.println("Bean To JSON");
		System.out.println(JSONUtil.toJsonStr(test));
		System.out.println("\n");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("bigValue", "1234567899876543210.000000000000000000000000001");
		jsonObject.put("smallValue", "0.00000000000000000005");
		System.out.println("JSON TO Bean");
		System.out.println(JSONUtil.toBean(jsonObject, TestVo.class));
	}

	@Data
	static class TestVo {

		private BigDecimal bigValue;
		private BigDecimal smallValue;
	}
}
