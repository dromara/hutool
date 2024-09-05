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

package org.dromara.hutool.core.util;

import lombok.Data;
import org.dromara.hutool.core.xml.XmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/dromara/hutool/issues/3136
 */
public class Issue3136Test {

	/**
	 * 此用例中，message节点无content，理解为空节点，转换为map后，此节点值为""，转为对象时，理应为null
	 */
	@Test
	void xmlToBeanTest() {
		final String xmlStr = "<?xml version=\"1.0\" encoding=\"gbk\" ?><response><code>02</code><message></message></response>";
		final Document doc = XmlUtil.parseXml(xmlStr);
		final SmsRes smsRes = XmlUtil.xmlToBean(doc.getDocumentElement(), SmsRes.class);

		Assertions.assertEquals("02", smsRes.getCode());
		Assertions.assertEquals(new Message(), smsRes.getMessage());
	}

	@Data
	static class SmsRes {
		/**
		 * 状态码.
		 */
		private String code;

		/**
		 * 消息.
		 */
		private Message message;
	}

	@Data
	static class Message {

		/**
		 * 消息项.
		 */
		private List<MessageItem> item = new ArrayList<>();
	}

	@Data
	static class MessageItem {

		/**
		 * 手机号.
		 */
		private String desmobile;
		/**
		 * 消息id.
		 */
		private String msgid;
	}
}
