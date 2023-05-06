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

package cn.hutool.core.net;

import cn.hutool.core.lang.Console;
import org.junit.Test;

public class IssueI70UPUTest {
	@Test
	public void encodeQueryTest() {
		String json = "{\n" +
			"  \"FodayGJ\": {\n" +
			"    \"ZTMC\": \"库存\",\n" +
			"    \"ZTBZ\": \"20\",\n" +
			"    \"KEYID\": 40313,\n" +
			"    \"KBH\": \"C0XFPQ-1B\",\n" +
			"    \"GCJC\": \"蒂森\",\n" +
			"    \"CCZL\": 8370,\n" +
			"    \"DQZL\": 8370,\n" +
			"    \"CZ\": \"HC340/590DPD+Z\",\n" +
			"    \"SCRQ\": \"2023-04-24\",\n" +
			"    \"GG\": \"1.5*1000*C\",\n" +
			"    \"GYS\": \"佛山亚铁\",\n" +
			"    \"WZH\": \"B19\"\n" +
			"  }\n" +
			"}";

		Console.log(URLEncodeUtil.encodeQuery(json));
	}
}
