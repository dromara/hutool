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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2507Test {

	@Test
	@Disabled
	public void xmlToJsonTest(){
		String xml = "<MsgInfo> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>低盐饮食[嘱托]]><![CDATA[]</DIET></row></body></msg>]]> </Msg> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>流质饮食</DIET></row></body></msg>]]> </Msg> </MsgInfo>";
		JSONObject jsonObject = JSONUtil.xmlToJson(xml);

		Console.log(jsonObject.toStringPretty());
	}
}
