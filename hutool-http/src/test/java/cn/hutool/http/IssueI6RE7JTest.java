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

package cn.hutool.http;

import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueI6RE7JTest {

	@Test
	@Ignore
	public void getTest() {
		HttpGlobalConfig.setDecodeUrl(true);
		final String baseUrl = "http://192.168.98.73/PIAPI/ArcValue";

		final Map<String,Object> map = new HashMap<>();
		map.put("tag","TXSM_SC2202-苯乙烯%");
		map.put("time","2023/03/28 08:00:00");
		HttpUtil.get(baseUrl, map);
	}
}
