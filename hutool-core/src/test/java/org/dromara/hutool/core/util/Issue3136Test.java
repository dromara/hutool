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

package org.dromara.hutool.core.util;

import lombok.Data;
import org.dromara.hutool.core.xml.XmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		final SmsRes smsRes = XmlUtil.xmlToBean(XmlUtil.parseXml(xmlStr).getDocumentElement(), SmsRes.class);

		Assertions.assertEquals("02", smsRes.getCode());
		Assertions.assertNull(smsRes.getMessage());
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
