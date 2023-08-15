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

package org.dromara.hutool.json.jwt;

import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://gitee.com/dromara/hutool/issues/I76TRQ
 */
public class IssueI76TRQTest {
	@Test
	void createTokenTest() {
		final String str = "{\"editorConfig\":{\"mode\":\"edit\",\"callbackUrl\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/IndexServlet?type\\u003dtrack\\u0026fileName\\u003dnew.docx\\u0026userAddress\\u003d172.16.30.53\",\"lang\":\"zh\",\"createUrl\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/EditorServlet?fileExt\\u003ddocx\",\"templates\":[{\"image\":\"\",\"title\":\"Blank\",\"url\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/EditorServlet?fileExt\\u003ddocx\"},{\"image\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/css/img/file_docx.svg\",\"title\":\"With sample content\",\"url\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/EditorServlet?fileExt\\u003ddocx\\u0026sample\\u003dtrue\"}],\"user\":{\"id\":\"uid-1\",\"name\":\"John Smith\",\"group\":\"\"},\"customization\":{\"goback\":{\"url\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/IndexServlet\"},\"forcesave\":false,\"submitForm\":false,\"about\":true,\"comments\":true,\"feedback\":true}},\"documentType\":\"word\",\"document\":{\"title\":\"new.docx\",\"url\":\"http://172.16.30.53:8080/OnlineEditorsExampleJava/IndexServlet?type\\u003ddownload\\u0026fileName\\u003dnew.docx\\u0026userAddress\\u003d172.16.30.53\",\"directUrl\":\"\",\"fileType\":\"docx\",\"key\":\"1956415572\",\"info\":{\"owner\":\"Me\",\"uploaded\":\"Fri May 19 2023\"},\"permissions\":{\"comment\":true,\"copy\":true,\"download\":true,\"edit\":true,\"print\":true,\"fillForms\":true,\"modifyFilter\":true,\"modifyContentControl\":true,\"review\":true,\"chat\":true,\"commentGroups\":{}}},\"type\":\"desktop\"}";
		final JSONObject payload = JSONUtil.parseObj(str);
		final String token = JWTUtil.createToken(payload, "123456".getBytes());
		Assertions.assertNotNull(token);

		final JWT jwt = JWTUtil.parseToken(token);
		final JWTPayload payload1 = jwt.getPayload();
		Assertions.assertEquals(4, payload1.getClaimsJson().size());
	}
}
