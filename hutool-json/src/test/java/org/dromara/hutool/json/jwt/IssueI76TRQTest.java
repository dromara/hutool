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
