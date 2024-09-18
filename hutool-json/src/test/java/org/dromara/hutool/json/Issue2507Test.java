/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue2507Test {

	@Test
	public void xmlToJsonTest(){
		final String xml = "<MsgInfo> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>低盐饮食[嘱托]]><![CDATA[]</DIET></row></body></msg>]]> </Msg> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>流质饮食</DIET></row></body></msg>]]> </Msg> </MsgInfo>";
		final JSONObject jsonObject = JSONUtil.xmlToJson(xml);

		assertEquals("{\"MsgInfo\":{\"Msg\":[\"<msg><body><row action=\\\"select\\\"><DIET>低盐饮食[嘱托\",\"]</DIET></row></body></msg>\",\"<msg><body><row action=\\\"select\\\"><DIET>流质饮食</DIET></row></body></msg>\"]}}"
			, jsonObject.toString());
	}
}
