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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.text.NamingCase;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue2202Test {

	/**
	 * https://github.com/dromara/hutool/issues/2202
	 */
	@Test
	public void mapToBeanWithFieldNameEditorTest(){
		final Map<String, String> headerMap = new HashMap<>(5);
		headerMap.put("wechatpay-serial", "serial");
		headerMap.put("wechatpay-nonce", "nonce");
		headerMap.put("wechatpay-timestamp", "timestamp");
		headerMap.put("wechatpay-signature", "signature");
		final ResponseSignVerifyParams case1 = BeanUtil.toBean(headerMap, ResponseSignVerifyParams.class,
				CopyOptions.of().setFieldEditor(entry -> {
					entry.setKey(NamingCase.toCamelCase(entry.getKey(), '-'));
					return entry;
				}));

		Assertions.assertEquals("serial", case1.getWechatpaySerial());
		Assertions.assertEquals("nonce", case1.getWechatpayNonce());
		Assertions.assertEquals("timestamp", case1.getWechatpayTimestamp());
		Assertions.assertEquals("signature", case1.getWechatpaySignature());
	}

	@Data
	static class ResponseSignVerifyParams {
		private String wechatpaySerial;
		private String wechatpaySignature;
		private String wechatpayTimestamp;
		private String wechatpayNonce;
		private String body;
	}
}
